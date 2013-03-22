package us.aaronweiss.pixalia.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.server.net.BufferLengthDecoder;
import us.aaronweiss.pixalia.server.net.BufferLengthEncoder;
import us.aaronweiss.pixalia.server.net.PacketDecoder;
import us.aaronweiss.pixalia.server.net.PacketEncoder;
import us.aaronweiss.pixalia.server.packets.Packet;
import us.aaronweiss.pixalia.server.tools.Configuration;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
	private static EventBus eventBus;
    private static Server instance;
	private ServerBootstrap bootstrap;
	private static final ChannelGroup channels = new DefaultChannelGroup("server");
	
	protected Server() {
		bootstrap = new ServerBootstrap();
		bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup());
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				channels.add(ch);
				ChannelPipeline pipeline = ch.pipeline();
				// Decoders
				pipeline.addLast("buffer_length_decoder", new BufferLengthDecoder());
				pipeline.addLast("packet_decoder", new PacketDecoder());
				
				// Encoder
				pipeline.addLast("packet_encoder", new PacketEncoder());
				pipeline.addLast("buffer_length_encoder", new BufferLengthEncoder());
			}
		});
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
	}
	
	public void bind(int port) {
		ChannelFuture cf = bootstrap.bind(port);
		cf.awaitUninterruptibly();
		logger.info("Bound on port " + port);
	}

	public void writeAll(Packet message) {
		channels.write(message);
	}
	
	public void shutdown() {
		bootstrap.shutdown();
	}
	
	public static EventBus getEventBus() {
		if (Server.eventBus == null) {
			int cores = Runtime.getRuntime().availableProcessors();
			ThreadPoolExecutor pool = new ThreadPoolExecutor(cores, Configuration.maxEventThreads(), Configuration.eventThreadKeepAlive(), TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>());
			Server.eventBus = new AsyncEventBus(pool);
			logger.info("AsyncEventBus created with " + cores + " cores.");
		}
		return Server.eventBus;
	}
	
	public static Server getInstance() {
		if (Server.instance == null) {
			Server.instance = new Server();
		}
		return Server.instance;
	}
}

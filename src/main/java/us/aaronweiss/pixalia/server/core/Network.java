package us.aaronweiss.pixalia.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.aaronweiss.pixalia.server.listeners.HandshakeHandler;
import us.aaronweiss.pixalia.server.net.PacketDecoder;
import us.aaronweiss.pixalia.server.net.PacketEncoder;
import us.aaronweiss.pixalia.server.net.PixaliaServerHandler;
import us.aaronweiss.pixalia.server.packets.Packet;
import us.aaronweiss.pixalia.server.tools.Constants;

public class Network {
	private static final Logger logger = LoggerFactory.getLogger(Network.class);
	private final ServerBootstrap bootstrap;
	private final EventLoopGroup bossGroup, workerGroup;
	private final PixaliaServerHandler handler = new PixaliaServerHandler();

	public Network(Server server) {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();
		handler.register(HandshakeHandler.OPCODE, new HandshakeHandler(server));
		// TODO: register moar handlers with server.
		bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast("buffer_length_encoder", new LengthFieldPrepender(2));
						pipeline.addLast("buffer_length_decoder", new LengthFieldBasedFrameDecoder(Constants.MAX_FRAME_LENGTH, 0, 2, 0, 2));

						pipeline.addLast("packet_decoder", new PacketDecoder());
						pipeline.addLast("packet_encoder", new PacketEncoder());

						pipeline.addLast("handler", handler);
					}
				});
	}

	public void bind(int port) throws InterruptedException {
		bootstrap.bind(port).sync();
		logger.info("Bound on port " + port);
	}

	public void writeAll(Packet packet) {
		handler.writeAll(packet);
	}

	public void writeAllExcept(Channel ch, Packet packet) {
		handler.writeAllExcept(ch, packet);
	}

	public void shutdown() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
}

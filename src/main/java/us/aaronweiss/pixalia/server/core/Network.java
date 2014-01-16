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
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.aaronweiss.pixalia.server.listeners.HandshakeHandler;
import us.aaronweiss.pixalia.server.listeners.MovementHandler;
import us.aaronweiss.pixalia.server.net.PacketDecoder;
import us.aaronweiss.pixalia.server.net.PacketEncoder;
import us.aaronweiss.pixalia.server.net.PixaliaServerHandler;
import us.aaronweiss.pixalia.server.packets.Packet;
import us.aaronweiss.pixalia.server.packets.PlayerJoinPacket;
import us.aaronweiss.pixalia.server.tools.Constants;

import java.util.Iterator;

public class Network {
	private static final Logger logger = LoggerFactory.getLogger(Network.class);
	private static final AttributeKey<Pixal> CHANNEL_PIXAL_ATTR = AttributeKey.valueOf(Constants.CHANNEL_PIXAL_ATTR_NAME);
	private final Server server;
	private final ServerBootstrap bootstrap;
	private final EventLoopGroup bossGroup, workerGroup;
	private final PixaliaServerHandler handler;

	public Network(Server server) {
		this.server = server;
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();
		handler = new PixaliaServerHandler();
		handler.register(HandshakeHandler.OPCODE, new HandshakeHandler(server));
		handler.register(MovementHandler.OPCODE, new MovementHandler(server));
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

	public void notifyOfPresentPixals(Channel ch) {
		Iterator<Pixal> i = server.worldIterator();
		while (i.hasNext()) {
			Pixal p = i.next();
			if (p != ch.attr(Network.CHANNEL_PIXAL_ATTR).get())
				ch.writeAndFlush(PlayerJoinPacket.newOutboundPacket(p.getHostname(), p.getColor(), p.getPosition()));
		}
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

	public static AttributeKey<Pixal> getChannelPixalAttr() {
		return CHANNEL_PIXAL_ATTR;
	}
}

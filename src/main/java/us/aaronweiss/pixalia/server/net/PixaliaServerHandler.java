package us.aaronweiss.pixalia.server.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.aaronweiss.pixalia.server.listeners.PacketHandler;
import us.aaronweiss.pixalia.server.packets.Packet;
import us.aaronweiss.pixalia.server.tools.Constants;
import us.aaronweiss.pixalia.server.tools.Utils;


@ChannelHandler.Sharable
public class PixaliaServerHandler extends SimpleChannelInboundHandler<Packet> {
	private static final Logger logger = LoggerFactory.getLogger(PixaliaServerHandler.class.getName());
	private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private PacketHandler[] handlers;

	public PixaliaServerHandler() {
		handlers = new PacketHandler[Constants.HANDLER_COUNT];
	}

	public void register(byte opcode, PacketHandler handler) {
		handlers[opcode] = handler;
	}

	public void writeAll(Packet packet) {
		for (Channel c : channels)
			c.writeAndFlush(packet);
	}

	public void writeAllExcept(Channel ch, Packet packet) {
		for (Channel c : channels)
			if (c != ch)
				c.writeAndFlush(packet);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("User connected from " + ctx.channel().remoteAddress().toString());
		channels.add(ctx.channel());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
		logger.info("Message received: " + Utils.toHexString(packet.array()));
		Packet response = handlers[packet.opcode()].process(packet);
		logger.info("Sending out result: " + Utils.toHexString(response.array()));
		if (response.channel() != null)
			response.channel().writeAndFlush(response);
		else
			writeAll(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("Unexpected exception caught from downstream.", cause);
		ctx.close();
	}
}

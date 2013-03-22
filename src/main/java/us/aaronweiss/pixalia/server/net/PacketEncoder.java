package us.aaronweiss.pixalia.server.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundMessageHandlerAdapter;
import us.aaronweiss.pixalia.server.packets.Packet;

public class PacketEncoder extends ChannelOutboundMessageHandlerAdapter<Packet> {
	@Override
	protected void flush(ChannelHandlerContext ctx, Packet msg) throws Exception {
		ctx.nextOutboundByteBuffer().writeBytes(msg.array());
	}
}

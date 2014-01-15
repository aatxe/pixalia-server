package us.aaronweiss.pixalia.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import us.aaronweiss.pixalia.server.packets.Packet;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) {
		out.writeBytes(msg.array());
	}
}

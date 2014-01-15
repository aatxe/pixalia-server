package us.aaronweiss.pixalia.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.server.core.Server;
import us.aaronweiss.pixalia.server.packets.HandshakePacket;
import us.aaronweiss.pixalia.server.packets.MessagePacket;
import us.aaronweiss.pixalia.server.packets.MovementPacket;
import us.aaronweiss.pixalia.server.packets.Packet;
import us.aaronweiss.pixalia.server.packets.VHostChangeRequestPacket;
import us.aaronweiss.pixalia.server.tools.Utils;
import us.aaronweiss.pixalia.server.tools.Vector;

import javax.rmi.CORBA.Util;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(PacketDecoder.class);

	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		byte opcode = in.readByte();
		Packet event = null;
		logger.info("Packet recieved. (Opcode: " + opcode + ")");
		logger.info("Packet data: " + Utils.toHexString(in));
		switch (opcode) {
			case HandshakePacket.OPCODE:
				String username = Utils.readString(in.readByte(), in);
				logger.info("Handshaked with " + username);
				if (in.readableBytes() > 2)
					event = HandshakePacket.newInboundPacket(ctx.channel(), username, Utils.readString(in.readByte(), in));
				else
					event = HandshakePacket.newInboundPacket(ctx.channel(), username);
				break;
			case MovementPacket.OPCODE:
				event = MovementPacket.newInboundPacket(ctx.channel(), Vector.fromByteBuf(2, in));
				break;
			case MessagePacket.OPCODE:
				event = MessagePacket.newInboundPacket(ctx.channel(), Utils.readString(in.readByte(), in));
				break;
			case VHostChangeRequestPacket.OPCODE:
				event = VHostChangeRequestPacket.newInboundPacket(ctx.channel(), Utils.readString(in.readByte(), in));
				break;
			default:
				logger.debug("Unexpected packet recieved. (Opcode: " + opcode + ")");
		}
		if (event != null)
			out.add(event);
	}
}

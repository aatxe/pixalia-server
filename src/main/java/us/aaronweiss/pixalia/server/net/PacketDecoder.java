package us.aaronweiss.pixalia.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;

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

public class PacketDecoder extends ChannelInboundMessageHandlerAdapter<ByteBuf> {
    private static final Logger logger = LoggerFactory.getLogger(PacketDecoder.class);
    
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		byte opcode = in.readByte();
		Packet event;
		logger.info("Packet recieved. (Opcode: " + opcode + ")");
		switch (opcode) {
		case HandshakePacket.OPCODE:
			String username = Utils.readString(in.readInt(), in);
			if (in.readableBytes() > 2)
				event = HandshakePacket.newInboundPacket(ctx.channel(), username, Utils.readString(in.readInt(), in));
			else
				event = HandshakePacket.newInboundPacket(ctx.channel(), username);
			break;
		case MovementPacket.OPCODE:
			event = MovementPacket.newInboundPacket(ctx.channel(), Vector.fromByteBuf(2, in));
			break;
		case MessagePacket.OPCODE:
			event = MessagePacket.newInboundPacket(ctx.channel(), Utils.readString(in.readInt(), in));
			break;
		case VHostChangeRequestPacket.OPCODE:
			event = VHostChangeRequestPacket.newInboundPacket(ctx.channel(), Utils.readString(in.readInt(), in));
			break;
		default:
			logger.debug("Unexpected packet recieved. (Opcode: " + opcode + ")");
			event = null;
		}
		if (event != null)
			Server.getEventBus().post(event);
	}
}

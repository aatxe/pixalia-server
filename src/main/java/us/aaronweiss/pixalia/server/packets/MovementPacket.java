package us.aaronweiss.pixalia.server.packets;

import io.netty.channel.Channel;
import us.aaronweiss.pixalia.server.tools.Utils;
import us.aaronweiss.pixalia.server.tools.Vector;

public class MovementPacket extends Packet {
	public static final byte OPCODE = 0x02;

	private MovementPacket(Channel channel, Vector position) {
		super(channel, OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeBytes(position.asByteBuf(2));
		trim();
	}
	
	private MovementPacket(String hostname, Vector position) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeByte(hostname.getBytes().length);
		this.buffer.writeBytes(hostname.getBytes());
		this.buffer.writeBytes(position.asByteBuf(2));
		trim();
	}
	
	public String hostname() {
		if (this.packetType.is(PacketType.OUTBOUND)) {
			this.ready();
			return Utils.readString(this.buffer.readInt(), this.buffer);
		}
		return null;
	}
	
	public Vector position() {
		this.ready();
		if (this.packetType.is(PacketType.OUTBOUND)) {
			this.buffer.readBytes(this.buffer.readInt());
		}
		return Vector.fromByteBuf(2, this.buffer);
	}
	
	public static MovementPacket newOutboundPacket(String hostname, Vector position) {
		return new MovementPacket(hostname, position);
	}
	
	public static MovementPacket newInboundPacket(Channel channel, Vector position) {
		return new MovementPacket(channel, position);
	}
}

package us.aaronweiss.pixalia.server.packets;

import us.aaronweiss.pixalia.server.tools.Utils;
import us.aaronweiss.pixalia.server.tools.Vector;

public class PlayerJoinPacket extends Packet {
	public static final byte OPCODE = 0x05;

	private PlayerJoinPacket(String hostname, Vector playerColor) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeByte(hostname.getBytes().length);
		this.buffer.writeBytes(hostname.getBytes());
		this.buffer.writeBytes(playerColor.asByteBuf(4));
		trim();
	}

	public static PlayerJoinPacket newOutboundPacket(String hostname, Vector playerColor) {
		return new PlayerJoinPacket(hostname, playerColor);
	}

	public String hostname() {
		if (this.packetType.is(PacketType.OUTBOUND)) {
			this.ready();
			return Utils.readString(this.buffer.readInt(), this.buffer);
		}
		return null;
	}

	public Vector playerColor() {
		this.ready();
		if (this.packetType.is(PacketType.OUTBOUND)) {
			Utils.readString(this.buffer.readInt(), this.buffer);
			return Vector.fromByteBuf(4, this.buffer);
		}
		return null;
	}
}

package us.aaronweiss.pixalia.server.packets;

import io.netty.channel.Channel;
import us.aaronweiss.pixalia.server.tools.Utils;

public class PlayerQuitPacket extends Packet {
	public static final byte OPCODE = 0x07;

	protected PlayerQuitPacket(Channel ch) {
		super(ch, OPCODE);
		this.packetType = PacketType.INBOUND;
		trim();
	}

	protected PlayerQuitPacket(String hostname) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeByte(hostname.getBytes().length);
		this.buffer.writeBytes(hostname.getBytes());
		trim();
	}

	public static PlayerQuitPacket newInboundPacket(Channel ch) {
		return new PlayerQuitPacket(ch);
	}

	public static PlayerQuitPacket newOutboundPacket(String hostname) {
		return new PlayerQuitPacket(hostname);
	}

	public String hostname() {
		this.ready();
		if (this.packetType.is(PacketType.OUTBOUND)) {
			return Utils.readString(this.buffer.readByte(), this.buffer);
		}
		return null;
	}
}

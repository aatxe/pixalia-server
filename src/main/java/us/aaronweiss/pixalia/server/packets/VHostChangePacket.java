package us.aaronweiss.pixalia.server.packets;

import us.aaronweiss.pixalia.server.tools.Utils;

public class VHostChangePacket extends Packet {
	public static final byte OPCODE = 0x05;
	
	private VHostChangePacket(String hostname, String newHostname) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeByte(hostname.getBytes().length);
		this.buffer.writeBytes(hostname.getBytes());
		this.buffer.writeByte(newHostname.getBytes().length);
		this.buffer.writeBytes(newHostname.getBytes());
		trim();
	}
	
	public static VHostChangePacket newOutboundPacket(String hostname, String newHostname) {
		return new VHostChangePacket(hostname, newHostname);
	}
	
	public String hostname() {
		if (this.packetType.is(PacketType.OUTBOUND)) {
			this.ready();
			return Utils.readString(this.buffer.readInt(), this.buffer);
		}
		return null;
	}
	
	public String newHostname() {
		this.ready();
		if (this.packetType.is(PacketType.OUTBOUND)) {
			Utils.readString(this.buffer.readInt(), this.buffer);
			return Utils.readString(this.buffer.readInt(), this.buffer);
		}
		return null;
	}
}

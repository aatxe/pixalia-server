package us.aaronweiss.pixalia.server.packets;

import io.netty.channel.Channel;
import us.aaronweiss.pixalia.server.tools.Utils;

public class VHostChangeRequestPacket extends Packet {
	public static final byte OPCODE = 0x04;
	
	private VHostChangeRequestPacket(Channel channel, boolean status) {
		super(channel, OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeBoolean(status);
		trim();
	}
	
	private VHostChangeRequestPacket(Channel channel, String desiredVHost) {
		super(channel, OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeByte(desiredVHost.getBytes().length);
		this.buffer.writeBytes(desiredVHost.getBytes());
		trim();
	}
	
	public boolean status() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND)) {
			return this.buffer.readBoolean();
		}
		return false;
	}
	
	public String virtualHost() {
		this.ready();
		if (this.packetType.is(PacketType.OUTBOUND)) {
			return Utils.readString(this.buffer.readInt(), this.buffer);
		}
		return null;
	}
	
	public static VHostChangeRequestPacket newOutboundPacket(Channel channel, boolean status) {
		return new VHostChangeRequestPacket(channel, status);
	}
	
	public static VHostChangeRequestPacket newInboundPacket(Channel channel, String desiredVHost) {
		return new VHostChangeRequestPacket(channel, desiredVHost);
	}
}

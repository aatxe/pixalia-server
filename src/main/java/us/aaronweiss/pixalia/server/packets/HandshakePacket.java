package us.aaronweiss.pixalia.server.packets;

import io.netty.channel.Channel;
import us.aaronweiss.pixalia.server.tools.Utils;
import us.aaronweiss.pixalia.server.tools.Vector;

public class HandshakePacket extends Packet {
	public static final byte OPCODE = 0x01;
	private final boolean hasVhost;
	
	private HandshakePacket(Channel channel, boolean status, Vector playerColor) {
		super(channel, OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeBoolean(status);
		this.buffer.writeBytes(playerColor.asByteBuf(4));
		this.hasVhost = false;
		trim();
	}
	
	private HandshakePacket(Channel channel, String username) {
		super(channel, OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeByte(username.getBytes().length);
		this.buffer.writeBytes(username.getBytes());
		this.hasVhost = false;
		trim();
	}
	
	private HandshakePacket(Channel channel, String username, String virtualHost) {
		super(channel, OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeByte(username.getBytes().length);
		this.buffer.writeBytes(username.getBytes());
		this.buffer.writeByte(virtualHost.getBytes().length);
		this.buffer.writeBytes(virtualHost.getBytes());
		this.hasVhost = true;
		trim();
	}
	
	public boolean status() {
		this.ready();
		if (this.packetType.is(PacketType.OUTBOUND)) {
			return this.buffer.readBoolean();
		}
		return false;
	}
	
	public Vector color() {
		this.ready();
		if (this.packetType.is(PacketType.OUTBOUND)) {
			this.buffer.readBoolean();
			return Vector.fromByteBuf(4, this.buffer);
		}
		return null;
	}

	public String username() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND)) {
			return Utils.readString(this.buffer.readByte(), this.buffer);
		}
		return null;
	}
	
	public String virtualHost() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND) && this.hasVhost) {
			Utils.readString(this.buffer.readByte(), this.buffer);
			return Utils.readString(this.buffer.readByte(), this.buffer);
		}
		return null;
	}
	
	public boolean hasVirtualHost() {
		return this.hasVhost;
	}
	
	public static HandshakePacket newOutboundPacket(Channel channel, boolean status, Vector playerColor) {
		return new HandshakePacket(channel, status, playerColor);
	}
	
	public static HandshakePacket newInboundPacket(Channel channel, String username) {
		return new HandshakePacket(channel, username);
	}
	
	public static HandshakePacket newInboundPacket(Channel channel, String username, String virtualHost) {
		return new HandshakePacket(channel, username, virtualHost);
	}
}

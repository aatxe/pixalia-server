package us.aaronweiss.pixalia.server.listeners;

import us.aaronweiss.pixalia.server.core.Pixal;
import us.aaronweiss.pixalia.server.core.Server;
import us.aaronweiss.pixalia.server.packets.HandshakePacket;
import us.aaronweiss.pixalia.server.packets.Packet;
import us.aaronweiss.pixalia.server.packets.PlayerJoinPacket;
import us.aaronweiss.pixalia.server.tools.Utils;

public class HandshakeHandler extends PacketHandler {
	public static final byte OPCODE = HandshakePacket.OPCODE;

	public HandshakeHandler(Server server) {
		super(server);
	}

	@Override
	public Packet process(Packet event) {
		HandshakePacket p = (HandshakePacket) event;
		boolean hostnameStatus = true;
		String defaultHost = Integer.toHexString(event.channel().remoteAddress().hashCode());
		Pixal pixal;
		if (p.hasVirtualHost() && server.isHostnamePresent(p.virtualHost())) {
			hostnameStatus = false;
			pixal = new Pixal(defaultHost, Utils.getRandomColor());
		} else if (p.hasVirtualHost()) {
			pixal = new Pixal(p.virtualHost(), Utils.getRandomColor());
		} else {
			pixal = new Pixal(defaultHost, Utils.getRandomColor());
		}
		// TODO: attr pixal to p.channel()
		server.registerPlayer(pixal);
		server.getNetwork().writeAllExcept(p.channel(), PlayerJoinPacket.newOutboundPacket(pixal.getHostname(), pixal.getColor()));
		return HandshakePacket.newOutboundPacket(p.channel(), hostnameStatus, pixal.getColor());
	}
}

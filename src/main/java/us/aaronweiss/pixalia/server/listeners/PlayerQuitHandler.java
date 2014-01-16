package us.aaronweiss.pixalia.server.listeners;

import us.aaronweiss.pixalia.server.core.Network;
import us.aaronweiss.pixalia.server.core.Pixal;
import us.aaronweiss.pixalia.server.core.Server;
import us.aaronweiss.pixalia.server.packets.Packet;
import us.aaronweiss.pixalia.server.packets.PlayerQuitPacket;

public class PlayerQuitHandler extends PacketHandler {
	public static final byte OPCODE = PlayerQuitPacket.OPCODE;

	public PlayerQuitHandler(Server server) {
		super(server);
	}

	@Override
	public Packet process(Packet event) {
		PlayerQuitPacket p = (PlayerQuitPacket) event;
		Pixal px = p.channel().attr(Network.getChannelPixalAttr()).get();
		server.getNetwork().writeAllExcept(p.channel(), PlayerQuitPacket.newOutboundPacket(px.getHostname()));
		server.unregisterPlayer(px);
		p.channel().close();
		return PlayerQuitPacket.newOutboundPacket(px.getHostname());
	}
}

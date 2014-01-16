package us.aaronweiss.pixalia.server.listeners;

import io.netty.util.AttributeKey;
import us.aaronweiss.pixalia.server.core.Network;
import us.aaronweiss.pixalia.server.core.Pixal;
import us.aaronweiss.pixalia.server.core.Server;
import us.aaronweiss.pixalia.server.packets.MovementPacket;
import us.aaronweiss.pixalia.server.packets.Packet;
import us.aaronweiss.pixalia.server.tools.Constants;

public class MovementHandler extends PacketHandler {
	public static final byte OPCODE = MovementPacket.OPCODE;

	public MovementHandler(Server server) {
		super(server);
	}

	@Override
	public Packet process(Packet event) {
		MovementPacket p = (MovementPacket) event;
		Pixal px = p.channel().attr(Network.getChannelPixalAttr()).get();
		// TODO: validate movement, maybe?
		px.setPosition(p.position());
		return MovementPacket.newOutboundPacket(px.getHostname(), px.getPosition());
	}
}

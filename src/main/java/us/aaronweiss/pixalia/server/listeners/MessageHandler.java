package us.aaronweiss.pixalia.server.listeners;

import us.aaronweiss.pixalia.server.core.Network;
import us.aaronweiss.pixalia.server.core.Pixal;
import us.aaronweiss.pixalia.server.core.Server;
import us.aaronweiss.pixalia.server.packets.MessagePacket;
import us.aaronweiss.pixalia.server.packets.Packet;

public class MessageHandler extends PacketHandler {
	public static final byte OPCODE = MessagePacket.OPCODE;

	public MessageHandler(Server server) {
		super(server);
	}

	@Override
	public Packet process(Packet event) {
		MessagePacket p = (MessagePacket) event;
		Pixal px = (Pixal) p.channel().attr(Network.getChannelPixalAttr()).get();
		if (p.message().length() == 0)
			return null;
		return MessagePacket.newOutboundPacket(px.getHostname(), p.message());
	}
}

package us.aaronweiss.pixalia.server.listeners;

import us.aaronweiss.pixalia.server.core.Server;
import us.aaronweiss.pixalia.server.packets.Packet;

public abstract class PacketHandler {
	protected final Server server;

	public PacketHandler(Server server) {
		this.server = server;
	}

	public abstract Packet process(Packet event);
}

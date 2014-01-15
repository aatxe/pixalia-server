package us.aaronweiss.pixalia.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.aaronweiss.pixalia.PixaliaServer;
import us.aaronweiss.pixalia.server.net.PacketDecoder;
import us.aaronweiss.pixalia.server.net.PacketEncoder;
import us.aaronweiss.pixalia.server.net.PixaliaServerHandler;
import us.aaronweiss.pixalia.server.packets.Packet;
import us.aaronweiss.pixalia.server.tools.Constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static Server instance;
	private final Network network;
	private final Map<String, Pixal> world;
	
	protected Server() {
		network = new Network(this);
		world = new HashMap<>();
	}

	public void registerPlayer(Pixal pixal) {
		world.put(pixal.getHostname(), pixal);
	}

	public boolean isHostnamePresent(String hostname) {
		return world.containsKey(hostname);
	}

	public Iterator<Pixal> worldIterator() {
		return world.values().iterator();
	}

	public Network getNetwork() {
		return this.network;
	}

	public void bind(int port) throws InterruptedException {
		network.bind(port);
	}

	public void shutdown() {
		network.shutdown();
	}

	public static Server getInstance() {
		if (Server.instance == null) {
			Server.instance = new Server();
		}
		return Server.instance;
	}
}

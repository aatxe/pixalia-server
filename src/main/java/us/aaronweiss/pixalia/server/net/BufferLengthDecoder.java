package us.aaronweiss.pixalia.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.server.tools.Utils;

public class BufferLengthDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(BufferLengthDecoder.class);
    
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		logger.info(Utils.toHexString(in.array()));
		if (in.readableBytes() < 3)
			return null;
		int length = in.readInt();
		if (in.readableBytes() < length)
			return null;
		return in.readBytes(length);
	}
}

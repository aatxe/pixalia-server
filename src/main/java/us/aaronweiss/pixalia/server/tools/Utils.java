package us.aaronweiss.pixalia.server.tools;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * A set of static utilities for Pixalia.
 * @author Aaron Weiss
 * @version 1.0
 * @since alpha
 */
public class Utils {
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	private static final CharsetDecoder utfDecoder = Charset.forName("UTF-8").newDecoder();

	public static String readString(int length, ByteBuf b) {
		try {
			byte[] data = new byte[length];
			b.readBytes(data);
			return utfDecoder.decode(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)).toString();
		} catch (CharacterCodingException e) {
			logger.error("Failed to load UTF String in buffer.", e);
		}
		return null;
	}

	public static String toHexString(ByteBuf buf) {
		byte[] data = new byte[buf.capacity()];
		buf.getBytes(0, data);
		return toHexString(data);
	}
	
	public static String toHexString(byte[] array) {
		String ret = "";
		for (byte b : array) {
			String s = Integer.toHexString(b);
			ret += ((s.length() == 1) ? "0" : "") + s + " ";
		}
		return ret;
	}

	public static Vector getRandomColor() {
		return new Vector((float) Math.random(), (float) Math.random(), (float) Math.random());
	}
}

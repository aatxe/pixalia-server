package us.aaronweiss.pixalia.server.tools;

import io.netty.buffer.ByteBuf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * A set of static utilities for Pixalia.
 * @author Aaron Weiss
 * @version 1.0
 * @since alpha
 */
public class Utils {
	public static String readString(int length, ByteBuf b) {
		return b.readBytes(length).toString();
	}
	
	public static String toHexString(byte[] array) {
		String ret = "";
		for (byte b : array) {
			ret += Integer.toHexString(b) + " ";
		}
		return ret;
	}
}

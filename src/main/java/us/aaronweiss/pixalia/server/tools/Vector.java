package us.aaronweiss.pixalia.server.tools;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Vector {
	private float x, y, z, w;
	
	public Vector(float x) {
		this(x, 0f);
	}
	
	public Vector(float x, float y) {
		this(x, y, 0f);
	}
	
	public Vector(float x, float y, float z) {
		this(x, y, z, 0f);
	}
	
	public Vector(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getR() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public float getG() {
		return this.y;
	}
	
	public float getZ() {
		return this.z;
	}
	
	public float getB() {
		return this.z;
	}
	
	public float getW() {
		return this.w;
	}
	
	public float getA() {
		return this.w;
	}
	
	public float[] asFloatArray(int n) {
		float vec[] = new float[n];
		if (n >= 4) {
			vec[0] = x;
			vec[1] = y;
			vec[2] = z;
			vec[3] = w;
		} else if (n == 3) {
			vec[0] = x;
			vec[1] = y;
			vec[2] = z;
		} else if (n == 2) {
			vec[0] = x;
			vec[1] = y;
		} else if (n == 1) {
			vec[0] = x;
		} else {
			return null;
		}
		return vec;
	}
	
	public ByteBuf asByteBuf(int n) {
		ByteBuf ret = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
		float vec[] = this.asFloatArray(n);
		for (float f : vec) {
			ret.writeFloat(f);
		}
		return ret;
	}
	
	public static Vector fromByteBuf(int n, ByteBuf b) {
		Vector ret;
		switch (n) {
		case 1:
			ret = new Vector(b.readFloat());
			break;
		case 2:
			ret = new Vector(b.readFloat(), b.readFloat());
			break;
		case 3:
			ret = new Vector(b.readFloat(), b.readFloat(), b.readFloat());
			break;
		default:
		case 4:
			ret = new Vector(b.readFloat(), b.readFloat(), b.readFloat(), b.readFloat());
			break;
		}
		return ret;
	}
}

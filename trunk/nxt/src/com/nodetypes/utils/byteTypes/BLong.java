package com.nodetypes.utils.byteTypes;

public class BLong {
	
	public static final int LENGTH = 8;

	public static byte[] toByteArray(long l, boolean msbFirst) {
		return BIntType.toByteArray(l, msbFirst, LENGTH);
	}

	public static long fromByteArray(byte[] byteArray, boolean msbFirst) {

		if (byteArray.length != LENGTH) {
			throw new RuntimeException("wrong dimension of byteArray");
		}

		return BIntType.fromByteArray(byteArray, msbFirst, LENGTH);
	}
}

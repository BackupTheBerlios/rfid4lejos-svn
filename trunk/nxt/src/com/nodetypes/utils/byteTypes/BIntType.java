package com.nodetypes.utils.byteTypes;

public abstract class BIntType {

	protected static byte[] toByteArray(long l, boolean msbFirst, int length) {
		byte[] b = new byte[length];

		if (msbFirst) {
			for (int i = 0; i < length; i++) {
				b[i] = (byte) ((l >> 8 * (length - i - 1)) & 0xFF);
			}
		} else {
			for (int i = 0; i < length; i++) {
				b[i] = (byte) ((l >> 8 * i) & 0xFF);
			}
		}

		return b;
	}

	protected static long fromByteArray(byte[] byteArray, boolean msbFirst,
			int length) {
		long ret = 0;

		if (msbFirst) {
			for (int j = 0; j < length; j++) {
				ret = ret
						| (((long) (byteArray[j] & 0xFF)) << ((length - 1 - j) * 8));
			}
		} else {
			for (int j = 0; j < length; j++) {
				ret = ret | (((long) (byteArray[j] & 0xFF)) << (j * 8));
			}
		}

		return ret;
	}

}

package de.berlios.rfid4lejos.iso15693.test;

/**
 * this class is necessary since lejos doesn't support String.format() methods
 * 
 * @author Arne Bosien
 * 
 */
public class ByteFormatter {

	private static final String[] strArray = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	/**
	 * creates corresponding string from given byte, e.g. 0xFF -> "FF"
	 * 
	 * @param b
	 * @return string representation
	 */
	public static String toHexString(byte b) {
		return strArray[(b >> 4) & 0xF] + strArray[b & 0x0F];
	}

	public static String toHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(16);

		for (int i = 0; i < b.length; i++) {
			sb.append(toHexString(b[i]));
		}

		return sb.toString();
	}

	public static String toHexString(long l) {
		StringBuffer sb = new StringBuffer(16);
		for (int i = 0; i < 8; i++) {
			sb.append(strArray[(int) ((l >> 8 * (8 - i - 1) + 4) & 0xF)]);
			sb.append(strArray[(int) ((l >> 8 * (8 - i - 1)) & 0xF)]);
		}

		return sb.toString();
	}
}

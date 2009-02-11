package de.berlios.rfid4lejos.iso15693.api;

public interface ISO15693Tag {

	/**
	 * @return 64 bits ID of tag
	 */
	public long getID();

	/**
	 * Before this method is called, is must be assured, that the data has been
	 * read from the real tag, otherwise the data is 0
	 * 
	 * @see RFIDReader#readTagData(ISO15693Tag)
	 * @see RFIDReader#readTagData(ISO15693Tag, int, int)
	 * @see RFIDReader#writeTagData(ISO15693Tag, int, int)
	 * 
	 * @return content of tag
	 */
	public byte[] getContents();

	/**
	 * set each data byte to 0
	 */
	public void erase();

	/**
	 * copies given data to tag contents
	 * 
	 * @param startByte
	 *            index of byte where start at contents
	 * @param data
	 *            data to be copied
	 */
	public void setContents(int startByte, byte[] data);

	/**
	 * 0x00 means unknown
	 * 
	 * @return ID of manufacturer
	 */
	public byte getManufacturer();

	/**
	 * @return number of bytes in one block
	 */
	public int getBlockSize();

	/**
	 * @return no of writable bytes = noOfBlocks * blockSize
	 */
	public int getTagCapacity();

	/**
	 * @return no of writable blocks
	 */
	public int getNoOfBlocks();

}

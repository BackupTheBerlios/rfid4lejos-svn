package de.berlios.rfid4lejos.iso15693.api;

/**
 * an ISO-15693 RFID reader
 * 
 * @author Arne Bosien
 * 
 */
public interface RFIDReader {

	/**
	 * performs a normal inventory
	 * 
	 * @return all found tags in range of he RFID reader
	 */
	public ISO15693Tag[] inventory();

	/**
	 * updates the whole data field of given tag
	 * 
	 * @param rfidTag
	 * @return true if update was successful
	 */
	public boolean readTagData(ISO15693Tag rfidTag);

	/**
	 * 
	 * @param rfidTag
	 *            tag that should be updated
	 * @param startByte
	 *            index of first byte to read
	 * @param length
	 *            number of bytes to read
	 * @return true if update was successful
	 */
	public boolean readTagData(ISO15693Tag rfidTag, int startByte, int length);

	/**
	 * write data to real tag and update RFIDTag
	 * 
	 * @param rfidTag
	 *            tag that should be updated
	 * @param startByte
	 *            index of first byte to write
	 * @param length
	 *            number of bytes to write
	 * @return true if update was successful
	 */
	public boolean writeTagData(ISO15693Tag rfidTag, int startByte, int length);

	/**
	 * activates the RFID reader
	 */
	public void activate();

	/**
	 * deactivates the rfid reader
	 */
	public void deactivate();

	/**
	 * @see #activate()
	 * @see #deactivate()
	 * @return true if reader is active
	 */
	public boolean isActive();

}

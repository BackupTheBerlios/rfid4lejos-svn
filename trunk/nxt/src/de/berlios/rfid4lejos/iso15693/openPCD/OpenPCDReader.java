package de.berlios.rfid4lejos.iso15693.openPCD;

import com.nodetypes.utils.byteTypes.BLong;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import de.berlios.rfid4lejos.iso15693.api.ISO15693Tag;
import de.berlios.rfid4lejos.iso15693.api.RFIDReader;
import de.berlios.rfid4lejos.iso15693.api.TagFactory;

/**
 * check parameters and active state, maybe should throw exceptions:
 * communicationError, readerStateError, ...
 */
public class OpenPCDReader extends I2CSensor implements RFIDReader {

	private static final int CMD_GETINVENTORY = 0x41;
	private static final int CMD_WRITETAGID = 0x43;
	private static final int CMD_READTAG = 0x44;
	private static final int CMD_WRITETAG = 0x45;
	private static final int CMD_READSTATUSCODE = 0x46;
	private static final int CMD_RDROFF = 0x47;
	private static final int CMD_RDRON = 0x48;

	private static final byte STAT_OK = 0x00;
	private static final byte STAT_BUSY = 0x01;
	private static final byte STAT_FAIL = 0x02;

	private static final int ERR_UNKNOWN = 0xFF;
	private static final int sleeptime = 20;

	private boolean active;
	private TagFactory tagFactory;

	public OpenPCDReader(I2CPort port, TagFactory tagFactory) {
		super(port);

		this.tagFactory = tagFactory;
		active = false;
	}

	public void deactivate() {
		sendData(CMD_RDROFF, (byte) 0);
		try {
			Thread.sleep(sleeptime);
		} catch (InterruptedException e) {
		}

		active = false;
	}

	public void activate() {
		sendData(CMD_RDRON, (byte) 0);
		try {
			Thread.sleep(sleeptime);
		} catch (InterruptedException e) {
		}

		active = true;
	}

	public ISO15693Tag[] inventory() {
		byte[] recData = new byte[8 * 8 + 1];
		int length = 0;

		// get data
		if (getData(CMD_GETINVENTORY, recData, length) != 0) {
			return null;
		}

		// create tags
		ISO15693Tag[] tags = new ISO15693Tag[(length - 1) / 8];
		byte[] id = new byte[8];

		for (int i = 0; i < tags.length; i++) {
			System.arraycopy(recData, 1 + i * 8, id, 0, 8);
			tags[i] = tagFactory.createTag(recData);
		}

		return tags;
	}

	public boolean readTagData(ISO15693Tag rfidTag) {
		return readTagData(rfidTag, 0, rfidTag.getTagCapacity());
	}

	public boolean readTagData(ISO15693Tag rfidTag, int startByte, int length) {
		// TODO: fix this workaround
		startByte = 0;
		length = length + startByte;
		// end workaround

		// send tag ID
		byte[] sendData = BLong.toByteArray(rfidTag.getID(), true);

		if (sendData(CMD_WRITETAGID, sendData, sendData.length) != 0) {
			return false;
		}

		// receive tag data
		byte[] recData = new byte[1 + length];
		int recLength = 0;
		if (getData(CMD_READTAG, recData, recLength) != 0) {
			return false;
		}

		// copy received data to tag
		if (recData[0] == STAT_OK) {
			byte[] tagData = new byte[recLength - 1];
			System.arraycopy(sendData, 1, tagData, 0, tagData.length);
			rfidTag.setContents(startByte, recData);
		} else {
			return false;
		}

		return true;
	}

	public boolean writeTagData(ISO15693Tag rfidTag, int startByte, int length) {
		byte[] sendData;

		// send tag ID
		sendData = BLong.toByteArray(rfidTag.getID(), true);
		if (sendData(CMD_WRITETAGID, sendData, sendData.length) != 0) {
			return false;
		}

		// send tag contents
		if (startByte != 0) {
			sendData = new byte[length];
			System.arraycopy(rfidTag.getContents(), startByte, sendData, 0,
					length);
		} else {
			sendData = rfidTag.getContents();
		}

		if (sendData(CMD_WRITETAG, sendData, length) != 0) {
			return false;
		}

		return true;
	}

	public boolean isActive() {
		return active;
	}

}

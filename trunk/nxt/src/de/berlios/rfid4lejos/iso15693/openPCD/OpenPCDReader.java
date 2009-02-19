package de.berlios.rfid4lejos.iso15693.openPCD;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;

import com.nodetypes.utils.byteTypes.BLong;

import de.berlios.rfid4lejos.iso15693.api.CommunicationException;
import de.berlios.rfid4lejos.iso15693.api.ISO15693Tag;
import de.berlios.rfid4lejos.iso15693.api.RFIDReader;
import de.berlios.rfid4lejos.iso15693.api.ReaderStateException;
import de.berlios.rfid4lejos.iso15693.api.TagFactory;
import de.berlios.rfid4lejos.iso15693.test.ByteFormatter;

/**
 * TODO: check parameters, read/write return always true if no exception occurs
 * 
 * @author Arne Bosien
 */
public class OpenPCDReader extends I2CSensor implements RFIDReader {

	private static final int CMD_GETINVENTORY = 0x41;
	private static final int CMD_WRITETAGID = 0x43;
	private static final int CMD_READTAG = 0x44;
	private static final int CMD_WRITETAG = 0x45;
	// private static final int CMD_READSTATUSCODE = 0x46;
	private static final int CMD_RDROFF = 0x47;
	private static final int CMD_RDRON = 0x48;

	private static final byte STAT_OK = 0x00;
	// private static final byte STAT_BUSY = 0x01;
	// private static final byte STAT_FAIL = 0x02;

	// private static final int ERR_UNKNOWN = 0xFF;
	private static final int sleeptime = 20;

	private boolean active;
	private TagFactory tagFactory;

	public OpenPCDReader(I2CPort port, TagFactory tagFactory) {
		super(port);

		this.tagFactory = tagFactory;
		active = false;
	}

	public synchronized void deactivate() {
		sendData(CMD_RDROFF, (byte) 0);
		try {
			Thread.sleep(sleeptime);
		} catch (InterruptedException e) {
		}

		active = false;
	}

	public synchronized void activate() {
		sendData(CMD_RDRON, (byte) 0);
		try {
			Thread.sleep(sleeptime);
		} catch (InterruptedException e) {
		}

		active = true;
	}

	public synchronized ISO15693Tag[] inventory() throws ReaderStateException,
			CommunicationException {
		if (!isActive())
			throw new ReaderStateException(
					"inventory failed, reader is not active");

		byte[] recData = new byte[8 * 8 + 2];

		// get data
		if (getData(CMD_GETINVENTORY, recData, recData.length) != 0) {
			throw new CommunicationException();
		}

		if (recData[0] != STAT_OK) {
			throw new ReaderStateException("inventory failed, Stat: "
					+ recData[0]);
		}

		// create tags
		ISO15693Tag[] tags = new ISO15693Tag[recData[1]];
		byte[] id = new byte[8];

		for (int i = 0; i < tags.length && 2 + i * 8 + 8 < 8 * 8 + 2; i++) {
			System.arraycopy(recData, 2 + i * 8, id, 0, 8);
			tags[i] = tagFactory.createTag(id);
		}

		return tags;
	}

	public boolean readTagData(ISO15693Tag rfidTag)
			throws ReaderStateException, CommunicationException {
		return readTagData(rfidTag, 0, rfidTag.getTagCapacity());
	}

	public synchronized boolean readTagData(ISO15693Tag rfidTag, int startByte,
			int length) throws ReaderStateException, CommunicationException {
		if (!isActive())
			throw new ReaderStateException(
					"reading failed, reader is not active");

		// TODO: fix this workaround
		length += startByte;
		startByte = 0;
		// end workaround

		// send tag ID
		byte[] sendData = BLong.toByteArray(rfidTag.getID(), true);

		if (sendData(CMD_WRITETAGID, sendData, sendData.length) != 0) {
			throw new CommunicationException();
		}

		// receive tag data
		byte[] recData = new byte[1 + length];

		if (getData(CMD_READTAG, recData, recData.length) != 0) {
			throw new CommunicationException();
		}

		// copy received data to tag
		if (recData[0] == STAT_OK) {
			byte[] tagData = new byte[length];
			System.arraycopy(recData, 1, tagData, 0, tagData.length);
			rfidTag.setContents(startByte, tagData);
		} else {
			throw new ReaderStateException("couldn't read tag "
					+ ByteFormatter.toHexString(rfidTag.getID()) + " Stat: "
					+ recData[0]);
		}

		return true;
	}

	public synchronized boolean writeTagData(ISO15693Tag rfidTag,
			int startByte, int length) throws ReaderStateException,
			CommunicationException {
		if (!isActive())
			throw new ReaderStateException(
					"writing failed, reader is not active");

		// TODO: fix this workaround
		length = length + startByte;
		startByte = 0;
		// end workaround

		byte[] sendData;

		// send tag ID
		sendData = BLong.toByteArray(rfidTag.getID(), true);
		if (sendData(CMD_WRITETAGID, sendData, sendData.length) != 0) {
			throw new CommunicationException();
		}

		// send tag contents
		if (startByte != 0) {
			sendData = new byte[length];
			System.arraycopy(rfidTag.getContents(), startByte, sendData, 0,
					sendData.length);
		} else {
			sendData = rfidTag.getContents();
		}

		if (sendData(CMD_WRITETAG, sendData, sendData.length) != 0) {
			throw new CommunicationException();
		}

		return true;
	}

	public boolean isActive() {
		return active;
	}

}

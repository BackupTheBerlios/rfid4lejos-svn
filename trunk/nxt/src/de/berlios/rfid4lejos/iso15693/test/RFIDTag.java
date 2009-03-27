package de.berlios.rfid4lejos.iso15693.test;

import de.berlios.rfid4lejos.iso15693.api.ISO15693Tag;

/**
 * simple RFIDTag, most methods are not implemented
 * 
 * @author Arne Bosien
 * 
 */
public class RFIDTag implements ISO15693Tag {
	private long ID;
	private byte[] contents;
	private boolean dirty;

	public RFIDTag(long ID) {
		this.ID = ID;

		contents = new byte[128];
		dirty = true;
	}

	public void erase() {
		for (int i = 0; i < contents.length; i++)
			contents[i] = 0;
		dirty = true;
	}

	public int getBlockSize() {
		return 0;
	}

	public byte[] getContents() {
		return contents;
	}

	public long getID() {
		return ID;
	}

	public byte getManufacturer() {
		return 0;
	}

	public int getNoOfBlocks() {
		return 0;
	}

	public int getTagCapacity() {
		return 0;
	}

	public void setContents(int startByte, byte[] data) {
		// copy all data and check that there is not index out of bounds
		int length = data.length < contents.length - startByte ? data.length
				: contents.length - startByte;

		System.arraycopy(data, 0, contents, startByte, length);
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setIsDirty(boolean dirty) {
		this.dirty = dirty;
		
	}

	public void setContents(int startByte, byte data) {
		// TODO Auto-generated method stub
		
	}

}

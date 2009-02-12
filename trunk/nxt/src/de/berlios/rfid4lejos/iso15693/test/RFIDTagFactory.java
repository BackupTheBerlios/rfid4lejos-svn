package de.berlios.rfid4lejos.iso15693.test;

import com.nodetypes.utils.byteTypes.BLong;

import de.berlios.rfid4lejos.iso15693.api.ISO15693Tag;
import de.berlios.rfid4lejos.iso15693.api.TagFactory;

public class RFIDTagFactory extends TagFactory {

	public ISO15693Tag createTag(byte[] id) {
		return new RFIDTag(BLong.fromByteArray(id, true));
	}

}

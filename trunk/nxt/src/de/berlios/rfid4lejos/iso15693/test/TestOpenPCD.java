package de.berlios.rfid4lejos.iso15693.test;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import de.berlios.rfid4lejos.iso15693.api.CommunicationException;
import de.berlios.rfid4lejos.iso15693.api.ISO15693Tag;
import de.berlios.rfid4lejos.iso15693.api.RFIDReader;
import de.berlios.rfid4lejos.iso15693.api.ReaderStateException;
import de.berlios.rfid4lejos.iso15693.api.TagFactory;
import de.berlios.rfid4lejos.iso15693.openPCD.OpenPCDReader;

/**
 * Test class for communication with RFID OpenPCD reader
 * 
 * @author Arne Bosien
 * 
 */
public class TestOpenPCD {

	public static void main(String[] args) {
		TagFactory tf = new RFIDTagFactory();
		ISO15693Tag[] tags;

		RFIDReader reader = new OpenPCDReader(SensorPort.S1, tf);

		while (true) {
			try {
				// do inventory
				tags = reader.inventory();

				// print IDs
				for (int i = 0; i < tags.length; i++) {
					LCD.drawString(ByteFormatter.toHexString(tags[i].getID()),
							0, i % 6);
				}

			} catch (ReaderStateException e) {
			} catch (CommunicationException e) {
			} // try-catch

		} // while
	} // main

}

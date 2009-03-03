package de.berlios.rfid4lejos.iso15693.test;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import de.berlios.rfid4lejos.iso15693.api.CommunicationException;
import de.berlios.rfid4lejos.iso15693.api.ISO15693Tag;
import de.berlios.rfid4lejos.iso15693.api.ReaderStateException;
import de.berlios.rfid4lejos.iso15693.api.TagFactory;
import de.berlios.rfid4lejos.iso15693.openPCD.OpenPCDReader;

/**
 * Test class for communication with RFID OpenPCD reader
 * 
 * @author Arne Bosien
 * 
 */
public class TestOpenPCD implements ButtonListener {
	public TestOpenPCD() {
		Button.ESCAPE.addButtonListener(this);

		int counter = 0;

		TagFactory tf = new RFIDTagFactory();
		ISO15693Tag[] tags;

		OpenPCDReader reader = new OpenPCDReader("", SensorPort.S1, tf);

		reader.activate();

		while (true) {
			try {
				// do inventory
				tags = reader.inventory();

				// print IDs
				LCD.clear();
				LCD.drawString("IDs " + tags.length + " " + counter, 0, 0);
				for (int i = 0; i < tags.length; i++) {
					LCD.drawString(ByteFormatter.toHexString(tags[i].getID()),
							0, 1 + i % 6);
				}
				LCD.refresh();

			} catch (ReaderStateException e) {
				LCD.clear();
				LCD.drawString("RSEx", 1, 1);

			} catch (CommunicationException e) {
				LCD.clear();
				LCD.drawString("ComEx", 1, 1);
			} // try-catch

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

			counter++;
		} // while
	}

	public static void main(String[] args) {
		new TestOpenPCD();

	} // main

	public void buttonPressed(Button b) {
		System.exit(0);
	}

	public void buttonReleased(Button b) {
	}

}

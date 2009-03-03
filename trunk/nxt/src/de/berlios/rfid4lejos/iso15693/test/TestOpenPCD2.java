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
 * Test class for communication with two RFID OpenPCD reader
 * 
 * @author Arne Bosien
 * 
 */
public class TestOpenPCD2 implements ButtonListener {
	public TestOpenPCD2() {
		Button.ESCAPE.addButtonListener(this);

		int counter = 0;

		TagFactory tf = new RFIDTagFactory();
		ISO15693Tag[] tags;

		OpenPCDReader reader1 = new OpenPCDReader("", SensorPort.S1, tf);
		OpenPCDReader reader2 = new OpenPCDReader("", SensorPort.S4, tf);

		while (true) {
			try {
				// do inventory for first reader
				reader1.activate();
				tags = reader1.inventory();
				reader1.deactivate();

				// print IDs
				LCD.clear();
				LCD.drawString("IDs " + tags.length + " " + counter, 0, 0);
				for (int i = 0; i < tags.length; i++) {
					LCD.drawString(ByteFormatter.toHexString(tags[i].getID()),
							0, 1 + i % 6);
				}
				LCD.refresh();

				// do inventory for second reader
				reader2.activate();
				tags = reader2.inventory();
				reader2.deactivate();

				// print IDs
				LCD.drawString("IDs " + tags.length + " " + counter, 0, 4);
				for (int i = 0; i < tags.length; i++) {
					LCD.drawString(ByteFormatter.toHexString(tags[i].getID()),
							0, 5 + i % 6);
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
		new TestOpenPCD2();

	} // main

	public void buttonPressed(Button b) {
		System.exit(0);
	}

	public void buttonReleased(Button b) {
	}

}

package de.berlios.rfid4lejos.iso15693.test;

import com.nodetypes.utils.byteTypes.BLong;

import de.berlios.rfid4lejos.iso15693.api.CommunicationException;
import de.berlios.rfid4lejos.iso15693.api.ISO15693Tag;
import de.berlios.rfid4lejos.iso15693.api.ReaderStateException;
import de.berlios.rfid4lejos.iso15693.api.TagFactory;
import de.berlios.rfid4lejos.iso15693.openPCD.OpenPCDReader;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

/**
 * Test class for reading and writing with one reader
 * 
 * @author arne
 * 
 */
public class TestOpenPCD3 implements ButtonListener {

	public TestOpenPCD3() {
		Button.ESCAPE.addButtonListener(this);

		int counter = 0;

		TagFactory tf = new RFIDTagFactory();
		ISO15693Tag[] tags;

		OpenPCDReader reader1 = new OpenPCDReader(SensorPort.S1, tf);
		byte[] data;

		while (true) {
			reader1.activate();
			counter++;

			LCD.clear();

			try {
				// inventory
				tags = reader1.inventory();

				LCD.drawString("IDs " + tags.length + " " + counter, 0, 0);

				// write all tags
				data = BLong.toByteArray((long)counter, true);

				for (int i = 0; i < tags.length; i++) {
					tags[i].setContents(0, data);
					reader1.writeTagData(tags[i], 0, data.length);
				}

				// read contents of all tags
				for (int i = 0; i < tags.length; i++) {
					reader1.readTagData(tags[i], 0, data.length);

					LCD.drawString(ByteFormatter.toHexString(tags[i].getID()),
							0, 1);

					byte[] con = tags[i].getContents();

					for (int j = 0; j < data.length; j++) {
						LCD.drawString(ByteFormatter.toHexString(con[j]),
								(j * 2) % 16, j / 16 + 2);
					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
				}
			} catch (ReaderStateException e) {
				LCD.clear();
				LCD.drawString("RSEx", 1, 1);

			} catch (CommunicationException e) {
				LCD.clear();
				LCD.drawString("ComEx", 1, 1);
			} // try-catch

			reader1.deactivate();

		}
	}

	public static void main(String[] arg) {
		new TestOpenPCD3();
	}

	public void buttonPressed(Button b) {
		System.exit(0);
	}

	public void buttonReleased(Button b) {
	}
}

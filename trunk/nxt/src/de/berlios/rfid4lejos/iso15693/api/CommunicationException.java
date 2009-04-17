package de.berlios.rfid4lejos.iso15693.api;

/**
 * Error while communication with the reader
 * 
 * @author Arne Bosien
 * 
 */
public class CommunicationException extends Exception {
	public CommunicationException(String msg) {
		super(msg);
	}

	public CommunicationException() {
		super();
	}
}

package de.berlios.rfid4lejos.iso15693.api;

/**
 * reader was not in corresponding state to process the task
 * 
 * @author Arne Bosien
 * 
 */
public class ReaderStateException extends Exception {
	public ReaderStateException(String msg){
		super(msg);
	}
	
	public ReaderStateException(){
	}
}

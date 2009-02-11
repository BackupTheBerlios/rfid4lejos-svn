package de.berlios.rfid4lejos.iso15693.api;

public abstract class TagFactory {
	public abstract ISO15693Tag createTag(byte[] id);
}

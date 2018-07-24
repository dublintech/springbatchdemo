package com.prototypes.aug.extract.pojos;

public class DogPojo {
	private final String dogName;
	private final String ownerName;

	public DogPojo(String dogName, String ownerName) {
		this.dogName = dogName;
		this.ownerName = ownerName;
	}

	@Override
	public String toString() {
		return "DogPojo{" +
				"dogName=" + dogName + 
				"ownerName=" + ownerName + 
				'}';
	}
	
	public String getDogName() {
		return dogName;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
}

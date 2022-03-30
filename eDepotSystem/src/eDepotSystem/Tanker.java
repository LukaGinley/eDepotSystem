package eDepotSystem;

import java.util.Date;

public class Tanker extends Vehicle {
	private int liquidCapacity;
	private String liquidType;

	public Tanker(String make, String model, int weight, String regNo, Date moveDate, int liquidCapacity, String liquidType) {
			super(make, model, weight, regNo, moveDate);
			this.liquidCapacity = liquidCapacity;
			this.liquidType = liquidType;
	}
	
	public String toString() {
		return "Make: " + make +
				" | Model: " + model +
				" | Weight: " + weight +
				" | Reg No: " + regNo + 
				" | Liquid Capacity: " + liquidCapacity + 
				" | Liquid Type: " + liquidType + "\n";
	}
		
//--- Getters and Setters ---//
		
	public String getMake() {
		return make;
	}
		
	public String getModel() {
		return model;
	}
		
	public int getWeight() {
		return weight;
	}
		
	public String getRegNo() {
		return regNo;
	}
		
	public int getLiquidCapacity() {
		return liquidCapacity;
	}
		
	public String getLiquidType() {
		return liquidType;
	}
		
	public Date getMoveDate() {
		return moveDate;
	}
}

package eDepotSystem;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
	
public abstract class Vehicle implements Serializable {
		protected String make, model, regNo;
		protected int weight;
		protected Date moveDate;
		
		protected ArrayList<WorkSchedule> arraySchedule = new ArrayList<WorkSchedule>();
		
		public Vehicle(String make, String model, int weight, String regNo, Date moveDate) {
			this.make = make;
			this.model = model;
			this.weight = weight;
			this.regNo = regNo;
			this.moveDate = moveDate;
		}
		
//-- Vehicle Availability --//
				
	public boolean isAvailable() {
		for(WorkSchedule schedule : this.arraySchedule) {
			if(schedule.getScheduleState() == eDepotSystem.WorkSchedule.ScheduleState.PENDING) {
				return false;
			}
			if(schedule.getScheduleState() == eDepotSystem.WorkSchedule.ScheduleState.ACTIVE) {
				return false;
			}
		}
		return false;
	}
	
	public void setSchedule (WorkSchedule workSchedule) {
		addSchedule(workSchedule);
		workSchedule.setVehicle(this);
	}
	
	public void addSchedule(WorkSchedule schedule) {
		this.arraySchedule.add(schedule);
	}
	
//--- Getters and Setters---//
	
	public ArrayList<WorkSchedule> getSchedule() {
		return arraySchedule;
	}
	
	public String getRegNo() {
		return regNo;
	}
	
	public Date getMoveDate() {
		return moveDate;
	}
	
	public String getMake() {
		return make;
	}
	
	public String getModel() {
		return model;
	}
	
	public int getWeight() {
		return weight;
	}
}
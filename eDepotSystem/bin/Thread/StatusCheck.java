package Thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eDepotSystem.Driver;
import eDepotSystem.Vehicle;
import eDepotSystem.WorkSchedule;

public class StatusCheck implements Runnable {
	private List<WorkSchedule> schedule;
	private Integer delay;

	public StatusCheck(List<WorkSchedule> arraySchedule, Integer seconds) {
		this.schedule = arraySchedule;
		setSeconds(seconds);
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(delay);
				synchronized (schedule) {
					Date currentDate = new Date();
					for (WorkSchedule s : schedule) {
						if(s.getStartDate().before(currentDate) && (s.getEndDate().before(currentDate))){
							s.updateState();
							Driver driver = s.getDriver();
							ArrayList<WorkSchedule> driverSchedule = driver.getSchedule();
							for(int i = 0; i < driverSchedule.size(); i++ ) {
								if(driverSchedule.get(i).getID() == s.getID()) {
									driverSchedule.get(i).updateState();
								}
							}
							Vehicle vehicle = s.getVehicle();
							ArrayList<WorkSchedule> vehicleSchedule = vehicle.getSchedule();
							for(int i = 0; i < vehicleSchedule.size(); i++) {
								if(vehicleSchedule.get(i).getID() == s.getID()) {
									vehicleSchedule.get(i).updateState();
								}
							}
						}
						if(s.getStartDate().before(currentDate) && (s.getEndDate().before(currentDate))){
							s.updateState();
							Driver driver = s.getDriver();
							ArrayList<WorkSchedule> driverSchedule = driver.getSchedule();
							for(int i = 0; i < driverSchedule.size(); i++ ) {
								if(driverSchedule.get(i).getID() == s.getID()) {
									driverSchedule.get(i).updateState();
								}
							}
							Vehicle vehicle = s.getVehicle();
							ArrayList<WorkSchedule> vehicleSchedule = vehicle.getSchedule();
							for(int i = 0; i < vehicleSchedule.size(); i++) {
								if(vehicleSchedule.get(i).getID() == s.getID()) {
									vehicleSchedule.get(i).updateState();
								}
							}
						}
					}
					
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void setSeconds(Integer seconds) {
		delay = seconds * 1000;
	}

	public Integer getSeconds() {
		return delay / 1000;
	}

}

package eDepotSystem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import Thread.StatusCheck;
import Thread.TankerCheck;
import Thread.TruckCheck;
import eDepotSystem.WorkSchedule.ScheduleState;

public final class Depot implements Serializable {
		private static Scanner scan;
		private String depotName;
		private boolean userAuthenticate;
		private List<WorkSchedule> arraySchedule = Collections.synchronizedList(new ArrayList<WorkSchedule>());
		private List<Driver> arrayDrivers = Collections.synchronizedList(new ArrayList<Driver>());
		private List<Tanker> arrayTanker = Collections.synchronizedList(new ArrayList<Tanker>());
		private List<Truck> arrayTruck = Collections.synchronizedList(new ArrayList<Truck>());
		public Driver userAccount;
	
		
		public Depot(String depotName) {
			userAuthenticate = false;
			this.depotName = depotName;
			
		}
		
		public void startCheck () {
			new Thread(new StatusCheck(arraySchedule, 10)).start();
		}
		
		public void startTruckCheck () {
			new Thread(new TruckCheck(arrayTruck, 10)).start();
		}
		
		public void startTankerCheck () {
			new Thread(new TankerCheck(arrayTanker, 10)).start();
		}
		
		public boolean getAuthenticate () {
			return userAuthenticate;
		}
		
		
		
//--- Log On ---//
    
    public void logOn() throws NumberFormatException, ParseException  {
    		scan = new Scanner(System.in);
    			System.out.print("Please enter your username: ");
    			String inputUsername = scan.nextLine();
    			System.out.print("Please enter your password: ");
    			String inputPassword = scan.nextLine();
    			
    			userAuthenticate = authenticateUser(inputUsername, inputPassword);
    			
    			if(userAuthenticate) {
    				System.out.println("Login Successful!");
    			} else {
    				System.out.println("Login Failed!");
    			}
		}
    
    
    
//--- Add Driver ---//
    
    public void addDriver(String username, String password) {
    	arrayDrivers.add(new Driver(username, password));
    }
    
    public void addManager(String username, String password) {
    	arrayDrivers.add(new Manager(username, password));
    }
    
   public Driver getDriver(String driverName) {
	   for(Driver driverAccount : arrayDrivers) {
		   if ((driverAccount.getDriverName()).equals(driverName)) {
			   return driverAccount;
		   }
	   }
	   
	   return null;
   }
   
   public void addNewDriver() {
	   	if (userAccount instanceof Manager) {
		   			System.out.println("Enter Driver's Username: ");
		   			String driverUsername = scan.nextLine();
		   			System.out.println("Enter Driver's Password" );
		   			String driverPassword = scan.nextLine();
		   			addDriver(driverUsername, driverPassword);
		   			
	   				} else {
	   					System.out.println("You need to be a mnager to add a new driver!");
	   				}
   }
   
   
   
//--- Authenticate User ---//
   
   public boolean authenticateUser(String inputUsername, String inputPassword) {
	   userAccount = getDriver(inputUsername);
	   
	   if (userAccount != null) {
		   return userAccount.checkPassword(inputPassword);
	   }
	   else {
		   return false;
	   }
   }
   
   
   
//--- Add Schedule ---//
   
   public void addSchedule(WorkSchedule schedule) {
	   		this.arraySchedule.add(schedule);
   }
    
   public void setUpWorkSchedule() throws Exception {
    	if (userAccount instanceof Manager) {
    			System.out.println("Specify 4 digit Schedule ID: ");
    			int scheduleID = Integer.valueOf(scan.nextLine());
    			System.out.println("Specify Client: ");
    			String client = scan.nextLine();
    			System.out.println("Specify Start Date and Time (dd-mm-yyyy HH:mm): ");
    			String startDate = scan.nextLine();
    			System.out.println("Specify End Date and Time (dd-mm-yyyy HH:mm): ");
    			String endDate = scan.nextLine();
    			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    			Date startDate1 = format.parse(startDate);
    			Date endDate1 = format.parse(endDate);
    			ScheduleState state = ScheduleState.PENDING;
    			System.out.println("Specify Driver: ");
    			String driverString = scan.nextLine();
    			Driver driver = getDriver(driverString);
    			if(driver.isAvailable(startDate1, endDate1)) {
    				System.out.println("Specify Vehicle Reg No: ");
    				String vehicleString = scan.nextLine();
    				Vehicle vehicle = getVehicle(vehicleString);
    				WorkSchedule schedule = new WorkSchedule(scheduleID, client, startDate1, endDate1, state, driver, vehicle);
    				addSchedule(schedule);
    				driver.addSchedule(schedule);
    				vehicle.addSchedule(schedule);
    				System.out.println("Job has been added to the Work Schedule!");
    			}
    			else {
    				System.out.println("Schedule overlaps with an existing job!");
    			}
    		} else {
    			System.out.println("You need to be a manager to setup the work schedule.");
    		}
    	}
    	
   public boolean scanForOverlap(Date date) {
	   for(WorkSchedule schedule : this.arraySchedule) {
		   	if(date.after(schedule.getStartDate())
		   					&& date.before(schedule.getEndDate()))
		   			return true;
	   }
	   return false;
   }
   
   public void viewWorkSchedule() throws NumberFormatException, ParseException {
	   	System.out.println(arraySchedule.toString());
   }
   
   
   
//--- Add Vehicle ---//
   
   public void addNewTruck(Truck newTruck) {
	   		this.arrayTruck.add(newTruck);
   }
   
   public void addNewTank(Tanker newTank) {
	   		this.arrayTanker.add(newTank);
   }
   
   public void addVehicle() throws Exception {
	   		if (userAccount instanceof Manager) {
	   				System.out.println("Specify Type of vehicle [Truck or Tanker] :");
	   				String type = scan.nextLine();
	   				
	   					if (type.equals("Truck")) {
	   						Truck newTruck = getTruck();
	   						addNewTruck(newTruck);
	   					} else if (type.contentEquals("Tanker")) {
	   						Tanker newTank = getTank();
	   						addNewTank(newTank);
	   					} else {
	   						System.out.println("Vehicle type not appropriate");
	   						addVehicle();
	   					}
	   				} else {
	   					System.out.println("You need to be a manager to add a new vehicle");
	   				}
   }
   
   public Tanker getTank() {
		System.out.println("Specify Make :");
		String make = scan.nextLine();
		System.out.println("Specify Model :");
		String newModel = scan.nextLine();
		System.out.println("Specify Weight :");
		int newWeight = Integer.valueOf(scan.nextLine());
		System.out.println("Specify RegNo :");
		String newRegNo = scan.nextLine();
		
		for(int i = 0; i < arrayTanker.size(); i++) {
			if(arrayTanker.get(i).getRegNo().equals(newRegNo)) {
				System.out.println("Vehicle already exists");
				getTank();
			}
		}
		
		System.out.println("Specify Liquid Capacity :");
		int newLiquidCapacity = Integer.valueOf(scan.nextLine());
		System.out.println("Specify Liquid Type :");
		String newLiquidType = scan.nextLine();
		Date newMoveDate = null;
		
		return new Tanker(make, newModel, newWeight, newRegNo, newMoveDate, newLiquidCapacity, newLiquidType);
	}
	
	public Truck getTruck() throws ParseException {
		System.out.println("Specify Make :");
		String make = scan.nextLine();
		System.out.println("Specify Model :");
		String newModel = scan.nextLine();
		System.out.println("Specify Weight :");
		int newWeight = Integer.valueOf(scan.nextLine());
		System.out.println("Specify RegNo :");
		String newRegNo = scan.nextLine();
		
		for(int i = 0; i < arrayTruck.size(); i++) {
			if(arrayTruck.get(i).getRegNo().equals(newRegNo)) {
				System.out.println("Vehicle already exists");
				getTruck();
			}
		}
		
		System.out.println("Specify Cargo Capacity :");
		int newCargoCapacity = Integer.valueOf(scan.nextLine());
		Date newMoveDate = null;
		
		return new Truck(make, newModel, newWeight, newRegNo, newMoveDate, newCargoCapacity); 
	}
	
	
	
//--- Remove Vehicle ---//
   
	public void removeVehicle() {
 		if(userAccount instanceof Manager) {
 			System.out.println(arrayTruck.toString());
 			System.out.println(arrayTanker.toString());
 			
 			System.out.println("Select Vehicle to Remove by RegNo :");
 			String regNoInput = scan.nextLine();
 			Vehicle selectedVehicle = getVehicle(regNoInput);
 			if(selectedVehicle instanceof Truck) {
 				getRemoveTruck(regNoInput);
 			}
 			if(selectedVehicle instanceof Tanker) {
 				getRemoveTanker(regNoInput);
 			}
 		} else {
 			System.out.println("You need to be a manager to remove a vehicle!");
 		}
	} 
 	
 	public void removeVehicleByRegNo(String regNo) {
 		Vehicle selectedVehicle = getVehicle(regNo);
 		if(selectedVehicle instanceof Truck) {
 			getRemoveTruck(regNo);
 		}
 		if(selectedVehicle instanceof Tanker) {
 			getRemoveTanker(regNo);
 		}
 	}
 	
 	public void getRemoveTruck(String regNo) {
 		for (int i = arrayTruck.size() - 1; i >=0; --i) {
 		    final Truck tr = arrayTruck.get(i);
 		    if (tr.regNo.equals(regNo)) {
 		      arrayTruck.remove(i);
 		      return;
 		    } else {   	
 		    	System.out.println("Vehicle does not exist");
 		    	removeVehicle();
 		    }
 		}
 	}
 	
 	public void getRemoveTanker(String regNo) {
 		for (int i = arrayTanker.size() - 1; i >=0; --i) {
 		    final Tanker ta = arrayTanker.get(i);
 		    if (ta.regNo.equals(regNo)) {
 		      arrayTanker.remove(i);
 		      return;
 		    } else {
 		    	System.out.println("Vehicle does not exist");
 		    	removeVehicle();
 		    }
 		}
    }
 	
 	
 	
//--- Move Vehicle Pt1 ---//
   
 	public Vehicle displayVehicleMenu() throws NumberFormatException, ParseException {
		System.out.println(arrayTruck.toString());
		System.out.println(arrayTanker.toString());
		System.out.println("Select Vehicle by RegNo :");
		String regNoInput = scan.nextLine();
		
		Vehicle selectedVehicle = getVehicle(regNoInput);
		System.out.println(selectedVehicle);
		
		if(selectedVehicle.isAvailable()) {
			return selectedVehicle;
		}
		return null;
	}
	
	public void viewVehicles() {
		System.out.println(arrayTruck.toString());
		System.out.println(arrayTanker.toString());
	}
	
    public Vehicle getVehicle(String regNo) {
        for (Vehicle vehicle : arrayTanker) {
            if ((vehicle.getRegNo()).equals(regNo)) {
                return vehicle;
            }
        }
        
        for (Vehicle vehicle : arrayTruck) {
            if ((vehicle.getRegNo()).equals(regNo)) {
                return vehicle;
            }
        }
        return null;
    }
    

    
//--- Move Vehicle Pt2 ---//
   
    public String toString() {
		return "Depot: " + depotName + "\n";
	}

	public void addTanker(String make, String model, int weight, String regNo, Date moveDate, int liquidCapacity, String liquidType) {
		arrayTanker.add(new Tanker(make, model, weight, regNo, moveDate, liquidCapacity, liquidType));
	}
	
	public void addTankerAsVehicle(Vehicle vehicle) {
		for(int i = 0; i < arrayTanker.size(); i++) {
			if(!(arrayTanker.get(i).getRegNo().equals(vehicle.getRegNo()))) {
				arrayTanker.add(new Tanker(vehicle.getMake(), vehicle.getModel(), vehicle.getWeight(), vehicle.getRegNo(), null, ((Tanker) vehicle).getLiquidCapacity(), ((Tanker) vehicle).getLiquidType()));
			}
		}
	}
	
	public void addTruck(String make, String model, int weight, String regNo, Date moveDate, int cargoCapacity) {
		arrayTruck.add(new Truck(make, model, weight, regNo, moveDate, cargoCapacity));
	}
	
	public void addTruckAsVehicle(Vehicle vehicle) {
		for(int i = 0; i < arrayTruck.size(); i++) {
			if(!(arrayTruck.get(i).getRegNo().equals(vehicle.getRegNo()))) {
				arrayTruck.add(new Truck(vehicle.getMake(), vehicle.getModel(), vehicle.getWeight(), vehicle.getRegNo(), null, ((Truck) vehicle).getCargoCapacity()));
			}
		}
	}

	public String getDepotName() {
		return depotName;
	}
   

	public boolean checkifManager() {
		if (userAccount instanceof Manager) {
			return true;
		}
		else { 
			return false;
		}
	}
	
	public boolean checkifDriver() {
		if (userAccount instanceof Driver) {
			return true;
		}
		else { 
			return false;
		}
	}
}


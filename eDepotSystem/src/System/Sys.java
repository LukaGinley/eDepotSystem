package System;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Date;
import java.util.Collections;
import java.util.Arrays;


import Thread.TankerCheck;
import Thread.TruckCheck;
import eDepotSystem.Depot;
import eDepotSystem.Tanker;
import eDepotSystem.Truck;
import eDepotSystem.Vehicle;
import eDepotSystem.WorkSchedule.ScheduleState;


public class Sys {
	
	private static final String PATH = "/Users/lukaginley/Library/CloudStorage/OneDrive-LiverpoolJohnMooresUniversity/Year 2/Semester 2/Object Oriented Systems Development (5104COMP)/Assignment/eDepotSystem/src/eDepotSystem";
		
	private static final Scanner scan = new Scanner(System.in);
	
	private List<Depot> arrayDepot = Collections.synchronizedList(new ArrayList<Depot>());
	private Depot depot;
	private int depotInt;
	
	public Sys() throws ParseException	{
		
		arrayDepot.add(new Depot("LPool"));
		arrayDepot.add(new Depot("MChester"));
		arrayDepot.add(new Depot("Leeds"));
		
		arrayDepot.get(0).addManager("Glyn", "GlynPassword");
		arrayDepot.get(1).addManager("Sorren", "SorrenPassword");
		
		arrayDepot.get(0).addDriver("Mark", "MarkPassword");
		arrayDepot.get(1).addDriver("Kirsty", "KirstyPassword");
		arrayDepot.get(2).addDriver("Andy", "AndyPassword");
		
		arrayDepot.get(0).addTruck("Volvo", "FH16", 20500, "TRZ 253I", null, 10000);
		arrayDepot.get(1).addTruck("ERF", "ECT", 19200, "VIG 5921", null, 8000);
		arrayDepot.get(0).addTanker("Daff", "CF", 22000, "LJZ 9143", null, 11000, "Chemicals");
		arrayDepot.get(1).addTanker("Scania", "P-SEEPER", 21000, "CTV 646K", null, 10500, "Oil");
		
		deSerialize();
		for (Depot s : arrayDepot) {
				s.startCheck();
		}
	}
	
	private void deSerialize() {
			ObjectInputStream OIS;
			
			try {
					OIS = new ObjectInputStream(new FileInputStream(PATH + ""));
					
					arrayDepot = (List<Depot>)OIS.readObject();
					
					OIS.close();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	
	private void serialize() {
			ObjectOutputStream OOS;
			
			try {
					OOS = new ObjectOutputStream(new FileOutputStream(PATH + ""));
					OOS.writeObject(arrayDepot);
					
					OOS.close();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}
	
	public Depot getDepot(String selectedDepot) {
		for (Depot depot : arrayDepot) {
			if ((depot.getDepotName()).equals(selectedDepot)) {
				return depot;
			}
		}
		return null;
	}
	
	//---LogOn---//
	
	private void logOn() throws Exception {
		System.out.println(arrayDepot.toString());
		System.out.println("Choose a Depot Location (e.g. Liverpool = 1, Manchester = 2, etc.): ");
		depotInt = Integer.valueOf(scan.nextLine());
		depotInt -= 1;
		depot = arrayDepot.get(depotInt);
		depot.logOn();
		if(depot.checkifManager()) {
			managerMainMenu();
		}
		if(depot.checkifDriver()) {
			driverMainMenu();
		}
		else {
				System.out.println("Access Denied.");
		}
	}
	
	public void entryMenu() throws Exception {
		String choice = "";
		
		do {
				System.out.println("-- Welcome to Depot System --");
				System.out.println("1. Login");
				System.out.println("2. Quit");
				System.out.print("Pick: ");
				
				choice = scan.nextLine().toUpperCase();
				
				switch (choice) {
						case "1" : {
								logOn();
								break;
						}
				}
				
		} while (!choice.equals("2"));
		serialize();
		System.out.println("Exit System");
		scan.close();
	}
	
	public void managerMainMenu() throws Exception {
		String choice = "";
		do {
				System.out.println("-- Main Menu --");
				System.out.println("Current user: Manager");
				System.out.println("1. Setup work schedule");
				System.out.println("2. View list of vehicles");
				System.out.println("3. Move vehicle");
				System.out.println("4. Add vehicle");
				System.out.println("5. Remove vehicle");
				System.out.println("6. Add driver");
				System.out.println("7. Log out");
				System.out.print("Pick: ");
				
				choice = scan.nextLine();
				
				switch(choice) {
						case "1" : {
								depot.setUpWorkSchedule();
								break;
						}
						case "2" : {
							depot.viewVehicles();
							break;
						}
						case "3" : {
								if(depot.checkifManager()) {
									Vehicle selectedVehicle = depot.displayVehicleMenu();
									System.out.println(arrayDepot.toString());
									System.out.println("Select Depot to Move Vehicle (e.g. Liverpool = 1, Manchester = 2 etc.): ");
									int depotChoice = Integer.valueOf(scan.nextLine());
									depotChoice-=1;
									Depot selectedDepot = arrayDepot.get(depotChoice);
								
									System.out.println("Specify Move Date & Time for Vehicle [dd-mm-yyyy HH:MM] :");
									String moveDate = scan.nextLine();
									SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
									Date moveDate1 = format.parse(moveDate);
									System.out.println("Vehicle will be moved to " + selectedDepot + " at date: " + moveDate1);
						
									if(selectedVehicle instanceof Tanker) {
										String regNo = ((Tanker) selectedVehicle).getRegNo();
										arrayDepot.get(depotInt).getRemoveTanker(regNo);
										TankerCheck.addToBufferTanker(selectedVehicle, selectedDepot, moveDate1);
										depot.startTankerCheck();
									}
									else if(selectedVehicle instanceof Truck) {
										String regNo = ((Truck) selectedVehicle).getRegNo();
										arrayDepot.get(depotInt).getRemoveTruck(regNo);
										TruckCheck.addToBufferTruck(selectedVehicle, selectedDepot, moveDate1);
										depot.startTruckCheck();
									}
							}
							else {
									System.out.println("Unable to move vehicle");
							}
							break;
						}
						case "4" : {
							depot.addVehicle();
							System.out.println("Vehicle Added!");
							break;
						}
						case "5" : {
							depot.removeVehicle();
							System.out.println("Vehicle Removed!");
							break;
						}
						case "6" : {
							depot.addNewDriver();
							System.out.println("Driver Added!");
							break;
						}
					}	
			} while (!choice.equals("7"));
		entryMenu();
		}	
	
		public void driverMainMenu() throws Exception {
			String choice = "";
			do {
					System.out.println("-- Main Menu --");
					System.out.println("Current user: Driver");
					System.out.println("1. View work schedule");
					System.out.println("2. Log Out");
					System.out.print("Pick: ");
				
				choice = scan.nextLine();
				
				switch(choice) {
						case "1" : {
								depot.viewWorkSchedule();
								break;
						}
					}
			} while (!choice.equals("2"));
		}	
	}

 
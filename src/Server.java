import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(2400, 10);
		int id = 0;
		while (true) {
			Socket clientSocket = serverSocket.accept();
			ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
			cliThread.start();
		}
	}
}

class ClientServiceThread extends Thread {
	Socket clientSocket;
	String menu;
	String menu2;
	int clientID = -1;
	boolean running = true;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	// Variables for registration + login
	private String regBusinessName;
	private String regBusinessID;
	private String regEmail;
	private String businessId;
	private String businessName;
	
	// Variables to verify Registration + Login
	private boolean unique = true;
	private boolean loggedIn;
	
	// Variables for Machine
	private String machineName;
	private int machineAge;
	private String machineID;
	private String clubID;
	private String vendor;
	private double valuation;
	private int lastServiceKm;
	private Date lastServiceDate;
	private int nextServiceKm;
	private int currentKm;
	private boolean forSale = false;
	
	// Variables for Machine Record
	private String serviceAgent;
	private int currentMileage;
	private String description;
	
	// Variables Update mileage 
	private String requestedMachineId;
	private String newMileage;
	
	// Used for searching for bug ID
	private String requestedMachineName;
	private String foundLine = "";
	// Used for options 5 & 6
	private String forSaleMachines = "";
	// For whatever reason these weren't working as booleans
	private int found;
	
	// Used to generate unique random machine ID
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	ClientServiceThread(Socket s, int i) {
		clientSocket = s;
		clientID = i;
	}

	// Server ran
	public void run() {
		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Accepted Client : ID - " + clientID);

			// While user doesn't enter -1
			do {
				// Prompt user to Register or login
				out.writeObject("Please enter\n" 
								+ "1. Register\n" 
								+ "2. Login\n" 
								+ "-1. To end!");
				out.flush();
				menu = (String) in.readObject();

				// Registration
				if (menu.equalsIgnoreCase("1") || menu.equalsIgnoreCase("register")) {
					unique = true;
					// Writing to the file
					BufferedWriter usersFile = new BufferedWriter(new FileWriter("Users.txt", true));
					// Reading the file for validation
					Scanner scanner = new Scanner(new File("Users.txt"));
					out.writeObject("You may now Register");
					out.flush();
					
					out.writeObject("Please enter Business name");
					out.flush();
					regBusinessName = (String) in.readObject();
 
					out.writeObject("Please enter Business ID");
					out.flush();
					regBusinessID = (String) in.readObject();
					out.writeObject("Please enter your Email");
					out.flush();
					regEmail = (String) in.readObject();
					// Check to see user has registered before
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						if (line.contains(regBusinessName) || line.contains(regBusinessID)) {
							out.writeObject("The Name or ID you have entered has already been registered");
							out.flush();
							// Make false to stop from writing to the file
							unique = false;
						}
					}
					// If register was a success write information to the file
					if (unique != false) {
						// Writing to the file
						usersFile.newLine();
						usersFile.write("Username: " + regBusinessName + " | Employee ID: " + regBusinessID + " | Email: " + regEmail);
						out.writeObject("Welcome " + regBusinessName
								+ "\nRegistration successful");
						out.flush();
					}
					// Closing files
					scanner.close();
					usersFile.close();
				} // Registration if

				// Login
				else if (menu.equalsIgnoreCase("2")) {
					// Logged in = false until user verified/logged in correctly
					loggedIn = false;
					// Reading from the file
					Scanner uScanner = new Scanner(new File("Users.txt"));

					out.writeObject("Please enter your Business name");
					out.flush();
					businessName = (String) in.readObject();
					

					out.writeObject("Please enter your Buisness ID");
					out.flush();
					businessId = (String) in.readObject();

					// Runs through every line to see if there is a match with both
					while (uScanner.hasNextLine()) {
						String line = uScanner.nextLine();
						if (line.contains(businessId) && line.contains(businessName)) {
							// ID Matched
							loggedIn = true;
							out.writeObject("Login successful!");
							out.flush();
						}
					}
					// If login failed return the user to the menu to login/register
					if (loggedIn == false) {
						out.writeObject("The Name or ID was incorrect\n" + "Login Failed!");
						out.flush();
						
					}
					// If login was a success
					else {
						do {
							out.writeObject("\nWelcome " + businessName + "\n"
									+ "Enter 1 to add a piece of machinery to the fleet.\n"
									+ "Enter 2 to update Machine Service Record.\n"
									+ "Enter 3 to update Mileage on the machine.\n"
									+ "Enter 4 to view machines within 1,000km of next service.\n"
									+ "Enter 5 to remove a machine from the fleet.\n" 
									+ "Enter 6 to register a machine for Sale.\n"
									+ "Enter 7 to view all machines for Sale.\n"
									+ "Enter -1 to Log Out\n");
							out.flush();
							menu2 = (String) in.readObject();

							// Add a new piece of machinery to the System.
							if (menu2.equalsIgnoreCase("1")) {
								BufferedWriter machineFile = new BufferedWriter(new FileWriter("fleetItem.txt", true));
								out.writeObject("Enter Details of the machine below!");
								out.flush();

								out.writeObject("Machine Name: ");
								out.flush();
								machineName = (String) in.readObject();
								
								out.writeObject("Machine Age: ");
								out.flush();
								machineAge = (Integer) in.readObject();
								
								machineID = getMachineID(); // get unique id
								
								out.writeObject("Club ID: ");
								out.flush();
								clubID = (String) in.readObject();
								
								out.writeObject("Club ID: ");
								out.flush();
								vendor = (String) in.readObject();
								
								out.writeObject("Valuation in €: ");
								out.flush();
								valuation = in.readDouble();
								
								out.writeObject("Last Sevice in km: ");
								out.flush();
								lastServiceKm = (Integer) in.readObject();

								out.writeObject("Last Sevice date in DD/MM/YYYY: ");
								out.flush();
								lastServiceDate = (Date) in.readObject();
								
								out.writeObject("Next Serice in km: ");
								out.flush();
								nextServiceKm = (Integer) in.readObject();
								
								out.writeObject("Current KM: ");
								out.flush();
								currentKm = (Integer) in.readObject();
								
								// Writing the machine to the file
								machineFile.write(("machine ID: " + machineID
										+ " | Machine Name: " + machineName 
										+ " | Machine Age: " + machineAge
										+ " | Club ID: " + clubID
										+ " | Vendor: " + vendor 
										+ " | Valuation: €" + valuation
										+ " | Last Service : " + lastServiceKm + "km" 
										+ " | Last Service Date: " + lastServiceDate
										+ " | Next Service: " + nextServiceKm + "km"
										+ " | Current Millage: " + currentKm + "km" + " |\n"));
								machineFile.close();
							} // if(add machine)
							
							// Update machine record
							if (menu2.equalsIgnoreCase("2")) {
								BufferedWriter recordsFile = new BufferedWriter(new FileWriter("MachineRecord.txt", true));
								out.writeObject("Enter details of the Machine Record Below!");
								out.flush();

								out.writeObject("Name of Service Agent: ");
								out.flush();
								serviceAgent = (String) in.readObject();

								out.writeObject("Kilometers on the machine: ");
								out.flush();
								currentMileage = (Integer) in.readObject();
								
								out.writeObject("Description of Service: ");
								out.flush();
								description = (String) in.readObject();
							
								out.writeObject("Last Sevice in km: ");
								out.flush();
								lastServiceKm = (Integer) in.readObject();
								
								out.writeObject("Next Serice in km: ");
								out.flush();
								nextServiceKm = (Integer) in.readObject();
								
								// Writing the machine to the file
								recordsFile.write(("Service Agent: " + serviceAgent
										+ " | Current Mileage in km: " + currentMileage 
										+ " | Serice Description: " + description + " |\n"));
								recordsFile.close();
							} // if(update machine record)
							
							// Update Mileage
							if(menu2.equalsIgnoreCase("3")) {
								Scanner mileageScan = new Scanner(new File("fleetItem.txt"));
								found = 0;
								
								out.writeObject("Enter Machine name: ");
								out.flush();
								requestedMachineId = (String) in.readObject();
								
								// Search file for the machine name
								while (mileageScan.hasNextLine()) {
									String line = mileageScan.nextLine();
									if (line.contains("Machine found: " + requestedMachineId)) { // machine found
										foundLine = line;
										found = 1;
										out.writeObject("Enter the new Mileage of of the machine!");
										out.flush();
										newMileage = (String) in.readObject();
									}
								}
								if (found == 0) {
									out.writeObject("Machine ID was not found on the fleet");
									out.flush();
								}
								else {
									newMileage = "";
									Scanner updateScanner = new Scanner(new File("fleetItem.txt"));
									while (updateScanner.hasNextLine()) {
										String line = updateScanner.nextLine();
										if (line.contains("Mileage: ")) {
											line = line.replace(foundLine,
													foundLine + " Mileage: |");
											newMileage += line + "\n";
										}
										else {
											// newMileage adds the milleage to the fleet
											newMileage += line + "\n";
										}
									}

									// Making blank temporarily
									BufferedWriter blankWriter = new BufferedWriter(new FileWriter("fleetItem.txt"));
									blankWriter.write("");
									blankWriter.close();

									// Same file, just putting in the new data with user responsible appended
									BufferedWriter writeToNewBug = new BufferedWriter(
											new FileWriter("Bugs.txt", true));
									writeToNewBug.write(newMileage);
									writeToNewBug.close();
									updateScanner.close();
									mileageScan.close();
								}
							} // If(update mileage)

							// Search all fleet items that are currently within 1000Km of their next service.
							if (menu2.equalsIgnoreCase("4")) {
								foundLine = "";
								found = 0;
								Scanner scanThrough = new Scanner(new File("fleetItem.txt"));
								out.writeObject("All machines within 10,000km of next service");
								out.flush();

								while (scanThrough.hasNextLine()) {
									String line = scanThrough.nextLine();
									if (line.contains("Last service: " + (10000>=lastServiceKm))) {
										// Output the line it has found
										foundLine += line + "\n";
										found =1;
									}
								}
								if (found == 0) {
									out.writeObject("No machines within 10,000km of next nervice!");
									out.flush();
								}
								else {
									out.writeObject(foundLine);
									out.flush();
								}
								scanThrough.close();
							} // If (<1000Km of their next service)
							
							// Remove machine from the fleet
							if (menu2.equalsIgnoreCase("5")) {
								Scanner searchScanner = new Scanner(new File("fleetItem.txt"));
								found = 0;

								requestedMachineName = (String) in.readObject();
								out.writeObject("Enter the machine name you would like to remove.");
								out.flush();

								// If the/ machine name entered matches a machine name in the file
								while (searchScanner.hasNextLine()) {
									String line = searchScanner.nextLine();
									if (line.contains("Machine Name: " + requestedMachineName)) {
										// Preserving the line it's found on in this variable
										foundLine = line;
										found = 1;
										line = line.replace(("Machine Name: " + requestedMachineName), "\n");
										out.writeObject("Fleet removed!");
										out.flush();
									}
								}
								// Tells the user no match and returns them to the menu
								if (found == 0) {
									out.writeObject("There are no machines with <10,000km to next service");
									out.flush();
									// If found is true
								} 
								
									searchScanner.close();
							} // If (Delete machine)

							// Add item for sale
							if (menu2.equalsIgnoreCase("6")) {
								Scanner searchScanner = new Scanner(new File("fleetItem.txt"));
								BufferedWriter recordsFile = new BufferedWriter(new FileWriter("MachineRecord.txt", true));
								found = 0;
								
								out.writeObject("Enter Machine name you want to make for sale: ");
								out.flush();
								requestedMachineId = (String) in.readObject();
								
								// Search file for the machine name
								while (searchScanner.hasNextLine()) {
									String line = searchScanner.nextLine();
									if (line.contains("Machine found: " + requestedMachineId)) { // machine found
										foundLine = line;
										found = 1;
										forSale = true;
										recordsFile.write(("For Sale for €" + valuation + " |\n"));
										recordsFile.close();
									}
								}
								if (found == 0) {
									out.writeObject("Machine ID was not found on the fleet");
									out.flush();
								}
								searchScanner.close();
							} // If (add item for sale)
							
							// View all machines for sale
							if (menu2.equalsIgnoreCase("7")) {
								// Clear line
								forSaleMachines = "";
								Scanner scanThrough = new Scanner(new File("fleetItem.txt"));
								out.writeObject("All machines for Sale:");
								out.flush();

								while (scanThrough.hasNextLine()) {
									// Looping for each line
									String line = scanThrough.nextLine();
									if (line.contains("For Sale")) {
										forSaleMachines += line + "\n";
									}
								}
								scanThrough.close();
								out.writeObject(forSaleMachines);
								out.flush();

							}

								// Client logs out if -1 is entered
						} while (!menu2.equalsIgnoreCase("-1"));
					// Closing files
					uScanner.close();
					}
				} // Login ends

				out.writeObject("Please enter\n" + "1. Register\n" + "2. Login\n" + "-1. END!");
				out.flush();
				menu = (String) in.readObject();
			} while (!menu.equalsIgnoreCase("-1")); // Do while

			// Client terminated
			System.out.println("Terminating Client : ID - " + clientID );
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // Run()
	
	// Called to generate a unique machine ID
	public String getMachineID() {
		int idLength = 5;
		Random random = new Random();
		StringBuilder builder = new StringBuilder(idLength);

		// Looping 5 times, one for each char
		for (int i = 0; i < idLength; i++) {
			builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
		}
		// Generates a random ID
		return builder.toString();
	}
		
	// Method for sending messages to the client
	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("Sending to client => " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
} // Class Server

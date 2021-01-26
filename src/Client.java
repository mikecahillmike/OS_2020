import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message = "";
	String menu = "";
	int intMessage;
	double doubleMessage;
	String option = "";
	String ipaddress;
	Scanner input;

	Client() {
	}

	void run() {
		Scanner input = new Scanner(System.in);
		try {
			// 1. creating a socket to connect to the server
			//System.out.println("Please Enter your IP Address");
			//ipaddress = input.next();
			requestSocket = new Socket("127.0.0.1", 2400);
			System.out.println("Connected to " + ipaddress + " in port 2400");
			// Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());

			do {
				// List of options 1-2 for the user
				menu = (String) in.readObject();
				System.out.println(menu);
				menu = input.next();
				sendMessage(menu);

				// Register
				if (menu.equals("1") || menu.equals("register")) {
					// Registration message
					message = (String) in.readObject();
					System.out.println(message);

					// Business Name
					message = (String) in.readObject();
					System.out.println(message);
					message = input.nextLine();
					message = input.nextLine();
					out.writeObject(message);
					out.flush();

					// Business ID
					message = (String) in.readObject();
					System.out.println(message);
					message = input.next();
					out.writeObject(message);
					out.flush();

					// Email
					message = (String) in.readObject();
					System.out.println(message);
					message = input.next();
					sendMessage(message.toLowerCase());

					// Registration Result
					message = (String) in.readObject();
					System.out.println(message);
				}

				// Login
				else if (menu.equals("2") || menu.equalsIgnoreCase("login")) {

					// Business name
					message = (String) in.readObject();
					System.out.println(message);
					message = input.nextLine();
					message = input.nextLine();
					sendMessage(message);

					// Business ID
					message = (String) in.readObject();
					System.out.println(message);
					message = input.next();
					sendMessage(message);

					// Login Result
					message = (String) in.readObject();
					System.out.println(message);
					message = input.next();
					message = input.next();
					
					if (message.contains("Login successful!")) {
						// Output menu

						do {
							// menu
							message = (String) in.readObject();
							System.out.println(message);
							option = input.next();
							sendMessage(option);
							// Add a new piece of machinery to the System.
							if (option.equals("1")) {
								// Output Enter Details of machine
								message = (String) in.readObject();
								System.out.println(message);

								// Machine Name
								message = (String) in.readObject();
								System.out.println(message);
								message = input.next();
								message = input.nextLine();
								sendMessage(message);

								// Machine age
								message = (String) in.readObject();
								System.out.println(message);
								intMessage = input.nextInt();
								out.writeObject(intMessage);
								out.flush();
								
								// Club ID
								message = (String) in.readObject();
								System.out.println(message);
								message = input.nextLine();
								sendMessage(message);
								
								// Vendor
								message = (String) in.readObject();
								System.out.println(message);
								message = input.nextLine();
								sendMessage(message);
								
								// Valuation
								message = (String) in.readObject();
								System.out.println(message);
								doubleMessage = input.nextDouble();
								out.writeObject(doubleMessage);
								out.flush();
								
								// Last service km
								message = (String) in.readObject();
								System.out.println(message);
								intMessage = input.nextInt();
								out.writeObject(intMessage);
								out.flush();
								
								// Last service Date
								message = (String) in.readObject();
								System.out.println(message);
								message = input.nextLine();
								sendMessage(message);
								
								// Next service km
								message = (String) in.readObject();
								System.out.println(message);
								intMessage = input.nextInt();
								out.writeObject(intMessage);
								out.flush();
								
								// Current KM
								message = (String) in.readObject();
								System.out.println(message);
								intMessage = input.nextInt();
								out.writeObject(intMessage);
								out.flush();
							}

							// Add machine record
							if (option.equals("2")) {
								// Enter details of the Machine Record Below
								message = (String) in.readObject();
								System.out.println(message);

								// Agent Name
								message = (String) in.readObject();
								System.out.println(message);
								message = input.next();
								message = input.nextLine();
								sendMessage(message);
								
								// KM on the machine
								message = (String) in.readObject();
								System.out.println(message);
								intMessage = input.nextInt();
								out.writeObject(intMessage);
								out.flush();
								
								// Description
								message = (String) in.readObject();
								System.out.println(message);
								message = input.next();
								message = input.nextLine();
								sendMessage(message);
								
								// Last in service km
								message = (String) in.readObject();
								System.out.println(message);
								intMessage = input.nextInt();
								out.writeObject(intMessage);
								out.flush();
								
								// Next in service km
								message = (String) in.readObject();
								System.out.println(message);
								intMessage = input.nextInt();
								out.writeObject(intMessage);
								out.flush();


							}

							// Update module
							if (option.equals("3")) {
								// Enter machine
								message = (String) in.readObject();
								System.out.println(message);

								// Enter new mileage
								message = (String) in.readObject();
								System.out.println(message);
								message = input.nextLine();
								out.writeObject(message);
								out.flush();
							}

							// Search all under 1000km of next service
							if (option.equals("4")) {
								// Enter the machine name you would like to remove
								message = (String) in.readObject();
								System.out.println(message);

								// Output Result
								message = (String) in.readObject();
								System.out.println(message);
							}
							
							// Delete from fleet
							if (option.equals("5")) {
								// Enter the machine name you would like to remove
								message = (String) in.readObject();
								System.out.println(message);

								// Result
								message = (String) in.readObject();
								System.out.println(message);
								
							}
							
							// Add item for sale
							if (option.equals("6")) {
								// Enter the machine name you would like to remove
								message = (String) in.readObject();
								System.out.println(message);

								// Returning no machines
								message = (String) in.readObject();
								System.out.println(message);
							}

							// All machines for Sale
							if (option.equals("7")) {
								// All machines for Sale
								message = (String) in.readObject();
								System.out.println(message);

								// Display all machines for Sale
								message = (String) in.readObject();
								System.out.println(message);
							}

							// If option was entered correctly
							if (!option.equals("1") && !option.equals("2") && !option.equals("3") && !option.equals("4")
									&& !option.equals("5") && !option.equals("6") && !option.equals("7") && !option.equalsIgnoreCase("-1")) {
								System.out.println("Please enter 1-7 or -1");
							}
							// If they don't wish to exit
							if (!option.equalsIgnoreCase("-1")) {
								// Input for menu 2
								message = (String) in.readObject();
								System.out.println(message);
								option = input.next();
								sendMessage(option);

							}
							// Logout
						} while (!option.equalsIgnoreCase("-1"));
						System.out.println("Logging Out ...");
					}
				} // if (Login)

				// Back to first menu
				message = (String) in.readObject();
				System.out.println(message);
				message = input.next();
				sendMessage(message);

			} while (!message.equalsIgnoreCase("-1"));
			System.out.println("Ending Client Connection ...");
		}

		catch (UnknownHostException unknownHost) {
			System.out.println("Unknown host");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Closing connection
		try {
			in.close();
			out.close();
			requestSocket.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	

	// Sending messages to the server
	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("Sending to server => " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Main method
	public static void main(String args[]) {
		Client client = new Client();
		client.run();
	}
} // Class Client
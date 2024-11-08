package edu.seg2105.server.ui;

import edu.seg2105.edu.server.backend.EchoServer;
import edu.seg2105.client.common.*;

import java.io.*;
import java.util.Scanner;

public class ServerConsole implements ChatIF {
	
	//Class variables *************************************************
	  
	  /**
	   * The default port to listen on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	  
	//Instance variables **********************************************
	
	/**
	* Scanner to read from the console
	*/
	Scanner fromConsole;
	
	/**
	* The instance of the server that created this ServerConsole.
	*/
	EchoServer server;
	
	//Constructors ****************************************************

	  /**
	   * Constructs an instance of the ServerConsole UI.
	   *
	   * @param port The port to listen on.
	   */
	public ServerConsole (int port) {
		try {
			server = new EchoServer(port, this);
		} catch (IOException ex) {
			System.out.println("Error: Cannot start listening. Terminating server.");
			System.exit(1);
		}
		
		// Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	}
	
	/**
	* This method is responsible for the creation of the Server UI.
	*
	* @param args what the end-user of the server typed in.
	*/
	public static void main (String[] args) {
		int port = 0;
		
		try {
			port = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			port = DEFAULT_PORT;
		}
		
		ServerConsole chat = new ServerConsole(port);
		chat.accept();
	}
	
	//Instance methods ************************************************
	  
	/**
	* This method waits for input from the console. 
	* If the input is a command, does an appropriate action.
	* If the input is not a command, sends it to the server's message handler.
	*/
	public void accept() {
		try {
			
			String message;
			
			while (true) {
				message = fromConsole.nextLine().trim();
				
				if (!message.equals("") && message.charAt(0) == '#') {
					//message is some command
					String[] message_split_up = message.split(" ", 2);
					
					if (message.equals("#quit")) {
						server.close();
						System.exit(0);
					} else if (message.equals("#stop")) {
						if (server.isListening()) {
							server.stopListening();
						} else { //!server.isListening()
							display("Server is already not listening for new clients.");
						}
					} else if (message.equals("#close")) {
						server.close();
					} else if (message_split_up[0].equals("#setport")) {
						if (server.isListening()) {
							display("Cannot set port when server is not closed.");
						} else { //!server.isListening()
							try {
								int newPort = Integer.parseInt(message_split_up[1]);
								server.setPort(newPort);
								display("Port number set to: " + message_split_up[1]);
							} catch (NumberFormatException ex) {
								display("Port number must be an integer.");
							}
						}
					} else if (message.equals("#start")) {
						if (server.isListening()) {
							display("Server is already running.");
						} else { //!server.isListening()
							server.listen();
							display("Server is now listening for new clients.");
						}
					} else if (message.equals("#getport")) {
						  display("Current port number: " + server.getPort());
					} else {
						//was not one of the preset commands
						display("That was not a valid command.");
					}
					
				} else {
					//message is some message to send to the server to be broadcasted to all users
					message += " <SERVER MSG>";
					display(message);
					server.sendToAllClients(message);
				}
			}
			
		} catch (Exception ex) {
			display("Unexpected error while reading from console!");
		}
	}

	/**
	* This method overrides the method in the ChatIF interface.  It
	* displays a message onto the screen.
	*
	* @param message The string to be displayed.
	*/
	public void display(String message) {
		System.out.println("> " + message);
	}
}

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
				message = fromConsole.nextLine();
				
				if (!message.equals("") && message.charAt(0) == '#') {
					//message is some command
					String[] message_split_up = message.split(" ", 2);
					
					//TODO: implement the commands
					
				} else {
					//message is some message to send to the server to be broadcasted to all users
					message += "SERVER MSG>";
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

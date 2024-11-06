package edu.seg2105.server.ui;

import edu.seg2105.edu.server.backend.EchoServer;
import edu.seg2105.client.common.*;

import java.io.*;
import java.util.Scanner;

public class ServerConsole implements ChatIF {

	/**
	   * This method overrides the method in the ChatIF interface.  It
	   * displays a message onto the screen.
	   *
	   * @param message The string to be displayed.
	   */
	public void display(String message) {
		System.out.println("> " + message);
	}
	
	/**
	* Scanner to read from the console
	*/
	Scanner fromConsole;
	
	/**
	* The instance of the server that created this ConsoleChat.
	*/
	EchoServer server;
	
	/**
	* This method is responsible for the creation of the Server UI.
	*
	* @param args what the end-user of the server typed in.
	*/
	public static void main (String[] args) {
		try {
			  String message;

			  while (true) {
				  message = fromConsole.nextLine();
				  
				  if (!message.equals("") && message.charAt(0) == '#') {
					  //message is some command
					  String[] message_split_up = message.split(" ", 2);
					  
					  if (message_split_up[0].equals("#quit")) {
						  client.quit();
					  } 
					  else if (message_split_up[0].equals("#logoff")) {
						  if (client.isConnected()) {
							  client.closeConnection();
						  } else { //!client.isConnected()
							  System.out.println("There is no connection to server to log off from.");
						  }
					  } 
					  else if (message_split_up[0].equals("#sethost")) {
						  if (!client.isConnected()) {
							  client.setHost(message_split_up[1]);
							  System.out.println("Host name set to: " + message_split_up[1]);
						  } else { //client.isConnected()
							  System.out.println("Can only set host name if disconnected. "
							  		+ "Please disconnect first.");
						  }
					  } 
					  else if (message_split_up[0].equals("#setport")) {
						  if (!client.isConnected()) {
							  client.setPort(Integer.parseInt(message_split_up[1]));
							  System.out.println("Port number set to: " + message_split_up[1]);
						  } else { //client.isConnected()
							  System.out.println("Can only set port number if disconnected. "
								  		+ "Please disconnect first.");
						  }
					  } 
					  else if (message_split_up[0].equals("#login")) {
						  if (client.isConnected()) {
							  System.out.println("Already connected to server.");
						  } else { //!client.isConnected()
							  client.openConnection();
							  System.out.println("Have now connected to server.");
						  }
					  }
					  else if (message_split_up[0].equals("#gethost")) {
						  System.out.println("Current host name: "
								  + client.getHost());
					  }
					  else if (message_split_up[0].equals("#getport")) {
						  System.out.println("Current port number: "
								  + client.getPort());
					  }
					  else {
						  //was not one of the preset commands
						  System.out.println("That was not a valid command.");
					  }
					  
				  } else {
					  //message is actually a message to be sent to the client
					  client.handleMessageFromClientUI(message);
				  }
			  }
			  
		  } catch (Exception ex) {
			  System.out.println("Unexpected error while reading from console!");
		  }
	}
}

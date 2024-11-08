package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ClientConsole.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginID, String host, int port) 
  {
    try 
    {
      client= new ChatClient(loginID, host, port, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * Modified for Exercise 2, Client Side a) to add support for some commands.
   * This method waits for input from the console. 
   * If the input is a command, does an appropriate action.
   * If the input is not a command, sends it to the client's message handler.
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
					  client.quit();
				  } 
				  else if (message.equals("#logoff")) {
					  if (client.isConnected()) {
						  client.closeConnection();
					  } else { //!client.isConnected()
						  display("Client is already not connected to a server.");
					  }
				  } 
				  else if (message_split_up[0].equals("#sethost")) {
					  if (!client.isConnected()) {
						  client.setHost(message_split_up[1]);
						  display("Host name set to: " + message_split_up[1]);
					  } else { //client.isConnected()
						  display("Can only set host name if disconnected. "
						  		+ "Please disconnect first.");
					  }
				  } 
				  else if (message_split_up[0].equals("#setport")) {
					  if (!client.isConnected()) {
						  try {
							  int newPort = Integer.parseInt(message_split_up[1]);
							  client.setPort(newPort);
							  display("Port number set to: " + message_split_up[1]); 
						  } catch (NumberFormatException ex) {
							  display("Port number must be an integer.");
						  }
					  } else { //client.isConnected()
						  display("Can only set port number if disconnected. "
							  		+ "Please disconnect first.");
					  }
				  } 
				  else if (message.equals("#login")) {
					  if (client.isConnected()) {
						  display("Already connected to server.");
					  } else { //!client.isConnected()
						  client.openConnection();
						  display("Have now connected to server.");
					  }
				  }
				  else if (message.equals("#gethost")) {
					  display("Current host name: "
							  + client.getHost());
				  }
				  else if (message.equals("#getport")) {
					  display("Current port number: "
							  + client.getPort());
				  }
				  else {
					  //was not one of the preset commands
					  display("That was not a valid command.");
				  }
				  
			  } else {
				  //message is actually a message to be sent to the client
				  client.handleMessageFromClientUI(message);
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
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args what the user typed in.
   */
  public static void main(String[] args) 
  {
	  String loginID = "";
	  
	  try {
		  loginID = args[0];
	  } catch (ArrayIndexOutOfBoundsException ex) {
		  System.out.println("ERROR - No login ID specified.  Connection aborted.");
		  System.exit(1);
	  }
	  
	  String host = "";
	  
	  try {
		  host = args[1];
	  } catch (ArrayIndexOutOfBoundsException ex) {
		  host = "localhost";
	  }
	  
	  int port = 0;
	  
	  try {
		  port = Integer.parseInt(args[2]);
	  } catch (ArrayIndexOutOfBoundsException ex) {
		  port = DEFAULT_PORT;
	  } catch (NumberFormatException ex) {
		  System.out.println("Given port number was not an integer.");
		  port = DEFAULT_PORT;
	  }
	  
	  ClientConsole chat = new ClientConsole(loginID, host, port);
	  chat.accept();
  }
}
//End of ConsoleChat class

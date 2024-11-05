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
   * The instance of the client that created this ConsoleChat.
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
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client= new ChatClient(host, port, this);
      
      
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
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;

    try
    {
      host = args[0];
      port = Integer.parseInt(args[1]);
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
      port = DEFAULT_PORT;
    }
    ClientConsole chat= new ClientConsole(host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class

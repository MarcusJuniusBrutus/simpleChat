package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  /**
  * The interface type variable.  It allows the implementation of 
  * the display method in the server.
  */
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) throws IOException
  {
    super(port);
    this.serverUI = serverUI;
    listen();
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  /**
     * Implemented for Exercise 1, Server Side c)
	 * A method to announce when a client connects.
	 * @param client the connection connected to the client.
	 */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client " + 
			  client.getInetAddress().toString() + " connected. Welcome, we are happy to have you!");
  }

	/**
	 * Implemented for Exercise 1, Server Side c)
	 * A method to announce when a client disconnects.
	 * @param client the connection with the client.
	 */
  @Override
  synchronized protected void clientDisconnected(
		  ConnectionToClient client) {
	  System.out.println("A client has disconnected. We will miss them!");
	}
  
  /**
	 * Implemented for Exercise 1, Server Side c)
	 * A method to announce when a client has some exception occur.
	 * @param client the connection with the client.
	 * @param exception the exception the client threw
	 */
  @Override
  synchronized protected void clientException(
			ConnectionToClient client, Throwable exception) {
	  System.out.println("A client has disconnected. We will miss them!");
  }
}
//End of EchoServer class

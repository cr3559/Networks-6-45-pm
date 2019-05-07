import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles finding the next destination IP address,
 * as well as sending the message to that IP address.
 * @author Christopher Roadcap
 */
public class RequestHandler implements Runnable
{
char routerNumber;
Socket incomingSocket;
ServerSocket serverSocket;
String routerTable;
Socket destinationSocket;

	public RequestHandler(Socket incomingSocket, String routerTable, char routerNumber  ) throws IOException
	{
		//Socket which we are listening for messages on
		//this.serverSocket = serverSocket;
		 this.incomingSocket = incomingSocket;
		
		//The file name of the appropriate routing table
		this.routerTable = routerTable;
		
		//The number of the router ( 1 or 2 , for my portion of the project)
		this.routerNumber = routerNumber;
	}
	
	
	
	public void run()
	{
		String message = "";
			try 
			{
				
			
				//InputStream stream = incomingSocket.getInputStream();
				//this.incomingSocket = serverSocket.accept();
				//Reads input Stream of server
				byte[] incoming = new byte[10];
				incoming = incomingSocket.getInputStream().readAllBytes();
				message = new String(incoming);
				
				
		
				//The next location to send the message
				if(determineIfMyNetwork(message))
				{
					message = setMyNewDestinationMessage(message);
					String destination = findDestination(message);
				
					
					System.out.println(message);
					
					if(message.charAt(1) == routerNumber )
					{
						//Destination of message is the client running on this machine
						destinationSocket = new Socket("127.0.0.1", 7771 );
					}
					else
					{	//destination of message is another router
						System.out.println("DESTINATION:"+  destination);
						destinationSocket = new Socket(destination, 4447);
					}
					
					//Determines if checksum is correct
					if(verifyCheckSum(message))
					{
						System.out.println("Checksum Verified");
						System.out.println("Full Message: " + message + "\n");
						
						//Output stream to write the message to
						DataOutputStream output = new DataOutputStream(destinationSocket.getOutputStream());
						
						//Write the message to the stream
						output.writeBytes(message +"\n");  
						output.flush();
						output.close();
					}
					else 
					{
						System.out.println("Data Corrupted, message discarded.\n");
					}
					incomingSocket.close();
					destinationSocket.close();
				}
				else
				{
					routeToBGP(message);
				}
				

					
				}
			//Error in I/O
			catch (IOException e) 
			{	
			 e.printStackTrace();
			} 
	}
	
	private String setMyNewDestinationMessage(String message) 
	{
		String result ="";
		char dest = message.charAt(4);
		char source = message.charAt(0);
		String remainder = message.substring(2, 10);
		char newDest = '0';
		
		switch(dest)
		{
		case 'q':
			newDest = '1';
			break;
		case 'r':
			newDest = '2';
			break;
		case 's':
			newDest = '3';
			break;
		case 't':
			newDest = '4';
			break;
		}
		result = ""+source + newDest + remainder;
		
		
		return result;
	}



	/**
	 * Finds the appropriate destination of the message via a routing table
	 * @param message the message to be sent
	 * @return the IP address to send the message to
	 * @throws FileNotFoundException
	 */
	public String findDestination(String message) throws FileNotFoundException
	{
		/*
		 *List of DataHolders that hold the source, destination, and destination IP address
		 *according to the routing table(DataHolders can be thought as 1 row of the routing table);
		 */
		ArrayList<DataHolder> list = TableReader.readTable(this.routerTable);
		
		//The source of the message (where it was sent from)
		char source = message.charAt(0);
	    
		//The destination of the message (where it is going)
		char destination = message.charAt(1);
		
		/*
		 * Loops through the DataHolder array until it finds the DataHolder with the corresponding
		 * source, and destination. This data holder must hold the destination IP address.
		 */
		for(DataHolder dataHolder: list)
		{
			if(dataHolder.source.charAt(0) == source && dataHolder.destination.charAt(0) == destination)
			{
				DataHolder destinationData = dataHolder;
				
				//The destination IP address
				return destinationData.address;
			}
		}
		return null;
	}

	/**
	 * Determines if the message's checksum is correct
	 * @param message the message received, and to be routed
	 * @return true(valid checksum) or false(invalid checksum)
	 */
	public boolean verifyCheckSum(String message)
	{
		boolean isValid;
		int finalChecksum = 0 ;
		String payload = message.substring(3, 10);
		
		
		for(int i = 0; i < payload.length(); i++)
		{	
			finalChecksum += payload.charAt(i);
			
			if(finalChecksum > 255)
			{
				finalChecksum = payload.charAt(i);
			}
		}
			
			if (finalChecksum == (int) message.charAt(2))
			{
				isValid = true; 
			}
			else
			{
				isValid = false;
			}
			
			return isValid;
		}
	

	
	public String convertToString(byte[] bytes)
	{	String result = "";
		for(byte b: bytes)
		{
			result += (char)b;
		}
		System.out.println("RESULT" + result);
		return result;
	}
	
	public boolean determineIfMyNetwork(String message)
	{
		boolean result = false ;
		char networkDest = message.charAt(4);
		char[] array = {'q','r','s','t'};

		for(int i = 0; i < array.length; i++)
		{
			if(networkDest == array[i])
			{
				result = true;
			}
		}
		return result;
	}
	
	public void routeToBGP(String message) throws UnknownHostException, IOException
	{
		String payload = message.substring(2, 10);
		String destination = "56" +payload;
		byte[] bytes = destination.getBytes();
		
		ArrayList<DataHolder> destinationList = new TableReader().readTable("bgpout.txt");
		String address = "";
		
		for(DataHolder dh: destinationList)
		{
			if(routerNumber == dh.source.charAt(0))
			{
				address = dh.address;
			}
		}
		
		Socket destinationSocket = new Socket(address, 4566 );
		destinationSocket.getOutputStream().write(bytes);
		
		destinationSocket.close();
	
		
	}
	
	
	  
}

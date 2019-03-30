import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ThreadSender implements Runnable 
{
	static ServerSocket serverSocket;
	ArrayList<String> randomMessages = new ArrayList<String>();;
	boolean sabotagedMessage;
	Socket clientSocket;
	char clientNumber;
	
	public ThreadSender(char clientNumber)
	{
		this.clientNumber = clientNumber;
	}

	@Override
	public void run() 
	{
		//populates random message array list
		populateRandomMessages(randomMessages);
        try
        {
        	for( int i = 0;  i< randomMessages.size(); i++)
        	{
        		System.out.println("Message Number: " + (i + 1));
        		
        		//String to hold the message
        		String outgoingMessage = null;
 
        		//Checksum of every 5th message is purposely corrupted
	            if( (i + 1) % 5 == 0)
	            {
	            	sabotagedMessage = true;
	            }
	            else
	            {
	            	sabotagedMessage = false;
	            }
	            
	            //The message to be sent 
	            outgoingMessage = buildMessage(clientNumber,randomMessages.get(i), sabotagedMessage)+ ""+(char)255; // char(255) used for server scanner
	            
	            //socket created to this client's router (will change based on where we are running)
	            clientSocket = new Socket("127.0.0.1", 4446); 
	            
	            //The output stream to write to
	    		DataOutputStream out;
	    		try 
	    		{
	    			//assign the output stream to the sockets output stream
	    			out = new DataOutputStream(this.clientSocket.getOutputStream());
	    			out.flush();
	    			
	    			//write the message to the stream
	                out.writeBytes(outgoingMessage);
	                clientSocket.close();
	    		} 
	    		
	    		//I/O error or interrupt
	    		catch (IOException e) 
	    		{
	    			e.printStackTrace();
	    		} 

	            //Sleep so one message is sent every 2 seconds
	            Thread.sleep(2000);
        	}
        	
        	//After all messages have been sent, close the socket
        	clientSocket.close();
        }
        
        //Bad IP address
        catch (UnknownHostException e) 
        {
            e.printStackTrace();
        }
        
        //failed or interrupted I/O
        catch (IOException e)	
        {
            e.printStackTrace();
        } 
        catch (InterruptedException e) 
        {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Populates the random message ArrayList with random data
	 * @param list - the list to be populated
	 */
	public void populateRandomMessages(ArrayList<String> list)
	{
		for(int i = 0; i < 50; i++)
		{
			//The maximum value of a 7 char binary string
			int max = 127;
			
			//The minimum value of the binary string
			int min = 0 ;
			
			//a random number between 0 and 127
			int randomData = (int) (Math.random() * ((max - min) + 1)) + min;
			
			//used to store the padding (preceding 0s) of the binary string if necessary 
			String padding = "";
			
			//The binary string formed from randomData
			String binary = Integer.toBinaryString(randomData);
			for(int j= 0; j < 7 - binary.length(); j++)
			{
				padding += "0";
			}
			binary = padding + binary;
			
			//adds the random data to the ArrayList
			list.add(binary);
		}

	}
	
	/**
	 * Builds the message to be sent by concatenating all of the information 
	 * @param clientNumber the number of the client(origin of the message)
	 * @param input the binary string the message will contain
	 * @param sabotaged - whether or not the message has purposely had its 
	 * checksum calculated incorrectly
	 * @return the message to be sent
	 */
	public String buildMessage(char clientNumber, String input, boolean sabotaged)
	{
		//The client number or source of the message
		char source = clientNumber;
		
		//The destination the message should be sent to
		char destination = randomDestination();
		
		//The binary string
		String data = input;
		
		//The value of the binary string as an int
		int checkSum =Integer.parseInt(data,2);
		
		//The ascii value coresponding to a specified int
		char checkSumAsChar;
			
		//Purposely corrupts the checksum if the message is flagged as sabotaged
		if(!sabotaged)
		{
			checkSumAsChar = (char)(checkSum);
		}
		else
		{
			checkSumAsChar = (char)(checkSum +30);
		}
		
		System.out.println("Outgoing Destination: " + destination);
		System.out.println("Outgoing Data: " + data);
		
		//The message to be sent to the Server
		String result = new StringBuilder().append(source).append(destination).append(checkSumAsChar).toString() + data;
		
		return result;
	}
	
	/**
	 * Randomly generates a number that will determine the messages destination
	 * @return The randomly generated number as a character
	 */
	public char randomDestination()
	{
		//The int value of ascii char '4'
		int asciiMax = 52;
		
		//The int value of ascii char '1'
		int asciiMin = 49;
		
		//Holds the destination of the message as an int
		int destination = (int) (Math.random() * ((asciiMax - asciiMin) + 1)) + asciiMin;
		
		//Holds the destination of the message as a char
		char destinationAsChar = (char)destination;
		return destinationAsChar;
	}
	
}

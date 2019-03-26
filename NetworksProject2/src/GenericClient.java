import java.net.Socket;
import java.util.ArrayList;

/**
 * Abstract Class which contains all of the generic behavior of a client
 * @author Christopher Roadcap
 */
public abstract class GenericClient 
{
	//Socket for outgoing messages
	static Socket clientSocket;	
	
	//Holds randomly generated messages
	static ArrayList<String> randomMessages = new ArrayList<String>(); 
	
	//Used to determine if the checksum for the message will be purposely corrupted
	static boolean sabotagedMessage; 
	
	
    /**
     * Fills the randomMessages array with random binary strings with 
     * the appropriate padding
     */
	public void populateRandomMessages()
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
			randomMessages.add(binary);
		}
			
	}
	
	/**
	 * 
	 * @param input the randomly generated binary string
	 * @param sabotaged indicates whether the checksum should be purposely calculated incorrectly
	 * @return The message to send to the server
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
		
		System.out.println("Checksum as int: " + checkSum);
		
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
		
		System.out.println("New implementaion: " + result);
		return result;
	}
	
	
	/**
	 * Calculates a random destination to send the message to (1-4)
	 * @return the destination of the message
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

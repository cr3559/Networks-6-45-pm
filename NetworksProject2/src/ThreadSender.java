import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.zip.CRC32;

public class ThreadSender implements Runnable 
{
	static ServerSocket serverSocket;
	ArrayList<String> randomMessages = new ArrayList<String>();;
	boolean sabotagedMessage;
	Socket clientSocket;
	char clientNumber;
	int port;
	char sourceNetwork = '7';
	//char source
	
	//Constructor
	public ThreadSender(char clientNumber, int port)
	{
		this.clientNumber = clientNumber;
		this.port = port;
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
        		String outgoingMessage = "";
 
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
	            outgoingMessage = buildMessage(clientNumber,randomMessages.get(i) , sabotagedMessage); // char(255) used for server scanner
	            
	            //socket created to this client's router (will change based on where we are running)
	            clientSocket = new Socket("127.0.0.1", this.port); 
	            
	            //The output stream to write to
	    		DataOutputStream out;
	    		try 
	    		{
	    			//assign the output stream to the sockets output stream
	    			out = new DataOutputStream(this.clientSocket.getOutputStream());
	    			out.flush();
	    			
	    			byte[] bytes= outgoingMessage.getBytes();
	    			//write the message to the stream
	                out.write(bytes);
	                out.flush();
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
		for(int i = 0; i < 10; i++)
		{
			String data = "";
			//The maximum value of a 7 char binary string
			int max = 126;
			
			//The minimum value of the binary string
			int min = 32 ;
			
			//a random number between 32 and 126
			
			for(int j = 0; j < 5; j++ )
			{
				int randomData = (int) (Math.random() * ((max - min) + 1)) + min;
				data += (char) randomData;
			}
			
			//adds the random data to the ArrayList
			list.add(data);
		}

	}
	
	/**
	 * Builds the message to be sent by concatenating all of the information 
	 * @param clientNumber the number of the client(origin of the message)
	 * @param input the payload of the message
	 * @param sabotaged - whether or not the message has purposely had its 
	 * checksum calculated incorrectly
	 * @return the message to be sent
	 */
	public String buildMessage(char clientNumber, String input, boolean sabotaged)
	{
		//The client number or source of the message
		char localSource = clientNumber;
		
		//The destination the message should be sent to
		char localDestination = randomDestination();
		
		//The binary string
		byte[] dataArray = input.getBytes();
		
		String dataString = new String(dataArray);
		
		
		
		String stringChecksum = determineSourceNetworkClientChar() + randomNetworkDestination() + input;
		//The ascii value corresponding to a specified int
		char checkSumAsChar = generateCheckSum(stringChecksum);
			
		//Purposely corrupts the checksum if the message is flagged as sabotaged
		if(sabotaged)
		{
		//	checkSumAsChar +=1;
		}
		
		System.out.println("Outgoing Destination: " + localDestination);
		System.out.println("Outgoing Data: " + input + "\n");
		
		
		//The message to be sent to the Server
		String result = "" + localSource + localDestination + checkSumAsChar + determineSourceNetworkClientChar() + randomNetworkDestination() + dataString	;			
				
		System.out.println("RESULT IS THIS: " + result);
		
		return result;
	}
	
	/**
	 * Randomly generates a number that will determine the messages destination
	 * @return The randomly generated number as a character
	 */
	public static char randomDestination()
	{
		//The int value of ascii char '4'
		int asciiMax = 49;
		
		//The int value of ascii char '1'
		int asciiMin = 49;
		
		//Holds the destination of the message as an int
		int destination = (int) (Math.random() * ((asciiMax - asciiMin) + 1)) + asciiMin;
		
		//Holds the destination of the message as a char
		char destinationAsChar = (char)destination;
		return destinationAsChar;
	}
    
    //Determines the network Source character based on HEX ascii value
    public char determineSourceNetworkClientChar()
    {
    	char result;
    	
    	if(this.clientNumber == '1')
    	{
    		//7:1
    		result = 'q';
    	}
    	else if(this.clientNumber == '2') 
    	{
    		//7:2
    		result = 'r';
    	}
    	else if(this.clientNumber =='3')
    	{
    		//7:3
    		result = 's';
    	}
    	else
    	{
    		//7:4
    		result = 't';
    	}
    	return result;
    }
    
    public static char randomNetworkDestination()
    {
    	String result= "";
    	int minAsciiNetwork = 1;
    	int maxAsciiNetwork = 7;
    	int destinationNetwork = (int) (Math.random() * ((maxAsciiNetwork - minAsciiNetwork) + 1)) + minAsciiNetwork;
    	
    	char a = randomDestination();
    	result = "" + destinationNetwork + a;
    	System.out.println("DN: "+ destinationNetwork + " RD: " +a ); 
    	
    	
    	
    	//decimal value of string as hex
    	int hex = Integer.parseInt(result,16);
    	
    	System.out.println("HEX STRING " + hex);
    	return (char) hex;
    	
    }
    
    
    public char generateCheckSum(String input)
    {
    	int finalChecksum = 0 ;
    	byte tempSum = 0;
		for(int i = 0; i < input.length(); i++)
		{	
			finalChecksum += input.charAt(i);
			
			if(finalChecksum > 255)
			{
				finalChecksum =  input.charAt(i);
			}
		}
		return (char) finalChecksum;
		
    }
    
    public static void main (String [] args)
    {
    	randomNetworkDestination();
    }
	
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

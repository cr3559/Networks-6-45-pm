import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client1 implements Runnable
{
	static Socket clientSocket;
	static ArrayList<String> randomMessages = new ArrayList<String>();
	static boolean sabotagedMessage;
	
    public  void setupClient() throws IOException, InterruptedException
    {	
    	populateRandomMessages();
    	
    
        try
        {

        	ServerSocket serverSocket = new ServerSocket(7771);
        	for( int i = 0;  i< 50; i++)
        	{
        		System.out.println("Message Number: " + (i + 1));
        		clientSocket = new Socket("127.0.0.1", 4446); 
        		String outgoingMessage = null;
        		
        	
	    		// Setup a socket that will try to connect to a specific address(usually an IPv4 address)
	    		// and port number.
        		
        	
        		
	            // First the client sets up its send connection
	            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream()); //USE PrintWriter out = new PrintWriter<clientSocket.getOutputStream(),true);(FROM GIRARD);
	            out.flush(); //Need this
	            
	            //Scanner to read message from  server
	            Scanner in = new Scanner(clientSocket.getInputStream());
	            
	           
	            if( (i + 1) % 5 == 0)
	            {
	            	sabotagedMessage = true;
	            }
	            else
	            {
	            	sabotagedMessage = false;
	            }
	            
	            outgoingMessage = buildMessage(randomMessages.get(i), sabotagedMessage)+ ""+(char)255; // delimitingCharacter
	          
	            //Sends user input to the server
	            out.writeBytes(outgoingMessage);
	            
	          
	            
	            Thread thread = new Thread(new ClientListener(serverSocket.accept()));
	            thread.start();
	           
	            
	            Thread.sleep(1000);
        	}
        	
        	clientSocket.close();

        }
        catch (UnknownHostException e) //bad address
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)	//error when setting up connection
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	}

    
    
	public static void populateRandomMessages()
	{
		for(int i = 0; i < 50; i++)
		{
			int max = 127;
			int min = 0 ;
			int randomData = (int) (Math.random() * ((max - min) + 1)) + min;
			String padding = "";
			
			String binary = Integer.toBinaryString(randomData);
			for(int j= 0; j < 7 - binary.length(); j++)
			{
				padding += "0";
			}
			binary = padding + binary;
			randomMessages.add(binary);
		}
			
	}
	
	public static String buildMessage(String input, boolean sabotaged)
	{
		char source = '1';
		char destination = randomDestination();
		String data = input;
		int checkSum =Integer.parseInt(data,2);
		char checkSumAsChar;
		System.out.println("Checksum as int: " + checkSum);
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
		
		String result = new StringBuilder().append(source).append(destination).append(checkSumAsChar).toString() + data;
		
		System.out.println("New implementaion: " + result);
		return result;
	}
	
	public static char randomDestination()
	{
		int asciiMax = 52;
		int asciiMin = 49;
		int destination = (int) (Math.random() * ((asciiMax - asciiMin) + 1)) + asciiMin;
		char destinationAsChar = (char)destination;
		return destinationAsChar;
		
	}

	@Override
	public void run() 
	{
		try 
		{
			setupClient();
		} 
		catch (IOException | InterruptedException e) 
		{
			e.printStackTrace();
		}
		
	}
	
    public static void main (String[] args) throws IOException, InterruptedException
    {
//    	Thread clientThread = new Thread(new Client1());
//    	clientThread.start();
    	
    	new Client1().setupClient();
    	
    }
    
    
    
    
    
}

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client1 
{
	static Socket clientSocket;
	static ArrayList<String> randomMessages = new ArrayList<String>();
	static boolean sabotagedMessage;
	
	
	
	public static void main (String[] args) throws IOException, InterruptedException
	{
	new Client1().setupClient();
	}
	

	
    public  void setupClient() throws IOException, InterruptedException
    {	
    	populateRandomMessages();
    	
    
        try
        {

        
        	ServerSocket serverSocket = new ServerSocket(7771);
        	for( int i = 0;  i< 50; i++)
        	{
        		System.out.println("Message Number: " + (i + 1));
        		clientSocket = new Socket("192.168.1.7", 4446); //adjust IP here
        		String outgoingMessage = null;
 
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
	            Thread clientSend = new Thread(new ClientSender(clientSocket, outgoingMessage));
	            clientSend.start();
	          
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

    
    
	public void populateRandomMessages()
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
	
	public String buildMessage(String input, boolean sabotaged)
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
	
	public char randomDestination()
	{
		int asciiMax = 52;
		int asciiMin = 49;
		int destination = (int) (Math.random() * ((asciiMax - asciiMin) + 1)) + asciiMin;
		char destinationAsChar = (char)destination;
		return destinationAsChar;
		
	}
 
}

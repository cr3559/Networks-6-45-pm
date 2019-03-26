import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * @author Christopher Roadcap
 * A client that sends outgoing messages, as well as receives incoming messages.
 * received messages are verified to ensure valid data via a checksum
 */
public class Client1 extends GenericClient
{

	/**
	 * Populates messages containing a random destination, and a random binary string.
	 * Then creates a thread to send the message, and a thread to receive incoming messages.
	 * @throws IOException
	 * @throws InterruptedException
	 */
    public  void setupClient() throws IOException, InterruptedException
    {	
    	populateRandomMessages();
        try
        {
        	//Socket that listens for incoming messages
        	ServerSocket serverSocket = new ServerSocket(7771);
        	
        	for( int i = 0;  i< randomMessages.size(); i++)
        	{
        		System.out.println("Message Number: " + (i + 1));
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
	            outgoingMessage = buildMessage('1',randomMessages.get(i), sabotagedMessage)+ ""+(char)255; // char(255) used for server scanner
	            
	            //socket created to this client's router (will change based on where we are running)
	            clientSocket = new Socket("192.168.1.7", 4446); 
	          
	            //Create thread to send message and start the thread
	            Thread clientSend = new Thread(new ClientSender(clientSocket, outgoingMessage));
	            clientSend.start();
	            
	            //Create thread to accept incoming messages and start the thread
	            Thread thread = new Thread(new ClientListener(serverSocket.accept()));
	            thread.start();
	            
	            //Sleep so one message is sent every 2 seconds
	            Thread.sleep(2000);
        	}
        	
        	//After all messages have been sent, close the socket
        	clientSocket.close();

        }
        
        //Bad IP address
        catch (UnknownHostException e) //bad address
        {
            e.printStackTrace();
        }
        
        //failed or interrupted I/O
        catch (IOException e)	
        {
            e.printStackTrace();
        }
    }
    
    public static void main (String[] args) throws IOException, InterruptedException
    {
    	new Client1().setupClient();
    }


}

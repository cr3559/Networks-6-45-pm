import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Christopher Roadcap
 * A client that sends outgoing messages, as well as receives incoming messages.
 * received messages are verified to ensure valid data via a checksum
 */
public class Client4 
{

	/**
	 * Populates messages containing a random destination, and a random binary string.
	 * Then creates a thread to send the message, and a thread to receive incoming messages.
	 * @throws IOException
	 * @throws InterruptedException
	 */
    public  void setupClient() throws IOException, InterruptedException
    {	
    	ServerSocket serverSocket = new ServerSocket(7771);
		//Socket thats listens for incoming messages
    	///ServerSocket serverSocket = new ServerSocket(7771);
    	
    	//Thread that sends messages
		Thread sender = new Thread(new ThreadSender('4',4447)); //ensure ports are correct
		sender.start();
		
		while(true)
		{
	    //Thread to accept incoming messages
        Thread receiver = new Thread(new ClientListener(serverSocket.accept()));
        receiver.start();
		}
    }
    
    public static void main (String[] args) throws IOException, InterruptedException
    { 
    	new Client4().setupClient();
    }

}

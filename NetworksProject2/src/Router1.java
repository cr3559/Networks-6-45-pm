import java.io.IOException;
import java.net.ServerSocket;
/**
 * @author Christopher Roadcap
 * 
 * A router that receives incoming messages and determines, 
 * where to send them based on the message contents and by 
 * referencing a routing table. Verifies the message has not been 
 * corrupted via a checksum.
 */
public class Router1 
{
	//Socket for receiving incoming messages
	ServerSocket serverSocket;
	
	/**
	 * Creates a server socket to listen for incoming messages. When a message is received,
	 * A thread is made to determine where the message should be sent next, and then sends the message
	 * @throws InterruptedException
	 */
    public void setupServer() throws InterruptedException
    {    	  
        try
        {	//Listening for incoming messages on port 4446
            serverSocket = new ServerSocket(4446); 	
        }
        //I/O error or interrupt
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
        		//Creates a new thread for each socket accept, thread determines destination and sends the message
        		Thread thread =  new Thread(new RequestHandler(serverSocket, "router_1_table.txt",'1'));
        		
        			//starts the thread
        			thread.start();
        			thread.join();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
	public static void main(String[] args) throws InterruptedException 
	{
		Router1 router1 = new Router1();
		router1.setupServer();

	}
}

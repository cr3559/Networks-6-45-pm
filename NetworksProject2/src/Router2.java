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
public class Router2 
{
	//Socket for receiving incoming messages
	ServerSocket serverSocket;
	
	/**
	 * Creates a server socket to listen for incoming messages. When a message is received,
	 * A thread is made to determine where the message should be sent next, and then sends the message
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public void setupServer() throws InterruptedException, IOException
    {    	  
       serverSocket = new ServerSocket(4447);
        try
        {
        	while(true)
        	{
        		//Creates a new thread for each socket accept, thread determines destination and sends the message
        		Thread thread =  new Thread(new RequestHandler(serverSocket.accept(), "router_2_table.txt",'2'));
        		
    			//starts the thread
    			thread.start();
    			thread.join();
        	}
        }
        //I/O error
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		Router2 router2 = new Router2();
		router2.setupServer();
	}
}

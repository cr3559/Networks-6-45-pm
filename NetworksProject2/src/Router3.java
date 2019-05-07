import java.io.IOException;
import java.net.ServerSocket;

public class Router3 
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
	            serverSocket = new ServerSocket(4566); 	
	        }
	        //I/O error or interrupt
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	        try
	        {
	        	while(true)
	        	{
	        		//Creates a new thread for each socket accept, thread determines destination and sends the message
	        		Thread thread =  new Thread(new RequestHandler(serverSocket.accept(), "router_3_table.txt",'3'));
	        		
	        			//starts the thread
	        			thread.start();
	        			
	        			//joins the thread
	        			thread.join();
	        	}
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	    }
	    
		public static void main(String[] args) throws InterruptedException 
		{
			Router3 router3 = new Router3();
			router3.setupServer();

		}
}



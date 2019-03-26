import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientSender implements Runnable
{
	Client1 client = new Client1();
	Socket clientSocket;
	String outgoingMessage;
	ArrayList<String> randomMessages = new ArrayList<String>();
	boolean sabotagedMessage;
	
	
	
	
	
	
	
	public ClientSender(Socket clientSocket, String outgoingMessage) 
	{
		this.clientSocket = clientSocket;
		this.outgoingMessage = outgoingMessage;
	}







	@Override
	public void run() 
	{
		 DataOutputStream out;
		try {
			out = new DataOutputStream(this.clientSocket.getOutputStream());
			out.flush();
            out.writeBytes(outgoingMessage);

		} 
		catch (IOException e) 
		{
			
			e.printStackTrace();
		} 
    }
}


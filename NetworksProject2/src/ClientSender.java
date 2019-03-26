import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class which handles sending messages from the client
 * @author Christopher Roadcap
 *
 */
public class ClientSender implements Runnable
{
	//The socket to send messages from
	Socket clientSocket;
	
	//The message to be sent
	String outgoingMessage;

	public ClientSender(Socket clientSocket, String outgoingMessage) 
	{
		//The socket to send messages from
		this.clientSocket = clientSocket;
		
		//The message to be sent
		this.outgoingMessage = outgoingMessage;
	}

	@Override
	public void run() 
	{	
		//The output stream to write to
		DataOutputStream out;
		try {
			//assign the output stream to the sockets output stream
			out = new DataOutputStream(this.clientSocket.getOutputStream());
			out.flush();
			
			//write the message to the stream
            out.writeBytes(outgoingMessage);
            clientSocket.close();

		} 
		//I/O error
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
    }
}


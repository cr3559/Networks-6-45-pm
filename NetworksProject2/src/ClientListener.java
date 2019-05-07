import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/**
 * Class used for listening for, and handling incoming messages to the client
 * @author Chris Roadcap
 */
public class ClientListener  implements Runnable
{
	//The socket which the message is recieved through
	Socket socket;
	ServerSocket serverSocket;
	
	public ClientListener(Socket incomingSocket) throws IOException
	{	
		//Socket which the message is received through
		socket = incomingSocket;
	}


	public  void run()
	{
		try 
		{	
			//Scanner used to read the incoming message
			Scanner scanner = new Scanner(socket.getInputStream());
			//Reads input Stream of server
			byte[] incoming = new byte[10];
			incoming = this.socket.getInputStream().readAllBytes();
			
			//String that will hold the message, formed from the input stream of the socket
			String message = new String(incoming);
			
			
			//Verify the checksum of the message
			if(verifyCheckSum(message))
			{
				System.out.println("Checksum Verified: Data Intact\n" + "Message Received\n" + message);
				this.socket.close();
			}
			else
			{
				System.out.println("Unable to verify checksum: Data Corrupt\n");
				this.socket.close();
			} 
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Determines if the checksum is valid by comparing the 
	 * ascii value as an int to the binary string as an int 
	 * @param incomingMessage the message that has been received
	 * @return True(valid checksum) or False(invalid checksum)
	 */
	public boolean verifyCheckSum(String message)
	{
		boolean isValid;
		int finalChecksum = 0 ;
		String payload = message.substring(3, 10);
		
		
		for(int i = 0; i < payload.length(); i++)
		{	
			finalChecksum += payload.charAt(i);
			
			if(finalChecksum > 255)
			{
				finalChecksum = payload.charAt(i);
			}
		}
			
			if (finalChecksum == (int) message.charAt(2))
			{
				//valid checksum
				isValid = true; 
			}
			else
			{
				//invalid checksum
				isValid = false;
			}
			
			return isValid;
		}
	}	
//}


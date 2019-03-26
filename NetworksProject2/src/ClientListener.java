import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
	
	public ClientListener(Socket socket) throws IOException
	{	
		//Socket which the message is received through
		this.socket = socket;
	}


	public  void run()
	{
		try 
		{	//Scanner used to read the incoming message
			Scanner scanner = new Scanner(socket.getInputStream());
			
			//The delimiting character for the scanner
			char delimiterChar = (char) 255;
			
			//The delimiter as a string
			String stringDelimiter = new StringBuilder().append(delimiterChar).toString();
			
			//Setting the delimiter
			scanner = scanner.useDelimiter(stringDelimiter);
			{
				if(scanner.hasNext())
				{
					//Message that is received
					String message = scanner.next();
					
					//Determines if the checksum is valid
					if(verifyCheckSum(message))
					{
						System.out.println("Checksum Verified: Data Intact\n");
						this.socket.close();
					}
				}
				else
				{
					System.out.println("Unable to verify checksum: Data Corrupt\n");
				} 
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
	public boolean verifyCheckSum(String incomingMessage)
	{
		//The source of the incoming message
		char source = incomingMessage.charAt(0);
		
		//The destination of the incoming message
		char destination = incomingMessage.charAt(1);
		
		//The checksum of the incoming message
		int checkSum = incomingMessage.charAt(2);
		
		//The binary string of the incoming data
		String data = incomingMessage.substring(3, 10);
		System.out.println("data: " + data);
		
		//The binary string in decimal form
		int actualSum = Integer.parseInt(data, 2);
		
		System.out.println("Incoming Source: "+ source);
		System.out.println("Incoming Destination: " + destination);
		System.out.println("Incoming Checksum: " + checkSum);		
		System.out.println("Incoming Data: " + data);

		if(checkSum == actualSum)
		{
			return true;
		}
		else
		{
			return false;
		}
	
	}
		
}


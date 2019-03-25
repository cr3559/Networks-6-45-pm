import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.util.Scanner;

public class ClientListener  implements Runnable
{
	Socket socket;
	public ClientListener(Socket socket) throws IOException
	{
		this.socket = socket;
		
	}


		public  void run()
		{
			try 
			{
				Scanner scanner = new Scanner(socket.getInputStream());
				char delimiterChar = (char) 255;
				String stringDelimiter = new StringBuilder().append(delimiterChar).toString();
				
				scanner = scanner.useDelimiter(stringDelimiter);

				//while(true)// Added this while loop to get C code to run
				{
					if(scanner.hasNext())
					{
						String message = scanner.next();
						
						if(verifyCheckSum(message))
						{
							System.out.println("Checksum Verified: Data Intact\n");
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
		
		
		
	
		
	
		public boolean verifyCheckSum(String incomingMessage)
		{
			char source = incomingMessage.charAt(0);
			char destination = incomingMessage.charAt(1);
			int checkSum = incomingMessage.charAt(2);
			String data = incomingMessage.substring(3, 10);
			System.out.println("data: " + data);
			int actualSum = Integer.parseInt(data, 2);
			
			System.out.println("Checksum: " + checkSum);
			System.out.println("binary checksum: " + actualSum) ;
			
			System.out.println("Incoming Destination: " + destination);
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


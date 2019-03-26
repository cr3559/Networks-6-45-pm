import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class RequestHandler implements Runnable
{
Socket socket;
String routerTable;
	
	
	
	public RequestHandler(Socket socket, String routerTable) throws IOException
	{
		this.socket = socket;
		this.routerTable = routerTable;
	}
	
	
	
	public void run()
	{
		try 
		{
			Socket destinationSocket = new Socket("127.0.0.1", 7771 ); // use routing table for ip here
			Reader reader = new InputStreamReader(socket.getInputStream());
			
			//Reads input Stream of server
			Scanner scanner = new Scanner(socket.getInputStream());
			char delimiterChar = (char) 255;
			String stringDelimiter = new StringBuilder().append(delimiterChar).toString();
			scanner = scanner.useDelimiter(stringDelimiter);

				if(scanner.hasNext())
				{
					String message = "";
					message = message + scanner.next();
					
					if(verifyCheckSum(message))
					{
						
					System.out.println("Checksum Verified");
					System.out.println("Full Message: " + message + "\n");
					findDestination(message);
					
					
					//OutputStream of server
					DataOutputStream output = new DataOutputStream(destinationSocket.getOutputStream());
					
					output.writeBytes(message +"\n");  // Need newline here for client scanner to find.
					output.flush();
					destinationSocket.close();
					}
			
					else 
					{
						System.out.println("Data Corrupted, message discarded.\n");
						destinationSocket.close();
					}
				}

		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}
	
	public String findDestination(String message) throws FileNotFoundException
	{
		ArrayList<DataHolder> list = TableReader.readTable(this.routerTable);

		char source = message.charAt(0);
		char destination = message.charAt(1);
		for(DataHolder dataHolder: list)
		{
			if(dataHolder.source.equals(source) && dataHolder.destination.equals(destination))
			{
				DataHolder destinationData = dataHolder;
				return destinationData.address;
			}
		}
		return null;
	}

	public boolean verifyCheckSum(String message)
	{
		int checkSum = message.charAt(2);
		String data = message.substring(3, 10);
		int actualSum = Integer.parseInt(data, 2);

		if(checkSum == actualSum)
		{
			char source = message.charAt(0);
			char destination = message.charAt(1);
			System.out.println("Source: " + source);
			System.out.println("Destination: " + destination);
			System.out.println("Data: " + data);
		
			return true;
		}
		
		else
		{
			return false;
		}
		
	}
	  
}

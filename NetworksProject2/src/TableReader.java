import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TableReader 
{
	
	public static  ArrayList<DataHolder> readTable(String fileName) throws FileNotFoundException
	{
		File routingTable = new File(fileName);
		Scanner scanner = new Scanner(routingTable);
		
		ArrayList<DataHolder> list = new ArrayList<DataHolder>();
		//scanner.useDelimiter(" ");
		while(scanner.hasNext())
		{
			String source = scanner.next();
			String destination = scanner.next();
			String address = scanner.next();
			//System.out.println("s" + source + "d " + destination + "a " + address);
			DataHolder dataHolder = new DataHolder(source, destination,  address);
			list.add(dataHolder);
		}
		scanner.close();
		return list;
		
	}
	
	
	
}

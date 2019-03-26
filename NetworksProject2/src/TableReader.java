import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Class used for reading the contents of the routing table
 * @author Christopher Roadcap
 *
 */
public class TableReader 
{
	/**
	 * Returns a list of all of the rows of the routing table, encapsulated by a DataHolder object
	 * @param fileName the name of the routing table file
	 * @return a list of DataHolder objects, each of which represents a row in the routing table
	 * @throws FileNotFoundException
	 */
	public static  ArrayList<DataHolder> readTable(String fileName) throws FileNotFoundException
	{
		//The routing table 
		File routingTable = new File(fileName);
		
		//Scanner for reading the routing table
		Scanner scanner = new Scanner(routingTable);
		
		//Holds each row of the table as a separate object
		ArrayList<DataHolder> list = new ArrayList<DataHolder>();
		while(scanner.hasNext())
		{
			//The source column of the routing table
			String source = scanner.next();
			
			//The destination column of the routing table
			String destination = scanner.next();
			
			//The IP address column of the routing table
			String address = scanner.next();
			
			//Object that encapsulates 1 row of the routing table
			DataHolder dataHolder = new DataHolder(source, destination,  address);
			list.add(dataHolder);
		}
		scanner.close();
		return list;
		
	}
	
	
	
}

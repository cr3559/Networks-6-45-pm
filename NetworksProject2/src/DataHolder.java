/**
 * An object used to encaspulate 1 row of a routing table
 * @author Christopher Roadcap
 *
 */
public class DataHolder 
{
	//The source column of the routing table
	public String source;
	
	//The destination column of the routing table
	public String destination;
	
	//The address column of the routing table
	public String address;
	
	/**
	 * Constructor
	 * @param source The source column of the routing table
	 * @param destination The destination column of the routing table
	 * @param address The address column of the routing table
	 */
	public DataHolder(String source, String destination, String address)
	{
		this.source = source;
		this.destination = destination;
		this.address = address;
	}
}

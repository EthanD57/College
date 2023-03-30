package Project8;

public class Tower<T>
{
	private Disk<T> base;  // pointer to the first) disk at the BASE of the tower
	private Disk<T> top;  // pointer to the last disk at the TOP of the tower

	public Tower()
	{	base = null; // compiler does this anyway. just for emphasis
	}
	
	public boolean empty()
	{
		return (base==null);
	}

	// i.e. the old insertAtTail or now insertAtTop we call it a PUSH
	public void push(T label)
	{	// BASE CASE  (get it? ;)
		if ( empty() )
		{	base = new Disk<T>( label );
			top = base;
			return; 
		}	
		top.next = new Disk<T>( label );
		top = top.next; // 	NOW TOP PTS AT NEW TOP NODE
	}

	// i.e. the old removeAtTail or now removeAtTop we call it a POP
	public Disk<T> pop() throws Exception
	{
		// BASE CASE #1  NO DISKS ON TOWER
		if ( empty() ) throw new Exception("\nFATAL ERROR: ATTEMPTED TO POP DISK OFF EMPTY TOWER\n");
		
		// BASE CASE #2  EXACTLY 1 DISK ON THE TOWER
		if ( base.next==null ) // BE CLEAR HERE! base.next IS THE .next REF IN THE FIRST DISK ON THE BASE
		{	Disk<T> topDisk = base; // TOP DISK NOW PTS AT FIRST & ONLY DISK. THE ONE WE WANT TO RETURN
			base = top = null; // WE JUST REMOVED THE ONLY DISK ON THE TOWER SO ITS DEF. EMPTY
			return topDisk;
		}
		
		// CASE #3  TWO OR MORE DISKS ON THE TOWER. MUST WALK TO AND STOP UNDER THE *NEXT TO LAST* DISK  
		Disk<T> curr = base; // CURR PTS AT 2ND DISK
		while (curr.next.next != null )
			curr = curr.next;
		
		// NOW CURR PTS AT NEXT TO TOP DISK
		Disk<T> topDisk = curr.next; 
		curr.next=null; // THE TOP DISK HAS BENN UN HOOKED FROM THE END OF LIST
		top = curr;
		return topDisk; // NO WORRIES WE KEPT A REF TO IT AND WE RETURN THAT REF
	}	
	
	// prints the tower base to top, left to right,  respectively
	// 
	public String toString()
	{	if  (empty() ) 	return "EMPTY\t";
		String toString = "";
		for ( Disk<T> curr = base; curr != null ; curr=curr.next )
			toString += curr.label + " ";
			
		return toString;
	}
} // END TOWER CLASS

/*###############################################################################*/

class Disk<T>
{
	T label;
	Disk<T> next;

	Disk(T data)
	{
		this( data, null );
	}
  
	Disk(T label, Disk<T> next)
	{
		this.label = label;
		this.next = next; 
	}
	
} // END DISK CLASS

//I did not write this file. It was provided by the professor.
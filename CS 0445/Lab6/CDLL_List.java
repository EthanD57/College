package Lab6;
import java.io.*;

public class CDLL_List<T>
{
	private CDLL_Node<T> head;  // pointer to the front (first) element of the list

	public CDLL_List()
	{
		head = null; // compiler does this anyway. just for emphasis
	}

	// LOAD LINKED LIST FORM INCOMING FILE

	public CDLL_List( String fileName, String insertionMode ) throws Exception
	{
			BufferedReader infile = new BufferedReader( new FileReader( fileName ) );
			while ( infile.ready() )
			{	@SuppressWarnings("unchecked")
				T data = (T) infile.readLine(); // CAST CUASES WARNING (WHICH WE CONVENIENTLY SUPPRESS)
				if ( insertionMode.equals("atFront") )
					insertAtFront( data );
				else if ( insertionMode.equals( "atTail" ) )
					insertAtTail( data );
				else
					die( "FATAL ERROR: Unrecognized insertion mode <" + insertionMode + ">. Aborting program" );
			}
			infile.close();
	}

	private void die( String errMsg )
	{
		System.out.println( errMsg );
		System.exit(0);
	}

	// ########################## Y O U   W R I T E / F I L L   I N   T H E S E   M E T H O D S ########################

	// OF COURSE MORE EFFICIENT TO KEEP INTERNAL COUNTER BUT YOU COMPUTE IT DYNAMICALLY WITH A TRAVERSAL LOOP
	public int size()
	{
		CDLL_Node<T> temp = head;
		int count = 1;
		temp = temp.next;
		while (temp != head)
			{
				temp = temp.next;
				count++;
			} 
			return count;
	}

	
	public void insertAtFront(T data)
	{
		insertAtTail(data);
		head = head.prev;
	}

	
	public void insertAtTail(T data)
	{
		if (head == null)
		{
			head = new CDLL_Node<T>(data, head, head);
			head.next = head;
			head.prev = head;
		}
		else
		{
			head.prev = new CDLL_Node<T>(data, head.prev, head);
			head.prev.prev.next = head.prev;
		}
	}

	// RETURN TRUE/FALSE THIS LIST CONTAINS A NODE WITH DATA EQUALS KEY
	public boolean contains( T key )
	{
		return search( key ) != null; 
	}

	// RETURN REF TO THE FIRST NODE (SEARCH CLOCKWISE FOLLOWING next) THAT CONTAINS THIS KEY. DO -NOT- RETURN REF TO KEY ISIDE NODE
	// RETURN NULL IF NOT FOUND
	public CDLL_Node<T> search( T key )
	{
		CDLL_Node<T> temp = head;
		if (temp.data.equals(key))
		{
			return temp;
		}
		temp = temp.next;
		while (temp != head)
		{
			if (temp.data.equals(key))
			{
				return temp;
			}
			temp = temp.next;
		}
		return null;
	}

	// RETURNS CONATENATION OF CLOCKWISE TRAVERSAL
	public String toString()
	{
		CDLL_Node<T> temp = head;
		String str = "";
		str += temp.data;
		temp = temp.next;
		while (temp != head)
		{
			str += "<=>" + temp.data;
			temp = temp.next;
		}
		return str;
	}

}

// PRIVATE TO CODE OUTSIDE FILE. BUT PUBLIC TO CODE INSIDE
class CDLL_Node<T>
{
  T data; // DONT DEFINE MEMBERS AS PUBLIC OR PRIVATE
  CDLL_Node<T> prev, next; //
  CDLL_Node() 		{ this( null, null, null ); }
  CDLL_Node(T data) { this( data, null, null);  }
  CDLL_Node(T data, CDLL_Node<T> prev, CDLL_Node<T> next)
  {	this.data=data; this.prev=prev; this.next=next;
  }
  public String toString() // TOSTRING MUST BE PUBLIC
  {	return ""+data;
  }
} //END NODE CLASS

//A starter file was given. I only wrote the code inside the "contains", "search", "toString", "insertAtFront", "insertAtTail", and "size" methods. The rest was given.


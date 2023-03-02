import java.io.*;


public class CDLL_JosephusList<T>
{
	private CDLL_Node<T> head;  // pointer to the front (first) element of the list
	// private Scanner kbd = new Scanner(System.in); // FOR DEBUGGING. See executeRitual() method 
	public CDLL_JosephusList()
	{
		head = null; // compiler does this anyway. just for emphasis
	}

	// LOAD LINKED LIST FORM INCOMING FILE
	
	public CDLL_JosephusList( String infileName ) throws Exception
	{
		BufferedReader infile = new BufferedReader( new FileReader( infileName ) );	
		while ( infile.ready() )
		{	@SuppressWarnings("unchecked") 
			T data = (T) infile.readLine(); // CAST CUASES WARNING (WHICH WE CONVENIENTLY SUPPRESS)
			insertAtTail( data ); 
		}
		infile.close();
	}
	

	
	// ##########################T H E S E   M E T H O D S ########################
	
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
	
	// RETURN REF TO THE FIRST NODE CONTAINING  KEY. ELSE RETURN NULL
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
	
	void removeNode( CDLL_Node<T> deadNode )
	{
		if (deadNode == head)
		{
			head = head.next;
		}
		else
		{
			deadNode.prev.next = deadNode.next;
			deadNode.next.prev = deadNode.prev;
		}
	}
	
	public void executeRitual( T first2Bdeleted, int skipCount )
	{
		if (size() <= 1 ) return;
		CDLL_Node<T> curr = search( first2Bdeleted );
		if ( curr==null ) return;
		
		do
		{
			CDLL_Node<T> deadNode = curr;
			T deadName = deadNode.data;
			
			System.out.println( "stopping on " + deadName + " to delete " + deadName);

			if (skipCount > 0)
			{
				curr = curr.next;
			}
			else
			{
				curr = curr.prev;
			}

			if (head == deadNode)
			{
				head = curr;
			}

			removeNode(deadNode);

			System.out.println("deleted. list now: " + toString() );
			
			if (size() == 1)
			{
				return;
			}
			
			System.out.print("resuming at " + curr.data + ", skipping " + curr.data + " " + (Math.abs(skipCount)-1) + " nodes");

			if (skipCount > 0)
			{
				System.out.println(" CLOCKWISE");
			}
			else
			{
				System.out.println(" COUNTERWISE");
			}

			if (skipCount > 0)
			{
				for (int i = 0; i < skipCount; i++)
				{
					curr = curr.next;
				}
			}
			else
			{
				for (int i = 0; i > skipCount; i--)
				{
					curr = curr.prev;
				}
			}
			
		}
		while (true);  

	}
	
} // END CDLL_LIST CLASS

//A starter file was given. I only wrote the methods below the indicated line.

class CDLL_Node<T>
{
	T data;
	CDLL_Node<T> prev, next; // EACH CDLL_Node PTS TO ITS PREV  & NEXT

CDLL_Node()
  {
	this( null, null, null );  // 3 FIELDS TO INIT
  }

CDLL_Node(T data)
  {
	this( data, null, null);
  }

CDLL_Node(T data, CDLL_Node<T> prev, CDLL_Node<T> next)
  {
	this.data =  data;
	this.prev = prev;
	this.next = next;
  }

  public String toString()
  {
	return ""+ this.data;
  } 
	 
} //EOF
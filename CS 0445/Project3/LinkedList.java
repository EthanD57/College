package Project3;
import java.io.*;


public class LinkedList <T extends Comparable<T>> 
{
	private Node<T> head;  

	public LinkedList()
	{
		head = null;
	}


	@SuppressWarnings("unchecked")
	public LinkedList( String fileName, boolean orderedFlag )
	{	head = null;
		try
		{
			BufferedReader infile = new BufferedReader( new FileReader( fileName ) );
			while ( infile.ready() )
			{
				if (orderedFlag)
					insertInOrder( (T)infile.readLine() );  
				else
					insertAtTail( (T)infile.readLine() );  
			}
			infile.close();
		}
		catch( Exception e )
		{
			System.out.println( "FATAL ERROR CAUGHT IN C'TOR: " + e );
			System.exit(0);
		}
	}


	public void insertAtFront(T data)
	{
		head = new Node<T>(data,head);
	}


	public String toString()
	{
		String toString = "";

		for (Node<T> curr = head; curr != null; curr = curr.next )
		{
			toString += curr.data;
			if (curr.next != null)
				toString += " ";
		}

		return toString;
	}

	// ########################## I WROTE THESE METHODS ########################



	public int size() 
	{
		int count = 0;
        if (head == null) return 0;
		for (Node<T> temp = head; temp != null; temp = temp.next )
		{
			count++;
		}
		return count;
	}

	public boolean empty()
	{
		return this.size() == 0 ? true : false;
	}

	public boolean contains( T key )
	{
		return search(key) == null ? false : true;
	}

	public Node<T> search( T key )
	{
		if (head == null) return null;

		for (Node<T> temp = head; temp != null; temp = temp.next )
		{
			if (temp.next.data.equals(key))
			{
				return temp;
			}
			temp = temp.next;
		} 
		return null;
	}

	public void insertAtTail(T data)
	{
		Node<T> temp = head;
		if (head == null)
		{
			insertAtFront(data);
		}
		else
		{
			while (temp.next != null)
			{
				temp = temp.next;
			}
			temp.setNext(new Node<T>(data));
		}
		
	}
	
	public void insertInOrder(T  data)
	{
		Node<T> temp = head;
		int length = this.size();
		int count = 0;
        if (temp == null)
        {
            insertAtFront(data);
        }
        else
        {
            while (temp.next != null || count < length)
            {
                if (temp.data.compareTo(data) > 0)
                {
                    insertAtFront(data);
                    return;
                }
				else if (temp.data.compareTo(data) < 0 && temp.next == null)
				{
					temp.setNext(new Node<T>(data, temp.next));
					return;
				}
                else if (temp.data.compareTo(data) < 0 && temp.next.data.compareTo(data) > 0)
                {
                    temp.setNext(new Node<T>(data, temp.next));
                    return;
                }
                temp = temp.next;
				count++;
            }
            temp.next = new Node<T>(data);
        }
    }

	public boolean remove(T key)
	{
		Node<T> temp = head;
		if (head == null) return false;
		else if (head.data.equals(key)) 
		{
			head = head.next;
			return true;
		}
		while (temp != null)
		{
			if (temp.next == null && !(temp.data.equals(key))) return false; 
			else if (temp.next.next == null && temp.next.data.equals(key))
			{
				temp.next = null;
				return true;	
			}
			temp = temp.next;
		}
		return false;
	}

	public boolean removeAtTail()	// RETURNS TRUE IF THERE WAS NODE TO REMOVE
	{
		Node<T> secondToLast = head;
		if (head == null) return false;
		if (head.next == null)
		{
			head = null;
			return true;
		}
		while (secondToLast.next.next != null)
		{
			secondToLast = secondToLast.next;
		}
		secondToLast.next = null;
		return true;
	
	}

	public boolean removeAtFront() 
	{
		if (head == null) return false;
		head = head.next;
		return true;
	}

	public LinkedList<T> union( LinkedList<T> other )
	{
		LinkedList<T> union = new LinkedList<T>();
		Node<T> set1 = head;
		Node<T> set2 = other.head;
		while (set1 != null)
		{
			union.insertAtTail(set1.data);
			set1 = set1.next;
		}
		while (set2 != null)
		{
			for (Node<T> temp = union.head; temp != null; temp = temp.next)
			{
				if (temp.data.equals(set2.data)) break;
				else if (temp.next == null)
				{
					union.insertInOrder(set2.data);
				}
				
			}
			set2 = set2.next;
		}

		return union;
	}

	public LinkedList<T> inter( LinkedList<T> other )
	{
		LinkedList<T> inter = new LinkedList<T>();
		for (Node<T> set1 = head; set1 != null; set1 = set1.next)
		{
			for (Node<T> set2 = other.head; set2 != null; set2 = set2.next)
				{
					if (set1.data.equals(set2.data))
					{
						inter.insertInOrder(set1.data);
					}
				}
		}

		return inter;
	}
	public LinkedList<T> diff( LinkedList<T> other )
	{
		LinkedList<T> diff = new LinkedList<T>();
		Node<T> set1 = head;
		while (set1 != null)
		{
			for (Node<T> temp = other.head; temp != null; temp = temp.next)
			{
				if (set1.data.equals(temp.data)) break;
				else if (temp.next == null)
				{
					diff.insertInOrder(set1.data);
				}
			}
			set1 = set1.next;
		}

		return diff;
	}
	public LinkedList<T> xor( LinkedList<T> other )
	{
		return this.union(other).diff(this.inter(other));
	}

}  



class Node<T extends Comparable<T>> 

{
  T data;
  Node<T> next;

  public Node()
  {
    this( null, null );
  }

  public Node(T data)
  {
    this( data, null );
  }

  public Node(T data, Node<T> next)
  {
    this.data = data;
    this.next = next;
  }

  public void setNext(Node<T> next)
  {
    this.next = next;
  }
  public String toString()
  {
	  return ""+ this.next;
  } 
	 
} 

//A starter file was provided for Project 3 by Timothy Hoffman for CS 0445. 
//Only the methods below the indicated line were written by me. I did not write the Node Class. 

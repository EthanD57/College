package Lab3;
public class LinkedList<T>
{
	private Node
	<T> head;

	public LinkedList()
	{
		head = null; 
	}

	public void insertAtFront(T data)
	{
		head = new Node<T>(data,head);
	}

	public String toString()
	{
		String toString = "";

		for (Node<T> curr = head; curr != null; curr = curr.getNext())
		{
			toString += curr.getData();
			if (curr.getNext() != null)
				toString += " -> ";
		}

		return toString + "\n";
	}

	// ########################## I WROTE THE METHODS BELOW THIS ########################


	public void insertAtTail(T data)
	{
		Node<T> temp = head;
		if (head == null)
		{
			insertAtFront(data);
		}
		else
		{
			while (temp.getNext() != null)
			{
				temp = temp.getNext();
			}
			temp.setNext(new Node<T>(data));
		}
		
	
	}


	public int size()
	{
		Node<T> temp = head;
		int count = 1;
		while (temp.getNext() != null)
			{
				temp = temp.getNext();
				count++;
			} 
			return count;
	}
	


	public boolean contains( T key )
	{
		return search(key) == null ? false : true;
	}

	public Node<T> search( T key )
	{
		Node<T> temp = head;
		if (head == null) return null;;

		while (temp.getNext() != null)
			{
				if (temp.getData().equals(key))
				{
					return temp;
				}
				temp = temp.getNext();
			} 
			return null;
	}
} 
// A starter file was provied by Timothy Hoffman for the class CS 0445 at the University of Pittsburgh. 
//I only wrote the methods below the indicated line.
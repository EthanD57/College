public class LL_Recursive<T>
{
	private Node<T> head;  

	public LL_Recursive()
	{
		head = null;
	}

	public void insertAtFront(T data)
	{
		head = new Node<T>(data,head);
	}

 	public boolean contains( T key )
 	{
 		if (search(key) == null) return false;
		else return true;
	}

	public void insertAtTail(T data)
	{
		if (head == null) insertAtFront(data);
		else insertAtTailHelper(data, head);
	}
	public void insertAtTailHelper(T data, Node<T> curr)
	{
		if (curr.next == null) curr.next = new Node<T>(data);
		else insertAtTailHelper(data, curr.next);
	}

	public int size()
	{
		return sizeHelper( head ); 
	}
	private int sizeHelper( Node<T> head )
	{
		if (head==null) return 0;
		return 1 + sizeHelper(head.next);
	}
	
	public String toString()
	{
		return toStringHelper(head);
	}
	public String toStringHelper( Node<T> curr )
	{
		if (curr == null) return "";
		if (curr.next == null) return curr.data + "";
		return curr.data + " -> " + toStringHelper(curr.next);
	}

	public Node<T> search( T key )
	{
		return searchHelper(key, head);
	}
	public Node<T> searchHelper( T key, Node<T> curr )
	{
		if (curr == null) return null;
		if (curr.data.equals(key)) return curr;
		return searchHelper(key, curr.next);
	}
} 

//A starter file was provided. I only wrote the code in "contains", "insertAtTail", "toString", "search", "searchHelper", 
//"toStringHelper", and "insertAtTailHelper". The rest of the code was provided by the instructor.


class Node<T>
{ T data;
  Node<T> next;
  Node() { this( null, null ); }
  Node(T data){this( data, null ); }
  Node(T data, Node<T> next) { this.data=data; this.next=next; }
  public String toString() { return ""+data; }
}
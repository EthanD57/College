public class LL_Recursive<T>
{
	private NodeR<T> head;  

	public LL_Recursive()
	{
		head = null;
	}

	public void insertAtFront(T data)
	{
		head = new NodeR<T>(data,head);
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
	public void insertAtTailHelper(T data, NodeR<T> curr)
	{
		if (curr.next == null) curr.next = new NodeR<T>(data);
		else insertAtTailHelper(data, curr.next);
	}

	public int size()
	{
		return sizeHelper( head ); 
	}
	private int sizeHelper( NodeR<T> head )
	{
		if (head==null) return 0;
		return 1 + sizeHelper(head.next);
	}
	
	public String toString()
	{
		return toStringHelper(head);
	}
	public String toStringHelper( NodeR<T> curr )
	{
		if (curr == null) return "";
		if (curr.next == null) return curr.data + "";
		return curr.data + " -> " + toStringHelper(curr.next);
	}

	public NodeR<T> search( T key )
	{
		return searchHelper(key, head);
	}
	public NodeR<T> searchHelper( T key, NodeR<T> curr )
	{
		if (curr == null) return null;
		if (curr.data.equals(key)) return curr;
		return searchHelper(key, curr.next);
	}
} 

//A starter file was provided. I only wrote the code in "contains", "insertAtTail", "toString", "search", "searchHelper", 
//"toStringHelper", and "insertAtTailHelper". The rest of the code was provided by the instructor.


class NodeR<T>
{ T data;
  NodeR<T> next;
  NodeR() { this( null, null ); }
  NodeR(T data){this( data, null ); }
  NodeR(T data, NodeR<T> next) { this.data=data; this.next=next; }
  public String toString() { return ""+data; }
}
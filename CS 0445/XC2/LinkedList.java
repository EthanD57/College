package XC2;
public class LinkedList<T extends Comparable<T>>
{
	private Node<T> head;  // pointer to the front (first) element of the list
	private Node<T> tail;  // pointer to the last elem of the list ( caboose or tail node)

	public LinkedList()
	{
		head = null; // compiler does this anyway. just for emphasis
		tail = head;
	}

	// USE THE TOSTRING AS OUR PRINT
	public String toString()
	{
		String toString = "";

		for (Node<T> curr = head; curr != null; curr = curr.next)
		{
			toString += curr.data;		// WE ASSUME OUR T TYPE HAS toString() DEFINED
			if (curr.next != null)
				toString += " -> ";
		}

		return (String) (toString + " ");
	}
	
	// ########################## Y O U   W R I T E   T H E S E    M E T H O D S  
	// . . .AND ANY SUPPORTING METHODS YOU NEED FOR THEM 

	// THIS VERSION JUST LOADS THE LISTS FROM THE FILE BEFORE THEY ARE MERGED
	public void insertAtTail(T data)
	{
		Node<T> temp = head;
		if (head == null)
		{
			head = new Node<T>(data,head);
		}
		else
		{
			while (temp.next != null)
			{
				temp = temp.next;
			}
			temp.next = new Node<T>(data);
		}
		
	
	}
	public LinkedList<T> merge(LinkedList<T> other) 
	{
		LinkedList<T> result = new LinkedList<T>();
		Node<T> current1 = head;
		Node<T> current2 = other.head;
	
		// Compare the first elements of each list and add the smaller one to the result
		while (current1 != null && current2 != null) {
			if (current1.data.compareTo(current2.data) <= 0) {
				result.insertAtTail(current1.data);
				current1 = current1.next;
			} else {
				result.insertAtTail(current2.data);
				current2 = current2.next;
			}
		}
	
		// If there are any remaining elements in the first list, add them to the result
		while (current1 != null) {
			result.insertAtTail(current1.data);
			current1 = current1.next;
		}
	
		// If there are any remaining elements in the second list, add them to the result
		while (current2 != null) {
			result.insertAtTail(current2.data);
			current2 = current2.next;
		}
	
		return result;
	}
	
} //END OF LINKEDLIST CLASS DEFINITION

// NODE CLASS
 class Node<T>
{
  T data;
  Node<T> next;

  Node(T data)
  {
    this( data, null );
  }
  Node(T data, Node<T> next)
  {
    this.data = data; 
    this.next = next; 
  }

  public String toString()
  {
	  return "" + data; // stringify the data
  } 
	 
} //EOF

//This file was provided by the instructor. I only wrote the "merge" method and the "insertAtTail" method.
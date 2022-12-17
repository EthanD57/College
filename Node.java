public class Node
{
  private String data;
  private Node next;

  public Node()
  {
    this( null, null );
  }

  public Node(String data)
  {
    this( data, null );
  }

  public Node(String data, Node next)
  {
    setData( data );
    setNext( next );
  }

  public String getData()
  {
    return data;
  }

  public Node getNext()
  {
    return next;
  }

  public void setData(String data)
  {
     this.data = data;
  }

  public void setNext(Node next)
  {
    this.next = next;
  }
  public String toString()
  {
	  return ""+getData();
  } 
	 
} //EOF
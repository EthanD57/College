package Lab3;
public class Node<T>
{
  private T data;
  private Node<T> next;

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
    setData( data );
    setNext( next );
  }

  public T getData()
  {
    return data;
  }

  public Node<T> getNext()
  {
    return next;
  }

  public void setData(T data)
  {
     this.data = data;
  }

  public void setNext(Node<T> next)
  {
    this.next = next;
  }
  public String toString()
  {
	  return ""+getData();
  } 
	 
}

//This is a starter file for Lab 3.  You will need this file to run LinkedList.java
//This file was written by Timothy Hoffman for CS 0445 at the University of Pittsburgh. I did not write this file.
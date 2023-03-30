package Lab8;
import java.util.concurrent.ExecutionException;

public class Tower<T>
{
	private Disk<T> base;  // pointer to first disk at BASE of the tower (i.e. the old base pointer)
	
	public Tower()
	{	base = null; // compiler does this anyway. just for emphasis
	}

	public boolean empty()
	{
		return (base==null);
	}

	// i.e. the old insertAtTail or now insertAtTop we call it a PUSH
	public void push(T label)
	{
		Disk<T> temp = base;
		if (base == null)
		{
			base = new Disk<T>(label,base);
		}
		else
		{
			while (temp.next != null)
			{
				temp = temp.next;
			}
			temp.next = (new Disk<T>(label));
		}
	}

	// i.e. the old removeAtTail or now removeAtTop we call it a POP
	public Disk<T> pop() throws Exception
	{
		Disk<T> secondToLast = base;
		if (base == null) new ExecutionException(null);
		if (base.next == null)
		{
			base = null;
			return secondToLast;
		}
		while (secondToLast.next.next != null)
		{
			secondToLast = secondToLast.next;
		}
		secondToLast.next = null;
		return secondToLast;
	}

	// prints the tower base to top, left to right,  respectively //
	public String toString()
	{	if (base == null ) 	return "EMPTY\t";
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

	Disk(T label)
	{	this( label, null );
	}

	Disk(T label, Disk<T> next)
	{	this.label = label;
		this.next = next;
	}

} // END DISK CLASS

//A starter file was given. I only wrote the "pop" and "push" methods. 
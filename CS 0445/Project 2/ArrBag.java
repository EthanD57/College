import java.io.*;

public class ArrBag<T>
{
	final int NOT_FOUND = -1;
	final int INITIAL_CAPACITY = 1;
	private int count; // LOGICAL SIZE
	T[] theArray; 

	// DEFAULT C'TOR
	@SuppressWarnings("unchecked") // SUPRESSION GOES **HERE** BEFORE METHOD
	public ArrBag()
	{	theArray = (T[]) new Object[INITIAL_CAPACITY]; // SUPPRESSION DOES NOT BELONG INSIDE THE METHOD
		count = 0; // i.e. logical size, actual number of elems in the array
	}

	// SPECIFIC INITIAL LENGTH VERSION OF CONSTRUCTOR
	// only the union,intersect,diff call this version of constructor
	@SuppressWarnings("unchecked")
	public ArrBag( int optionalCapacity )
	{
		theArray = (T[]) new Object[optionalCapacity ]; // CASTING TO T[] (creates warning we have to suppress
		count = 0; // i.e. logical size, actual number of elems in the array
	}

	// C'TOR LOADS FROM FILE Of STRINGS
	@SuppressWarnings("unchecked")
	public ArrBag( String infileName ) throws Exception
	{
		count = 0; // i.e. logical size, actual number of elems in the array
		BufferedReader infile = new BufferedReader( new FileReader(  infileName ) );
		while ( infile.ready() )
			this.add( (T) infile.readLine() ); // Note the 'this.' is redundant !!
		infile.close();
	}

	// APPENDS NEW ELEM AT END OF LOGICAL ARRAY. UPSIZES FIRST IF NEEDED
	public boolean add( T element ) // THIS IS AN APPEND TO THE LOGICAL END OF THE ARRAY AT INDEX count
	{	if (element == null ) return false;
		if (size() == theArray.length) upSize(); // DOUBLES PHYSICAL CAPACITY
		theArray[ count++] = element; // ADD IS THE "setter"
		return true; // success. it was added
	}

	// INCR EVERY SUCESSFULL ADD, DECR EVERY SUCESSFUL REMOVE
	public int size()
	{
		return count;
	}

	// RETURNS TRUE IF THERE ARE NO ELEMENTS IN THE ARRAY, OTHERWISE FALSE
	public boolean isEmpty()
	{
		return false;
	}

	public T get( int index )
	{
		if ( index < 0 || index >=size() )
			die("FATAL ERROR IN .get() method. Index to uninitialized element or out of array bounds. Bogus index was: " + index + "\n" );
		return theArray[index];
	}

	// SEARCHES FOR THE KEY. TRUE IF FOUND, OTHERWISE FALSE
	public boolean contains( T key )
	{	if (key == null) return false;
		for ( int i=0 ; i < size() ; ++i )
			if ( get(i).equals( key ) ) // WE ARE MAKING AN ASSUMPTION ABOUT TYPE T... WHAT IS IT?
		return true;
		return false;
	}

	void die( String errMsg )
	{
		System.out.println( errMsg );
		System.exit(0);
	}

	// --------------------------------------------------------------------------------------------
	// # # # # # # # # # # #   I WROTE THE METHODS BELOW  # # # # # # # # # # #
	// --------------------------------------------------------------------------------------------

	public void clear()
	{
		count = 0;
	}

	@SuppressWarnings("unchecked")
	private void upSize()
	{
        T[] tempArray = (T[]) new Object[(theArray.length)*2];
		
        for (int i = 0; i < theArray.length; i++) {
            tempArray[i] = get(i);
        }
        theArray = tempArray;

	}

	public String toString()
	{
		String toString  = ""; 
        for (int i = 0; i < size(); i++) {
            toString += theArray[i] + " ";
        }
		return toString;
	}


	public ArrBag<T> union( ArrBag<T> other )
	{

		ArrBag<T> union = new ArrBag<T>( this.size() + other.size() ); 
        for (int i = 0; i < theArray.length; i++) {
            union.add(get(i));
        }
        for (int i = 0; i < other.size(); i++) {
            for (int j = 0; j < theArray.length; j++) {
                if (union.contains(other.get(i))) {
                    break;
                }
                else 
                {
                    union.add(other.get(i));
                }
            }
        }
        union.trim();

		return union;
	}


	public ArrBag<T> intersection( ArrBag<T> other )
	{
		ArrBag<T> intersection = new ArrBag<T>( this.size()); 
        ArrBag<T> temp = new ArrBag<T>(other.size() );

        for (int i = 0; i < theArray.length; i++) {
            if (other.contains(get(i))) {
                temp.add(get(i));
            }
        }

        intersection = temp;
        intersection.trim();
		return intersection;
	}

	
	public ArrBag<T> difference( ArrBag<T> other )
	{
		ArrBag<T> difference = new ArrBag<T>( this.size()); 
        ArrBag<T> temp = new ArrBag<T>(other.size() );

        for (int i = 0; i < theArray.length; i++) {
            if (other.contains(get(i))==false) {
                temp.add(get(i));
            }
        }

        difference = temp;
        difference.trim();
		return difference;
	}


	public ArrBag<T> xor( ArrBag<T> other )
	{
		return this.union(other).difference(this.intersection(other));
    } 

    @SuppressWarnings("unchecked")    
    public void trim() {
        T[] tempArray = (T[]) new Object[size()];
        for (int i = 0; i < size(); i++) {
            tempArray[i] = get(i);
        }
        theArray = tempArray;
    }
    
}

//A starter file was provided and written by Timothy Hoffman for CS 0445 at the University of Pittsburgh.
// I wrote the methods below the indicated line. 
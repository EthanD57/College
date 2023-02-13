public class MyString implements MyComparable
{
	private char[] letters;
	final int NOT_FOUND = -1;

	public MyString( String other )
	{
		letters = other.toCharArray();
	}

	public MyString( MyString other )
	{
		this( other.toString() );
	}

	public String toString()
	{
		return new String(letters);
	}

	public int length()
	{
		return letters.length;
	}

	public char charAt(int index)
	{
		return letters[index];
	}

///////////////// Y O U    M U S T    W R I T E    T H E S E    T W O    M E T H O D S //////////////

	//RETURNS 0 if strings are lexically identical in every way, +1 if this string greater, else -1
	public int myCompareTo(MyString other)
	{

		int shorter = Math.min(this.length(), other.length());

		for (int i = 0; i < shorter; i++)
		{
			if (this.charAt(i) > other.charAt(i))
				return 1;
			else if (this.charAt(i) < other.charAt(i))
				return -1;
		}

		if (this.length() > other.length())
			return 1;

		else if (this.length() < other.length())
			return -1;

		return 0;
		
		
	}

	//RETURNS 0 iff strings are lexically identical
	public boolean equals(MyString other)
	{
		if (this.length() != other.length()) 
			return false;

		for (int i=0; i< this.length(); i++)
		{
			if (this.charAt(i) != other.charAt(i))
				return false;
		}
		return true;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////

} 

//This is a starter file for Xtra Credit 1. I only wrote the "equals" and "myCompareTo" methods. 
//The rest of the file was provided by the instructor.
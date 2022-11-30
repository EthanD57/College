package Project7;
public class SignedFraction extends Fraction {
	private int sign;

	public SignedFraction(int n, int d) {
		if (n < 0 && d < 0) setSign(1);
		else if (n < 0 || d < 0)setSign(-1);
		else setSign(1);

		n = Math.abs(n);
		d = Math.abs(d);
	
//THESE ARE ONLY HERE TO MAKE VSCODE IGNORE THE PROBLEMS.
		// int gcd = gcd(n, d);
		// setNumer(n / gcd);
		// setDenom(d / gcd);
	}

	private void setSign(int s) {
		if (s != 1 && s != -1) // THE ONLY POSSIBLE LEAGAL VALUES FOR THE SIGN
		{
			System.out.println("FATAL ERROR: Attempt to assign invalid sign value: " + s);
			System.exit(0);
		}
		sign = s;
	}

	private int getSign() {
		return sign;
	}

	// OVERWRITE/RIDE PARENT toString
	public String toString() {
		return getSign() * getNumer() + "/" + getDenom() + "\t=" + +(getSign() * (double) getNumer() / (double) getDenom());
	}

}

//A STARTER FILE WAS GIVEN. ONLY THE CODE IN THE "SignedFraction" CONSTRUCTOR AND THE toString WAS WRITTEN BY ME. 
//This is a subclass for Project 7 in CS 0401. It is a fraction with a sign. This will not work by itself.

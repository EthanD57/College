
public class Fraction {

	private int numer;
	private int denom;

	public int getNumer() {
		return numer;
	}

	public int getDenom() {
		return denom;
	}

	public String toString() {
		return numer + "/" + denom;
	}

	public void setNumer(int n) {
		numer = n;
	}

	public void setDenom(int d) {
		if (d != 0)
			denom = d;
		else {
			System.out.println("You cannot divide a number by 0!");
			System.exit(0);

		}
	}

	public Fraction() {
		this(0, 1);
	}

	public Fraction(int n) {
		this(n, 1);
	}

	public Fraction(int n, int d) {
		int gcd = gcd(n, d);
		setNumer(n / gcd);
		setDenom(d / gcd);
	}

	public Fraction(Fraction other) {
		this(other.getNumer(), other.getDenom());
	}

	// ALL OF THE METHODS BELOW ARE WRITTEN BY ME

	private int gcd(int n, int d) {
		return (n % d != 0) ? gcd(d, n % d) : d;
	}

	public Fraction add(Fraction that) {
		return new Fraction((this.getNumer() * that.getDenom()) + (this.getDenom() * that.getNumer()),
				(this.getDenom() * that.getDenom()));
	}

	public Fraction subtract(Fraction that) {
		return new Fraction((this.getNumer() * that.getDenom() - this.getDenom() * that.getNumer()),
				(this.getDenom() * that.getDenom()));
	}

	public Fraction multiply(Fraction that) {
		return new Fraction(this.getNumer() * that.getNumer(), this.getDenom() * that.getDenom());
	}

	public Fraction divide(Fraction that) {
		return new Fraction(this.getNumer() * that.getDenom(), this.getDenom() * that.getNumer());
	}

	public Fraction reciprocal() {
		return new Fraction(this.getDenom(), this.getNumer());
	}

}

// A STARTER FILE WAS GIVEN FOR THIS CODE. THE CODE SECTION INDICATED WAS MADE
// BY ME.

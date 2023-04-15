package Lab10;
import java.io.*;

public class HashCodeTester
{
  public HashCodeTester() {}

  public static void main(String[] args) throws Exception
  {
    double startTime = System.currentTimeMillis();

    if (args.length < 3) die("usage: java HashTester <numOfBuckets> <maxBucketSize> <fileOfStrings>");
    // remove commas from the args[0] i.e. number of buckets i.e. array size

    args[0] = args[0].replace(",", ""); // use reg expression to replace evey comma with nothing
    int numOfBuckets = Integer.parseInt(args[0]);
    int idealBucketSize = Integer.parseInt(args[1]);
    String infileName = args[2];
    MyHashCode hcode = new MyHashCode(numOfBuckets, idealBucketSize);
    BufferedReader infile = new BufferedReader(new FileReader(infileName));
    while (infile.ready())
    {
      hcode.add(infile.readLine());
      if (hcode.size() == numOfBuckets * idealBucketSize) break;
    }
    infile.close();
    double stopTime = System.currentTimeMillis();
	double yourRunTime = (stopTime-startTime) / 1000.0;
    double yourVariance = hcode.printStats(); // i.e. hopefully not much more than 9.5 variance on bucket lengths
    System.out.format( "YourRunTime: %3.2f sec. yourVariance: %3.2f\n", yourRunTime, yourVariance  );
  }

  static void die(String errMsg)
  {
    System.out.println(errMsg);
    System.exit(0);
  }
}

//This is meant to run with the MyHashCode.java file. I did not write this file. This was provided by the professor.
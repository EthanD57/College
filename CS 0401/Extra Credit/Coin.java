import java.util.Random;

public class Coin
{
    private int NumHeads;
    private int NumTails;
    private Random Random;

    public Coin (int seed) 
    {
        NumHeads = 0;
        NumTails = 0;
        this.Random = new Random(seed);
    }
    
    public String flip()
    {
        if (this.Random.nextInt(2) == 1)
        {
            NumHeads++;
            return "H";
        }
        else
        {
            NumTails++;
            return "T";
        }
    }
    
    public int getNumHeads() 
    {
        return this.NumHeads;
    }
    
    public int getNumTails()
    {
        return this.NumTails;
    }

    public void reset()
    {
        this.NumHeads = 0;
        this.NumTails = 0;
    }
}
//NO STARTER FILE GIVEN. 
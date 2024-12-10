import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashMap;

public class vmsim {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java vmsim –n <numframes> -a <opt|rand|clock|lru> <tracefile> ");
            return;
        }
        String filename = args[3];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            int numFrames = Integer.parseInt(args[1]);
            String algorithm = args[2];
            if (algorithm.equals("opt")) {

            }
            else if (algorithm.equals("rand")) {

            }
            else if (algorithm.equals("clock")) {

            }
            else if (algorithm.equals("lru")) {

            }
            else {
                System.out.println("Invalid algorithm choice");
                return;
            }

        }
        catch (Exception e) {
            System.out.println("File not found");
            return;
        }
    }

}

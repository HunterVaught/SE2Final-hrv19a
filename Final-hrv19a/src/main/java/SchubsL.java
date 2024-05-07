import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class SchubsL {
    /*
     * Program : SchubsL.java
     * Description : Compresses files using LZW compression. Accepts GLOB-ing for
     * filename, each file is compressed individually.
     * Author : Hunter Vaught
     * Date : 5/6/2024
     * Course : CS 375
     * Compile : javac SchubsL.java
     * Execute : java SchubsL <filename>
     * Execute from project root: java -cp target/classes SchubsL <filename>
     */

    public static final String PATH = "." + File.separator;
    private static final int R = 128; // number of input chars
    private static final int L = 256; // number of codewords = 2^W
    private static final int W = 8; // codeword width
    private static BinaryOut out;

    public static void compress(BufferedReader reader, String output) throws FileNotFoundException, IOException {
        // set output using BinaryOut
        out = new BinaryOut(new FileOutputStream(output));

        // use BufferedReader to read in the file contents
        String input = "";
        try {
            String line = reader.readLine();
            while (line != null) {
                input += line;
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println("Error reading file");
        }

        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++) {
            st.put("" + (char) i, i);
        }
        int code = R + 1; // R is codeword for EOF

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input); // Find max prefix match s.
            out.write(st.get(s), W); // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L) { // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            }
            input = input.substring(t); // Scan past s in input.
        }
        out.write(R, W);
        out.close();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // check if the number of arguments is correct
        if (args.length == 0) {
            throw new IllegalArgumentException("Please give an argument for the input file(s)");
        }

        // set up reader
        BufferedReader reader = null;
        String output = "";

        // compress every argument given
        for (int i = 0; i < args.length; i++) {
            // set up input file
            try {
                reader = new BufferedReader(new FileReader(PATH + args[i]));
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("Input File not found");
            }

            // set up output file
            output = PATH + args[i] + ".ll";

            // compress
            compress(reader, output);
        }

    }

}

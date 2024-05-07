/*
 * Program : Deschubs.java
 * Description : Uncompresses LZW and Huffman compressed files or extracts files from a Tars archive.
 *               Type of decompression determined by file extension. Can only be fed one file name, does not handle GLOB-ing.
 * Author : Hunter Vaught
 * Date : 5/6/2024
 * Course : CS 375
 * Compile : javac Deschubs.java
 * Execute : java Deschubs <filename>.hh|ll or java Deschubs <archive-name>.zh
 * Execute from project root: java -cp target/classes Deschubs <filename>.hh|ll or java -cp target/classes Deschubs <archive-name>.zh
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Deschubs {

    public static final String PATH = "." + File.separator;

    // stuff for LZW
    private static final int R = 128; // number of input chars
    private static final int L = 256; // number of codewords = 2^W
    private static final int W = 8; // codeword width

    public static void expandL() {
        String[] st = new String[L];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = ""; // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R)
                break;
            String s = st[codeword];
            if (i == codeword)
                s = val + val.charAt(0); // special case hack
            if (i < L)
                st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }

    // Huffman trie node
    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    // expand Huffman-encoded input from standard input and write to standard output
    public static void expandH() {

        // read in Huffman trie from input stream
        Node root = readTrie();

        // number of bytes to write
        int length = BinaryStdIn.readInt();

        // decode using the Huffman trie
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = BinaryStdIn.readBoolean();
                if (bit)
                    x = x.right;
                else
                    x = x.left;
            }
            BinaryStdOut.write(x.ch);
        }
        BinaryStdOut.flush();
    }

    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        // check if the number of arguments is correct
        if (args.length != 1) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        // handle decompressions separately based on file extension
        // .ll file
        if (args[0].endsWith(".ll")) {
            // set up input file
            try {
                FileInputStream finstream = new FileInputStream(PATH + args[0]);
                System.setIn(finstream);
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("Input File not found");
            }

            // set up output file as input file name without extension
            try {
                String output = args[0].substring(0, args[0].lastIndexOf('.'));
                System.setOut(new PrintStream(new File(output)));
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("Output File not found");
            }

            // expand
            expandL();
        }

        // .hh file
        else if (args[0].endsWith(".hh")) {
            // set up input file
            try {
                FileInputStream finstream = new FileInputStream(PATH + args[0]);
                System.setIn(finstream);
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("Input File not found");
            }

            // set up output file as input file name without extension
            try {
                String output = args[0].substring(0, args[0].lastIndexOf('.'));
                System.setOut(new PrintStream(new File(output)));
            } catch (FileNotFoundException e) {
                System.out.println("Output File not found");
            }

            // expand
            expandH();
        }

        // .zh file
        else {
            // variables
            BinaryIn in = null; // input stream
            String trash = ""; // holds separator characters

            try {
                // check if the archive exists

                // check if the archive is a directory

                // check if the archive is empty

                // set up input stream
                in = new BinaryIn(args[0]);

                // extract files, repeating until input stream is empty
                while (!in.isEmpty()) {
                    // get file name size
                    int fileNameSize = in.readInt();

                    // remove separator
                    trash += in.readChar();

                    // get file name
                    String filename = "";
                    for (int i = 0; i < fileNameSize; i++) {
                        filename += in.readChar();
                    }

                    // check if file already exists
                    File file = new File(filename);
                    if (file.exists()) {
                        throw new IllegalArgumentException(
                                "Destination File already exists: move original copies before extracting from archive");
                    }

                    // create directory if it doesn't exist
                    if (!file.getParentFile().exists()) {
                        System.out.println("Creating directory: ");
                        file.getParentFile().mkdirs();
                    }

                    // remove separator
                    trash += in.readChar();

                    // get file size
                    long fileSize = in.readLong();

                    // remove separator
                    trash += in.readChar();

                    // set up output to file
                    BinaryOut out = new BinaryOut(filename);
                    for (int i = 0; i < fileSize; i++) {
                        out.write(in.readChar());
                    }

                    // close output stream
                    out.close();

                    // special case: check for trailing separator
                    if (in.isEmpty()) {
                        break;
                    }
                    trash += in.readChar();
                }

            } finally {
                System.err.println("Archive successfully decompressed.");
            }
        }

    }

}

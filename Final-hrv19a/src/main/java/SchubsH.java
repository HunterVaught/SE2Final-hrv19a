/*
 * Program : SchubsH.java
 * Description : Uses Huffman's algorithm to compress a text file.  Accepts GLOB-ing for filename, each file is compressed individually.
 * Author : Hunter Vaught
 * Date : 5/6/2024
 * Course : CS 375
 * Compile : javac SchubsH.java
 * Execute : java SchubsH <filename>
 * Execute from project root: java -cp target/classes SchubsH <filename>
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;

public class SchubsH {

    // alphabet size of extended ASCII
    private static final int R = 256;

    public static final String PATH = "." + File.separator;
    private static BinaryOut out;
    private static BinaryIn in;

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

    // compress bytes from standard input and write to standard output
    public static void compress(String output, String inp)
            throws FileNotFoundException, IOException {
        // set input and output using BinaryIn and BinaryOut
        out = new BinaryOut(new FileOutputStream(output));
        in = new BinaryIn(new FileInputStream(inp));

        // read the input
        String s = in.readString();
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        // build Huffman trie
        Node root = buildTrie(freq);

        // build code table
        String[] st = new String[R];
        buildCode(st, root, "");

        // print trie for decoder
        writeTrie(root);

        // print number of bytes in original uncompressed message
        out.write(input.length);

        // use Huffman code to encode input
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    out.write(false);
                } else if (code.charAt(j) == '1') {
                    out.write(true);
                } else
                    throw new RuntimeException("Illegal state");
            }
        }

        // flush output stream
        out.flush();
    }

    // build the Huffman trie given frequencies
    private static Node buildTrie(int[] freq) {

        // initialze priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0) {
                pq.insert(new Node(i, freq[i], null, null));
            }

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }

    // write bitstring-encoded trie to standard output
    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            out.write(true);
            out.write(x.ch);
            return;
        }
        out.write(false);
        writeTrie(x.left);
        writeTrie(x.right);
    }

    // make a lookup table from symbols and their encodings
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
        }
    }

    public static void main(String[] args) throws IOException, FileNotFoundException {
        String output = "", input = "";

        if (args.length == 0) {
            throw new IllegalArgumentException("Please give an argument for the input file(s)");
        }

        // compress each file given as an argument
        for (int i = 0; i < args.length; i++) {
            // prepare input and output to be passed to compress()
            input = PATH + args[i];
            output = PATH + args[i] + ".hh";

            if (!new File(input).exists())
                throw new FileNotFoundException("File not found: " + input);

            // check if file is empty
            if (new File(input).length() == 0)
                throw new IOException("File is empty: " + input);

            // compress
            compress(output, input);
        }
    }

}

/*
 * Program : SchubsArc.java
 * Description : Takes one or more files and converts them to Tars archive format.
 * Author : Hunter Vaught
 * Date : 5/6/2024
 * Course : CS 375
 * Compile : javac SchubsArc.java
 * Execute : java SchubsArc <archive-name> <GLOB> [<GLOB> or filenames...]
 * Execute from project root: java -cp target/classes SchubsArc <archive-name> <GLOB> [<GLOB> or filenames...]
 */

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class SchubsArc {
    public static void main(String[] args) throws IOException {
        // check for the correct number of arguments

        // variables
        File in = null;
        BinaryIn binIn = null;
        BinaryOut out = null;

        char separator = (char) 255; // all ones 11111111

        try {
            // check if the archive already exists
            File archiveZH = new File(args[0] + ".zh");
            if (archiveZH.exists()) {
                throw new IOException("Archive already exists");
            }

            // set output stream
            out = new BinaryOut(args[0] + ".zh");

            // write each file in args to the archive
            for (int i = 1; i < args.length; i++) {

                // check if input file exists
                in = new File(args[i]);
                if (!in.exists()) {
                    throw new FileNotFoundException("Source File not found");
                }

                // check if the file is a directory

                // get the file size
                long filesize = in.length();

                // check if the file is empty

                // get the filename size
                int filenamesize = args[i].length();

                // write stuff to archive
                out.write(filenamesize);
                out.write(separator);
                out.write(args[i]);
                out.write(separator);
                out.write(filesize);
                out.write(separator);
                binIn = new BinaryIn(args[i]);
                while (!binIn.isEmpty()) {
                    out.write(binIn.readChar());
                }
                out.write(separator);
            }

            // close output stream after all input files are written
            out.close();

            // compress the archive
            SchubsH.compress(args[0], archiveZH.toString());

        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
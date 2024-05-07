import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.Test;

/*
 * Program     : SchubsLTest.java
 * Description : Tests SchubsL.java
 * Author      : Hunter Vaught
 * Date        : 5/6/2024
 * Course      : CS 375
 * Compile     : mvn test
 * Execute     : mvn test
 */

public class SchubsLTest {
    public static final String PATH = "." + File.separator + "src" + File.separator + "files" + File.separator
            + "test_files" + File.separator;

    // helper function that compares two files
    // source:
    // https://www.baeldung.com/java-compare-files#:~:text=The%20method%20Files%3A%3Amismatch,bytes%20of%20the%20first%20mismatch.
    // Note: returns -1 if the files are identical
    public static long filesCompareByByte(Path path1, Path path2) throws IOException {
        try (BufferedInputStream fis1 = new BufferedInputStream(new FileInputStream(path1.toFile()));
                BufferedInputStream fis2 = new BufferedInputStream(new FileInputStream(path2.toFile()))) {

            int ch = 0;
            long pos = 1;
            while ((ch = fis1.read()) != -1) {
                if (ch != fis2.read()) {
                    return pos;
                }
                pos++;
            }
            if (fis2.read() == -1) {
                return -1;
            } else {
                return pos;
            }
        }
    }

    // does standard testing of LZW encoding
    @Test
    public void standardTest() throws IOException {
        // delete the generated file if it exists
        Path path = Path.of(PATH + "standard_test01.txt.ll");
        if (path.toFile().exists()) {
            path.toFile().delete();
        }
        // run LZW compression
        SchubsL.main(new String[] { "src/files/test_files/standard_test01.txt" });

        // create Path objects to use in filesCompareByByte
        Path path1 = Path.of(PATH + "standard_test01.txt.ll");
        Path path2 = Path.of(PATH + "standard_test01_correct.txt.ll");
        // compare generated compressed file against correct answer
        assertTrue(filesCompareByByte(path1, path2) == -1);
    }

    // tests with GLOBing
    @Test
    public void globTest() throws IOException {
        // delete the generated files if they exist
        Path path = Path.of(PATH + "standard_test01.txt.ll");
        if (path.toFile().exists()) {
            path.toFile().delete();
        }
        Path path1 = Path.of(PATH + "standard_test02.txt.ll");
        if (path1.toFile().exists()) {
            path1.toFile().delete();
        }
        Path path2 = Path.of(PATH + "standard_test03.txt.ll");
        if (path2.toFile().exists()) {
            path2.toFile().delete();
        }
        // run LZW compression
        SchubsL.main(new String[] { "src/files/test_files/standard_test01.txt",
                "src/files/test_files/standard_test02.txt", "src/files/test_files/standard_test03.txt" });

        // create Path objects to use in filesCompareByByte
        path1 = Path.of(PATH + "standard_test01.txt.ll");
        path2 = Path.of(PATH + "standard_test01_correct.txt.ll");
        // compare generated compressed file against correct answer
        assertTrue(filesCompareByByte(path1, path2) == -1);
        // create Path objects to use in filesCompareByByte
        path1 = Path.of(PATH + "standard_test02.txt.ll");
        path2 = Path.of(PATH + "standard_test02_correct.txt.ll");
        // compare generated compressed file against correct answer
        assertTrue(filesCompareByByte(path1, path2) == -1);
        // create Path objects to use in filesCompareByByte
        path1 = Path.of(PATH + "standard_test03.txt.ll");
        path2 = Path.of(PATH + "standard_test03_correct.txt.ll");
        // compare generated compressed file against correct answer
        assertTrue(filesCompareByByte(path1, path2) == -1);
    }

    // test for not enough arguments
    @Test(expected = IllegalArgumentException.class)
    public void notEnoughArgumentsTest() throws FileNotFoundException, IOException {
        // run SchubsL
        SchubsL.main(new String[] {});
    }

    // test for non-existent file
    @Test(expected = FileNotFoundException.class)
    public void nonExistentFileTest() throws FileNotFoundException, IOException {
        // run SchubsL
        SchubsL.main(new String[] { "nonExistent.txt" });
    }

    // test all uppercase
    @Test
    public void allUppercaseTest() throws IOException {
        // delete the generated file if it exists
        Path path = Path.of(PATH + "allUpperTest.txt.ll");
        if (path.toFile().exists()) {
            path.toFile().delete();
        }
        // run SchubsL
        SchubsL.main(new String[] { "src/files/test_files/allUpperTest.txt" });

        // create Path objects to use in filesCompareByByte
        Path path1 = Path.of(PATH + "allUpperTest.txt.ll");
        Path path2 = Path.of(PATH + "allUpperTestCorrect.txt.ll");
        // checks that the correct compressed file is equal to the generated file
        assertTrue(filesCompareByByte(path1, path2) == -1);
    }

    // test all lowercase
    @Test
    public void allLowercaseTest() throws IOException {
        // delete the generated file if it exists
        Path path = Path.of(PATH + "allLowerTest.txt.ll");
        if (path.toFile().exists()) {
            path.toFile().delete();
        }
        // run SchubsL
        SchubsL.main(new String[] { "src/files/test_files/allLowerTest.txt" });

        // create Path objects to use in filesCompareByByte
        Path path1 = Path.of(PATH + "allLowerTest.txt.ll");
        Path path2 = Path.of(PATH + "allLowerTestCorrect.txt.ll");
        // checks that the correct compressed file is equal to the generated file
        assertTrue(filesCompareByByte(path1, path2) == -1);
    }

    // test for file with no spaces
    @Test
    public void noSpacesTest() throws IOException {
        // delete the generated file if it exists
        Path path = Path.of(PATH + "noSpacesTest.txt.ll");
        if (path.toFile().exists()) {
            path.toFile().delete();
        }
        // run SchubsL
        SchubsL.main(new String[] { "src/files/test_files/noSpacesTest.txt" });

        // create Path objects to use in filesCompareByByte
        Path path1 = Path.of(PATH + "noSpacesTest.txt.ll");
        Path path2 = Path.of(PATH + "noSpacesTestCorrect.txt.ll");
        // checks that the correct compressed file is equal to the generated file
        assertTrue(filesCompareByByte(path1, path2) == -1);
    }

    // test for empty file
    @Test
    public void emptyFileTest() throws IOException {
        // delete the generated file if it exists
        Path path = Path.of(PATH + "emptyTest.txt.ll");
        if (path.toFile().exists()) {
            path.toFile().delete();
        }
        // run SchubsL
        SchubsL.main(new String[] { "src/files/test_files/emptyTest.txt" });

        // create Path objects to use in filesCompareByByte
        Path path1 = Path.of(PATH + "emptyTest.txt.ll");
        Path path2 = Path.of(PATH + "emptyTestCorrect.txt.ll");
        // checks that the correct compressed file is equal to the generated file
        assertTrue(filesCompareByByte(path1, path2) == -1);
    }
}

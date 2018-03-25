package test;

import java.util.*;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

import main.JavaJarFileReader;

/**
 * JUnit 4 Test for {@link JavaJarFileReader} class
 *
 * @author Sze Lok Irene Chan
 * @since 24 March 2018
 *
 */

public class TypeFinderRecursiveTest{

    /**
     * Check that the correct number of java files get found in a directory
     * @throws IOException
     */
    @Test
    public void testTotalNumberOfJavaFilesForNestedPath() throws IOException{
        String directoryPath = TestSuite.NESTED_JAVA_FILES_TEST_DIR;
        int result = JavaJarFileReader.getJavaFileNames(directoryPath).size();
        assertEquals(10, result);
    }

    /**
     * Check that trying to get files from an invalid directory throws a NullPointerException
     * @throws IOException
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testGetFileContentForInvalidDirectory() throws IOException{
        String directoryPath = "";
        ArrayList<String> fileNames = JavaJarFileReader.getAllFilesToString(directoryPath);
    }

    /**
     * Check that trying to read from an invalid file throws a FileNotFoundException
     * 
     * @throws IOException
     */
    @SuppressWarnings("unused")
    @Test(expected = FileNotFoundException.class)
    public void testGetFileToStringForInvalidFilePath() throws IOException{
        String filePath = "";
        String result = JavaJarFileReader.getJavaFileContentToString(filePath);
    }

}
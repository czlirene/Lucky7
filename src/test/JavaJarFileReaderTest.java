package test;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;

import org.junit.Test;

import main.JavaJarFileReader;

/**
 * JUnit 4 Test for {@link JavaJarFileReader} class
 *
 * @author Irene Chan
 * @since 20 March 2018
 *
 */
public class JavaJarFileReaderTest {

	/**
	 * The expected string representation of TestClass
	 */
	private static final String TestClassString = "package test;" + TestSuite.lineSeparator + TestSuite.lineSeparator
			+ "public class TestClass {" + TestSuite.lineSeparator + TestSuite.lineSeparator + "}"
			+ TestSuite.lineSeparator;

	/**
	 * Check if all the names of Java files of testPackage directory can be acquired
	 *
	 * @throws NotDirectoryException
	 */
	@Test
	public void testGetJavaFileNamesForTestPackage() throws NotDirectoryException {
		ArrayList<String> javaFileNames = JavaJarFileReader.getFileNames(TestSuite.JAVA_FILE_READER_TEST_DIR);

/* 		Actual results
		src/test/javaFileReaderTestPackage/Apple.java
		src/test/javaFileReaderTestPackage/Banana.java
		src/test/javaFileReaderTestPackage/Level 2.1/PassTest.java
		src/test/javaFileReaderTestPackage/Level 2/Big2.java
		src/test/javaFileReaderTestPackage/Level 2/Level 3/Bar.java
		src/test/javaFileReaderTestPackage/Level 2/Level 3/Foo.java
		src/test/javaFileReaderTestPackage/Level 2/Level 3/touche.jar
		src/test/javaFileReaderTestPackage/Level 2/SomeTrash.java
		src/test/javaFileReaderTestPackage/Zebra.java 
*/
		
		assertEquals(true, javaFileNames.get(0).endsWith("Apple.java"));
		assertEquals(true, javaFileNames.get(1).endsWith("Banana.java"));
		assertEquals(true, javaFileNames.get(2).endsWith("PassTest.java"));
		assertEquals(true, javaFileNames.get(3).endsWith("Big2.java"));
		assertEquals(true, javaFileNames.get(4).endsWith("Bar.java"));
		assertEquals(true, javaFileNames.get(5).endsWith("Foo.java"));
		assertEquals(true, javaFileNames.get(6).endsWith("touche.jar"));
		assertEquals(true, javaFileNames.get(7).endsWith("SomeTrash.java"));
		assertEquals(true, javaFileNames.get(8).endsWith("Zebra.java"));

	}

}

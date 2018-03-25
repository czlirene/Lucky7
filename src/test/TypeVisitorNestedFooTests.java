package test;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

import main.TypeVisitor;
import main.JavaJarFileReader;;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for all the files found inside a single directory (NestedFooTests)
 *
 * @author Sze Lok Irene Chan
 * @since 24 March, 2018
 *
 */
public class TypeVisitorNestedFooTests {
	
	private static ArrayList<String> source = new ArrayList<>(); 

	/**
	 * Configures ASTParser and visitor for source file
	 *
	 * @param dir
	 * @param type
	 * @param expectedOutputString
	 */
	private static void configureParser(String dir, String type, String expectedOutputString) {
		try {
			source = JavaJarFileReader.getAllFilesToString(dir);
		} catch (Exception e) {
		}

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { TestSuite.SOURCE_DIR };
		String[] classPath = { TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
		parser.setUnitName("NestedTest");
		
		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);
		
		TypeVisitor visitor = new TypeVisitor();

		for (String file : source){
			parser.setSource(file.toCharArray());
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			cu.accept(visitor);
		}

		Map<String, Integer> decCounter = visitor.getDecCount();
		Map<String, Integer> refCounter = visitor.getRefCount();
		ArrayList<String> types = visitor.getList();

		for (String t : types){
			System.out.println(t + ". Declarations found: " + decCounter.get(t) + "; references found: " + refCounter.get(t) + ".");
		}

        // assertEquals(expectedReferenceCount, ref_count);
    }

	/**
	 * Testing full count of test.NestedFooTests.Foo from all the files
	 * in the nested directory. 
	*/
	@Test
	public void testTotalCountForNestedFooTestsFoo() {
		configureParser(TestSuite.NESTED_FOO_FILES_TEST_DIR, "test.NestedFooTests.Foo", "");
	}
}
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
	private static void configureParser(String dir, String type, int expectedReferenceCount, int expectedDeclarationCount) {
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
		parser.setUnitName("NestedFooTest");
		
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

		int decl_count = 0;
		int ref_count = 0;
		try {
			decl_count = visitor.getDecCount().get(type);
			ref_count = visitor.getRefCount().get(type);
		} catch (Exception e) {
		}

		assertEquals(expectedDeclarationCount, decl_count);
		assertEquals(expectedReferenceCount, ref_count);
    }

	/**
	 * Testing test.NestedFooTests.Foo in all the files in the nested directory.
	*/
	@Test
	public void testNestedFooTests_Foo_Dec_1_Ref_4() {
		configureParser(TestSuite.NESTED_FOO_FILES_TEST_DIR, "test.NestedFooTests.Foo", 1, 4);
	}

	/**
	 * Testing test.NestedFooTests.Foo1 in all the files in the nested directory.
	*/
	@Test
	public void testNestedFooTests_Foo1_Dec_1_Ref_0() {
		configureParser(TestSuite.NESTED_FOO_FILES_TEST_DIR, "test.NestedFooTests.Foo1", 1, 0);
	}

	/**
	 * Testing test.NestedFooTests.Foo1 in all the files in the nested directory.
	*/
	@Test
	public void testLocalClass_Foo_Dec_1_Ref_0() {
		configureParser(TestSuite.NESTED_FOO_FILES_TEST_DIR, "Foo", 1, 0);
	}

	/**
	 * Testing test.NestedFooTests.Foobar.Foo in all the files in the nested directory.
	*/
	@Test
	public void testNestedFooTests_Foobar_Foo_Dec_1_Ref_4() {
		configureParser(TestSuite.NESTED_FOO_FILES_TEST_DIR, "test.NestedFooTests.Foobar.Foo", 1, 4);
	}

	/**
	 * Testing test.NestedFooTests.Foobar.Bar in all the files in the nested directory.
	*/
	@Test
	public void testNestedFooTests_Foobar_Bar_Dec_1_Ref_4() {
		configureParser(TestSuite.NESTED_FOO_FILES_TEST_DIR, "test.NestedFooTests.Foobar.Bar", 1, 4);
	}

	/**
	 * Testing test.NestedFooTests.Foobar.Boo in all the files in the nested directory.
	*/
	@Test
	public void testNestedFooTests_Foobar_Boo_Dec_1_Ref_3() {
		configureParser(TestSuite.NESTED_FOO_FILES_TEST_DIR, "test.NestedFooTests.Foobar.Boo", 1, 3);
	}

	/**
	 * Testing test.NestedFooTests.Foobar.Foo.FUBAR in all the files in the nested directory.
	*/
	@Test
	public void testNestedFooTests_Foobar_Foo_FUBAR_Dec_1_Ref_3() {
		configureParser(TestSuite.NESTED_FOO_FILES_TEST_DIR, "test.NestedFooTests.Foobar.Foo.FUBAR", 1, 3);
	}
}
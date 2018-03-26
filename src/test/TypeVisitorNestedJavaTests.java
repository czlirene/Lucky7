package test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.*;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.TypeVisitor;
import main.JavaJarFileReader;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for all the files found inside a single directory (NestedJavaTests)

 * @author Sze Lok Irene Chan
 * @since 24 March, 2018
 */
public class TypeVisitorNestedJavaTests{
    private static TypeVisitor visitor;

	@Before
	public void init(){
		try {
			List<String> source = JavaJarFileReader.getAllFilesToString(TestSuite.NESTED_PROPER_FILES_TEST_DIR);
			for (String file : source){
				configureParser(file);
			}
		} catch (Exception e){
		}
	}

	@After
	public void dump(){
		visitor.resetCounters();
	}
	/**
	 * Configures ASTParser and visitor for source file
	 *
	 * @param dir
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	private static void configureParser(String source) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		parser.setEnvironment(null, null, null, true);
		parser.setUnitName("NestedJavaTest");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		visitor = new TypeVisitor();
		cu.accept(visitor);

    }
	
	private static void validateCount(String type, int expectedDeclarationCount, int expectedReferenceCount){
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
	 * Testing to see all the files are read from the directory
	*/
	@Test
	public void testCorrectFileCountForNestedProperTests() throws Exception{
		int result = JavaJarFileReader.getAllFilesToString(TestSuite.NESTED_PROPER_FILES_TEST_DIR).size();
		assertEquals(5, result);
    }

    /**
	 * Testing to see if all the types have been detected
	*/
	@Test
	public void testCorrectTypeFoundForNestedJavaTests() throws Exception{
		int result = visitor.getList().size();
		assertEquals(27, result);
	}
	
	/**
	 * Testing test.NestedProperTests.Foo in all the files in the nested directory.
	*/
	@Test
	public void testNestedProperTests_Foo_Dec_1_Ref_1() throws IOException {
		validateCount("test.NestedProperTests.Foo", 1, 1);
	}
	
	/**
	 * Testing test.NestedProperTests.Foo1 in all the files in the nested directory.
	*/
	@Test
	public void testNestedProperTests_Foo1_Dec_1_Ref_0() throws IOException {
		validateCount("test.NestedProperTests.Foo1", 1, 0);
	}
	
	/**
	 * Testing test.NestedProperTests.Foobar.Foo in all the files in the nested directory.
	*/
	@Test
	public void testNestedProperTests_Foobar_Foo_Dec_1_Ref_2() throws IOException {
		validateCount("test.NestedProperTests.Foobar.Foo", 1, 2);
	}
	
	/**
	 * Testing test.NestedProperTests.Foobar.Foo.FUBAR in all the files in the nested directory.
	*/
	@Test
	public void testNestedProperTests_Foobar_Foo_FUBAR_Dec_1_Ref_0() throws IOException {
		validateCount("test.NestedProperTests.Foobar.Foo.FUBAR", 1, 0);
	}

	/**
	 * Testing test.NestedProperTests.Foobar.Boo in all the files in the nested directory.
	*/
	@Test
	public void testNestedProperTests_Foobar_Boo_Dec_1_Ref_3() throws IOException {
		validateCount("test.NestedProperTests.Foobar.Boo", 1, 3);
	}

	/**
	 * Testing test.NestedProperTests.Foobar.Bar in all the files in the nested directory.
	*/
	@Test
	public void testNestedProperTests_Foobar_Bar_Dec_1_Ref_5() throws IOException {
		validateCount("test.NestedProperTests.Foobar.Bar", 1, 5);
	}

	/**
	 * Testing test.NestedProperTests.Foobar.Bar.Fish in all the files in the nested directory.
	*/
	@Test
	public void testNestedProperTests_Foobar_Bar_Fish_Dec_1_Ref_3() throws IOException {
		validateCount("test.NestedProperTests.Foobar.Bar.Fish", 1, 3);
	}

	/**
	 * Testing test.NestedProperTests.Foobar.Bar.LambdaMe in all the files in the nested directory.
	*/
	@Test
	public void testNestedProperTests_Foobar_Bar_LambdaMe_Dec_1_Ref_1() throws IOException {
		validateCount("test.NestedProperTests.Foobar.Bar.LambdaMe", 1, 1);
	}

	/**
	 * Testing localme in all the files in the nested directory.
	*/
	@Test
	public void testLocalMe_Dec_1_Ref_0() throws IOException {
		validateCount("LocalMe", 1, 0);
	}

	/**
	 * Testing java.lang.String in all the files in the nested directory.
	*/
	@Test
	public void testString_Dec_0_Ref_6() throws IOException {
		validateCount("java.lang.String", 0, 6);
	}
	
	/**
	 * Testing java.lang.String[] in all the files in the nested directory.
	 */
	@Test
	public void testString_Array_Dec_0_Ref_2() throws IOException {
		validateCount("java.lang.String[]", 0, 2);
	}

	/**
	 * Testing java.lang.Integer in all the files in the nested directory.
	*/
	@Test
	public void testInteger_Dec_0_Ref_1() throws IOException {
		validateCount("java.lang.Integer", 0, 1);
	}
	
	/**
	 * Testing java.lang.Integer[] in all the files in the nested directory.
	*/
	@Test
	public void testInteger_Array_Dec_0_Ref_1() throws IOException {
		validateCount("java.lang.Integer[]", 0, 1);
	}

	/**
	 * Testing java.lang.Thread in all the files in the nested directory.
	*/
	@Test
	public void testThread_Dec_0_Ref_7() throws IOException {
		validateCount("java.lang.Thread", 0, 7);
	}

	/**
	 * Testing java.lang.System in all the files in the nested directory.
	*/
	@Test
	public void testSystem_Dec_0_Ref_1() throws IOException {
		validateCount("java.lang.System", 0, 1);
	}

	/**
	 * Testing java.lang.Deprecated in all the files in the nested directory.
	*/
	@Test
	public void testDeprecated_Dec_0_Ref_1() throws IOException {
		validateCount("java.lang.Deprecated", 0, 1);
	}

	/**
	 * Testing java.lang.Override in all the files in the nested directory.
	*/
	@Test
	public void testOverride_Dec_0_Ref_1() throws IOException {
		validateCount("java.lang.Override", 0, 1);
	}

	/**
	 * Testing java.lang.Object in all the files in the nested directory.
	*/
	@Test
	public void testObject_Dec_0_Ref_2() throws IOException {
		validateCount("java.lang.Object", 0, 2);
	}

	/**
	 * Testing java.util.List in all the files in the nested directory.
	*/
	@Test
	public void testUtil_List_Dec_0_Ref_3() throws IOException {
		validateCount("java.util.List", 0, 3);
	}

	/**
	 * Testing java.util.ArrayList in all the files in the nested directory.
	*/
	@Test
	public void testUtil_ArrayList_Dec_0_Ref_6() throws IOException {
		validateCount("java.util.ArrayList", 0, 6);
	}

	/**
	 * Testing java.util.LinkedList in all the files in the nested directory.
	*/
	@Test
	public void testUtil_LinkedList_Dec_0_Ref_2() throws IOException {
		validateCount("java.util.LinkedList", 0, 2);
	}

	/**
	 * Testing java.util.HashMap in all the files in the nested directory.
	*/
	@Test
	public void testUtil_HashMap_Dec_0_Ref_2() throws IOException {
		validateCount("java.util.HashMap", 0, 2);
	}

	/**
	 * Testing primitive type int in all the files in the nested directory.
	*/
	@Test
	public void testPrim_int_Dec_0_Ref_1() throws IOException {
		validateCount("int", 0, 1);
	}

	/**
	 * Testing primitive type double in all the files in the nested directory.
	*/
	@Test
	public void testPrim_double_Dec_0_Ref_2() throws IOException {
		validateCount("double", 0, 2);
	}

	/**
	 * Testing primitive type boolean in all the files in the nested directory.
	*/
	@Test
	public void testPrim_boolean_Dec_0_Ref_1() throws IOException {
		validateCount("boolean", 0, 1);
	}

	/**
	 * Testing primitive type float in all the files in the nested directory.
	*/
	@Test
	public void testPrim_float_Dec_0_Ref_1() throws IOException {
		validateCount("float", 0, 5);
	}


}
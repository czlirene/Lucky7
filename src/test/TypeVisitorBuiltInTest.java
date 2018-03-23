package test;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import main.TypeVisitor;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for Java built-in classes
 *
 * @author Sze Lok Irene Chan
 * @since 22 March, 2018
 *
 */
public class TypeVisitorBuiltInTest {

	/**
	 * Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param type
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	private static void configureParser(String source, String type, int expectedDeclarationCount,
			int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { TestSuite.SOURCE_DIR };
		String[] classPath = { TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// TODO: Fix up the name to be something other than name?
		parser.setUnitName("Name");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		TypeVisitor visitor = new TypeVisitor();
		cu.accept(visitor);

		int decl_count = 0;
		int ref_count = 0;
		try {
			decl_count = visitor.getDecCount().get(type);
		} catch (Exception e) {

		}
		try {
			ref_count = visitor.getRefCount().get(type);
		} catch (Exception e) {

		}

		assertEquals(expectedDeclarationCount, decl_count);
		assertEquals(expectedReferenceCount, ref_count);

	}

	/**
	 * Testing a String[] Array FIELD, looking for reference to java.lang.String
	 */
	@Test
	public void testFieldStringArrayWithoutAllocation_String_Dec_0_Ref_1() {
		configureParser("public class KFC {String[] newArray;} ", "java.lang.String", 0, 1);
	}

	/**
	 * Testing a String[] with Array FIELD, looking for reference to java.lang.String[]
	 */
	@Test
	public void testFieldStringArrayWithoutAllocation_StringA_Dec_0_Ref_1() {
		configureParser("public class KFC {String[] newArray;} ", "java.lang.String[]", 0, 1);
	}

	/**
	 * Testing a String[] with Array variable, looking for reference to java.lang.String[]
	 */
	@Test
	public void testVarStringArrayWithoutAllocation_String_Dec_0_Ref_1() {
		configureParser("public class KFC { public void Foo(){ String[] str; }} ", "java.lang.String", 0, 1);
	}

	/**
	 * Testing a String[] with Array variable, looking for reference to java.lang.String[]
	 */
	@Test
	public void testVarStringArrayWithoutAllocation_StringA_Dec_0_Ref_1() {
		configureParser("public class KFC { public void Foo(){ String[] str; }} ", "java.lang.String[]", 0, 1);
	}

	/**
	 * Testing a String[] with array variable, looking for reference to java.lang.String[]
	 * Allocating an empty set. 
	 */
	@Test
	public void testVarStringArrayWithAllocation_String_Dec_0_Ref_1() {
		configureParser("public class KFC { public void Foo(){ String[] str = {}; }} ", "java.lang.String", 0, 1);
	}

	/**
	 * Testing a String[] with array variable, looking for reference to java.lang.String[]
	 * Allocating an empty set. 
	 */
	@Test
	public void testVarStringArrayWithAllocation_StringA_Dec_0_Ref_1() {
		configureParser("public class KFC { public void Foo(){ String[] str = {}; }} ", "java.lang.String[]", 0, 1);
	}

	/**
	 * Testing a String[] with Array variable, looking for reference to java.lang.String.
	 * Create new instance
	 */
	@Test
	public void testVarStringArrayWithAllocation_String_Dec_0_Ref_2() {
		configureParser("public class KFC { public void Foo(){ String[] str = new String[5]; }} ", "java.lang.String", 0, 2);
	}

	/**
	 * Testing a String[] with Array variable, looking for reference to java.lang.String[]
	 * Create new instance
	 */
	@Test
	public void testVarStringArrayWithAllocation_StringA_Dec_0_Ref_2() {
		configureParser("public class KFC { public void Foo(){ String[] str = new String[5]; }} ", "java.lang.String[]", 0, 2);
	}

	/**
	 * Testing a String[] with Array variable, and methods, looking for reference to java.lang.String.
	 * Create new instance for the variable.
	 */
	@Test
	public void testVarMethStringArrayWithAlloc_String_Dec_0_Ref_3() {
		configureParser("public class KFC { public String[] Foo(){ String[] str = new String[5]; return str; }} ", "java.lang.String", 0, 3);
	}

	/**
	 * Testing a String[] with Array variable, and methods, looking for reference to java.lang.String[]
	 * Create new instance for the variable.
	 */
	@Test
	public void testVarMethStringArrayWithAlloc_StringA_Dec_0_Ref_3() {
		configureParser("public class KFC { public String[] Foo(){ String[] str = new String[5]; return str; }} ", "java.lang.String[]", 0, 3);
	}

	/**
	 * Testing a String[] with method return new, looking for reference to java.lang.String[]
	 */
	@Test
	public void testVarMethStringArrayWithReturn_StringA_Dec_0_Ref_3() {
		configureParser("public class KFC { public String[] Foo(){ String[] str; return new String[]; }} ", "java.lang.String[]", 0, 3);
	}

	/**
	 * Testing a String[] with method return new, looking for reference to java.lang.String
	 */
	@Test
	public void testVarMethStringArrayWithReturn_String_Dec_0_Ref_3() {
		configureParser("public class KFC { public String[] Foo(){ String[] str; return new String[]; }} ", "java.lang.String", 0, 3);
	}
}
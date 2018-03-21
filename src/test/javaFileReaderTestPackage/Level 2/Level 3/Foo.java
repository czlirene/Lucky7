package test;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import main.TypeVisitor;

/**
 * JUnit 4 Tests for {@link TypeVisitor} class. Checks type declaration and
 * reference counts for Foo
 *
 * @author Evan Quan
 * @since 14 March 2018
 *
 */
public class TypeVisitorFooTest {

	private static final String type = "Foo";

	/**
	 * Configures ASTParser and visitor for source file
	 *
	 * @param source
	 * @param expectedDeclarationCount
	 * @param expectedReferenceCount
	 */
	private static void configureParser(String source, int expectedDeclarationCount, int expectedReferenceCount) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		// these are needed for binding to be resolved due to SOURCE is a char[]
		String[] srcPath = { TestSuite.SOURCE_DIR };
		String[] classPath = { TestSuite.BIN_DIR };
		parser.setEnvironment(classPath, srcPath, null, true);
		// parser.setEnvironment(null, null, null, true);
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
	 * Check that declaring an variable of another class that references Foo as a
	 * generic parameter counts as a reference
	 */
	@Test
	public void test1ParamterizedType_Dec_0_Ref_1() {
		configureParser("public class Other{ Bar<Foo> bar;}", 0, 1);
	}

	/**
	 * Check that declaring an variable of another class that references Foo as a
	 * generic parameter twice counts as 2 references
	 */
	@Test
	public void test2ParameterizedTypes_Dec_0_Ref_2() {
		configureParser("public class Other{ Bar<Foo, Foo> bar;}", 0, 2);
	}

	/**
	 * Check that declaring an variable of another class that references Foo as a
	 * generic parameter twice counts as 2 references with another third generic
	 * parameter of another class
	 */
	@Test
	public void test3ParameterizedTypesMixed_Dec_0_Ref_2() {
		configureParser("public class Other{ Bar<Foo, String, Foo> bar;}", 0, 2);
	}

	/**
	 * Check if an annotation declaration is counted as a declaration
	 */
	@Test
	public void testAnnotationDeclaration_Dec_1_Ref_0() {
		configureParser("@interface Foo {}", 1, 0);
	}

	/**
	 * Check that instantiating an array of Foo counts a 1 reference
	 */
	@Test
	public void testArrayDeclarableVariableAndAllocate_Dec_0_Ref_1() {
		configureParser("public class Other {Bar[] bar = new Foo[1];}", 0, 1);
	}

	/**
	 * Check that declaring and instantiating an array of Foo counts as 2 references
	 */
	@Test
	public void testArrayDeclarableVariableAndAllocate_Dec_0_Ref_2() {
		configureParser("public class Other {Foo[] foo = new Foo[1];}", 0, 2);
	}

	/**
	 * Check that creating a variable of an array of Foo counts as a reference
	 */
	@Test
	public void testArrayDeclareVariable_Dec_0_Ref_1() {
		configureParser("public class Other {Foo[] foo;}", 0, 1);
	}

	/**
	 * Check if declaring a meta class of Foo counts as a reference
	 */
	@Test
	public void testClassClass_Dec_0_Ref_1() {
		configureParser("public class Other{Class<Foo> foo;}", 0, 1);
	}

	/**
	 * Check if declaring a meta class of Foo counts as a reference TODO return here
	 */
	@Test
	public void testClassClass_Dec_0_Ref_2() {
		configureParser("public class Other{Class<Foo> foo = Foo.class;}", 0, 2);
	}

	/**
	 * Check if a class declaration is counted as a declaration
	 */
	@Test
	public void testClassDeclaration_Dec_1_Ref_0() {
		configureParser("class Foo {}", 1, 0);
	}

	/**
	 * Check that declaring an variable inside a method counts as a reference
	 */
	@Test
	public void testDeclareAndInstantiateInsideMethod_Dec_0_Ref_2() {
		configureParser("public class Other{ public void bar() {Foo f = new Foo();}", 0, 2);
	}

	/**
	 * Check if a variable declaration and instantiating it with a constructor is
	 * counted as 2 references
	 */
	@Test
	public void testDeclareAndInstantiateVariable_Dec_0_Ref_2() {
		configureParser("public class Other { Foo foo = new Foo();}", 0, 2);
	}

	/**
	 * Check that declaring an variable inside a method counts as a reference
	 */
	@Test
	public void testDeclareInsideMethod_Dec_0_Ref_1() {
		configureParser("public class Other{ public void bar() {Foo f;}", 0, 1);
	}

	/**
	 * Check if a variable declaration is counted as a reference
	 */
	@Test
	public void testDeclareVariable_Dec_0_Ref_1() {
		configureParser("public class Other{ Foo foo;}", 0, 1);
	}

	/**
	 * Check if an enum declaration is counted as a declaration
	 */
	@Test
	public void testEnumDeclaration_Dec_1_Ref_0() {
		configureParser("enum Foo {}", 1, 0);
	}

	@Test
	public void testForLoopInitialization_Dec_0_Ref_1() {
		configureParser("public class Other { public void method() { for (Foo f;;){}}}", 0, 1);
	}

	/**
	 * Check that illegal Java syntax (cannot be compiled) results in no references
	 * or declarations
	 */
	@Test
	public void testIllegalSyntax_Dec_0_Ref_0() {
		configureParser("This is invalid Java syntax; Foo foo; Foo foo2 = new Foo();", 0, 0);
	}

	/**
	 * Check that a reference of Foo instantiated inside a parameter of another
	 * constructor is counted
	 */
	@Test
	public void testInstantiateInsideOtherConstructor_Dec_0_Ref_1() {
		configureParser("public class Other{ Other2 other = new Other2(new Foo());}", 0, 1);
	}

	/**
	 * Check that an instantiation of another class inside a Foo constructor counts
	 * the reference
	 */
	@Test
	public void testInstantiateOtherInsideConstructor_Dec_0_Ref_2() {
		configureParser("public class Other{ Foo foo = new Foo(new Other2());}", 0, 2);
	}

	/**
	 * Check that instantiating an instance of a Parent of Foo with the constructor
	 * of Foo counts 1 reference
	 */
	@Test
	public void testInstantiateVariableOfParent_Dec_1_Ref_1() {
		configureParser("public class Other{ FooParent foo = new Foo();}", 0, 1);
	}

	/**
	 * Check that instantiating an instance of a Child of Foo in a variable of type
	 * Foo counts as 1 reference
	 */
	@Test
	public void testInstantiatingVariableOfChild_Dec_0_Ref_1() {
		configureParser("public class Other{ Foo foo = new FooChild();}", 0, 1);
	}

	/**
	 * Check if an interface declaration is counted as a declaration
	 */
	@Test
	public void testInterfaceDeclaration_Dec_1_Ref_0() {
		configureParser("interface Foo {}", 1, 0);
	}

	/**
	 * Check if an annotation reference is counted as a reference
	 */
	@Test
	public void testMarkerAnnotationReference_Dec_0_Ref_1() {
		configureParser("public class Other{@Foo public void method() {}}", 0, 1);
	}

	/**
	 * Check if a return type reference in a method declaration is counted as a
	 * reference
	 */
	@Test
	public void testMethodReturn_Dec_0_Ref_1() {
		configureParser("public class Other{ public Foo methodName() {}}", 0, 1);
	}

	/**
	 * Check if a marker annotation that references Foo as a parameter is counted as
	 * a reference
	 */
	@Test
	public void testNormalAnnotationParameterReference_Dec_0_Ref_1() {
		configureParser("public class Other{@Test(expected = Foo.class) public void test() {}}", 0, 1);
	}

	/**
	 * Check if a class declaration of another type does not affect declaration
	 * count
	 */
	@Test
	public void testOtherClassDeclaration_Dec_0_Ref_0() {
		configureParser("class Other {}", 0, 0);
	}

	/**
	 * Check that returning an instance directly from constructor counts as a
	 * reference
	 */
	@Test
	public void testReturnConstructor_Dec_0_Ref_2() {
		configureParser("public class Other{public Foo bar() {return new Foo();}", 0, 2);
	}

	/**
	 * Check if a a variable declaration and setting as another variable's value is
	 * counted as a reference
	 */
	@Test
	public void testSetVariable_Dec_0_Ref_1() {
		configureParser("public class Other { Foo foo = anotherFoo;}", 0, 1);
	}

}

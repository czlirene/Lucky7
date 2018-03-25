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
	public void testCorrectFileCountForNestedFooTests() throws Exception{
		int result = JavaJarFileReader.getAllFilesToString(TestSuite.NESTED_PROPER_FILES_TEST_DIR).size();
		assertEquals(5, result);
    }
    
	/**
	 * Testing java.lang.String in all the files in the nested directory.
	*/
	@Test
	public void testString_Dec_0_Ref_0() throws IOException {
		validateCount("java.lang.String", 0, 0);
	}

}
package test;

import static org.junit.Assert.*;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import main.*;

import org.junit.Test;
import org.junit.Before;

public class testCounting {
	
	private ASTParser parser;
	
	// Maybe rename this ???
	private static void  configureParser(String source, 
			Map<String, Integer> decMapExpected, Map<String, Integer> refMapExpected) {

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setEnvironment(null, null, null, true);
		parser.setStatementsRecovery(true);
		parser.setUnitName("Name");
		
		parser.setSource(source.toCharArray());
		
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);
		
		CompilationUnit cu = (CompilationUnit)parser.createAST(null);
		TypeVisitor v = new TypeVisitor();
		cu.accept(v);
		
		Map<String, Integer> refmap = v.getRefCount();
		Map<String, Integer> decmap = v.getDecCount();
		
		// 
		refmap.remove("void");
		decmap.remove("void");
		
		System.out.println("declaration count(): " + decmap);
		System.out.println("reference count(): " + refmap);
		
		// Iterates through all entries of the expected decMap
		// and compares each entry to actual decMap
		for (Map.Entry<String, Integer> entry : decMapExpected.entrySet()) {
			
			String expectedDecType = entry.getKey();
			int expectedDecCount = entry.getValue();
			//System.out.println("decMapExpected: "+ expectedDecType + ", " + expectedDecCount);
			
			if (decmap.get(expectedDecType) != null) {
				int actualDecCount = decmap.get(expectedDecType);
				//System.out.println("decmap: " + actualDecCount);
				
				assertEquals(expectedDecCount, actualDecCount);
			}
			else
				fail( "\""+expectedDecType +"\" type is not counted");
		}
		
		for (Map.Entry<String, Integer> entry : refmap.entrySet()) {
			
			String expectedRefType = entry.getKey();
			int expectedRefCount = entry.getValue();
			//System.out.println("ref: "+ entry.getKey() + ", " + entry.getValue());
			
			if(refmap.get(expectedRefType) != null) {
				int actualRefCount = refmap.get(expectedRefType);
				//System.out.println("refExpected: "+refCountExpected);
				assertEquals(expectedRefCount, actualRefCount);
			}
			else 
				fail( "\""+expectedRefType +"\" type is not counted");
			
		}
		//assertEquals(decMapExpected, decmap);
		//assertEquals(refMapExpected, refmap);
		return;
	}

	// We won't need this
	@Before
	public void testBefore() {
		parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setEnvironment(null, null, null, true);
		parser.setStatementsRecovery(true);
		parser.setUnitName("Cow");
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);
	}
	
	@Test
	public void testa() {
		String source = "import java.util.HashMap; class Foo { HashMap map;}"; 
		
		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("Foo", 1);
		decExpected.put("java.util.HashMap", 0);
		decExpected.put("HashMap", 0);
		
		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("Foo", 0);
		refExpected.put("java.util.HashMap", 2);
		refExpected.put("HashMap", 1);
		
		configureParser(source, decExpected, refExpected);

	}
	// TODO
	@Test
	public void test() {
		String source1 = "package test.testPackage; import java.util.HashMap; class Foo { HashMap map;}";
		char[] source2 = source1.toCharArray();
		parser.setSource(source2);
		
		CompilationUnit cu = (CompilationUnit)parser.createAST(null);
		
		TypeVisitor v = new TypeVisitor();
		cu.accept(v);
		
		Map<String, Integer> refmap = v.getRefCount();
		Map<String, Integer> decmap = v.getDecCount();
		
		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("Foo", 0);
		refExpected.put("java.util.HashMap", 2);
		refExpected.put("HashMap", 1);
		
		
		//System.out.println("refmap = refExpected: " + (refmap.equals(refExpected)));
		
		System.out.println("declaration count (0): " + decmap);
		System.out.println("reference count (0): " + refmap);
		
		//assertEquals(refExpected, refmap);

	}
	
	@Test
	public void test1() {
		
		String source = "public class DoesItWork{\n"
				+ "private class MaybeWorks{} \n"
				+ "public void add(){ 1 + 1; }"
				+ "public void add2(){ add() }"
				+ "String a;"
				+ "a = \"Hello\";"
				+ "int b;"
				+ "char c;"
				+ "int d;"
				+ "Time e;"
				+ "enum Quark{ UP, DOWN}"
				+ "}\n"
				+ "interface PleaseWork{}\n";
		
		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("DoesItWork", 1);
		decExpected.put("DoesItWork.MaybeWorks", 1);
		decExpected.put("java.lang.String", 0);
		decExpected.put("int", 0);
		decExpected.put("char", 0);
		decExpected.put("Time", 0);
		decExpected.put("DoesItWork.Quark", 1);
		decExpected.put("PleaseWork", 1);
		
		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("DoesItWork", 0);
		refExpected.put("DoesItWork.MaybeWorks", 0);
		refExpected.put("java.lang.String", 1);
		refExpected.put("int", 2);
		refExpected.put("char", 1);
		refExpected.put("Time", 1);
		refExpected.put("DoesItWork.Quark", 0);
		refExpected.put("PleaseWork", 0);
		
		configureParser(source, decExpected, refExpected);
		
	}
	
	@Test
	public void test2() {
		
		String source = "package test; import java.lang.String; public class Cow {public static String yell() {return \"Moo\";} public int gotMilk() {return 0;} public static void main (String[] args) {Cow betty = new Cow(); String sound = Cow.yell(); int milk = betty.gotMilk(); String[] a = new String[1];}}";

		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("test", 0);
		decExpected.put("String", 0);
		decExpected.put("java.lang.String", 0);
		decExpected.put("test.Cow", 1);
		decExpected.put("java.lang.String[]", 0);
		decExpected.put("int", 0);

		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("test", 1);
		refExpected.put("String", 0);
		refExpected.put("java.lang.String", 6);
		refExpected.put("test.Cow", 1);
		refExpected.put("java.lang.String[]", 3);
		refExpected.put("int", 2);

		configureParser(source, decExpected, refExpected);
		
	}
	
	// TODO
	@Test
	public void test3() {
		String source1 = "class Pair<X,Y> { public void printPair( Pair<String,Long> pair){} Pair<String,Long> limit = new Pair<String,Long> (\"maximum\",1024L); String s;} ";
		char[] source2 = source1.toCharArray();
		parser.setSource(source2);
		
		CompilationUnit cu = (CompilationUnit)parser.createAST(null);
		
		TypeVisitor v = new TypeVisitor();
		cu.accept(v);
		
		Map<String, Integer> refmap = v.getRefCount();
		Map<String, Integer> decmap = v.getDecCount();
		
		System.out.println("declaration count (3): " + decmap);
		System.out.println("reference count (3): " + refmap);
	}
}

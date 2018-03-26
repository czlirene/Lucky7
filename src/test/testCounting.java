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
import org.junit.After;

public class testCounting {
	
	private static TypeVisitor v;
	/**
	 * Counts actual declarations and references and compares them
	 * to their expected values.
	 * 
	 * @param source
	 * @param decMapExpected
	 * @param refMapExpected
	 * @param testNumber
	 */
	private static void  configureParser(String source, 
			Map<String, Integer> decMapExpected, Map<String, Integer> refMapExpected, int testNumber) {

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
		v = new TypeVisitor();
		cu.accept(v);
		
		Map<String, Integer> refmap = v.getRefCount();
		Map<String, Integer> decmap = v.getDecCount();
		
		System.out.println("declaration count(" + testNumber + "): " + decmap);
		System.out.println("reference count(" + testNumber + "): " + refmap);
		System.out.println();
		
		// Iterates through all entries of the expected decMap
		// and compares each entry to actual decMap
		for (Map.Entry<String, Integer> entry : decMapExpected.entrySet()) {
			
			String expectedDecType = entry.getKey();
			int expectedDecCount = entry.getValue();
			
			if (decmap.get(expectedDecType) != null) {
				int actualDecCount = decmap.get(expectedDecType);
				assertEquals(expectedDecCount, actualDecCount);
			}
			else
				fail( "\""+expectedDecType +"\" type declaration is not counted");
		}
		
		for (Map.Entry<String, Integer> entry : refMapExpected.entrySet()) {
			
			String expectedRefType = entry.getKey();
			int expectedRefCount = entry.getValue();
			
			if(refmap.get(expectedRefType) != null) {
				int actualRefCount = refmap.get(expectedRefType);
				assertEquals(expectedRefCount, actualRefCount);
			}
			else 
				fail( "\""+expectedRefType +"\" type reference is not counted");	
		}
		
		//refmap.remove("void");
		//decmap.remove("void");
		
		// This checks if there are any types that were counted that are not expected
		for (Map.Entry<String, Integer> entry : refmap.entrySet()) {
			String type = entry.getKey();
			int count = entry.getValue();
			
			if (!refMapExpected.containsKey(type)) {
				System.out.println(type);;
				fail("unexpected type: " + type);
			}
		}
		
		return;
	}
	
	@After
	public void after() {
		v.resetCounters();
	}
	
	@Test
	public void test1() {
		String source = "import java.util.HashMap; class Foo { HashMap map;}"; 
		
		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("Foo", 1);
		decExpected.put("java.util.HashMap", 0);
		
		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("Foo", 0);
		refExpected.put("java.util.HashMap", 2);
		
		configureParser(source, decExpected, refExpected, 1);

	}
	// TODO
	@Test
	public void test2() {
		String source = "package test.testPackage; import java.util.HashMap; class Foo { HashMap map;}";
		
		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("test.testPackage.Foo", 1);
		decExpected.put("java.util.HashMap", 0);
		
		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("test.testPackage.Foo", 0);
		refExpected.put("java.util.HashMap", 2);
		
		configureParser(source, decExpected, refExpected, 2);

	}
	
	@Test
	public void test3() {
		
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
		
		configureParser(source, decExpected, refExpected, 3);
		
	}
	
	@Test
	public void test4() {
		
		String source = "package test; "
				+ "import java.lang.String; "
				+ "public class Cow {"
				+ "public static String yell() {"
				+ "return \"Moo\";}"
				+ " public int gotMilk() {"
				+ "return 0;}"
				+ " public static void main (String[] args) {"
				+ "Cow betty = new Cow();"
				+ " String sound = Cow.yell();"
				+ " int milk = betty.gotMilk();"
				+ " String[] a = new String[1];}}";

		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("java.lang.String", 0);
		decExpected.put("test.Cow", 1);
		decExpected.put("java.lang.String[]", 0);
		decExpected.put("int", 0);

		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("java.lang.String", 6);
		refExpected.put("test.Cow", 3);
		refExpected.put("java.lang.String[]", 3);
		refExpected.put("int", 2);

		configureParser(source, decExpected, refExpected, 4);
		
	}
	
	// TODO
	@Test
	public void test5() {
		String source = "class Pair<X,Y> { public void printPair( Pair<String,Long> pair){} Pair<String,Long> limit = new Pair<String,Long> (\"maximum\",1024L); String s;} ";

		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("Pair", 1);
		decExpected.put("java.lang.String", 0);
		decExpected.put("java.lang.Long", 0);
		
		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("Pair", 3);
		refExpected.put("java.lang.String", 4);
		refExpected.put("java.lang.Long", 3);
		
		configureParser(source, decExpected, refExpected, 5);
	}
	
	@Test
	public void test6() {
		String source = "package bar; class Other {  public Bar method() {return new Foo();   } }";
		
		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("bar.Other", 1);
		decExpected.put("Bar", 0);
		decExpected.put("Foo", 0);
		
		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("bar.Other", 0);
		refExpected.put("Bar", 1);
		refExpected.put("Foo", 1);
		
		configureParser(source, decExpected, refExpected, 6);
	}
	
	@Test
	public void test7() {
        String source = "public class X {public X () {}}";
		
		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("X", 1);
		
		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("X", 1);
		
		configureParser(source, decExpected, refExpected, 7);
	}
	
	@Test
	public void test8() {
		String source = "package bar; class Other { public Bar method() {Bar foo = new Foo(); return foo; }}";
		
		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("bar.Other", 1);
		decExpected.put("Bar", 0);
		decExpected.put("Foo", 0);
		
		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("bar.Other", 0);
		refExpected.put("Bar", 2);
		refExpected.put("Foo", 1);
		
		configureParser(source, decExpected, refExpected, 8);
	}
	
	@Test
	public void test9() {
		String source = "package bar; class Other {Bar bar = new Foo();}";
		
		Map<String, Integer> decExpected = new HashMap<String, Integer>();
		decExpected.put("bar.Other", 1);
		decExpected.put("Bar", 0);
		decExpected.put("Foo", 0);
		
		Map<String, Integer> refExpected = new HashMap<String, Integer>();
		refExpected.put("bar.Other", 0);
		refExpected.put("Bar", 1);
		refExpected.put("Foo", 1);
		
		configureParser(source, decExpected, refExpected, 9);
	}
}

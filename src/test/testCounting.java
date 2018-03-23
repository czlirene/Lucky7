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
	public void test() {
		String source1 = "import java.util.HashMap; class Foo { HashMap map;}";
		char[] source2 = source1.toCharArray();
		parser.setSource(source2);
		
		CompilationUnit cu = (CompilationUnit)parser.createAST(null);
		
		TypeVisitor v = new TypeVisitor();
		cu.accept(v);
		
		Map<String, Integer> refmap = v.getRefCount();
		Map<String, Integer> decmap = v.getDecCount();
		
		System.out.println("declaration count (0): " + decmap);
		System.out.println("reference count (0): " + refmap);
	}
	
	@Test
	public void test1() {
		String source1 = "public class DoesItWork{\n"
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
		char[] source2 = source1.toCharArray();
		parser.setSource(source2);
		
		CompilationUnit cu = (CompilationUnit)parser.createAST(null);
		
		TypeVisitor v = new TypeVisitor();
		cu.accept(v);
		
		Map<String, Integer> refmap = v.getRefCount();
		Map<String, Integer> decmap = v.getDecCount();
		
		System.out.println("declaration count (1): " + decmap);
		System.out.println("reference count (1): " + refmap);
	}
	
	@Test
	public void test2() {
		String source1 = "package test; import java.lang.String; public class Cow {public static String yell() {return \"Moo\";} public int gotMilk() {return 0;} public static void main (String[] args) {Cow betty = new Cow(); String sound = Cow.yell(); int milk = betty.gotMilk(); String[] a = new String[1];}}";
		char[] source2 = source1.toCharArray();
		parser.setSource(source2);
		
		CompilationUnit cu = (CompilationUnit)parser.createAST(null);
		
		TypeVisitor v = new TypeVisitor();
		cu.accept(v);
		
		Map<String, Integer> refmap = v.getRefCount();
		Map<String, Integer> decmap = v.getDecCount();
		
		System.out.println("declaration count (2): " + decmap);
		System.out.println("reference count (2): " + refmap);
	}
}

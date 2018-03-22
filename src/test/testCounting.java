package test;

import static org.junit.Assert.*;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import main.*;

import org.junit.Test;

public class testCounting {

	@Test
	public void test() {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		String source1 = "package test; public class Cow {public static String yell() {return \"Moo\";} public int gotMilk() {return 0;} public static void main (String[] args) {Cow betty = new Cow(); String sound = Cow.yell(); int milk = betty.gotMilk();}}";
		char[] source2 = source1.toCharArray();
		parser.setSource(source2);
		parser.setResolveBindings(true);
		parser.setEnvironment(null, null, null, true);
		parser.setStatementsRecovery(true);
		parser.setUnitName("Cow");
		
		//String[] checking = new String[5];
		
		CompilationUnit cu = (CompilationUnit)parser.createAST(null);
		
		TypeVisitor v = new TypeVisitor();
		cu.accept(v);
		
		Map<String, Integer> map = v.getRefCount();
		
		System.out.println(map);
	}

}

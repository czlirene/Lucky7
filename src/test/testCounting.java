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

public class testCounting {

	@Test
	public void test() {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		String source1 = "import java.util.HashMap; class Foo { HashMap map;}";
		char[] source2 = source1.toCharArray();
		parser.setSource(source2);
		parser.setResolveBindings(true);
		parser.setEnvironment(null, null, null, true);
		parser.setStatementsRecovery(true);
		parser.setUnitName("Cow");
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
		
		System.out.println("declaration count (0): " + decmap);
		System.out.println("reference count (0): " + refmap);
	}
	
}

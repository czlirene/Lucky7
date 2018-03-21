package main;

import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;


/**
 * Takes a pathname to indicate a directory of interest and a String to indicate
 * a fully qualified name of a Java type. Counts the number of declarations of a
 * Java type and references of each occurrence of that type within that
 * directory.
 *
 * @author Sze Lok Irene Chan
 * @author Evan Quan
 * @since 13 March 2018
 *
 */
public class TypeFinder {

	/* GLOBAL VARIABLES */
	/**
	 * Command line argument index for the directory path of interest
	 */
	public static final int DIRECTORY_PATH = 0;

	/**
	 * Command line argument index for Java type of interest
	 */
	public static final int JAVA_TYPE = 1;
	/**
	 * The number of command line arguments the user needs to input in order for the
	 * program to properly work.
	 */
	public static final int VALID_ARGUMENT_COUNT = 2;
	/**
	 * Error message when the user inputs a directory that TypeFinder cannot
	 * recognize. This may be because the directory does not exist, or is not
	 * accessible.
	 */
	public static final String INVALID_DIRECTORY_ERROR_MESSAGE = "Error: Invalid directory.";
	/**
	 * An IOException should never run (as opposed to a NotDirectoryException)
	 * because files that cannot be accessed or do not exist are not considered when
	 * looking for Java files in a directory anyways.
	 */
	public static final String YOU_DUN_GOOFED_UP_MESSAGE = "Error: This should never run.";
	/**
	 * Prompts the user on how to use the program properly.
	 */
	public static final String USAGE_MESSAGE = "Usage: java TypeFinder <directory> <Java type>";
	/**
	 * TODO This is currently unused.
	 */
	public static final String PROG_DESCRIPTION_MSG = "Determine the numerical count of declarations and references of a specified Java type for all Java files found in the given directory.";

	/**
	 * Error message when the user inputs an incorrect number of command line
	 * arguments when running the program.
	 */
	public static final String INVALID_ARGUMENT_ERROR_MESSAGE = "Error: Invalid number of arguments.\n" + USAGE_MESSAGE;

	private static String directory;

	private static String java_type;
	private static int decl_count = 0;
	private static int ref_count = 0;
	private static List<String> java_files_as_string = new ArrayList<String>(); // initialize it

	/**
	 *
	 * @return ASTParser configured to parse CompilationUnits for JLS8
	 */
	private static ASTParser getConfiguredASTParser() {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);

		// Given source is char[], these are required to resolve binding
		parser.setEnvironment(null, null, null, true);
		parser.setUnitName("SENG300GrpIt1");

		// ensures nodes are being parsed properly
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		return parser;
	}

	/**
	 * Retrieves Java source from directory to initialize TypeFinder
	 *
	 * @param args
	 *            command line arguments
	 * @return true if java source successfully retrieved, else false
	 */
	private static boolean initFinder(String[] args) {

		// Check if user has inputed a valid number arguments.
		if (args.length != VALID_ARGUMENT_COUNT) {
			System.err.println(INVALID_ARGUMENT_ERROR_MESSAGE);
			return false;
		}

		directory = args[DIRECTORY_PATH];
		java_type = args[JAVA_TYPE];

		try {
			// retrieve all java files (read to string) in directory, and store in ArrayList
			java_files_as_string = JavaJarFileReader.getAllFilesToString(directory);
		} catch (NotDirectoryException nde) {
			System.err.println(INVALID_DIRECTORY_ERROR_MESSAGE);
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Initiates the program
	 *
	 * @param args
	 *            command line arguments args[0] path of directory of interest
	 *            args[1] fully qualified name of Java type to search for
	 *            declarations and references
	 */
	public static void main(String[] args) {
		/* Initialization process */
		boolean initSuccessful = initFinder(args);

		if (!initSuccessful) {
			return;
		}

		/* Create AST */

		for (String file : java_files_as_string) {
			ASTParser parser = getConfiguredASTParser();
			parser.setSource(file.toCharArray());
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

			TypeVisitor visitor = new TypeVisitor();
			cu.accept(visitor);

			List<String> types = visitor.getList();
			Map<String, Integer> decCounter = visitor.getDecCount();
			Map<String, Integer> refCounter = visitor.getRefCount();

			// increment the total counter
			if (types.contains(java_type)) {
				decl_count += decCounter.get(java_type);
				ref_count += refCounter.get(java_type);
			}
		}
		
		System.out.println(java_type + ". Declarations found: " + decl_count + "; references found: " + ref_count + ".");
	}
}

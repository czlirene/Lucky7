package main;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: replace these fuckers with *
import org.eclipse.jdt.core.dom.*;

/**
 * TypeVisitor.java
 *
 * A visitor for abstract syntax trees. For each different concrete AST node
 * type T, the visitor will locate the different java types present in the
 * source code, and count the number of declarations of references for each of
 * the java types present.
 *
 * @author Sze Lok Irene Chan
 * @version 1.0.0
 *
 * @since 20 March 2018
 */
public class TypeVisitor extends ASTVisitor {

	// Global variables
	private static ArrayList<String> types;

	private static HashMap<String, Integer> decCounter;

	private static HashMap<String, Integer> refCounter;

	/**
	 * Checks if the passed type already exists within the types list. [false -> add
	 * type to list create entry <type, 0> in decCounter create entry <type, 0> in
	 * refCounter] [true -> do nothing]
	 *
	 * @param type:
	 *            String, java type
	 */
	private static void addTypeToList(String type) {
		if (!types.contains(type)) {
			types.add(type);
			decCounter.put(type, 0);
			refCounter.put(type, 0);
		}
	}

	/**
	 * Increment the counter value for a given type in decCounter.
	 *
	 * @param type
	 *            String, java type
	 */
	private static void incDecCount(String type) {
		// Check if the type exists, then increment their associated value by 1
		if (decCounter.containsKey(type)) {
			decCounter.put(type, decCounter.get(type) + 1);
		}
	}

	/**
	 * Increment the counter value for a given type in refCounter.
	 *
	 * @param type
	 *            String, java type
	 */
	private static void incRefCount(String type) {
		// Check if the type exists, then increment their associated value by 1
		if (refCounter.containsKey(type)) {
			refCounter.put(type, refCounter.get(type) + 1);
		}
	}

	/*
	 * ============================== HELPER FUNCTIONS ==============================
	 */

	/**
	 * constructor Intialize the list of types, and the HashMaps for the counters to
	 * null.
	 */
	public TypeVisitor() {
		// initialize list and counters to null
		types = new ArrayList<String>();
		decCounter = new HashMap<String, Integer>();
		refCounter = new HashMap<String, Integer>();
	}

	/**
	 * Accessor method. Fetches the map of declaration counter.
	 *
	 * @return HashMap : decCounter
	 */
	public Map<String, Integer> getDecCount() {
		return decCounter;
	}

	/**
	 * Accessor method. Fetches the list of types.
	 *
	 * @return ArrayList<String> : types
	 */
	public ArrayList<String> getList() {
		return types;
	}

	/**
	 * Accessor method. Fetches the map of reference counter.
	 *
	 * @return HashMap : refCounter
	 */
	public Map<String, Integer> getRefCount() {
		return refCounter;
	}

	/*
	 * ============================== ASTVisitor FUNCTIONS ==============================
	 */

	/**
	 * Visits an annotation type declaration AST node type. Looks for
	 *
	 * @interface <identifier> { }
	 *
	 *            Determine the type of the annotation, add it to types, and
	 *            increment its type's counter in decCounter.
	 *
	 *            CounterType: DECLARATION
	 *
	 * @param node
	 *            AnnotationTypeDeclaration
	 * @return boolean true to visit the children of this node
	 */
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();

		addTypeToList(type);
		incDecCount(type);

		return true;
	}

	/**
	 * Visits a Enum declaration AST node type. Determine the type of the Enum
	 * identifier, add it to types, and increment its type's counter value in
	 * decCounter.
	 *
	 * CounterType: DECLARATION
	 *
	 * @param node
	 *            : EnumDeclaration
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(EnumDeclaration node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();

		addTypeToList(type);
		incDecCount(type);

		return true;
	}

	/**
	 * Visits a type declaration node type. Type declaration node is the union of
	 * class declaration, and interface declaration.
	 *
	 * Determine the type of class, add it to types, and increment the declaration
	 * counter associated to the type.
	 *
	 * CounterType: DECLARATION
	 *
	 * @param node
	 *            : TypeDeclaration
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();

		addTypeToList(type);
		incDecCount(type);

		return true;
	}
	
	/**
	 * Visits a type declaration node type. Type declaration node is the union of
	 * class declaration, and interface declaration.
	 *
	 * Determine the type of class, add it to types, and increment the declaration
	 * counter associated to the type.
	 *
	 * CounterType: DECLARATION
	 *
	 * @param node
	 *            : TypeDeclaration
	 * @return boolean : True to visit the children of this node
	 */
	
	/**
	 * Visits a type declaration node type. Type declaration node is the union of
	 * class declaration, and interface declaration.
	 *
	 * Determine the type of class, add it to types, and increment the declaration
	 * counter associated to the type.
	 *
	 * CounterType: DECLARATION
	 *
	 * @param node
	 *            : TypeDeclaration
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(QualifiedName node) {
		
		String type = node.getFullyQualifiedName();
		
		
		
//		ITypeBinding typeBind = node.resolveBinding();
//		String type = typeBind.getQualifiedName();

		addTypeToList(type);
		incRefCount(type);

		return true;
	}
	
	/**
	 * Visits a type declaration node type. Type declaration node is the union of
	 * class declaration, and interface declaration.
	 *
	 * Determine the type of class, add it to types, and increment the declaration
	 * counter associated to the type.
	 *
	 * CounterType: DECLARATION
	 *
	 * @param node
	 *            : TypeDeclaration
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(SimpleName node) {
		
		if (!node.isDeclaration())
		{
			String type1 = node.getFullyQualifiedName();
			
			ITypeBinding typeBind = node.resolveTypeBinding();
			if (typeBind != null) {
				String type2 = typeBind.getName();
				
				if (type1.equals(type2)) {
					String type = typeBind.getQualifiedName();
					addTypeToList(type);
					incRefCount(type);
				}
			}
			else {
				addTypeToList(type1);
				incRefCount(type1);
			}
		}

		return true;
	}
	
	@Override
	public boolean visit(PrimitiveType node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();
		
		addTypeToList(type);
		incRefCount(type);
		return true;
	}
	
	@Override
	public boolean visit(VariableDeclarationStatement node){
		boolean isParameterized = node.getType().isParameterizedType();
		
		// get parameterized variables
		if (isParameterized){
			ITypeBinding typeBind = node.getType().resolveBinding().getTypeDeclaration();
			String type = typeBind.getQualifiedName();

			addTypeToList(type);
			incRefCount(type);


			// inc count for all the arguments
			for (ITypeBinding paramBind : node.getType().resolveBinding().getTypeArguments()){
				String paramType = paramBind.getQualifiedName();
				addTypeToList(paramType);
				incRefCount(paramType);
			}

		} else {
			// iterate through all the fragments, and increment the type counter
			for (Object fragment : node.fragments()){
				if (fragment instanceof VariableDeclarationFragment){
					ITypeBinding typeBind = ((VariableDeclarationFragment) fragment).resolveBinding().getType();
					boolean isDeclaration = ((VariableDeclarationFragment) fragment).getName().isDeclaration();
					String type = typeBind.getQualifiedName();

					addTypeToList(type);
					incRefCount(type);
				}
			}

		}


		return true;
	}


}

package main;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private static ArrayList<String> types = new ArrayList<String>();

	private static HashMap<String, Integer> decCounter = new HashMap<String, Integer>();

	private static HashMap<String, Integer> refCounter = new HashMap<String, Integer>();

	private static boolean debug = false;

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
	
	public static void resetCounters(){
		types.clear();
		decCounter.clear();
		refCounter.clear();
	}
	
	public static void printTypes() {
		for (String type : types) {
			int refCount = refCounter.get(type);
			int decCount = decCounter.get(type);
			System.out.println(type + ". Declarations found: " + decCount + "; references found: " + refCount + ".");
		}
	}

	/*
	 * ============================== HELPER FUNCTIONS ==============================
	 */

	/**
	 * constructor, removed implementation
	 */
	public TypeVisitor() {}

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
		// get the Identifier and add +1 to the declaration count
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();

		addTypeToList(type);
		incDecCount(type);
		
		// If there are TypeParameters, add +1 to the reference count
		// for each TypeParameter
		List typeParameters = node.typeParameters();
		if (typeParameters != null) {
			for (int i = 0; i < typeParameters.size(); i++) {
				TypeParameter typeParameter = (TypeParameter) typeParameters.get(i);
				ITypeBinding typeParameterBind = typeParameter.resolveBinding();
				String typeParameterName = typeParameterBind.getQualifiedName();
				
				addTypeToList(typeParameterName);
				incRefCount(typeParameterName);
			}
		}
		
		// If the interface is extending some Types, add +1 to the reference count
		// for each super interfaces
		List superinterfaces = node.superInterfaceTypes();
		if (superinterfaces != null) {
			for (int i = 0; i < superinterfaces.size(); i++) {
				Type superinterfaceType = (Type) superinterfaces.get(i);
				ITypeBinding superinterfaceBind = superinterfaceType.resolveBinding();
				String superinterfaceName = superinterfaceBind.getQualifiedName();
							
				addTypeToList(superinterfaceName);
				incRefCount(superinterfaceName);
			}
		}
		
		if (!node.isInterface()) {
			// If the class/interface is extending some Type, add +1 to the reference count
			Type superclass = node.getSuperclassType();
			if (superclass != null) {
				ITypeBinding superclassBind = superclass.resolveBinding();
				String superclassName = superclassBind.getQualifiedName();
							
				addTypeToList(superclassName);
				incRefCount(superclassName);
			}
		}

		return true;
	}
	
	/**
	 * Visits a SimpleName node type. A simple name is an identifier other
	 * than a keyword, boolean literal ("true", "false") or null literal ("null").
	 * SimpleName:
	 *      Identifier
	 *
	 * General Algorithm:
	 * 1. Determine the name of the identifier by calling node.getFullyQualifiedName()
	 * 2. Determine the name of the identifier after resolving the binding.
	 * 3. Check if the result from 1 and 2 are the same. If they are, add +1 to the ref count.
	 *
	 * CounterType: REFERENCE
	 *
	 * @param node
	 *            : SimpleName
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(SimpleName node) {
		// isDeclaration() returns true if a name is defined:
		// the type name in a TypeDeclaration node
		// the method name in a MethodDeclaration node providing isConstructor is false
		// The variable name in any type of VariableDeclaration node
		// The enum type name in a EnumDeclaration node
		// The enum constant name in an EnumConstantDeclaration node
		// The variable name in an EnhancedForStatement node
		// The type variable name in a TypeParameter node
		// The type name in an AnnotationTypeDeclaration node
		// The member name in an AnnotationTypeMemberDeclaration node
		
		// NOTICE HOW ImportDeclaration and PackageDeclaration types
		// will ALWAYS pass this, since they are not listed above.
		// Therefore we can use SimpleName for ImportDeclaration and PackageDeclaration;
		// However, the PackageDeclaration will have the bindtype of 1,
		// so it will not be counted in this node -> Gotta count the PackageDeclaration
		// in a separate node
		if (!node.isDeclaration())
		{
			String type1 = node.getFullyQualifiedName();
			
			// Determine what kind of binding this is.
			// bindtype == 1 if it's a package binding -> DONE
			// bindtype == 2 if it's a type binding -> DONE
			// bindtype == 3 if it's a field or local variable binding -> WE DON'T HAVE TO BOTHER
			// bindtype == 4 if it's a method or constructor binding -> DONE
			// bindtype == 5 if it's a annotation binding -> DONE
			// bindtype == 6 if it's a member value pair binding
			// If the binding null, then set the bindtype to 0
			IBinding binding = node.resolveBinding();
			int bindtype;
			
			if (binding == null) {
				bindtype = 0;
			}
			else {
				bindtype = binding.getKind();
			}
			
			if (debug)
				System.out.println(type1 + "'s bindtype: " + bindtype);
			
			if (bindtype == 2) {
				ITypeBinding typeBind = node.resolveTypeBinding();
				String type2 = typeBind.getName();
				
				// taking care of the parameterized types under the SimpleName type
				if (type2.contains("<") && type2.contains(">")) {
					// parse out only the identifier part
					type2 = type2.substring(0, type2.indexOf("<"));
					
					// check if the identifier part of the node name is the same as
					// the identifier of the name after resolving binding.
					// If they are equal, then add this to the ref count.
					if (type1.equals(type2)) {
						String type = typeBind.getQualifiedName();
						type = type.substring(0, type.indexOf("<"));
						addTypeToList(type);
						incRefCount(type);
					}
				}
				
				// taking care of the rest of the SimpleName types
				else {
					// check if the identifier part of the node name is the same as
					// the identifier of the name after resolving binding.
					// If they are equal, then add this to the ref count.
					if (type1.equals(type2)) {
						String type = typeBind.getQualifiedName();
						addTypeToList(type);
						incRefCount(type);
					}
				}
			}
			
			// if the bindtype is 0, add the reference counter;
			// this takes care of the cases where there is a reference to a random class
			// that has not been implemented.
			else if (bindtype == 0) {
				// see if this is from SimpleType
				// if it is not, don't count it in
				ASTNode parent = node.getParent();
				
				if (parent.getClass().getName().contains("SimpleType")) {
					addTypeToList(type1);
					incRefCount(type1);
				}
			}

			// if the bindtype is 4, add the reference counter ONLY IF
			// the parent node is MethodDeclaration and only if the MethodDeclaration node
			// is a constructor
			else if (bindtype == 4) {
				ASTNode parent = node.getParent();
				
				if (parent.getClass().getName().contains("MethodDeclaration")) {
					MethodDeclaration mNode = (MethodDeclaration) parent;
					
					if (mNode.isConstructor()) {
						addTypeToList(type1);
						incRefCount(type1);
					}
				}
			}
		}

		return true;
	}
	
	/**
	 * Visits a PrimitiveType node type.
	 * PrimitiveType:
	 *     { Annotation } byte
	 *     { Annotation } short
	 *     { Annotation } char
	 *     { Annotation } int
	 *     { Annotation } long
	 *     { Annotation } float
	 *     { Annotation } double
	 *     { Annotation } boolean
	 *     { Annotation } void
	 *
	 * Determine the type of the binding, add it to types, and increment the reference
	 * counter associated to the type.
	 *
	 * CounterType: REFERENCE
	 *
	 * @param node
	 *            : PrimitiveType
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(PrimitiveType node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();
		
		if (node.getPrimitiveTypeCode().toString() != "void") {
			addTypeToList(type);
			incRefCount(type);
		}
		
		return true;
	}
	
	/**
	 * Visits a ArrayType node type.
	 * ArrayType:
	 *     Type Dimension { Dimension }
	 *
	 * Determine the type of the binding, add it to types, and increment the reference
	 * counter associated to the type.
	 *
	 * CounterType: REFERENCE
	 *
	 * @param node
	 *            : ArrayType
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(ArrayType node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();
		
		addTypeToList(type);
		incRefCount(type);
		return true;
	}
}

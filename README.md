Lucky7 - Iteration 2
-------------------------

Team Members
----
ABOU GHARIB, Patrick George
- UCID: 
- pgabough@ucalgary.ca

CHAN, Sze Lok Irene
- UCID: 10103807
- slchan@ucalgary.ca

CHOW, Tyler
- UCID: 30024575
- tyler.chow@ucalgary.ca

CHUNG, Ha (Esther)
- UCID: 10080030
- hkchung@ucalgary.ca

SCHIJNS, Joshua
- UCID: 10017168
- jmschijn@ucalgary.ca

Tasks
-----
- Detect all Java files in a directory (DONE)
	- Having the user input the paths for each individual file is not enough because we need to be able to read from ALL java files in a directory
	- Remember to check for `.java` file ending (DONE)
	- Remember to check for `.jar` file ending (DONE)
	- Recursively find all `.java` and `.jar` files (DONE)
	- Extract `.jar` files (DONE: Used for Irene's debug, can be ignored)
	- Dynamically read `.jar` files during execution (DONE, it's done right Patrick?)
- Convert file contents to string
	- Done 1 file at a time (DONE)
	- Should probably save to an ArrayList<String> (DONE)
- Configure AST parser to parse file contents (DONE)
- Get Type declaration and reference information
	- Distribute ASTVisitor node types between all of us (DONE)
	- Implement ASTVisitor nodes for all reference counts 
- Error checking for user input
	- Invalid number of arguments (DONE)
	- Invalid directory (DONE)
- Output
	- Declaration/Reference count overflow (DONE)
		- Practically impossible using BigIncrementer (DONE)
	- Invalid input creates proper error messages
- Directory (RECURSIVE SEARCH) -- Need testing
	- Contains no Java files (DONE)
	- Contains 1 Java file (DONE)
	- Contains multiple Java files (DONE)
	- Contains non-Java files (DONE)
	- Contains non-readable files. ie. do not have read permission

Classes
-------
TypeFinder
- Main
- Gets user input
- Outputs results

JavaJarFileReader
- Gets files from input directory
- Converts file contents to Strings

ASTParser
- Parses file contents

ASTNode
- Has declaration and reference information from file contents

TypeVisitor
- Get declaration and reference information from AST

BigIncrementer (Removed for now)
- Tracks declaration and reference counts

Diagrams
--------
UML diagram

Sequence Diagrams
- main(TypeFinder)
- initASTParser(TypeFinder)
- getAllJavaFiles(JavaFileReader)


State
- Our classes (main package)
- ASTParser, ASTNode, ASTParser

Sequence
-


Resources
---------
- [ASTParser Javadoc](https://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2Fdom%2FASTParser.html)
- [Diagram Drawing Tool](https://draw.io)
	- When changes are made, export both the `.xml` and `.png` files to the report directory

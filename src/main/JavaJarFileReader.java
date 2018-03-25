package main;

import java.io.*;
import java.util.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NotDirectoryException;


/**
 * Recursively find all ".java" and ".jar" files in a given directory. This reader will
 * extract all the JAR files, and retrieve source code from all found JAVA files.
 *    
 * @author 	Sze Lok Irene Chan
 * @since 	20 March, 2018
 * @version 1.0
 */

public class JavaJarFileReader {

	/**
	 * Check the file extension for ".java"
	 * 
	 * @param fileName
	 * @return true if file is a ".java" file, false otherwise
	 */
    private static boolean isJavaFile(String fileName){
        return fileName.endsWith(".java");
    }

    /**
     * Check the file extension for ".jar"
     * 
     * @param fileName
     * @return true if file is a ".jar" file, false otherwise
     */
    private static boolean isJarFile(String fileName){
        return fileName.endsWith(".jar");
    }

    /**
     * From a given directory path, find and read the content of all JAVA files,
     * and return their content as an array list of strings. 
     *  
     * @param dirPath 		Directory to be searched
     * @return ArrayList<String> 	List of each JAVA file as String
     * @throws DirectoryNotEmptyException 
     * 			Thrown if a directory cannot be found
     * @throws IOException
     * 			Thrown if any other IO problems are encountered.
     */
    public static ArrayList<String> getAllFilesToString(String dirPath) 
            throws DirectoryNotEmptyException, IOException {

        ArrayList<String> filesContent = new ArrayList<String>();

        // Recursively find all ".jar" files in the given directory path
        // ArrayList<String> jarFileNames = getJarFileNames(dirPath);
        String fileContent = "";
        
        // Extract all the ".jar" files into the given directory path
        // for (String jarFile : jarFileNames) {
        //     extractJarFile(jarFile, dirPath);
        // }

        // Recursively find all ".java" files in the given directory path
        ArrayList<String> javaFileNames = getJavaFileNames(dirPath);

        // Retrieve the content of each ".java" file, and store it into the list
        for (String fileName : javaFileNames){
            if (isJavaFile(fileName)){
                fileContent = getJavaFileContentToString(fileName);
            }
            filesContent.add(fileContent);
        }

        return filesContent;
    } 

    /**
     * Given a JAR file, and the target directory path, extract the JAR file
     * into the target path.
     * 
     * @param jarFile	The absolute path of the JAR file to be extracted
     * @param destDir	Target path to extract the JAR file into
     * @throws IOException
     * 			Thrown if any IO problems are encountered
     */
    public static void extractJarFile(String jarFile, String destDir) throws IOException{
        // Open the file as a JAR file
        JarFile jar = new JarFile(new File(jarFile));
        Enumeration<JarEntry> enumEntries = jar.entries();

        // Get all elements in the JAR file.
        while (enumEntries.hasMoreElements()){
            JarEntry jFile = enumEntries.nextElement();

            File file = new File (destDir, jFile.getName());

            // Check if file already exists
            if (!file.exists()){
                // Create directories if it's not 
                file.getParentFile().mkdirs();
                file = new File (destDir, jFile.getName());
            }

            if (jFile.isDirectory()){
                continue;
            }

            // Extract the file
            InputStream is = jar.getInputStream(jFile);
            FileOutputStream fos = new FileOutputStream(file);
            while (is.available() > 0){
                fos.write(is.read());
            }

            // Close streams
            fos.close();
            is.close();
        }
        // Close the JAR file
        jar.close();
    }

    /**
     * Recursively find all the JAR files from the given directory path, 
     * and store the path of the JAR file into a list.
     * 
     * @param dirPath 	The directory of interest
     * @return	ArrayList<String> 	An ArrayList of type String containing all the
     * 								paths to all found JAR files.
     */
    public static ArrayList<String> getJarFileNames(String dirPath) throws NotDirectoryException{
        File directory = new File(dirPath);
        // Call the recursive function to find all JAR files
        ArrayList<String> jarFileNames = getAllJarFileNames(directory);
        return jarFileNames;
    }

    /**
     * Recursive function to find all the JAR files located in the 
     * current directory and its sub directories. 
     * 
     * @param currDirectory 	Current directory path
     * @return ArrayList<String> 	An ArrayList of type String containing all the
     * 								paths to all found JAR files located in the current
     * 								directory and all of its sub directories.
     */
    public static ArrayList<String> getAllJarFileNames(File currDirectory) throws NotDirectoryException{
        ArrayList<String> allJarFiles = new ArrayList<String>();
        ArrayList<String> subDirJarFiles;

        try {
            File[] files = currDirectory.listFiles();
            for (File file : files){
                // if there exists a sub directory
                if (file.isDirectory()){
                    // Store the JAR files found in sub directories
                    subDirJarFiles = new ArrayList<String>(getAllJarFileNames(file));
                    // Add all the JAR files found in sub directories to the main list
                    allJarFiles.addAll(subDirJarFiles);
                } else {
                // otherwise, check if the file is a JAR file
                    if (file.isFile() && isJarFile(file.getName())){
                        allJarFiles.add(file.getPath());
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return allJarFiles;
    }

    /**
     * Read the content of a JAVA file, and returns the content
     * of the file as a String.
     * 
     * @param file 		Path of the JAVA file to be read
     * @return String 	Content of the JAVA file
     * @throws IOException
     * 			Thrown if any IO problems are encountered 
     */
    public static String getJavaFileContentToString(String file) throws IOException{
        FileReader fileReader = new FileReader(file);
        BufferedReader buffReader = new BufferedReader(fileReader);
        StringBuffer strBuffer = new StringBuffer();
        String lineSeparator = System.getProperty("line.separator");

        // Combine the content into a long String
        try {
            String line = buffReader.readLine();
            while (line != null){
                strBuffer.append(line);
                strBuffer.append(lineSeparator);
                line = buffReader.readLine();
            }
            return strBuffer.toString();
        } finally {
            buffReader.close();
        }
    }

    /**
     * Recursively find all the JAVA files from the given directory path,
     * and store the path of the JAVA file into a list.
     * 
     * @param dirPath 	The directory of interest
     * @return ArrayList<String> 	An ArrayList of type String containing all
     * 								the paths to all found JAVA files.
     */
    public static ArrayList<String> getJavaFileNames(String dirPath) throws IOException{
        File directory = new File(dirPath);
        // call recursive function to retrieve all JAVA files
        ArrayList<String> fileNames = getAllJavaFileNames(directory);

        // Sort all the path names to the files by alphabetical order
        Collections.sort(fileNames);
        return fileNames;
    }

    /**
     * Recursive function to find all the JAVA files located
     * in the current directory and its sub directories.
     * 
     * @param currDirectory 	Current directory path
     * @return ArrayList<String> 	An ArrayList of type String containing all the
     * 								paths to all found JAVA files located in the current
     * 								directory and all of its sub directories .
     */
    public static ArrayList<String> getAllJavaFileNames(File currDirectory) throws IOException{
        ArrayList<String> allFileNames = new ArrayList<String>();
        ArrayList<String> subDirFiles;

        File[] files = currDirectory.listFiles();
        for (File file : files){
            // If there exists a sub directory
            if (file.isDirectory()){
                // store the JAVA files found in the sub directories
                subDirFiles = new ArrayList<String>(getAllJavaFileNames(file));
                // add all the JAVA files found in the sub directories to main list
                allFileNames.addAll(subDirFiles);
            } else {
            // otherwise, check if the file is a JAVA file
                if (file.isFile() && isJavaFile(file.getName())){
                    allFileNames.add(file.getPath());
                }
            }
        }

        return allFileNames;
    }
    
    // Debug purpose
    // public static void main (String[] args) throws IOException{
    //     JavaJarFileReader x = new JavaJarFileReader();
    //     String destDir = "/home/slchan/eclipse-workspace/SENG300G2/src/test/javaFileReaderTestPackage/";
    //     List<String> jarnames = x.getJarFileNames(destDir);

    //     for (String jar : jarnames){
    //         System.out.println(jar);
    //         x.extractJarFile(jar, destDir);
    //     }

    //     List<String> javaNames = x.getJavaFileNames(destDir);

    //     for (String name : javaNames){
    //         System.out.println(name);
    //     }
    // }
}
package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class DocumentData {
	
	/** Reads in a .mtx file from the specified filename
	 * @param filename
	 * @param hasHeader
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public static HashMap<Integer, HashMap<Integer, Integer>> ReadInData(String filename, Boolean hasHeader)
			throws FileNotFoundException, Exception {
		HashMap<Integer, HashMap<Integer, Integer>> documents = new HashMap<Integer, HashMap<Integer, Integer>>();
		Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
		if ( hasHeader ) {
			// The first two lines are header, telling about the matrix
			// we will ignore them
			sc.nextLine();
			sc.nextLine();
		}
		while (sc.hasNextLine()) {
			ParseInLine(sc.nextLine(), documents);
		}
		sc.close();
		return documents;
	}
	
	/** Parses a line from a .mtx file and adds it to the set of documents
	 * @param line
	 * @param documents
	 * @throws Exception
	 */
	private static void ParseInLine( String line, HashMap<Integer, HashMap<Integer, Integer>> documents ) throws Exception {
		String[] fields = line.split(" ");
		if ( fields.length != 3 ) {
			throw new Exception("Matrix line has incorrect number of fields: " + line);
		}
		Integer termId = Integer.parseInt(fields[0]);
		Integer docId = Integer.parseInt(fields[1]);
		Integer count = Integer.parseInt(fields[2].split("\\.")[0]);
		if ( !documents.containsKey(docId) ) {
			documents.put(docId, new HashMap<Integer, Integer>());
		}
		documents.get(docId).put(termId, count);
	}


}

// Sahana Sarangi
// 8 August 2024
// CSE 122
// TA: Abby & Connor
// P3: Program Linting
// This class is a checker (implements the Check interface). It checks for the usage of an 
// empty System.out.println statement (specifically the pattern System.out.println("")) in
// a line of code and recognizes the usage as an error.

import java.util.*;

public class BlankPrintlnCheck implements Check {

	// Behavior:
	//	- This method looks for instances of empty System.out.println statements (specifically,
	//	  the pattern System.out.println("")) in a line of code. If the pattern is found, an error 
	//	  error message describing the issue is returned. If the pattern is not found in 
	//	  the line of code, an empty placeholder is returned.
	// Parameters:
	//	- line: the line of code (as a String) the method is checking for the keyword in
	//	- lineNumber: the line number (Integer) that the line of code being passed into the method
	//				  is located on in the user's file
	// Return:
	//	- Optional<Error>: An Optional object holding an Error object if the pattern is found, or 
	//					   otherwise is empty
	public Optional<Error> lint(String line, int lineNumber) {
		if (line.contains("System.out.println(\"\")")) {
			String message = "Blank System.out.println used.";
			return Optional.of(new Error(3, lineNumber, message));
		}
		return Optional.empty();
	}
}

// Sahana Sarangi
// 8 August 2024
// CSE 122
// TA: Abby & Connor
// P3: Program Linting
// This class is a checker (implements the Check interface). It checks the length of a line of 
// code. If the length of the line is at least 100 characters, it recognizes this as an error.

import java.util.*;

public class LongLineCheck implements Check {

	// Behavior:
	//	- This method checks whether a line of code is longer than or equal to 100 characters in 
	//	  length. If the line of code is at least 100 character, an error message describing the
	//	  issue is returned. If the line of code is less than 100 characters in length, an empty
	//	  placeholder is returned.
	// Parameters:
	//	- line: the line of code (as a String) the method is checking for the keyword in
	//	- lineNumber: the line number (Integer) that the line of code being passed into the method
	//				  is located on in the user's file
	// Return:
	//	- Optional<Error>: An Optional object holding an Error object if the keyword is found, or 
	//					   otherwise is empty
	public Optional<Error> lint(String line, int lineNumber) {
		if (line.length() >= 100) {
			String message = "Line is too long (over 100 characters).";
			return Optional.of(new Error(1, lineNumber, message));
		}
		return Optional.empty();
	}
}

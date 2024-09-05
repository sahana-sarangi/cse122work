// Sahana Sarangi
// 8 August 2024
// CSE 122
// TA: Abby & Connor
// P3: Program Linting
// This class defines the properties of an error found in a user's code file. It gives the user
// the line number that an error was recognized on, the error code of that error, and a brief
// description of the nature of the error found.

public class Error {

	private int code;
	private int lineNumber;
	private String message;

	// Behavior:
	//	- This constructor sets the fields of the Error object
	// Parameters:
	//	- code: the error code (Integer) of the applicable error
	//	- lineNumber: The line number (Integer) of the user's file where the error is applicable
	//	- message: a description of the type of error that is applicable in a situation (String)
	public Error(int code, int lineNumber, String message) {
		this.code = code;
		this.lineNumber = lineNumber;
		this.message = message;
	}

	// This method returns the String representation of the Error object
	public String toString() {
		return "(Line: " + this.lineNumber + ") has error code " + this.code + "\n" + this.message;
	}

	// This method is a getter that returns the field lineNumber
	public int getLineNumber() {
		return this.lineNumber;
	}


	// This method is a getter that returns the field code
	public int getCode() {
		return this.code;
	}


	// This method is a getter that returns the field message
	public String getMessage() {
		return this.message;
	}
}

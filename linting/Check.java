import java.util.*;

// A common interface for linters that can check a single line of code
// Every Check will check for its own type of error on a single line of code.
public interface Check {
	// Checks for this Check's error condition on this line with this line number.
	// If an error exists on this line, returns an Optional with an Error present 
	// indicating an error occurred. If no errors are present, returns an empty Optional.
	public Optional<Error> lint(String line, int lineNumber);
}

// Sahana Sarangi
// 8 August 2024
// CSE 122
// TA: Abby & Connor
// P3: Program Linting
// This class is a Linter that runs checks on a code file. 

import java.util.*;
import java.io.*;

public class Linter {
    private List<Check> checks;

    // Behavior:
    //  - This constructor sets the field checks to a new ArrayList
    // Parameters:
    //  - checks: a List of the Check type containing all the different checks that the linter
    //            must check for
    public Linter(List<Check> checks) {
        this.checks = new ArrayList<Check>(checks);
    }

    // Behavior:
    //  - This method reads the user's file, performs checks to find if any errors are applicable,
    //    and then stores those errors in a List
    // Parameters:
    //  - fileName: the name of the file that the method scans through
    // Return:
    //  - List<Error>: an ArrayList of the Error type that contains all the errors that are
    //                 applicable to the user's file
    public List<Error> lint(String fileName) throws FileNotFoundException {
        Scanner fileScan = new Scanner(new File(fileName));
        List<Error> errors = new ArrayList<>();

        int lineNum = 1;
        while (fileScan.hasNextLine()) {
            String line = fileScan.nextLine();
            for (Check check : checks) {
                Optional<Error> potentialError = check.lint(line, lineNum);
                if (potentialError.isPresent()) {
                    errors.add(potentialError.get());
                }
            }
            lineNum++;
        }
        return errors;
    }
}

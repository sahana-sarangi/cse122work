// Sahana Sarangi
// 8 August 2024
// CSE 122
// TA: Abby & Connor
// P3: Program Linting
// This class is the user implementation of the Linter program

import java.util.*;
import java.io.*;

public class LinterMain {
    public static final String FILE_NAME = "TestFile.java";

    public static void main(String[] args) throws FileNotFoundException {
        List<Check> checks = new ArrayList<>();
        checks.add(new LongLineCheck());
        checks.add(new BreakCheck());
        checks.add(new BlankPrintlnCheck());
        Linter linter = new Linter(checks);
        List<Error> errors = linter.lint(FILE_NAME);
        for (Error e : errors) {
            System.out.println(e);
        }
    }
}

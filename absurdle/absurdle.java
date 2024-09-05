// Sahana Sarangi
// 08/04/2024
// CSE 122
// P2: Absurdle
// TA: Abby & Connor
// This class allows the user to play the game Absurdle, a variation on Wordle. It takes in the
// length of the words that a user wants to guess and allows the user to continuously guess
// words, with the intention of prolonging the game for as long as possible. When the user 
// guesses the last word possible to guess given the clues the program has given them, the
// user wins and the game is over. 

import java.util.*;
import java.io.*;

public class Absurdle  {
    public static final String GREEN = "🟩";
    public static final String YELLOW = "🟨";
    public static final String GRAY = "⬜";

    // [[ ALL OF MAIN PROVIDED ]]
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the game of Absurdle.");

        System.out.print("What dictionary would you like to use? ");
        String dictName = console.next();

        System.out.print("What length word would you like to guess? ");
        int wordLength = console.nextInt();

        List<String> contents = loadFile(new Scanner(new File(dictName)));
        Set<String> words = pruneDictionary(contents, wordLength);

        List<String> guessedPatterns = new ArrayList<>();
        while (!isFinished(guessedPatterns)) {
            System.out.print("> ");
            String guess = console.next();
            String pattern = findMostCommonPattern(guess, words, wordLength);
            guessedPatterns.add(pattern);
            System.out.println(": " + pattern);
            System.out.println();
        }
        System.out.println("Absurdle " + guessedPatterns.size() + "/∞");
        System.out.println();
        printPatterns(guessedPatterns);
    }

    // [[ PROVIDED ]]
    // Prints out the given list of patterns.
    // - List<String> patterns: list of patterns from the game
    public static void printPatterns(List<String> patterns) {
        for (String pattern : patterns) {
            System.out.println(pattern);
        }
    }

    // [[ PROVIDED ]]
    // Returns true if the game is finished, meaning the user guessed the word. Returns
    // false otherwise.
    // - List<String> patterns: list of patterns from the game
    public static boolean isFinished(List<String> patterns) {
        if (patterns.isEmpty()) {
            return false;
        }
        String lastPattern = patterns.get(patterns.size() - 1);
        return !lastPattern.contains("⬜") && !lastPattern.contains("🟨");
    }

    // [[ PROVIDED ]]
    // Loads the contents of a given file Scanner into a List<String> and returns it.
    // - Scanner dictScan: contains file contents
    public static List<String> loadFile(Scanner dictScan) {
        List<String> contents = new ArrayList<>();
        while (dictScan.hasNext()) {
            contents.add(dictScan.next());
        }
        return contents;
    }

    //Behavior: 
    //  - This method prunes the program's dictionary to only include words that are the 
    //    length of the word that the user wants to guess.
    //Exception:
    //  - If the user chooses a guess word length that is less than 1, an IllegalArgumentException
    //    is thrown.
    //Return:
    //  - A HashSet of the String type that contains all the remaining words in the pruned 
    //    dictionary
    //Parameters:
    //  - contents: an ArrayList of the String type containing all the words in the program's
    //              dictionary
    //  - wordLength: the length of the word that the user wants to guess
    public static Set<String> pruneDictionary(List<String> contents, int wordLength) {
        if (wordLength < 1) {
            throw new IllegalArgumentException();
        }

        Set<String> prunedDict = new TreeSet<>();

        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).length() == wordLength) {
                prunedDict.add(contents.get(i));
            }
        }
        return prunedDict;
    }

    //Behavior:
    //  - This method finds the pattern of green, yellow, and gray emojis that the program will
    //    output that will leave the greatest possible number of words for the user to guess after
    //    recieving that pattern.
    //Exception:
    //  - if the program's dictionary is empty or the user guesses a word whose length is 
    //    different from the length the user already indicated they wanted to guess, an 
    //    IllegalArgumentException is thrown.
    //Returns:
    //  - The pattern that will leave the greatest possible number of words for the user to guess
    //    after recieving that pattern (as a String).
    //Parameters:
    //  - guess: the word that the user guessed
    //  - words: a HashSet of the String type containing all the remaining words in the user's
    //           dictionary
    //  - wordLength: the length of the word that the user wants to guess
    public static String findMostCommonPattern(String guess, Set<String> words, int wordLength) {

        if (words.isEmpty() || guess.length() != wordLength) {
            throw new IllegalArgumentException();
        }

        Map<String, Set<String>> patterns = new TreeMap<>();

        for (String word : words) {
            String patternForWord = patternForWord(word, guess);
            if (!patterns.containsKey(patternForWord)) {
                patterns.put(patternForWord, new HashSet<String>());
            }
            patterns.get(patternForWord).add(word);
        }

        int patternMaxWords = 0;
        String maxPattern = "";
        for (String pattern : patterns.keySet()) {
            if (patterns.get(pattern).size() > patternMaxWords) {
                patternMaxWords = patterns.get(pattern).size();
                maxPattern = pattern;
                }
            }
        
        editDictionary(words, patterns, maxPattern);
        return maxPattern;
    }  

    //Behavior:
    //  - This method prunes the program's dictionary by removing words that are not one of the
    //    words that the user could guess after receiving a pattern from the program.
    //Parameters:
    //  - words: a HashSet of the String type containing all the words left in the program's
    //           dictionary
    //  - patterns: a HashMap with keys that are Strings and values that are HashSets of the 
    //              String type that maps all the possible patterns that the program could give
    //              the user based on their guess to all the words that the user could still guess
    //              after recieving that pattern
    public static void editDictionary(Set<String> words, Map<String, Set<String>> patterns, 
            String maxPattern) {
        words.clear();
        for (String maxPatternWord : patterns.get(maxPattern)) {
            words.add(maxPatternWord);
        }
    }  

    //Behavior:
    //  - This method finds the pattern to output given a guess and a target word. For all letters
    //    in the guess and the target word that are the same and in the same position in both
    //    words, the program will change those letters in the guess to a green emoji. For all the 
    //    remaining letters in the guess and the target word that are the same but not in the same
    //    position in both words, the program will change those letters in the guess to a yellow
    //    emoji. All remaining letters in the guess will be changed to a gray emoji.
    //Return:
    //  - The method returns a String containing the appropriate yellow, green, and gray emojis 
    //    that have been added according to the user's guess and a target word. 
    //Parameters:
    //  - word: a target word that the user's guess will be compared to in order to assign the
    //          green, yellow, and gray emojis
    //  - guess: the word that the user guessed
    public static String patternForWord(String word, String guess) {
        String[] chars = new String[word.length()];
        for (int i = 0; i < word.length(); i++) {
            chars[i] = Character.toString(guess.charAt(i));
        }

        for (int j = 0; j < word.length(); j++) {
            if (word.charAt(j) == chars[j].charAt(0)) {
                word = word.substring(0, j) + "!" + word.substring(j + 1);
                chars[j] = "!";
            }
        }
        
        for (int k = 0; k < word.length(); k++) {
            for (int l = 0; l < word.length(); l++) {
                if ((word.charAt(k) != '!') && (word.charAt(k) == chars[l].charAt(0))) {
                    word = word.substring(0, k) + "$" + word.substring(k + 1);
                    chars[l] = "$";
                }
            }
        }
        return convertToPattern(word, chars);
    }

    //Behavior:
    //  - This method converts all the special characters in the in the user's guess into
    //    their corresponding green, yellow, and gray emojis. Exclamation marks are changed
    //    to green emojis, dollar signs are changed to yellow emojis, and all other remaining
    //    letters are changed to gray emojis.
    //Parameters:
    //  - word: a target word that the user's guess will be compared to in order to assign the
    //          green, yellow, and gray emojis
    //  - chars: an Array of the String type containing all the characters and special characters
    //           in the user's guess
    public static String convertToPattern(String word, String[] chars) {
        for (int b = 0; b < word.length(); b++) {
            if (chars[b] == "!") {
                chars[b] = GREEN;
            } else if (chars[b] == "$") {
                chars[b] = YELLOW;
            } else {
                chars[b] = GRAY;
            }
        }
        String patternForWord = "";
        for (int c = 0; c < word.length(); c++) {
            patternForWord += chars[c];
        }
        return patternForWord;
    }
}

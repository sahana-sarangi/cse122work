// Sahana Sarangi
// 08/02/2024
// CSE 122
// C2
// TA: Abby & Connor
// This class finds the optimal ski resort in the PNW for the user to visit. It takes in the
// user's skill level, the location they want to visit, their crowd preference, their ideal budget,
// and the time of year they want to travel to give them the best ski resort to visit. 

import java.util.*;
import java.io.*;

public class Skilection {
    public static void main(String[] args) throws FileNotFoundException {
        
        File skiResortInfo = new File("SkiResorts.txt");
        if (skiResortInfo.length() - 1 == 0) {
            throw new IllegalArgumentException();
        }
        Scanner fileScan = new Scanner(skiResortInfo);
        Scanner console = new Scanner(System.in);

        System.out.println("Welcome to Skilection!");
        System.out.println();

        Map<String, List<String>> data = new HashMap<>();
        loadData(fileScan, data);
 
        String menuChoice = "";
        boolean loopRunning = true;
        while (loopRunning) {
            menu();
            menuChoice = console.nextLine();

            if (menuChoice.equalsIgnoreCase("q")) {
                loopRunning = false;
            } else if (menuChoice.equalsIgnoreCase("l")) {
                viewAllResorts(data);
            } else if (menuChoice.equalsIgnoreCase("m")) {
                
            } else if (menuChoice.equalsIgnoreCase("n")) {
                System.out.println("Which resort's details would you like to view?");
                String resortChoiceInput = console.nextLine();
                displayDetails(data, resortChoiceInput);
            } else if (menuChoice.equalsIgnoreCase("o")) {
                findSkiResort(console, data);
            } else {
                System.out.println("Your input was invalid. Please try again.");
            }
        }

    }

    //Behavior:
    // - This method prints out the initial menu options that the user chooses from.
    public static void menu() {
        System.out.println("Please select an option from the menu below!");
        System.out.println(" - (L) view all top ski resorts in the PNW");
        System.out.println(" - (O) answer questions to find your ski resort!");
        System.out.println(" - (M) print this menu again");
        System.out.println(" - (N) pick a resort and see its details");
        System.out.println(" - (Q) quit this program");

    }

    //Behavior:
    // - This method loads all the data from the input file that the program will use into
    //   a map. It maps all the ski resorts given to their specifications (ex. crowd, 
    //   terrain, location, etc.)
    //Paramters:
    // - fileScan: a scanner that scans over SkiResorts.txt
    // - data: a HashMap that maps ski resort names (strings) to their specifications (stored
    //         in ArrayLists).
    public static void loadData(Scanner fileScan, Map<String, List<String>> data) {
        while (fileScan.hasNextLine()) {
            String line = fileScan.nextLine();
            int resortEndIdx = line.indexOf("1") - 2;
            String resort = line.substring(0, resortEndIdx);
            int difficultyEndIdx = line.indexOf("2") - 2;
            String difficulty = line.substring(resortEndIdx + 5, difficultyEndIdx);
            int openingEndIdx = line.indexOf("3") - 2;
            String opening = line.substring(difficultyEndIdx + 5, openingEndIdx);
            int expenseEndIdx = line.indexOf("4") - 2;
            String expense = line.substring(openingEndIdx + 5, expenseEndIdx);
            int crowdEndIdx = line.indexOf("5") - 2;
            String crowd = line.substring(expenseEndIdx + 5, crowdEndIdx);
            int snowfallEndIdx = line.indexOf("6") - 2;
            String snowfall = line.substring(crowdEndIdx + 5, snowfallEndIdx);
            int locationEndIdx = line.indexOf("7") - 2;
            String location = line.substring(snowfallEndIdx + 5, locationEndIdx);
            String condition = line.substring(locationEndIdx + 5);
            data.put(resort, new ArrayList<String>());
            Collections.addAll(data.get(resort), difficulty, opening, expense, crowd, snowfall, 
                    location, condition);
        }
    }

    //Behavior:
    // - This method prints all the resorts present in the input file.
    //Paramaters:
    // - data: a HashMap that maps ski resort names (strings) to their specifications (stored
    //         in ArrayLists).
    public static void viewAllResorts(Map<String, List<String>> data) {
        for (String resort : data.keySet()) {
            System.out.println(resort);
        }
    }

    //Behavior: 
    //  - This method displays all the details/specifications of a ski resort that the user
    //    requests.
    //Exception:
    //  - If the user enters a ski resort that is not present in the input file, an 
    //    IllegalArgumentException is thrown.
    //Parameters:
    // - data: a HashMap that maps ski resort names (strings) to their specifications (stored
    //         in ArrayLists).
    // - resortChoiceInput: a String that contains the name of the resort that the user wants
    //                      to see details about.
    public static void displayDetails(Map<String, List<String>> data, String resortChoiceInput) {
        String resortChoice = resortChoiceInput.toLowerCase();

        if (!data.containsKey(resortChoice)) {
            throw new IllegalArgumentException("We don't recognize that resort! Please try again.");
        }

        System.out.println("The details of " + resortChoice + " ski resort are as follows:");
        for (int i = 0; i < data.get(resortChoice).size(); i++) {
                    System.out.println((i+1) + ") " + data.get(resortChoice).get(i));
        }

    }

    //Behavior:
    //  - This method calculates the optimal ski resort for the user to visit based on their 
    //    preferences. To do this, it uses a point system. Points are awarded to/subtracted from
    //    a ski resort depending on how well they match a user's preferences.
    //Parameters:
    //  - data: a HashMap that maps ski resort names (strings) to their specifications (stored
    //          in ArrayLists).
    //  - console: A Scanner that reads the user's input in the console.
    public static void findSkiResort(Scanner console, Map<String, List<String>> data) {
        Map<String, Double> points = new HashMap<>();

        calculateLocation(data, points, console);
        
        calculateSeason(data, points, console);
        
        calculateExpense(data, points, console);

        calculateCrowd(data, points, console);

        calculateSnowfallAndConditions(data, points);

        findResortWithMostPoints(data, points);
        
    }

    //Behavior:
    //  - This method prunes the ski resorts that can no longer be an optimal ski resort
    //    for the user depending on the location that the user wants to visit. All ski resorts
    //    that are not in the user's chosen location are pruned (not able to gain any points
    //    to become an optimal ski resort). If the user does not specify a location they want to
    //    visit and instead choose 'Anywhere,' no ski resorts are pruned.
    //Exception:
    //  - If the user names a location/input that is not 'Canada,' 'Washington,' 'Oregon,' 
    //    or 'Anywhere,' an IllegalArgumentException is thrown.
    //Parameters:
    //  - data: a HashMap that maps ski resort names (strings) to their specifications (stored
    //          in ArrayLists).
    //  - points: a HashMap that maps ski resort names (strings) to the number of points they have
    //            (stored as doubles).
    //  - console: A Scanner that reads the user's input in the console.
    public static void calculateLocation(Map<String, List<String>> data, 
            Map<String, Double> points, Scanner console) {
        System.out.println("Which location would you like to look for a resort in?");
        System.out.println(" - Canada");
        System.out.println(" - Washington");
        System.out.println(" - Oregon");
        System.out.println(" - Anywhere");
        String locationChoiceInput = console.nextLine();
        if (!locationChoiceInput.equalsIgnoreCase("canada") && 
                !locationChoiceInput.equalsIgnoreCase("washington") &&
                !locationChoiceInput.equalsIgnoreCase("oregon") && 
                !locationChoiceInput.equalsIgnoreCase("anywhere")) {
            throw new IllegalArgumentException("That is not a valid input.");
        }
        String locationChoice = locationChoiceInput.substring(0, 1).toUpperCase() + 
                locationChoiceInput.substring(1).toLowerCase();
        for (String eachResort : data.keySet()) {
            if (data.get(eachResort).contains(locationChoice) || 
                    locationChoice.equals("Anywhere")) {
                points.put(eachResort, 0.0);
            }
        }
    }

    //Behavior:
    //  - This method awards points to ski resorts if they are open during the time that the user
    //    wants to visit. All ski resorts are open during the winter and spring, so points are only
    //    awarded to ski resorts that are open in fall or summer and the user wants to visit
    //    in either fall or summer. If the user wants to visit in fall/summer and the resort is open
    //    during the season of the user's choice, then the resort is awarded 1 point.
    //Exception:
    //  - If the user enters a season/input that is not 'w,' 's,' 'f,' 'su,' or 'n' (non-case
    //    sensitively), then an IllegalArgumentException is thrown.
    //Parameters:
    //  - data: a HashMap that maps ski resort names (strings) to their specifications (stored
    //          in ArrayLists).
    //  - points: a HashMap that maps ski resort names (strings) to the number of points they have
    //            (stored as doubles).
    //  - console: A Scanner that reads the user's input in the console.
    public static void calculateSeason(Map<String, List<String>> data, Map<String, Double> points,
            Scanner console) {

        System.out.println("What season do you plan to vacation in?");
        System.out.println(" - (W) Winter");
        System.out.println(" - (S) Spring");
        System.out.println(" - (F) Fall");
        System.out.println(" - (Su) Summer");
        System.out.println(" - (N) Not sure");
        String seasonChoice = console.nextLine();
        if (!seasonChoice.equalsIgnoreCase("w") && !seasonChoice.equalsIgnoreCase("s") && 
                !seasonChoice.equalsIgnoreCase("f") && !seasonChoice.equalsIgnoreCase("su") && 
                !seasonChoice.equalsIgnoreCase("n")) {
            throw new IllegalArgumentException("That is not a valid input.");
        }
        for (String eachResort : points.keySet()) {
            if ((data.get(eachResort).contains("Open Fall") && seasonChoice.equalsIgnoreCase("f"))
                    || (data.get(eachResort).contains("Open all year") && 
                    seasonChoice.equalsIgnoreCase("su"))) {
                points.put(eachResort, 1.0);
            }
        }
    }

    //Behavior:
    //  - This method awards points to ski resorts depending on if they match the user's expense
    //    preference for their ski visit. If the ski resort matches the user's preference (either
    //    budget or luxury), then it is awarded one point. If the user doesn't specify a
    //    preference and selects 'n,' no points are awarded.
    //Exception:
    //  - If the user enters an input that is not 'b,' 'l,' or 'n,' an IllegalArgumentException
    //    is thrown.
    //Parameters:
    //  - data: a HashMap that maps ski resort names (strings) to their specifications (stored
    //          in ArrayLists).
    //  - points: a HashMap that maps ski resort names (strings) to the number of points they have
    //            (stored as doubles).
    //  - console: A Scanner that reads the user's input in the console.
    public static void calculateExpense(Map<String, List<String>> data, Map<String, Double> points,
            Scanner console) {
        System.out.println("Are you looking for a budget-friendly or luxury trip?");
        System.out.println(" - (B) Budget");
        System.out.println(" - (L) Luxury");
        System.out.println(" - (N) Not sure");
        String expense = console.nextLine();
        if (!expense.equalsIgnoreCase("b") && !expense.equalsIgnoreCase("l") && 
                !expense.equalsIgnoreCase("n")) {
            throw new IllegalArgumentException("That is not a valid input.");
        }
        for (String eachResort : points.keySet()) {
            if ((data.get(eachResort).contains("Budget") && expense.equalsIgnoreCase("b")) || 
                    (data.get(eachResort).contains("Luxury") && expense.equalsIgnoreCase("l"))) {
                double currPointCount = points.get(eachResort);
                points.put(eachResort, currPointCount + 1);
            }
        }
    }

    //Behavior:
    //  - This method awards and subtracts points based on the user's crowd preference. It asks
    //    the user how much the crowd at a ski resort matters to them (on a scale of 1-3). If
    //    crowd matters more to the user, then more points are deducted when the crowd at a
    //    ski resort is high. Points are awarded when the crowd at a ski resort is low and
    //    the user specifies that the crowd at a ski resort matters at least somewhat to them
    //    (they choose either 2 or 3 on the scale). If the user chooses 1, no points are awarded
    //    or deducted.
    //Parameters:
    //  - data: a HashMap that maps ski resort names (strings) to their specifications (stored
    //          in ArrayLists).
    //  - points: a HashMap that maps ski resort names (strings) to the number of points they have
    //            (stored as doubles).
    //  - console: A Scanner that reads the user's input in the console.
    public static void calculateCrowd(Map<String, List<String>> data, Map<String, Double> points,
            Scanner console) {

        System.out.println("On a scale of 1-3, how much does the crowd at a ski resort" +
                " to you? A '1' signifies very little weightage and a '3' siginifies" + 
                " significant weightage. Please enter a 1, 2, or 3.");
        String crowdLevelInput = console.nextLine();
        int crowdLevel = Integer.parseInt(crowdLevelInput);
        for (String eachResort : points.keySet()) {
            double currPointCount = points.get(eachResort);
            if ((crowdLevel == 2) && data.get(eachResort).contains("Less crowd")) {
                points.put(eachResort, currPointCount + 1.0);
            } else if ((crowdLevel == 2) && data.get(eachResort).contains("Okay crowd")) {
                points.put(eachResort, currPointCount + 0.5);
            } else if ((crowdLevel == 3) && data.get(eachResort).contains("Less crowd")) {
                points.put(eachResort, currPointCount + 1.0);
            } else if ((crowdLevel == 3) && data.get(eachResort).contains("Crowded")) {
                points.put(eachResort, currPointCount - 1.0);
            }
        }
    }

    //Behavior:
    //  - This method adds and subtracts points from ski resorts depending on how much snowfall
    //    they recieve and their weather conditions on most days. Resorts that have good
    //    snowfall are awarded one point, resorts that have low snowfall are deducated half a
    //    point, and resorts that have good conditions are awarded half a point.
    //Parameters:
    //  - data: a HashMap that maps ski resort names (strings) to their specifications (stored
    //          in ArrayLists).
    //  - points: a HashMap that maps ski resort names (strings) to the number of points they have
    //            (stored as doubles).
    public static void calculateSnowfallAndConditions(Map<String, List<String>> data,
            Map<String, Double> points) {
        for (String eachResort : points.keySet()) {
            double currPointCount = points.get(eachResort);
            if (data.get(eachResort).contains("Good snowfall")) {
                points.put(eachResort, currPointCount + 1);
            } else if (data.get(eachResort).contains("Sad snowfall")) {
                points.put(eachResort, currPointCount - 0.5);
            }
            if (data.get(eachResort).contains("Good conditions")) {
                points.put(eachResort, currPointCount + 0.5);
            }
        }
    }

    //Behavior:
    //  - This method finds the resort(s) that has the most points after all points have been
    //    awarded and deducted. The resort with the most amount of points is printed the console,
    //    along with all of its details. If multiple resorts are tied for the highest number of 
    //    points, then all of those results and their details are printed to the console.
    //Parameters:
    //  - data: a HashMap that maps ski resort names (strings) to their specifications (stored
    //          in ArrayLists).
    //  - points: a HashMap that maps ski resort names (strings) to the number of points they have
    //            (stored as doubles).
    public static void findResortWithMostPoints(Map<String, List<String>> data,
            Map<String, Double> points) {
        double maxPoints = 0;
        for (String eachResort : points.keySet()) {
            if (points.get(eachResort) > maxPoints) {
                maxPoints = points.get(eachResort);
            }
        }
        System.out.println("Here is the best/some of the best ski resorts for you to visit and" + 
                "their details.");
        for (String eachResort : points.keySet()) {
            if (points.get(eachResort) == maxPoints) {
                System.out.println();
                System.out.println(eachResort + ": ");
                for (int i = 0; i < data.get(eachResort).size(); i++) {
                    if (i != data.get(eachResort).size() - 1) {
                        System.out.print(data.get(eachResort).get(i) + ", ");
                    } else {
                        System.out.println(data.get(eachResort).get(i));
                    }
                }
            }
        }
    }
}



// Sahana Sarangi
// 07/26/2024
// CSE 122
// P1: Music Playlist
// TA: Abby & Connor
// This class allows the user to maintain a music playlist. The user is able to add songs
// to their playlist, play the next song in their playlist, view the history of songs that
// have been played, and delete some or all of their history. 

import java.util.*;
import java.io.*;

public class MusicPlaylist {
    
    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);

        Queue<String> playlist = new LinkedList<String>();
        Stack<String> history = new Stack<String>();

        System.out.println("Welcome to the CSE 122 Music Playlist!");

        boolean loopRunning = true;
        String menuChoice = "";
        while (loopRunning) {
            displayMenu();

            System.out.print("Enter your choice: ");
            menuChoice = console.nextLine();

            if (menuChoice.equalsIgnoreCase("q")) {
                loopRunning = false;

            } else if (menuChoice.equalsIgnoreCase("a")) {

                addSong(playlist, console);

            } else if (menuChoice.equalsIgnoreCase("p")) {

                playSong(playlist, history);

            } else if (menuChoice.equalsIgnoreCase("pr")) {

                printHistory(history);

            } else if (menuChoice.equalsIgnoreCase("c")) {

                history.clear();

            } else if (menuChoice.equalsIgnoreCase("d")) {

                System.out.println("A positive number will delete from recent history.");
                System.out.println("A negative number will delete from the beginning of history.");
                System.out.print("Enter number of songs to delete: ");

                String numToDeleteInput = console.nextLine();
                int numToDelete = Integer.parseInt(numToDeleteInput);

                deleteHistory(history, numToDelete);
                System.out.println();
            } 
        }
    }

    //Behavior:
    //  - This method displays the six options on the menu that the user can choose
    //    from when the program is run.
    public static void displayMenu() {
        System.out.println("(A) Add song");
        System.out.println("(P) Play song");
        System.out.println("(Pr) Print history");
        System.out.println("(C) Clear history");
        System.out.println("(D) Delete from history");
        System.out.println("(Q) Quit");
        System.out.println();
    }

    //Behavior:
    //  - This method adds the user's requested song to the end of the user's playlist queue.
    //Parameters:
    //  - playlist: a queue containing all the songs in the user's playlist
    //  - console: a Scanner that scans the user's input in the console
    public static void addSong(Queue<String> playlist, Scanner console) {
        System.out.print("Enter song name: ");
        String songToAdd = console.nextLine();
        playlist.add(songToAdd);
        System.out.println("Successfully added " + songToAdd);
    }

    //Behavior:
    //  - This method plays the song at the beginning of the user's playlist as long
    //    as the playlist is not empty. To play the song at the beginning of the 
    //    playlist, it removes the song from the playlist and tells the user that
    //    the song is playing. It then adds the played song to the user's song history.
    //Exception:
    //  - If the playlist is empty, an IllegalStateException is thrown.
    //Parameters:
    //  - playlist: a queue containing all the songs in the user's playlist
    //  - history: a stack containing all the songs that the user has played
    //             in reverse chronological order
    public static void playSong(Queue<String> playlist, Stack<String> history) {
        if (playlist.isEmpty()) {
            throw new IllegalStateException();
        }
        String songPlaying = playlist.remove();
        System.out.println("Playing song: " + songPlaying);
        history.push(songPlaying);
    }

    //Behavior:
    //  - This method prints the user's song history (all the songs the user has 
    //    played) in reverse chronological order.
    //Exception:
    //  - If the user has not played any songs, an IllegalStateException is thrown.
    //Parameters:
    //  - history: a stack containing all the songs that the user has played
    //             in reverse chronological order
    public static void printHistory(Stack<String> history) {
        if (history.isEmpty()) {
            throw new IllegalStateException();
        }

        Stack<String> aux = new Stack<String>();

        while (!history.isEmpty()) {
            String playedSong = history.pop();
            System.out.println("    " + playedSong);
            aux.add(playedSong);
        }
        stackToStack(aux, history);
    }

    //Behavior:
    //  - This method deletes some or all of the user's song history. If the user chooses
    //    a positive number, that number of songs are deleted from the user's history starting
    //    from the most recently-played songs. If the user selects a negative number, the 
    //    absolute value of that number of songs are deleted from the user's song history, 
    //    starting from the chronologically-first songs that the user played.
    //Exception:
    //  - If the absolute value of the number of songs the user wants to delete is greater
    //    than the number of songs they have played (or are in the user's history), an
    //    IllegalArgumentException is thrown.
    //Parameters:
    //  - history: a stack containing all the songs that the user has played
    //             in reverse chronological order
    //  - numToDelete: an integer value of the number of songs the user wants to delete, which
    //                 can be either positive or negative
    public static void deleteHistory(Stack<String> history, int numToDelete) {
        if (Math.abs(numToDelete) > history.size()) {
            throw new IllegalArgumentException();
        }

        if (numToDelete > 0) {
            for (int i = 0; i < numToDelete; i++) {
                history.pop();
            }
        } else if (numToDelete < 0) {
            Stack<String> aux = new Stack<String>();
            stackToStack(history, aux);
            for (int j = 0; j < Math.abs(numToDelete); j++) {
                aux.pop();
            }
            stackToStack(aux, history);
        }
    }

    //Behavior:
    //  - This method transfers all elements (specifically song names) in a stack into 
    //    another stack. The original stack is emptied and the new stack contains all 
    //    the song names that were in the original stack, but in reverse order.
    //Parameters:
    //  - stack1: the stack that song names are transferred out of
    //  - stack2: the stack that song names are transferred into
    public static void stackToStack(Stack<String> stack1, Stack<String> stack2) {
        while (!stack1.isEmpty()) {
            stack2.push(stack1.pop());
        }
    }
}

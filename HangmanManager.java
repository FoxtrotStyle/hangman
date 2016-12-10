//HangmanManager.java

import java.util.Set;
import java.util.SortedSet;

/**
 * A HangmanManager keeps track of the state of a game of hangman.
 * Methods provide access to the current set of guesses,
 * number of wrong guesses remaining, and the current pattern
 * to be displayed to the user.
 * The <CODE>record</CODE> method updates state by recording a new guess
 * and updating the other values appropriately.
 * <P>
 * Implementing classes must include a constructor of the following form:
 * <PRE>
 * public HangmanManager(final List<String> dictionary,
 *                       final int length,
 *                       final int max)
 * </PRE>
 * This constructs a HangmanManager by first determining the initial
 * set of words with the specified length from the dictionary
 * then selecting the goal.
 * <UL>
 * <LI>The dictionary parameter is a list of candidate goal words.</LI>
 * <LI>The length parameter is the length of acceptable candidate goal words.</LI>
 * <LI>The max parameter is the maximum number of wrong guesses permitted.</LI>
 * <LI>The constructor throws and IllegalArgumentException if length < 1.
 * or if max < 0.</LI>
 * </UL>
 * </P>
 *
 * @author Dr. Jody Paul
 * @version 1.1
 */
public interface HangmanManager  {
    /**
     * Accesses the set of candidate goal words.
     * @return the set of candidate goal words
     */
    Set<String> words();

    /**
     * Accesses the number of allowable wrong guesses.
     * @return the number of wrong guesses the user has left
     */
    int guessesLeft();

    /**
     * Accesses the set of letters already guessed by the user.
     * @return the current set of letters guessed by the user
     */
    SortedSet<Character> guesses();

    /**
     * Return the hangman-style display pattern of letters and dashes
     * appropriate to the current state based on the letters already
     * guessed and the goal.
     * @throws IllegalStateException if there is no goal word
     * @return the hangman-style pattern to be displayed to the user
     */
    String pattern();

    /**
     * Record state changes based on new letter guess.
     * @throws IllegalStateException if no guesses left or no goal word
     * @throws IllegalArgumentException if letter is already guessed
     * @param guess the letter being guessed
     *   [Precondition: must be lower-case letter]
     *   [Precondition: must not be among letters already guessed]
     * @return the number of occurrences of the guessed letter in the goal
     */
    int record(char guess);
}

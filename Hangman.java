//Hangman.java
import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * Class Hangman is a devious implementation of the HangmanManager interface.
 * Hangman selects an intital dictionary at construction. Throughout the game,
 * the dictionary gets changed depending on the guesses the player makes. The
 * dictionary that gets chosen is the one that is the largest depending on the
 * guess. Hangman returns the characters guessed, the collection of words it's
 * using, and number of guesses left.
 * @author Brian Sherer
 * @version 1.0
 */
public class Hangman implements HangmanManager {

    /** Holds the words that are chosen. */
    private List<String> dictionaryOfWords;

    /** Holds number of guesses left. */
    private int guessesLeft;

    /** Holds sorted Characters that have been guessed. */
    private TreeSet<Character> guesses;

    /** Holds pattern for goal word. */
    private String pattern;

    /**
     * This constructor is passed a dictionary of words, a target word length,
     * and the maximum number of wrong guesses the player is allowed to make.
     * @param dictionary the dictionary of words to choose from
     * @param length the length of desired goal word
     * @param max the number of guesses
     * @throws IllegalArgumentException if length is less than 1 or if
     *                                  max is less than 0.
     */
    public Hangman(final List<String> dictionary, final int length,
                                                  final int max)
        throws IllegalArgumentException {
        if (length < 1 || max < 0) {
            throw new IllegalArgumentException();
        }
        dictionaryOfWords = dictionary.stream()
                                      .filter(s -> s.length() == length)
                                      .collect(Collectors.toList());
        pattern = getInitialPattern(length);
        guessesLeft = max;
        guesses = new TreeSet<Character>();
    }

    /**
     * Returns current set of words being considered by the hangman class.
     * @return set of words being considered
     */
    public Set<String> words() {
        return new HashSet<String>(dictionaryOfWords);
    }

    /**
     * Returns how many guesses the player has left.
     * @return how many guesses
     */
    public int guessesLeft() {
        return guessesLeft;
    }

    /**
     * Returns the current set of letters that have been guessed by the user.
     * @return the current set of guesses
     */
    public SortedSet<Character> guesses() {
        return guesses;
    }

    /**
     * Returns the current pattern to be displayed for the hangman game,
     * taking into account the goal and guesses that have been made.
     * Letters that have not yet been guessed are displayed as a dash and
     * there are spaces separating the symbols. There is no leading or
     * trailing spaces.
     * @return the current pattern
     * @throws IllegalStateException if the internal set is empty.
     */
    public String pattern() throws IllegalStateException {
        if (dictionaryOfWords.isEmpty()) {
            throw new IllegalStateException();
        }
        return pattern;
    }

    /**
     * Return the number of occurrences of the guessed letter in the new
     * pattern and updates the number of guesses left.
     * @param guess the char being guessed
     * @return the number of occurences of guess
     * @throws IllegalStateException if the list of words is empty or
     *                               guesses left is not at least 1.
     * @throws IllegalArgumentException if the list of words is nonempty
     *                                  and the character being guessed
     *                                  had been guessed previously.
     */
    public int record(final char guess) throws IllegalStateException,
                                               IllegalArgumentException {
        if (guessesLeft < 1 || words().isEmpty()) {
            throw new IllegalStateException();
        }
        if (guesses().contains(guess) && !words().isEmpty()) {
            throw new IllegalArgumentException();
        }
        Map<String, ArrayList<String>> map = getPatternMap(dictionaryOfWords,
                                                           guess);
        pattern = getPatternOfLargestList(map);
        dictionaryOfWords = map.get(pattern);
        //Shuffle the list for output when it selects a word.
        Collections.shuffle(dictionaryOfWords);
        int count = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (guess == pattern.charAt(i)) {
                    count++;
            }
        }
        if (count == 0) {
            guessesLeft--;
        }
        guesses.add(guess);
        return count;
    }

    /**
     * Returns a pattern of all dashes depending on the length.
     * @param length length of the pattern
     * @return the pattern of the word.
     */
    private String getInitialPattern(final int length) {
        String newPattern = "";
        for (int i = 0; i < length; i++) {
            newPattern += "- ";
        }
        newPattern.trim();
        return newPattern;
    }

    /**
     * Returns the pattern of a String with the selected letter and
     * letters in guesses revealed.
     * @param word the word to convert into a pattern
     * @param letter the letter to reveal in pattern if it's in the
     *        goal word.
     * @return the pattern of word.
     */
    private String getPattern(final String word, final Character letter) {
        String newPattern = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                newPattern += letter + " ";
            } else if (guesses.contains(word.charAt(i))) {
                newPattern += word.charAt(i) + " ";
            } else {
                newPattern += "- ";
            }
        }
        newPattern.trim();
        return newPattern;
    }

    /**
     * Utility method that takes a List and Character and generates a Map
     * that links a pattern with all words that match that pattern.
     * @param list the list to break up by pattern.
     * @param letter the character that's being added to the pattern.
     * @return the map that conatins patterns as keys.
     */
    private Map<String, ArrayList<String>> getPatternMap(
                                                     final List<String> list,
                                                     final Character letter) {
        HashMap<String, ArrayList<String>> dictionaryMap = new HashMap<>();
        for (String word : list) {
            String newPattern = getPattern(word, letter);
            if (!dictionaryMap.containsKey(newPattern)) {
                dictionaryMap.put(newPattern, new ArrayList<String>());
                dictionaryMap.get(newPattern).add(word);
            } else {
                dictionaryMap.get(newPattern).add(word);
            }
        }
        return dictionaryMap;
    }

    /**
     * Utility method that returns the pattern key of the largest list.
     * @param map the map to go through
     * @return the pattern associated with the largest list
     */
    private String getPatternOfLargestList(
                                    final Map<String, ArrayList<String>> map) {
        String newPattern = "";
        ArrayList<String> largestDictionary = new ArrayList<>();
        for (String mapKey : map.keySet()) {
            if (map.get(mapKey).size() > largestDictionary.size()) {
                largestDictionary = map.get(mapKey);
                newPattern = mapKey;
            }
        }
        return newPattern;
    }
}

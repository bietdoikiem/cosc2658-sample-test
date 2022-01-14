import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RMITLetterUtility {
  public static void main(String[] args) {
    System.out.println("=== RMIT Search Problem ===");
    // Init letter array
    char[][] letters = {
        { 'A', 'R', 'N', 'U', 'R', 'M', 'U', 'V' },
        { 'X', 'L', 'N', 'U', 'T', 'M', 'J', 'C' },
        { 'A', 'Q', 'N', 'H', 'I', 'U', 'V', 'H' },
        { 'A', 'R', 'O', 'U', 'R', 'G', 'U', 'I' },
        { 'B', 'R', 'N', 'L', 'R', 'M', 'U', 'T' }
    };
    // Init utility
    RMITLetterUtility util = new RMITLetterUtility();
    // Problem 1
    System.out.println("+Problem 1: Scan letters for RMIT and their positions");
    ArrayList<RMITLetter> rmitLetters = util.scan(letters);
    // Store the scan results in utility class
    util.rmitLetters = rmitLetters;
    System.out.println(rmitLetters);
    // Problem 2
    System.out.println("+Problem 2: Is Connectable?");
    System.out.println(util.canConnect(util.rmitLetters.get(1), util.rmitLetters.get(4))); // true
    System.out.println(util.canConnect(util.rmitLetters.get(2), util.rmitLetters.get(8))); // false
    // Problem 3
    System.out.println("+Problem 3: Can find RMIT letters?");
    System.out.println(util.canFindRMIT());
  }

  /// Properties ///
  ArrayList<RMITLetter> rmitLetters;

  public RMITLetterUtility() {
  }

  /**
   * Scan through list of letters to find and construct 'R', 'M', 'I', 'T' letter
   * with their positions in the 2D array
   * 
   * @param letters
   * @return
   */
  public ArrayList<RMITLetter> scan(char[][] letters) {
    ArrayList<RMITLetter> list = new ArrayList<>();
    for (int i = 0; i < letters.length; i++) {
      for (int j = 0; j < letters[0].length; j++) {
        if (letters[i][j] == 'R' || letters[i][j] == 'M' || letters[i][j] == 'I' || letters[i][j] == 'T') {
          list.add(new RMITLetter(letters[i][j], i, j));
        }
      }
    }
    return list;
  }

  /**
   * Can the first letter connects the second letter?
   * 
   * @param l1 First RMITLetter
   * @param l2 Second RMITLetter
   * @return True if connectable || Else False
   */
  public boolean canConnect(RMITLetter l1, RMITLetter l2) {
    if ((l1.letter == 'R' && l2.letter == 'M') || (l1.letter == 'M' && l2.letter == 'I')
        || (l1.letter == 'I' && l2.letter == 'T')) {
      AtomicInteger steps = new AtomicInteger(0);
      return recursiveConnectable(l1.row, l1.col, steps, l1, l2);
    }
    return false;
  }

  /**
   * Recursively check for the necessary steps to reach from the 1st letter to the
   * 2nd letter
   * 
   * @param x1
   * @param y1
   * @param steps
   * @param l1
   * @param l2
   * @return
   */
  private boolean recursiveConnectable(int x1, int y1, AtomicInteger steps, RMITLetter l1, RMITLetter l2) {
    // If exceeded search space
    if (x1 > l2.row || y1 > l2.col) {
      return false;
    }
    // Exclude starting point
    if (!(x1 == l1.row && y1 == l1.col)) {
      steps.incrementAndGet();
    }
    // If the answer is found
    if (x1 == l2.row && y1 == l2.col && steps.get() <= 4) {
      return true;
    }
    if (recursiveConnectable(x1 + 1, y1, steps, l1, l2)) {
      return true;
    }
    if (recursiveConnectable(x1, y1 + 1, steps, l1, l2)) {
      return true;
    }
    // Backtrack
    steps.decrementAndGet();
    return false;
  }

  /**
   * Can you find the possible RMIT word in the 2D array
   * 
   * @return True if Found || False if NOT
   */
  public boolean canFindRMIT() {
    // Filter and Get all 'R' letters
    ArrayList<RMITLetter> RLetters = new ArrayList<>();
    this.rmitLetters.forEach(l -> {
      if (l.letter == 'R')
        RLetters.add(l);
    });
    // Filter and Get all 'T' letters
    ArrayList<RMITLetter> TLetters = new ArrayList<>();
    this.rmitLetters.forEach(l -> {
      if (l.letter == 'T')
        TLetters.add(l);
    });
    // Filter
    ArrayList<RMITLetter> filteredList = new ArrayList<>(this.rmitLetters);
    filteredList.removeIf(l -> l.letter == 'R');
    // Init solution array
    ArrayList<RMITLetter> solution = new ArrayList<>();
    for (RMITLetter r : RLetters) {
      solution.add(r);
      findWordRecursively(r, solution, TLetters, filteredList);
      if (solution.size() == 4) {
        System.out.println("Solution: " + solution);
        return true;
      }
      solution.clear();
    }
    return false;
  }

  /**
   * Find the RMIT word in 2D array recursively
   * 
   * @param currentLetter
   * @param solution
   * @param TLetters
   * @param filteredList
   * @return
   */
  private boolean findWordRecursively(RMITLetter currentLetter, ArrayList<RMITLetter> solution,
      ArrayList<RMITLetter> TLetters, ArrayList<RMITLetter> filteredList) {
    // If the answer has been found
    if (solution.size() == 4) {
      return true;
    }
    // For-loop check possible combination
    ArrayList<RMITLetter> possibleList = new ArrayList<>(filteredList);
    possibleList.removeIf(l -> !isValidRMITOrder(l.letter, solution));
    for (RMITLetter l : possibleList) {
      if (canConnect(currentLetter, l)) {
        solution.add(l);
        if (findWordRecursively(solution.get(solution.size() - 1), solution, TLetters, filteredList)) {
          return true;
        }
      }
    }
    // Backtrack invalid path
    if (solution.size() > 0) {
      solution.remove(solution.size() - 1);
    }
    return false;
  }

  /**
   * Check if the appended character is valid to be added to the current sequence
   * for a complete RMIT word
   * 
   * @param letter Letter to be appended
   * @param list   Current sequence
   * @return True || False
   */
  private boolean isValidRMITOrder(char letter, ArrayList<RMITLetter> list) {
    if (list.size() == 1 && letter != 'M')
      return false;
    if (list.size() == 2 && letter != 'I')
      return false;
    if (list.size() == 3 && letter != 'T')
      return false;
    return true;
  }
}

class RMITLetter {
  char letter;
  int row;
  int col;

  public RMITLetter(char letter, int row, int col) {
    this.letter = letter;
    this.row = row;
    this.col = col;
  }

  @Override
  public String toString() {
    return String.format("%c (%d, %d)", this.letter, this.row, this.col);
  }
}

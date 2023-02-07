import java.util.*;

// code to check if the opponent can play a "trap"

public class Board {

  private static final String[] winners = {"036", "147", "258", "012", "345", "678", "048", "246"};
  // designates winning values - represents a horzontal, vertical, or diagonal three in a row
  
  private int[][] status;
  // a 3x3 array that controls what the game board looks like
  
  private int botNum;
  // represents what the AI's team is (X or O)

  private int playerNum;
  // represents the player's team
  
  private boolean turn;
  // determines if its the computer's move or not

  public boolean gameIsOver;
  // determines if the game is over already

  private int moves;
  // counts the number of moves that have been made

  private boolean opening;
  private boolean otherOpening;
  // controls if the bot uses the opening that it is programmed with

  private int corner;
  // only for if the opening case is done

  private ArrayList<Integer> playerMoves;
  // saves the players moves in case they are needed in a method or something

  private String openingMethod;
  // saves what way to go on the opening

  private boolean angryMode;
  private boolean niceMode;
  // determines if the bot is mean or nice

  // constructor for board - makes all the values what they need to be
  public Board (boolean t, boolean a, boolean n) {

    // makes all the defaults in the board a -1 if nobody has played in it yet
    this.status = new int[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        this.status[i][j] = -1;
      }
    }

    // determines who goes first - if the bot goes first, it uses the opening
    if (t) { // change back to 0.5 after work done with opening
      this.turn = true;
      this.botNum = 1;
      this.playerNum = 0;
      this.opening = false;
      if (Math.random() < 0.5) {
        this.opening = true;
      }
      this.otherOpening = !this.opening;
    }
    else {
      this.turn = false;
      this.botNum = 0;
      this.playerNum = 1;
      this.opening = false;
      this.otherOpening = false;
    } 

    // more initialization
    this.gameIsOver = false;
    this.moves = 1;
    this.playerMoves = new ArrayList<Integer>();
    this.angryMode = a;
    this.niceMode = n;
  }

  // prints the result and does trash talk
  public int[] printResult(int[] record) {
    int result = findWinner();
    if (result == -1) {
      record[1]++;
    }
    else if (result == botNum) {
      record[2]++;
    }
    else {
      record[0]++;
    }

    // prints the board and one of the trash talk prompts
    System.out.println(this);
    System.out.println("Player: " + record[0] + "  Draw: " + record[1] + "  Bot: " + record[2] + "\n");
    if (this.angryMode) {
      if (result == -1) {
        System.out.println(TrashTalks.draw[(int) (Math.random() * TrashTalks.draw.length)] + "\n");
      }
      else if (result == botNum) {
        System.out.println(TrashTalks.win[(int) (Math.random() * TrashTalks.win.length)] + "\n");
      }
      else {
        System.out.println(TrashTalks.loss[(int) (Math.random() * TrashTalks.loss.length)] + "\n");
      }
    }
    else if (this.niceMode) {
      if (result == -1) {
        System.out.println(NiceTalks.draw[(int) (Math.random() * NiceTalks.draw.length)] + "\n");
      }
      else if (result == botNum) {
        System.out.println(NiceTalks.win[(int) (Math.random() * NiceTalks.win.length)] + "\n");
      }
      else {
        System.out.println(NiceTalks.loss[(int) (Math.random() * NiceTalks.loss.length)] + "\n");
      }
    }
    // updates the record in the main function
    return record;
  }
  
  // checks to see if the game is over or not
  public void isOver() {
    // checks if the board is filled - if it is, the game is over
    boolean found = false;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (this.status[i][j] == -1) {
          found = true;
        }
      }
    }
    if (!found) {
      this.gameIsOver = true;
      return;
    }

    // checks to see if there is a winner; if there is, the game is over
    if (findWinner() != -1) {
      this.gameIsOver = true;
      return;
    }
    else {
      this.gameIsOver = false;
      return;
    }
  }
  public int findWinner() {
    String game = "";
    for (int[] lis : this.status) {
      for (int num : lis) {
        if (num == -1) {
          game += "2";
        }
        else {
          game += num;
        }
      }
    }
    for (String combo : this.winners) {
      if (("" + game.charAt(numOf(combo.charAt(0)))).equals("" + game.charAt(numOf(combo.charAt(1)))) && ("" + game.charAt(numOf(combo.charAt(0)))).equals("" + game.charAt(numOf(combo.charAt(2))))) {
        if (("" + game.charAt(numOf(combo.charAt(0)))).equals("1")) {
          return 1;
        }
        else if (("" + game.charAt(numOf(combo.charAt(0)))).equals("0")) {
          return 0;
        }
        else {
          continue;
        }
      }
    }
    return -1;
  }
  public String toString() {
    // design for board from caleb
    
    String output = "";
    for (int i = 0; i < this.status.length - 1; i++) {
      output += getString(this.status[i]) + "\n-----------\n";
    }
    output += getString(this.status[this.status.length - 1]);
    return "\n" + output + "\n";
  }
  private String getString(int[] nums) {
    String output = "";
    for (int num : nums) {
      if (num == 0) {
        output += " O ";
      }
      else if (num == 1) {
        output += " X ";
      }
      else {
        output += "   ";
      }
      output += "|";
    }
    return output.substring(0, output.length() - 1);
  }
  public boolean isTurn() {
    return this.turn;
  }
  public void playerPlace(int inp) {
    inp -= 1;
    
    int y = inp / 3;
    int x = inp % 3;
    // method to determine coords from caleb
    
    if (this.status[y][x] == -1) {
      this.status[y][x] = this.playerNum;
      this.moves++;
    }
    else {
      if (angryMode) {
        System.out.println("It's not that hard to select a square that has not been taken, stupid.");
      }
      else {
        System.out.println("Please select a square in which there is not already a piece.");
      }
      return;
    }

    playerMoves.add(inp + 1);

    if (inp == 4) {
      this.opening = false;
    }
    if (moves == 3 && (inp == 0 || inp == 2 || inp == 6 || inp == 8)) {
      this.otherOpening = false;
    }
    
    this.turn = !this.turn;
  } 
  public void botPlace() {
    // initializes a donut of values - this is done to put all the potentailly adjacent pieces next to one another 
    /*
  `  0 | 1 | 2
    -----------
     7 |   | 3
    -----------
     6 | 5 | 4
    */
    // these are the indexes of each square on the grid - index 3 in this string corresponds to square 6
    // if indexes 2, 3, 4 are touching, then its a win - this is squares 3, 6, 9
    String squares = "12369874";

    // check for winning moves and immediately return when the mve is made
    if (winOppurtunity() != -1) {
      placeAt(winOppurtunity());
      nextTurn();
      return;
    }
    // if the opening is being done, execute these statements
    if (this.opening) {
      // if the first move is the bots, pick a random corner
      if (moves == 1) {
        this.corner = numOf(squares.charAt(((int) (Math.random() * 4)) * 2));
        placeAt(this.corner);
        nextTurn();
        return;
      }

      // if the player did not choose the middle square, execute different variations of the opening
      else if (moves == 3 && this.status[1][1] == -1) {
        if (this.angryMode) {
          System.out.println("You lost already.");
        }
        else if (this.niceMode) {
          System.out.println("You fell for the opening!");
        }
        // test for if the input is an edge that is not touching the corner
        if (oppositeEdgeCase(squares)) {
          this.openingMethod = "Edges";
          // determines if the edge is 3 in front of or 3 behind the corner in the donut
          if ((squares.indexOf("" + corner) + 3) % 8  == squares.indexOf("" + playerMoves.get(0))) {
            // if its 3 in front, place a piece at the corner that is 2 in front
            placeAt(numOf(squares.charAt((squares.indexOf("" + corner) + 2) % 8)));
          }
          else {
            // otherwise place one that is 2 behind (6 in front to avoid negative values)
            placeAt(numOf(squares.charAt((squares.indexOf("" + corner) + 6) % 8)));
          }
          nextTurn();
          return;
        }
        // test for if the input is an edge that is touching the corner
        else if (adjacentEdgeCase(squares)) {
          this.openingMethod = "Edges";
          // sees if it is an edge in front of the piece
          if ((squares.indexOf("" + corner) + 1) % 8  == squares.indexOf("" + playerMoves.get(0))) {
            // if so place a corner that is 2 behind (6 in front)
            placeAt(numOf(squares.charAt((squares.indexOf("" + corner) + 6) % 8)));
          }
          else {
            // otherwise place a corner that is 2 in front
            placeAt(numOf(squares.charAt((squares.indexOf("" + corner) + 2) % 8)));
          }
          nextTurn();
          return;
        }
        // check other corner cases
        else if (oppositeCornerCase(squares)) {
          this.openingMethod = "Opposite Corner";
          placeAt(numOf(squares.charAt((squares.indexOf("" + corner) + 2) % 8)));
          nextTurn();
          return;
        }
        else {
          this.openingMethod = "Adjacent Corner";
          placeAt(adjacentCornerCase(squares));
          nextTurn();
          return;
        }
      }
      else if (moves == 5) {
        // if the players first move was an edge, go at the middle and end the opening method because the next move should be a win
        if (angryMode) {
          System.out.println("Maybe if you weren't such an idiot, the game would be going better for you.");
        }
        if (this.openingMethod.equals("Edges")) {
          placeAt(5); 
          nextTurn();
          this.opening = false;
          return;
        }
        else if (this.openingMethod.equals("Opposite Corner")) {
          placeAt(numOf(squares.charAt((squares.indexOf("" + corner) + 6) % 8)));
          nextTurn();
          this.opening = false;
          return;
        }
        else {
          placeAt(numOf(squares.charAt((squares.indexOf("" + corner) + 4) % 8)));
          nextTurn();
          this.opening = false;
          return;
        } 
      }
    }
    else if (this.otherOpening) {
      if (this.moves == 1) {
        placeAt(5);
        nextTurn();
        return;
      }
      else if (this.moves == 3) {
        placeAt(numOf(squares.charAt((placedEdgeIndex(squares) + 3) % 8)));
        nextTurn();
        this.otherOpening = false;
        return;
      }
    }
    else {
      if (getValue(5) == -1) {
        placeAt(5);
        nextTurn();
        return;
      }
      else if (moves == 2 && getValue(5) == playerNum) {
        placeAt(numOf(squares.charAt((int) (Math.random() * 4) * 2)));
        nextTurn();
        return;
      }
      else if (fixTheCase(squares)) {
        placeAt(numOf(squares.charAt((int) (Math.random() * 4) * 2 + 1)));
        nextTurn();
        return;
      }
      else if (findTraps(squares) != -1) {
        placeAt(findTraps(squares));
        nextTurn();
        return;
      }
      else {
        placeAt(mostTouches(squares));
        nextTurn();
        return;
      }
    }
  }
  private int mostTouches(String squares) { // fix when the size of lists is 0
    // corner with most touches
    ArrayList<Integer> cornerCounts = new ArrayList<Integer>();
    ArrayList<Integer> corners = new ArrayList<Integer>();    
    for (int i = 0; i <= 6; i += 2) {
      int current = numOf(squares.charAt(i));
      if (getValue(current) == -1) {
        int count = 0;
        // checks the edge in front of the corner
        int frontBorderValue = getValue(numOf(squares.charAt((i + 1) % 8)));
        int backBorderValue = getValue(numOf(squares.charAt((i + 7) % 8)));
        if (frontBorderValue != -1) {
          count++;
          if (frontBorderValue == playerNum) {
            count++;
          }
        }
        // checks the edge behind the corner
        if (backBorderValue != -1) {
          count++;
          if (backBorderValue == playerNum) {
            count++;
          }
        }
        cornerCounts.add(count);
        corners.add(current);
      }
    }

    int maxCorner = 0;
    if (cornerCounts.size() > 0) {
      maxCorner = Collections.max(cornerCounts);
    }
    if (cornerCounts.size() != 0 && maxCorner >= 2) {
      return corners.get(indexOf(cornerCounts, maxCorner));
    }

    ArrayList<Integer> edgeCounts = new ArrayList<Integer>();
    ArrayList<Integer> edges = new ArrayList<Integer>();    
    for (int i = 1; i <= 7; i += 2) {
      int current = numOf(squares.charAt(i));
      if (getValue(current) == -1) {
        int count = 0;
        // checks the edge in front of the corner
        int frontBorderValue = getValue(numOf(squares.charAt((i + 1) % 8)));
        int backBorderValue = getValue(numOf(squares.charAt((i + 7) % 8)));
        if (frontBorderValue != -1) {
          count++;
          if (frontBorderValue == playerNum) {
            count++;
          }
        }
        // checks the edge behind the corner
        if (backBorderValue != -1) {
          count++;
          if (backBorderValue == playerNum) {
            count++;
          }
        }
        edgeCounts.add(count);
        edges.add(current);
      }
    }
    int maxEdge = 0;
    if (edgeCounts.size() > 0) {
      maxEdge = Collections.max(edgeCounts);
    }
    if (edgeCounts.size() != 0 && maxEdge >= 2) {
      return edges.get(indexOf(edgeCounts, maxEdge));
    }
    else if (corners.size() != 0) {
      return corners.get(indexOf(cornerCounts, maxCorner));
    }
    else {
      return edges.get(indexOf(edgeCounts, maxEdge));
    }
  }
  private int winOppurtunity() {
    // determine is there is a place to go that has the possiblity of a win
    int out = -1;
    for (String combo : this.winners) {
      int index1 = Integer.parseInt("" + combo.charAt(0)) + 1;
      int index2 = Integer.parseInt("" + combo.charAt(1)) + 1;
      int index3 = Integer.parseInt("" + combo.charAt(2)) + 1;
      int spot1 = getValue(index1);
      int spot2 = getValue(index2);
      int spot3 = getValue(index3); // prioritize personal win over opponent block
      if (spot1 == spot2 && spot1 != -1 && spot3 == -1) {
        if (spot1 == botNum) {
          return index3;
        }
        else {
          out = index3;
        }
      }
      else if (spot2 == spot3 && spot2 != -1 && spot1 == -1) {
        if (spot2 == botNum) {
          return index1;
        }
        else {
          out = index1;
        }
      }
      else if (spot1 == spot3 && spot1 != -1 && spot2 == -1) {
        if (spot1 == botNum) {
          return index2;
        }
        else {
          out = index2;
        }
      }
    }
    return out;
  }
  private void nextTurn() {
    this.turn = !this.turn;
    this.moves++;
  } 
  private void placeAt(int num) {
    num -= 1;

    int y = num / 3;
    int x = num % 3;

    this.status[y][x] = this.botNum; 
  }
  private int getValue(int num) {
    num -= 1;
    return this.status[num / 3][num % 3];
  }
  private boolean oppositeEdgeCase (String squares) {
    // goes through every edge on the board 12369874
    for (int i = 1; i <=7 ; i += 2) {
      int edge = numOf(squares.charAt(i));
      if ((getValue(edge) == playerNum && !(squares.contains(edge + "" + this.corner) || squares.contains(this.corner + "" + edge)) && !(edge == 4 && this.corner == 1))) {
        return true;
      }
    }
    return false;
  }
  private boolean adjacentEdgeCase (String squares) {
    for (int i = 1; i <= 7; i += 2) {
      int edge = numOf(squares.charAt(i));
      if (getValue(edge) == playerNum && ((squares.contains("" + edge + corner)) || squares.contains("" + corner + edge) || (edge == 4 && this.corner == 1))) {
        return true;
      }
    }
    return false;
  }
  private boolean oppositeCornerCase (String squares) {
    int oppositeCorner = numOf(squares.charAt((squares.indexOf(this.corner + "") + 4) % 8));
    if (getValue(oppositeCorner) == playerNum) {
      return true;
    }
    return false;
  }
  private int adjacentCornerCase(String squares) {
    int corner1 = numOf(squares.charAt((squares.indexOf(this.corner + "") + 2) % 8));
    int corner2 = numOf(squares.charAt((squares.indexOf(this.corner + "") + 6) % 8));
    if (getValue(corner1) == playerNum) {
      return corner2;
    }
    else {
      return corner1;
    }
  }
  private int numOf(char inp) {
    return Integer.parseInt("" + inp);
  }
  private static int indexOf(ArrayList<Integer> nums, int num) {
    for (int i = 0; i < nums.size(); i++) {
      if (nums.get(i) == num) {
        return i;
      }
    }
    return -1;
  }
  private int findTraps(String squares) {
    for (int i = 0; i < squares.length(); i++) {
      int botWinCount = 0;
      int playerWinCount = 0;
      int place = numOf(squares.charAt(i));
      if (getValue(place) == -1) {
        place--;
        for (String winningCombo : this.winners) {
          String combo = new String(winningCombo);
          if (combo.contains("" + place)) {
            combo = combo.replaceAll("" + place, "");
            if (getValue(numOf(combo.charAt(0)) + 1) == -1 && getValue(numOf(combo.charAt(1)) + 1) != -1) {
              if (getValue(numOf(combo.charAt(1)) + 1) == botNum) {
                botWinCount++;
              }
              else {
                playerWinCount++;
              }
            }
            else if (getValue(numOf(combo.charAt(1)) + 1) == -1 && getValue(numOf(combo.charAt(0)) + 1) != -1) {
              if (getValue(numOf(combo.charAt(0)) + 1) == botNum) {
                botWinCount++;
              }
              else {
                playerWinCount++;
              }
            }
            else {
              continue;
            }
          }
        }
        if (botWinCount >= 2) {
          return place + 1;
        }
        else if (playerWinCount >= 2) {
          return place + 1;
        }
      }
      else {
        continue;
      }
    }
    return -1;
  }
  private boolean fixTheCase(String squares) {
    if (moves == 4 && getValue(5) == botNum) {
      for (int i = 0; i <= 6 ; i += 2) {
        if (getValue(numOf(squares.charAt(i))) == getValue(numOf(squares.charAt((i + 4) % 8))) && getValue(numOf(squares.charAt(i))) == playerNum) {
          return true;
        } 
      }
    }
    return false;
  }
  private int placedEdgeIndex(String squares) {
    for (int i = 1; i <= 7; i += 2) {
      if (getValue(numOf(squares.charAt(i))) == playerNum) {
        return i;
      }
    }
    return -1;
  }
}
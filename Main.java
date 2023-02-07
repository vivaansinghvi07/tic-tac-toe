import java.util.Scanner;
import java.util.concurrent.TimeUnit;

// fix the losing case

public class Main {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    System.out.println("\nWelcome to Tic-Tac-Toe! When it's your turn, place your piece by typing a number according to the following reference. Each number corresponds to where it goes on the board:\n\n 1 | 2 | 3 \n-----------\n 4 | 5 | 6 \n-----------\n 7 | 8 | 9 \n\nRead the instructions and press enter when you are ready to begin.");
    scan.nextLine();
    int[] record = {0, 0, 0};
    boolean t; 
    if (Math.random() < 0.5) {
      t = true;
    }
    else {
      t = false;
    }
    boolean angry = false;
    boolean nice = false;
    System.out.print("Do you want the bot to chat to you? (y/n): ");
    String chat = scan.next();
    scan.nextLine();
    if (chat.equalsIgnoreCase("y")) {
      System.out.print("Do you want the board to be on angry mode (a) or nice mode (n)? (a/n): ");
      String a = scan.next();
      scan.nextLine();
      if (a.equalsIgnoreCase("a")) {
        angry = true;
        nice = false;
      }
      else if (a.equalsIgnoreCase("n")) {
        angry = false;
        nice = true;
      }
    }
    else {
      angry = false;
      nice = false;
    }
    while (true) {
      Board board = new Board(t, angry, nice);
      while (!board.gameIsOver) {
        if (board.isTurn()) {
          try {
            System.out.println(board);
            TimeUnit.SECONDS.sleep(1);
            System.out.println("My turn.");
            TimeUnit.SECONDS.sleep(1);
            board.botPlace();
          }
          catch (InterruptedException ex) {
            ex.printStackTrace();
          }
        }
        else {
          System.out.println(board);
          System.out.print("Your move. Where would you like to place your piece? ");
          board.playerPlace(scan.nextInt());
          scan.nextLine();
        }
        board.isOver();
      }
      record = board.printResult(record);
      System.out.print("Would you like to play again (y/n)? ");
      String again = scan.next();
      scan.nextLine();
      t = !t;
      if (again.equalsIgnoreCase("n")) {
        break;
      }
    }
    scan.close();
  }
}
/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

/**
 * This class is a model for TicTacToe.
 * 
 * This game adheres to a Model-View-Controller design framework. This framework
 * is very effective for turn-based games. We STRONGLY recommend you review
 * these lecture slides, starting at slide 8, for more details on
 * Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * This model is completely independent of the view and controller. This is in
 * keeping with the concept of modularity! We can play the whole game from start
 * to finish without ever drawing anything on a screen or instantiating a Java
 * Swing object.
 * 
 * Run this file to see the main method play a game of TicTacToe, visualized
 * with Strings printed to the console.
 */
public class ConnectFour {

    private static final int RED = -1;
    private static final int EMPTY = 0;
    private static final int BLACK = 1;
    private int[][] board;
    private int turn; // true if player 1's turn, false otherwise
    private int winner;

    /**
     * Constructor sets up game state.
     */
    public ConnectFour() {
        reset();
    }

    public ConnectFour(ConnectFour game) {
        board = game.getBoard();
        turn = game.getTurn();
        winner = game.winner;
    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is taken or
     * after the game has ended. If the turn is successful and the game has not
     * ended, the player is changed. If the turn is unsuccessful or the game has
     * ended, the player is not changed.
     * 
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean playMove(int col) {
        if (winner != 2) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            if (board[col][i] == EMPTY) {
                board[col][i] = turn;
                turn *= -1;
                winner = checkWinner();
                return true;
            }
        }
        return false;
    }

    /**
     * checkWinner checks whether the game has reached a win condition. checkWinner
     * only looks for horizontal wins.
     * 
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2 has
     *         won, 3 if the game hits stalemate
     */
    public int checkWinner() {
        int res = checkDirection(0, 4, 0, 6, 1, 0); // checks if there is a horizontal winning line
        if (res != 0) {
            return res;
        }
        res = checkDirection(0, 7, 0, 3, 0, 1); // checks if there is a vertical winning line
        if (res != 0) {
            return res;
        }
        res = checkDirection(0, 4, 0, 3, 1, 1); // checks for diagonal, upward sloping winning line
        if (res != 0) {
            return res;
        }
        res = checkDirection(0, 4, 3, 6, 1, -1); // checks for diagonal, downward sloping win
        if (res != 0) {
            return res;
        }

        if (checkTie()) {
            return 0;
        }

        return 2;

        // check horizontal win
    }

    private int checkDirection(int xl, int xu, int yl, int yu, int dx, int dy) {
        for (int i = xl; i < xu; i++) {
            for (int j = yl; j < yu; j++) {
                int sqr = board[i][j];
                if (sqr != EMPTY && board[i + dx][j + dy] == sqr && board[i + 2 * dx][j + 2 * dy] 
                        == sqr && board[i + 3 * dx][j + 3 * dy] == sqr) {
                    return sqr;
                }
            }
        }
        return EMPTY;
    }

    private boolean checkTie() {
        for (int i = 0; i < 7; i++) {
            if (board[i][5] == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * printGameState prints the current game state for debugging.
     */
    public void printGameState() {
        for (int row = 5; row >= 0; row--) {
            for (int col = 0; col < 6; col++) {
                System.out.print(board[col][row]);
                System.out.print("\t|\t");
            }
            System.out.println(board[6][row]);
            if (row > 0) {
                System.out.println("--------------------------------------------------------------"
                        + "------------------------------------");
            }
        }
        System.out.println();
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[7][6];
        turn = BLACK;
        winner = 2;
    }

    public int[][] getBoard() {
        int[][] res = new int[7][6];
        for (int i = 0; i < 7; i++) {
            res[i] = board[i].clone();
        }
        return res;
    }

    /**
     * getTurn is a getter for the player whose turn it is in the game.
     * 
     * @return true if it's Player 1's turn, false if it's Player 2's turn.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     * 
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public int getCell(int c, int r) {
        return board[c][r];
    }

    public int getWinner() {
        return winner;
    }

    /**
     * This main method illustrates how the model is completely independent of the
     * view and controller. We can play the game from start to finish without ever
     * creating a Java Swing object.
     * 
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     * 
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        ConnectFour t = new ConnectFour();

        t.playMove(0);
        t.playMove(1);
        t.playMove(0);
        t.playMove(1);
        t.playMove(0);
        t.playMove(1);
        t.playMove(0);
        t.playMove(1);
        t.printGameState();
        System.out.println();
        System.out.println();
        System.out.println("Winner is: " + t.checkWinner());
    }
}

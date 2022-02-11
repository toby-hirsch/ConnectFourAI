
/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This framework
 * is very effective for turn-based games. We STRONGLY recommend you review
 * these lecture slides, starting at slide 8, for more details on
 * Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with its
 * paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private ConnectFour game; // model for the game
    private JLabel status; // current status text
    private int playerTurn;
    private ConnectFourAI ai = new MiddleMonteCarlo(7, 0.1);

    // Game constants
    public static final int CELL_SIZE = 100;
    public static final int BOARD_WIDTH = 7 * CELL_SIZE;
    public static final int BOARD_HEIGHT = 6 * CELL_SIZE;

    private static final int PADDING = 10;
    private static final int TILE_LEN = CELL_SIZE - 2 * PADDING;
    private static final int RED = -1;
    private static final int EMPTY = 0;
    private static final int BLACK = 1;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key
        // listener.
        setFocusable(true);

        game = new ConnectFour(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game board based
         * off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (game.getTurn() == playerTurn) {
                    Point p = e.getPoint();
                    game.playMove(p.x / 100);
                    updateStatus();
                    repaint();

                    SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            game.playMove(ai.findMove(game));
                            return null;
                        }

                        @Override
                        protected void done() {
                            updateStatus();
                            repaint();
                        }
                    };
                    sw.execute();

                }
            }
        });

    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset(int pTurn) {
        updateAI();
        playerTurn = pTurn;
        game.reset();
        status.setText("Black's turn");
        repaint();

        if (pTurn == RED) {
            SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    game.playMove(ai.findMove(game));
                    return null;
                }

                @Override
                protected void done() {
                    updateStatus();
                    repaint();
                }
            };
            sw.execute();
        }

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void updateAI() {
        ai.update();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (game.getTurn() == 1) {
            status.setText("Black's Turn");
        } else {
            status.setText("Red's Turn");
        }

        int winner = game.getWinner();
        if (winner == 1) {
            status.setText("Black wins!");
        } else if (winner == -1) {
            status.setText("Red wins!");
        } else if (winner == 0) {
            status.setText("It's a tie");
        }
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach will not be
     * sufficient for most games, because it is not modular. All of the logic for
     * drawing the game board is in this method, and it does not take advantage of
     * helper methods. Consider breaking up your paintComponent logic into multiple
     * methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        for (int i = CELL_SIZE; i < BOARD_WIDTH; i += CELL_SIZE) {
            g.drawLine(i, 0, i, 600);
        }

        for (int i = CELL_SIZE; i < BOARD_HEIGHT; i += CELL_SIZE) {
            g.drawLine(0, i, 700, i);
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                int state = game.getCell(i, j);
                if (state == RED) {
                    g.setColor(Color.RED);
                    g.fillOval(PADDING + CELL_SIZE * i, BOARD_HEIGHT + PADDING - 
                            CELL_SIZE * (j + 1), TILE_LEN, TILE_LEN);
                } else if (state == BLACK) {
                    g.setColor(Color.BLACK);
                    g.fillOval(PADDING + CELL_SIZE * i, BOARD_HEIGHT + PADDING - 
                            CELL_SIZE * (j + 1), TILE_LEN, TILE_LEN);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
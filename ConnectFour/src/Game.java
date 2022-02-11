
/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This framework
 * is very effective for turn-based games. We STRONGLY recommend you review
 * these lecture slides, starting at slide 8, for more details on
 * Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view, implements a
 * bit of controller functionality through the reset button, and then
 * instantiates a GameBoard. The GameBoard will handle the rest of the game's
 * view and controller functionality, and it will instantiate a TicTacToe object
 * to serve as the game's model.
 */
public class Game implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("ConnectFour");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                board.updateAI();
                e.getWindow().dispose();
            }
        });

        final JButton playBlack = new JButton("Play as black");
        final JButton playRed = new JButton("Play as red");
        final JButton instr = new JButton("How to play");
        playBlack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset(1);
            }
        });
        playRed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset(-1);
            }
        });
        final String instructions = "Welcome to Connect Four! Click either play as black or play "
                + "as red,\nand the computer will play against you. It can take a little while to "
                + "come up with its moves at first,\nbut it quickly caches common board states and "
                + "gets much faster.";
        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, instructions, "How to play", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        control_panel.add(playBlack);
        control_panel.add(playRed);
        control_panel.add(instr);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset(1);
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements
     * specified in Game and runs it. IMPORTANT: Do NOT delete! You MUST include
     * this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
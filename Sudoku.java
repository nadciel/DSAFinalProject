import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
/**
 * The main Sudoku program
 */
public class Sudoku extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning
    private Timer timer;
    private int seconds;
    private Object mainPanel;
    Input input = new Input(9);

    // private variables
    GameBoardPanel board = new GameBoardPanel();
    // button New Game
    JButton btnNewGame = new JButton("New Game");
    //timer
    JLabel timerLabel = new JLabel("Timer: 0 Seconds");

    public static void quitActionPerformed(ActionEvent evt){
        System.exit(0);
    }

    // Constructor
    public Sudoku() {
        //Pesan pembuka
        JOptionPane.showMessageDialog(null, "Welcome to Sudoku! Click OK to start the game.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        //menginisialisasi layout frame
        cp.add(board, BorderLayout.CENTER);

        //Add a button to the south to re-start the game via board.newGame()
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnNewGame);
        buttonPanel.add(timerLabel);
        cp.add(buttonPanel, BorderLayout.SOUTH);

        //membuat menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem quit = new JMenuItem("Quit");
        setJMenuBar(menuBar);

        //Menambahkan menu bar
        menuBar.add(menu);
        menu.add(quit);

        //Menambahkan action listener
        btnNewGame.addActionListener(e -> startNewGame());

        //Menambahkan action listener untuk quit game
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Sudoku.quitActionPerformed(e);
            }
        });

        // Initialize the game board to start the game and timer
        board.newGame();
        initializeTimer();
        startTimer();
        board.newGame();

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    //Method untuk start new game
    private void startNewGame(){
        restartTimer();
        board.newGame();
    }

    //Method untuk menginisialisasi timer
    private void initializeTimer(){
        seconds = 0;
        timer = new Timer(1000, e ->{seconds++; updateTimerLabel();});
    }

    // Method untuk memulai timer
    private void startTimer(){
        timer.start();
    }

    // Method untuk restart timer
    private void restartTimer(){
        timer.stop();
        seconds = 0;
        updateTimerLabel();
        timer.start();
    }

    // Method untuk update timer
    private void updateTimerLabel(){
        timerLabel.setText("Timer: " + seconds + " seconds");
    }

    /** The entry main() entry method */
    public static void play() {
        // [TODO 1] Check "Swing program template" on how to run
        //  the constructor of "SudokuMain"
        new Sudoku();
    }
}
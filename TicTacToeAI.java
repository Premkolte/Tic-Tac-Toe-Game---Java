import java.util.*;

public class TicTacToeAI {
    static char[][] board;
    static char currentPlayer = 'X';
    static boolean useSmartAI = false; // Switch between AI modes
    static int boardSize; // Size of the board
    static int winCondition; // Number of consecutive marks required to win

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Board size selection
        System.out.println("Welcome to Tic Tac Toe (Single Player Mode)!");
        System.out.print("Enter the board size (e.g., 4 for 4x4, 5 for 5x5): ");
        while (!sc.hasNextInt()) { // Check if the next input is an integer
            System.out.println("Invalid input! Please enter an integer.");
            sc.next(); // Clear the invalid input
        }
        boardSize = sc.nextInt();
        winCondition = boardSize; // Set win condition based on board size

        // Initialize the board
        initializeBoard();

        // Choose AI mode
        System.out.println("Select AI mode:");
        System.out.println("1. Random AI (Beginner)");
        System.out.println("2. Smart AI (Advanced)");
        System.out.print("Enter your choice (1 or 2): ");
        while (!sc.hasNextInt()) { // Check if the next input is an integer
            System.out.println("Invalid input! Please enter 1 or 2.");
            sc.next(); // Clear the invalid input
        }
        int choice = sc.nextInt();
        useSmartAI = (choice == 2);

        System.out.println("Player: X | AI: O");
        printBoard();

        while (true) {
            if (currentPlayer == 'X') { // Player's turn
                System.out.println("Player X, it's your turn.");
                System.out.print("Enter row (1-" + boardSize + ") and column (1-" + boardSize + "): ");
                int row = sc.nextInt() - 1;
                int col = sc.nextInt() - 1;

                if (isValidMove(row, col)) {
                    board[row][col] = 'X';
                    printBoard();
                    if (isWinner('X')) {
                        System.out.println("Congratulations! Player X wins! 🎉");
                        break;
                    }
                    if (isBoardFull()) {
                        System.out.println("It's a draw! 🤝");
                        break;
                    }
                    currentPlayer = 'O'; // Switch to AI
                } else {
                    System.out.println("Invalid move! Try again.");
                }
            } else { // AI's turn
                System.out.println("AI (O) is making a move...");
                if (useSmartAI) {
                    makeBestMove(); // Smart AI
                } else {
                    makeRandomMove(); // Random AI
                }
                printBoard();
                if (isWinner('O')) {
                    System.out.println("AI wins! Better luck next time. 🤖");
                    break;
                }
                if (isBoardFull()) {
                    System.out.println("It's a draw! 🤝");
                    break;
                }
                currentPlayer = 'X'; // Switch back to Player
            }
        }
        sc.close();
    }

    // Function to initialize the board
    public static void initializeBoard() {
        board = new char[boardSize][boardSize]; // Create a board with dynamic size
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = 'X'; // Player starts first
    }

    // Function to print the board with improved UI
    public static void printBoard() {
        // System.out.println("\n=========================");
        System.out.print("   ");
        for (int i = 1; i <= boardSize; i++) {
            System.out.print(i + "   ");
        }
        System.out.println();
        System.out.println("  =======================");

        for (int i = 0; i < boardSize; i++) {
            System.out.print(i + 1 + " |");
            for (int j = 0; j < boardSize; j++) {
                System.out.print(board[i][j] + " |");
            }
            System.out.println();
            System.out.println("  =======================");
        }
    }

    // Function to check if a move is valid
    public static boolean isValidMove(int row, int col) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize && board[row][col] == ' ';
    }

    // AI: Function to make a random move
    public static void makeRandomMove() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(boardSize); // Random row (0 to boardSize-1)
            col = rand.nextInt(boardSize); // Random column (0 to boardSize-1)
        } while (!isValidMove(row, col)); // Repeat until a valid move is found

        board[row][col] = 'O'; // AI plays as 'O'
    }

    // AI: Function to make the best move using Minimax
    public static void makeBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1, bestCol = -1;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'O'; // AI makes a move
                    int score = minimax(board, false);
                    board[i][j] = ' '; // Undo the move

                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }

        board[bestRow][bestCol] = 'O'; // AI plays its best move
    }

    // Minimax function for AI
    public static int minimax(char[][] board, boolean isMaximizing) {
        if (isWinner('O')) return 10; // AI win
        if (isWinner('X')) return -10; // Player win
        if (isBoardFull()) return 0; // Draw

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O'; // AI makes a move
                        bestScore = Math.max(bestScore, minimax(board, false));
                        board[i][j] = ' '; // Undo the move
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X'; // Player makes a move
                        bestScore = Math.min(bestScore, minimax(board, true));
                        board[i][j] = ' '; // Undo the move
                    }
                }
            }
            return bestScore;
        }
    }

    // Function to check if a player has won
    public static boolean isWinner(char player) {
        // Check rows and columns
        for (int i = 0; i < boardSize; i++) {
            if (checkLine(player, i, 0, 0, 1)) return true; // Row check
            if (checkLine(player, 0, i, 1, 0)) return true; // Column check
        }

        // Check diagonals
        if (checkLine(player, 0, 0, 1, 1)) return true;
        if (checkLine(player, 0, boardSize - 1, 1, -1)) return true;

        return false;
    }

    // Function to check a line for a winner (row, column, or diagonal)
    public static boolean checkLine(char player, int startRow, int startCol, int deltaRow, int deltaCol) {
        int count = 0;
        for (int i = 0; i < winCondition; i++) {
            int row = startRow + i * deltaRow;
            int col = startCol + i * deltaCol;
            if (row >= 0 && row < boardSize && col >= 0 && col < boardSize && board[row][col] == player) {
                count++;
            }
        }
        return count == winCondition;
    }

    // Function to check if the board is full
    public static boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }
}
import java.util.Scanner;
import java.util.Random;

public class TicTacToe {

    public static boolean isEmpty(char[][] board, int i, int j) {
        return board[i][j] == ' ';
    }

    public static boolean isDraw(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isBoardFull(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (isEmpty(board, i, j)) {
                    return false; // Board is not full
                }
            }
        }
        return true; // Board is full
    }

    public static int[] minimax(char[][] board, boolean maximizingPlayer, int depth) {
        if (isWinner(board)) {
            return new int[]{(maximizingPlayer) ? -1 : 1, -1, -1}; // Return score along with move
        }

        if (isBoardFull(board) || depth == 0) {
            return new int[]{0, -1, -1}; // Return score along with move
        }

        int bestScore = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = {-1, -1};

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (isEmpty(board, i, j)) {
                    char currentPiece = maximizingPlayer ? 'O' : 'X';
                    board[i][j] = currentPiece;

                    int score = minimax(board, !maximizingPlayer, depth - 1)[0]; // Decrease depth in recursive call
                    board[i][j] = ' '; // Undo the move

                    if ((maximizingPlayer && score > bestScore) || (!maximizingPlayer && score < bestScore)) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        bestMove[0] = bestMove[0] == -1 ? 0 : bestMove[0]; // Default to 0 if no valid move found
        bestMove[1] = bestMove[1] == -1 ? 0 : bestMove[1];

        return new int[]{bestScore, bestMove[0], bestMove[1]};
    }

    // Modified function to make the bot move using minimax
    public static void botsMove(char[][] board, char botsPiece, int depth) {
        printBoard(board);

        boolean maximizingPlayer = (botsPiece == 'O') ? true : false;

        int[] bestMove = minimax(board, maximizingPlayer, depth); // true for maximizing player (bot)

        int bestMoveI = bestMove[1];
        int bestMoveJ = bestMove[2];

        if (bestMoveI != -1 && bestMoveJ != -1) {
            board[bestMoveI][bestMoveJ] = botsPiece;
        }

        // Check for winner or draw after bot's move
        if (isWinner(board)) {
            printBoard(board);
            System.out.println("BOT WINS");
            System.exit(0);

        } else if (isDraw(board)) {
            printBoard(board);
            System.out.println("DRAW");
            System.exit(0);
        }
    }

    public static boolean isWinner(char[][] board) {
        // check rows and cols
        for (int i = 0; i < board.length; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return true;
            } else if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return true;
            }
        }

        // check diag
        if (board[1][1] != ' ') {
            if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                return true;
            } else if (board[2][0] == board[1][1] && board[1][1] == board[0][2]) {
                return true;
            }
        }

        return false;
    }

    public static void makeMove(char[][] board, char piece, int playerNumber) {
        printBoard(board);

        System.out.println("Player " + playerNumber);
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter position x and y (separate them with space): ");
        String input = sc.nextLine();
        String[] position = input.split(" ");
        int i = Integer.parseInt(position[0]);
        int j = Integer.parseInt(position[1]);

        if (isValidMove(board, i, j)) {
            board[i][j] = piece;
        } else {
            System.out.println("Invalid position");
            makeMove(board, piece, playerNumber);
        }

        if (isWinner(board)) {
            printBoard(board);
            System.out.println("Player " + playerNumber + " wins");
            System.exit(0);
        }

        if (isDraw(board)) {
            printBoard(board);
            System.out.println("DRAW");
            System.exit(0);
        }
    }

    public static boolean isValidMove(char[][] board, int i, int j) {
        return (i >= 0 && i <= 2 && j >= 0 && j <= 2 && isEmpty(board, i, j));
    }

    public static void printBoard(char[][] board) {
        int n = board.length;

        System.out.println("- - - - - - -");
        for (int i = 0; i < n; i++) {
            System.out.print("| ");
            for (int j = 0; j < n; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("- - - - - - -");
        }
    }

    public static boolean isGameOver(char[][] board) {
        return isWinner(board) || isDraw(board);
    }

    private static boolean isFirstMove = true;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        char[][] board = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
        printBoard(board);

        System.out.println("Enter");
        System.out.println("1. PLAYER VS BOT");
        System.out.println("2. PLAYER VS PLAYER");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:

                int depth;
                System.out.println("Choose difficulty level:");
                System.out.println("1. Easy");
                System.out.println("2. Hard");
                System.out.println("3. Impossible");
                int difficultyChoice = sc.nextInt();

                switch (difficultyChoice) {
                    case 1:
                        depth = 1; // Easy
                        break;
                    case 2:
                        depth = 2; // Hard
                        break;
                    case 3:
                        depth = -1; // Impossible -> no restrictions on no of reccursion call
                        break;
                    default:
                        System.out.println("Invalid difficulty choice. Setting to easy.");
                        depth = 2;
                        break;
                }

                System.out.println("Choose X or O .... X goes first");
                char playerPiece = sc.next().charAt(0);
                char botPiece = (playerPiece == 'X') ? 'O' : 'X';

                while (!isGameOver(board)) {
                    if (playerPiece == 'X') {
                        makeMove(board, 'X', 1);
                        botsMove(board, botPiece, depth);
                    } else {
                        
                        if (isFirstMove) {
                            Random random = new Random();
                            int randomI = random.nextInt(3);
                            int randomJ = random.nextInt(3);
                            board[randomI][randomJ] = 'X';
                            isFirstMove = false;
                            makeMove(board, 'O', 1);
                        } else {
                            botsMove(board, 'X', depth);
                            makeMove(board, 'O', 1);
                        }
                    }
                }
                break;

            case 2:
                while (!isGameOver(board)) {
                    makeMove(board, 'X', 1);
                    makeMove(board, 'O', 2);
                }
                break;

            default:
                System.out.println("Invalid choice");
                break;
        }
    }
}

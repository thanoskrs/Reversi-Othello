import java.awt.*;
import java.util.ArrayList;


public class Board {

    public static final int X = 1;
    public static final int O = -1;
    public static final int EMPTY = 0;

    private Move lastMove;
    private int lastLetterPlayed;

    private int[][] gameBoard;

    public Board(int playsFirst) {
        lastMove = new Move();
        lastLetterPlayed = -playsFirst;
        gameBoard = new int[8][8];

        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                gameBoard[i][j] = EMPTY;
            }
        }

        // default 4 first moves
        gameBoard[3][3] = O;
        gameBoard[3][4] = X;
        gameBoard[4][3] = X;
        gameBoard[4][4] = O;

    }

    public Board(Board board)
    {
        lastMove = board.lastMove;
        lastLetterPlayed = board.lastLetterPlayed;
        gameBoard = new int[8][8];

        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                gameBoard[i][j] = board.gameBoard[i][j];
            }
        }
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    public int getLastLetterPlayed() {
        return lastLetterPlayed;
    }

    public void setLastLetterPlayed(int lastLatterPlayed) {
        this.lastLetterPlayed = lastLatterPlayed;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(int[][] gameBoard) {
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                this.gameBoard[i][j] = gameBoard[i][j];
            }
        }
    }

    public void makeMove(int row, int col, int letter) {
        gameBoard[row][col] = letter;
        changeLetters(row, col, letter);
        lastMove = new Move(row, col);
        lastLetterPlayed = letter;
    }

    public boolean isValidMove(int row, int col, int letterPlaying) {

        if (gameBoard[row][col] != EMPTY)
            return false;

        // we use function y = -x. So, if X=1 plays we look for -X=-1. Otherwise we look for -O=-(-1)=1.
        int requiredLetter = -letterPlaying;

        // up
        if (row <= 1) {
            if (checkDown(row, col, letterPlaying)) return true;

            // up left
            if (col <= 1) {
                if (checkRight(row, col, letterPlaying)) return true;
                if (checkDownRight(row, col, letterPlaying)) return true;
            }
            // up right
            if (col >= 6) {
                if (checkLeft(row, col, letterPlaying)) return true;
                if (checkDownLeft(row, col, letterPlaying)) return true;
            }
            // up middle
            if (col > 1 && col < 6) {
                if (checkRight(row, col, letterPlaying)) return true;
                if (checkDownRight(row, col, letterPlaying)) return true;
                if (checkDownLeft(row, col, letterPlaying)) return true;
                if (checkLeft(row, col, letterPlaying)) return true;
            }

            // down
        } else if (row >= 6) {
            if (checkUp(row, col, letterPlaying)) return true;

            // down left
            if (col <= 1) {
                if (checkUpRight(row, col, letterPlaying)) return true;
                if (checkRight(row, col, letterPlaying)) return true;
            }
            // down right
            if (col >= 6) {
                if (checkUpLeft(row, col, letterPlaying)) return true;
                if (checkLeft(row, col, letterPlaying)) return true;
            }
            // down middle
            if (col > 1 && col < 6) {
                if (checkUpRight(row, col, letterPlaying)) return true;
                if (checkRight(row, col, letterPlaying)) return true;
                if (checkUpLeft(row, col, letterPlaying)) return true;
                if (checkLeft(row, col, letterPlaying)) return true;
            }
            // middle
        } else {
            if (checkUp(row, col, letterPlaying)) return true;
            if (checkDown(row, col, letterPlaying)) return true;


            // middle left
            if (col <= 1) {
                if (checkUpRight(row, col, letterPlaying)) return true;
                if (checkRight(row, col, letterPlaying)) return true;
                if (checkDownRight(row, col, letterPlaying)) return true;
            }
            // middle right
            if (col >= 6) {
                if (checkUpLeft(row, col, letterPlaying)) return true;
                if (checkLeft(row, col, letterPlaying)) return true;
                if (checkDownLeft(row, col, letterPlaying)) return true;
            }
            // middle
            if (col > 1 && col < 6) {
                if (checkUpRight(row, col, letterPlaying)) return true;
                if (checkRight(row, col, letterPlaying)) return true;
                if (checkDownRight(row, col, letterPlaying)) return true;
                if (checkUpLeft(row, col, letterPlaying)) return true;
                if (checkLeft(row, col, letterPlaying)) return true;
                if (checkDownLeft(row, col, letterPlaying)) return true;
            }
        }

        return false;
    }

    public ArrayList<Board> getChildren(int letter) {

        ArrayList<Board> children = new ArrayList<>();
        Board child;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (gameBoard[row][col] == EMPTY) {
                    if (isValidMove(row, col, letter)) {
                        child = new Board(this);
                        child.makeMove(row, col, letter);
                        children.add(child);
                    }
                }
            }
        }
        //System.out.println("Children: " + children.size());
        return children;
    }

    public int evaluate() {
        int sum = 0;
        int sumOfX = 0;
        int sumOfO = 0;

        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                sum = sum + 2 * gameBoard[i][j];

                if (gameBoard[i][j] == X)
                    sumOfX++;
                if (gameBoard[i][j] == O)
                    sumOfO++;

                if (i == 0 || i == 7)
                    sum += 25 * gameBoard[i][j];

                if (j == 0 || j == 7)
                    sum += 25 * gameBoard[i][j];
            }
        }

        if (isTerminal() && sumOfX > sumOfO)
            sum += 1000;
        if (isTerminal() && sumOfX < sumOfO)
            sum -= 1000;

        return sum;
    }

    public boolean isTerminal() {


        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (gameBoard[row][col] == EMPTY) {
                    if (isValidMove(row, col, X) || isValidMove(row, col, O)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void print() {

        System.out.println();

        System.out.print("  ");
        for (int i=0; i <8; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        for (int row=0; row<8; row++) {
            System.out.print(row + " |");
            for (int col=0; col<8; col++) {
                switch (gameBoard[row][col])
                {
                    case X:
                        System.out.print("X|");
                        break;
                    case O:
                        System.out.print("O|");
                        break;
                    case EMPTY:
                        System.out.print("-|");
                        break;
                }
            }
            System.out.print("\n");

        }
        System.out.println();

    }

    public Graphics print(Graphics graphics) {

        int row;
        for (row=0; row<8; row++) {
            for (int col=0; col<8; col++) {
                switch (gameBoard[row][col])
                {
                    case X:
                        graphics.setColor(Color.BLACK);
                        graphics.fillOval(col * 64 + 7, row * 64 + 7, 50, 50);
                        break;
                    case O:
                        graphics.setColor(Color.WHITE);
                        graphics.fillOval(col * 64 + 7, row * 64 + 7, 50, 50);
                        break;
                    case EMPTY:
                        break;
                }


            }
            graphics.setColor(Color.BLACK);
            graphics.drawLine(0, row * 64, 512, row * 64);
            graphics.drawLine(row * 64, 0, row * 64, 512);
        }
        graphics.drawLine(0, row * 64, 512, row * 64);
        graphics.drawLine(row * 64, 0, row * 64, 512);
        return graphics;
    }

    public boolean canMakeMove(int letterPlaying) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (gameBoard[row][col] == EMPTY) {
                    if (isValidMove(row, col, letterPlaying)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int winner() {
        int sum = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                sum += gameBoard[row][col];
            }
        }

        return sum;
    }

    private void changeLetters(int row, int col, int letter) {


        if (row <= 1) {
            if (checkDown(row, col, letter)) {
                turnLettersDown(row, col, letter);
            }
            // up left
            if (col <= 1) {
                if (checkRight(row, col, letter)) {
                    turnLettersRight(row, col, letter);
                }
                if (checkDownRight(row, col, letter)) {
                    turnLettersDownRight(row, col, letter);
                }
            }
            // up right
            if (col >= 6) {
                if (checkLeft(row, col, letter)) {
                    turnLettersLeft(row, col, letter);
                }
                if (checkDownLeft(row, col, letter)) {
                    turnLettersDownLeft(row, col, letter);
                }
            }
            // up middle
            if (col > 1 && col < 6) {
                if (checkRight(row, col, letter)) {
                    turnLettersRight(row, col, letter);
                }
                if (checkDownRight(row, col, letter)) {
                    turnLettersDownRight(row, col, letter);
                }
                if (checkDownLeft(row, col, letter)) {
                    turnLettersDownLeft(row, col, letter);
                }
                if (checkLeft(row, col, letter)) {
                    turnLettersLeft(row, col, letter);
                }
            }

            // down
        } else if (row >= 6) {
            if (checkUp(row, col, letter)) {
                turnLettersUp(row, col, letter);
            }

            // down left
            if (col <= 1) {
                if (checkUpRight(row, col, letter)) {
                    turnLettersUpRight(row, col, letter);
                }
                if (checkRight(row, col, letter)) {
                    turnLettersRight(row, col, letter);
                }
            }
            // down right
            if (col >= 6) {
                if (checkUpLeft(row, col, letter)) {
                    turnLettersUpLeft(row, col, letter);
                }
                if (checkLeft(row, col, letter)) {
                    turnLettersLeft(row, col, letter);
                }
            }
            // down middle
            if (col > 1 && col < 6) {
                if (checkUpRight(row, col, letter)) {
                    turnLettersUpRight(row, col, letter);
                }
                if (checkRight(row, col, letter)) {
                    turnLettersRight(row, col, letter);
                }
                if (checkUpLeft(row, col, letter)) {
                    turnLettersUpLeft(row, col, letter);
                }
                if (checkLeft(row, col, letter)) {
                    turnLettersLeft(row, col, letter);
                }
            }
            // middle
        } else {
            if (checkUp(row, col, letter)) {
                turnLettersUp(row, col, letter);
            }
            if (checkDown(row, col, letter)) {
                turnLettersDown(row, col, letter);
            }

            // middle left
            if (col <= 1) {
                if (checkUpRight(row, col, letter)) {
                    turnLettersUpRight(row, col, letter);
                }
                if (checkRight(row, col, letter)) {
                    turnLettersRight(row, col, letter);
                }
                if (checkDownRight(row, col, letter)) {
                    turnLettersDownRight(row, col, letter);
                }
            }
            // middle right
            if (col >= 6) {
                if (checkUpLeft(row, col, letter)) {
                    turnLettersUpLeft(row, col, letter);
                }
                if (checkLeft(row, col, letter)) {
                    turnLettersLeft(row, col, letter);
                }
                if (checkDownLeft(row, col, letter)) {
                    turnLettersDownLeft(row, col, letter);
                }
            }
            // middle
            if (col > 1 && col < 6) {
                if (checkUpRight(row, col, letter)) {
                    turnLettersUpRight(row, col, letter);
                }
                if (checkRight(row, col, letter)) {
                    turnLettersRight(row, col, letter);
                }
                if (checkDownRight(row, col, letter)) {
                    turnLettersDownRight(row, col, letter);
                }
                if (checkUpLeft(row, col, letter)) {
                    turnLettersUpLeft(row, col, letter);
                }
                if (checkLeft(row, col, letter)) {
                    turnLettersLeft(row, col, letter);
                }
                if (checkDownLeft(row, col, letter)) {
                    turnLettersDownLeft(row, col, letter);
                }
            }

        }
    }

    private boolean checkRight(int row, int col, int letterPlaying) {
        int lastCol = col;

        for (int i = col + 1; i <= 7; i++) {
            lastCol = i;
            if (gameBoard[row][i] != -letterPlaying) {
                break;
            }
        }
        if (gameBoard[row][lastCol - 1] == -letterPlaying) {
            if (gameBoard[row][lastCol] == letterPlaying)
                return true;
        }

        return false;
    }

    private boolean checkLeft(int row, int col, int letterPlaying) {
        int lastCol = col;

        for (int i = col - 1; i >= 0; i--) {
            lastCol = i;
            if (gameBoard[row][i] != -letterPlaying) {
                break;
            }
        }
        if (gameBoard[row][lastCol + 1] == -letterPlaying) {
            if (gameBoard[row][lastCol] == letterPlaying)
                return true;
        }

        return false;
    }

    private boolean checkUp(int row, int col, int letterPlaying) {
        int lastRow = row;

        for (int i = row - 1; i >= 0; i--) {
            lastRow = i;
            if (gameBoard[i][col] != -letterPlaying) {
                break;
            }
        }
        if (gameBoard[lastRow + 1][col] == -letterPlaying) {
            if (gameBoard[lastRow][col] == letterPlaying)
                return true;
        }

        return false;
    }

    private boolean checkDown(int row, int col, int letterPlaying) {
        int lastRow = row;

        for (int i = row + 1; i <= 7; i++) {
            lastRow = i;
            if (gameBoard[i][col] != -letterPlaying) {
                break;
            }
        }
        if (gameBoard[lastRow - 1][col] == -letterPlaying) {
            if (gameBoard[lastRow][col] == letterPlaying)
                return true;
        }

        return false;
    }

    private boolean checkUpRight(int row, int col, int letterPlaying) {
        int currentRow = row;
        int currentCol = col;

        while (currentRow > 0 && currentCol < 7) {

            currentRow--;
            currentCol++;
            if (gameBoard[currentRow][currentCol] != -letterPlaying) {
                break;
            }

        }

        if (gameBoard[currentRow + 1][currentCol - 1] == -letterPlaying) {
            if (gameBoard[currentRow][currentCol] == letterPlaying)
                return true;
        }

        return false;
    }

    private boolean checkUpLeft(int row, int col, int letterPlaying) {
        int currentRow = row;
        int currentCol = col;

        while (currentRow > 0 && currentCol > 0) {

            currentRow--;
            currentCol--;
            if (gameBoard[currentRow][currentCol] != -letterPlaying) {
                break;
            }

        }

        if (gameBoard[currentRow + 1][currentCol + 1] == -letterPlaying) {
            if (gameBoard[currentRow][currentCol] == letterPlaying)
                return true;
        }

        return false;
    }

    private boolean checkDownRight(int row, int col, int letterPlaying) {
        int currentRow = row;
        int currentCol = col;

        while (currentRow < 7 && currentCol < 7) {

            currentRow++;
            currentCol++;
            if (gameBoard[currentRow][currentCol] != -letterPlaying) {
                break;
            }

        }

        if (gameBoard[currentRow - 1][currentCol - 1] == -letterPlaying) {
            if (gameBoard[currentRow][currentCol] == letterPlaying)
                return true;
        }

        return false;
    }

    private boolean checkDownLeft(int row, int col, int letterPlaying) {
        int currentRow = row;
        int currentCol = col;

        while (currentRow < 7 && currentCol > 0) {

            currentRow++;
            currentCol--;
            if (gameBoard[currentRow][currentCol] != -letterPlaying) {
                break;
            }

        }

        if (gameBoard[currentRow - 1][currentCol + 1] == -letterPlaying) {
            if (gameBoard[currentRow][currentCol] == letterPlaying)
                return true;
        }

        return false;
    }

    private void turnLettersRight(int row, int col, int letter) {

        for (int i = col + 1; i <= 7; i++) {
            if (gameBoard[row][i] != -letter) {
                break;
            } else {
                gameBoard[row][i] = letter;
            }
        }
    }

    private void turnLettersLeft(int row, int col, int letter) {
        for (int i = col - 1; i >= 0; i--) {
            if (gameBoard[row][i] != -letter) {
                break;
            } else {
                gameBoard[row][i] = letter;
            }
        }
    }

    private void turnLettersUp(int row, int col, int letter) {
        for (int i = row - 1; i >= 0; i--) {
            if (gameBoard[i][col] != -letter) {
                break;
            } else {
                gameBoard[i][col] = letter;
            }
        }
    }

    private void turnLettersDown(int row, int col, int letter) {
        for (int i = row + 1; i <= 7; i++) {
            if (gameBoard[i][col] != -letter) {
                break;
            } else {
                gameBoard[i][col] = letter;
            }
        }
    }

    private void turnLettersUpRight(int row, int col, int letter) {
        int currentRow = row;
        int currentCol = col;

        while (currentRow > 0 && currentCol < 7) {

            currentRow--;
            currentCol++;
            if (gameBoard[currentRow][currentCol] != -letter) {
                break;
            } else {
                gameBoard[currentRow][currentCol] = letter;
            }

        }
    }

    private void turnLettersUpLeft(int row, int col, int letter) {
        int currentRow = row;
        int currentCol = col;

        while (currentRow > 0 && currentCol > 0) {

            currentRow--;
            currentCol--;
            if (gameBoard[currentRow][currentCol] != -letter) {
                break;
            } else {
                gameBoard[currentRow][currentCol] = letter;
            }

        }
    }

    private void turnLettersDownRight(int row, int col, int letter) {
        int currentRow = row;
        int currentCol = col;

        while (currentRow < 7 && currentCol < 7) {

            currentRow++;
            currentCol++;
            if (gameBoard[currentRow][currentCol] != -letter) {
                break;
            } else {
                gameBoard[currentRow][currentCol] = letter;
            }

        }
    }

    private void turnLettersDownLeft(int row, int col, int letter) {
        int currentRow = row;
        int currentCol = col;

        while (currentRow < 7 && currentCol > 0) {

            currentRow++;
            currentCol--;
            if (gameBoard[currentRow][currentCol] != -letter) {
                break;
            } else {
                gameBoard[currentRow][currentCol] = letter;
            }

        }
    }
}
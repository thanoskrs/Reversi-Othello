import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class Main {


    public static void main(String[] args) {


        int[] coordinates = new int[2];

        Scanner input = new Scanner(System.in);
        System.out.println("Do you want to play first?");
        System.out.print("Y/N > ");

        String answer = input.nextLine();

        while (!answer.equals("Y") && !answer.equals("N")) {
            System.out.print("Y/N > ");
            answer = input.nextLine();
        }

        int playsFirst;
        switch (answer) {
            case "Y":
                playsFirst = Board.O;
                break;
            case "N":
                playsFirst = Board.X;
                break;
            default:
                playsFirst = 0;
        }


        System.out.println("Enter max depth for minimax algorithm.");
        int maxDepth = -1;

        while (maxDepth <= 0) {
            System.out.print("> ");
            try {
                maxDepth = Integer.valueOf(input.next());
            } catch (InputMismatchException inputMismatchException) {
                continue;
            } catch (NumberFormatException numberFormatException) {
                continue;
            }
        }


        GamePlayer XPlayer = new GamePlayer(maxDepth, Board.X);

        Board board = new Board(playsFirst);

        // create GUI
        JFrame frame = new JFrame("Othello Board Game");
        frame.setBounds(10, 10, 512, 512);

        frame.setUndecorated(true);
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics graphics) {
                graphics.setColor(Color.GREEN.darker().darker());
                graphics.fillRect(0, 0, 512, 512);
                board.print(graphics);
            }
        };

        frame.add(panel);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);

        // Let the player play using his mouse
        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                coordinates[0] = (int) e.getY() / 64;
                coordinates[1] = (int) e.getX() / 64;


                if (board.getLastLetterPlayed() == Board.X) {
                    if (board.canMakeMove(Board.O)) {
                        System.out.println("O moves: " + coordinates[0] + ", " + coordinates[1]);
                        //board.printValidMoves(frame.getGraphics(), Board.O);
                        if (board.isValidMove(coordinates[0], coordinates[1], Board.O)) {
                            System.out.println("O moves VALID: " + coordinates[0] + ", " + coordinates[1]);
                            board.makeMove(coordinates[0], coordinates[1], Board.O);
                            board.print(frame.getGraphics());
                        }
                    } else {
                        board.setLastLetterPlayed(Board.O);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };

        panel.addMouseListener(mouseListener);

        // While the game has not finished
        while (!board.isTerminal()) {

            if (board.getLastLetterPlayed() == Board.O) {

                if (board.canMakeMove(Board.X)) {

                    long start = System.currentTimeMillis();
                    Move XMove = XPlayer.MiniMax(board);
                    long end = System.currentTimeMillis();

                    double time = (double) (end - start) / 1000;

                    if (XMove.cannotMakeMove()) {

                        continue;
                    }

                    // pretend that algorithm is taking time to think
                    if (time < 1.0) {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println(time);
                    System.out.println("X moves: " + XMove.getRow() + ", " + XMove.getCol());
                    board.makeMove(XMove.getRow(), XMove.getCol(), Board.X);
                    board.print(frame.getGraphics());
                } else {
                    board.setLastLetterPlayed(Board.X);
                }
            }

        }


        int sum = board.winner();
        System.out.println("final sum: " + sum);

        if (sum > 0) {
            JOptionPane.showMessageDialog(null, "Sorry, you lost.", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else if (sum < 0) {
            JOptionPane.showMessageDialog(null, "Congratulations! You won!", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Draw.. Try again!", "Result", JOptionPane.INFORMATION_MESSAGE);
        }

        if (JOptionPane.CLOSED_OPTION == -1)
            frame.dispose();

    }
}

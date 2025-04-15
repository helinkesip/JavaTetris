import com.tetris.model.GameBoard;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel{
    private final int CELL_SIZE = 50;
    private GameBoard gameBoard;

    public GamePanel(){
        gameBoard = new GameBoard();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        int[][] board = gameBoard.getBoard();

        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                if(board[i][j] != 0){
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }
}

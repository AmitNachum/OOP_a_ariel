import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameLogic implements PlayableLogic
{
    private  Player firstPlayer;
    private Player secondPlayer;
    private Disc[][] board;
    private boolean isFirstPlayerTurn;
    private HashMap<Player , Color> playerColor;

    public GameLogic()
    {
        board = new Disc[8][8];
        this.isFirstPlayerTurn = true;
        this.playerColor = new HashMap<>();
        playerColor.put(firstPlayer, Color.WHITE);
        playerColor.put(secondPlayer, Color.BLACK);

    }

    private boolean isWithinBounds(Position a)
    {
        int row = a.getRow();
        int col = a.getCol();

        return row >=0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if(!isWithinBounds(a))
            return false;


        int row = a.getRow();
        int col = a.getCol();


        return  board[row][col] != null && board[row][col].equals(disc) ;
    }

    @Override
    public Disc getDiscAtPosition(Position position)
    {
      int row = position.getRow();
      int col = position.getCol();

      if(!isWithinBounds(position))
          return null;

      return board[row][col];


    }

    @Override
    public int getBoardSize() {
        return 8*8;
    }

    @Override
    public List<Position> ValidMoves()
    {

    }

    private boolean isValidMove(int rowIndex, int colIndex, Disc disc)
    {
        if(board[rowIndex][colIndex] != null)
            return false;

        boolean rightSide = false;
        boolean leftSide = false;
        boolean topPart = false;
        boolean downPart = false;
        boolean rightTopDiaganol = false;
        boolean leftDownDiaganol = false;
        boolean leftTopDiaganol = false;
        boolean rightDownDiaganol = false;

        //row check for "sandwich"

        for (int j = colIndex + 1; j < 8 ; j++)
        {
         if (board[rowIndex][j] != null)
             rightSide = true;

        }
        for (int j = colIndex - 1; j >= 0 ; j--)
        {
         if(board[rowIndex][j] !=null)
             leftSide = true;
        }

        if(rightSide && leftSide)
            return false;

        
    }

    @Override
    public int countFlips(Position a) {
        return 0;
    }

    @Override
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    @Override
    public Player getSecondPlayer() {
       return secondPlayer;
    }

    @Override
    public void setPlayers(Player player1, Player player2)
    {
   this.firstPlayer = player1;
   this.secondPlayer = player2;
    }

    @Override
    public boolean isFirstPlayerTurn() {
        return isFirstPlayerTurn;
    }

    @Override
    public boolean isGameFinished() {
        return false;
    }

    @Override
    public void reset() {
        board = new Disc[8][8];
        isFirstPlayerTurn = true;
    }

    @Override
    public void undoLastMove()
    {

    }

    public Color getFirstPLayerColor()
    {
        return playerColor.get(firstPlayer);
    }
    public Color getSecondPlayerColor()
    {
        return playerColor.get(secondPlayer);
    }
}

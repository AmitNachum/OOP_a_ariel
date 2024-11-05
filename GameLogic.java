import java.util.ArrayList;
import java.util.List;

public class GameLogic implements PlayableLogic
{
    private  Player firstPlayer;
    private Player secondPlayer;
    private Disc[][] board;
    private boolean isFirstPlayerTurn;

    public GameLogic()
    {
        board = new Disc[8][8];
        this.isFirstPlayerTurn = true;

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
    public List<Position> ValidMoves() {
       List<Position> moves = new ArrayList<>();
       for(int rowIndex = 0 ; rowIndex < 8 ; rowIndex++)
       {
           for (int colIndex = 0; colIndex < 8 ; colIndex++)
           {
            if(board[rowIndex][colIndex] == null)
            {

            }
           }
       }
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
}

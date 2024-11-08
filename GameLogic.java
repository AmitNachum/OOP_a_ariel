import java.util.ArrayList;
import java.util.List;

public class GameLogic implements PlayableLogic
{
    private  Player firstPlayer;
    private Player secondPlayer;
    private Disc[][] board;

    public GameLogic()
    {
        board = new Disc[8][8];


    }

    private void flipDisc(Disc disc, Position discPos) {
        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();

        if (disc.getType().equals("⬤"))
            disc.setOwner(player);

        if (disc.getType().equals("💣")){
            disc.setOwner(player);
            int row = discPos.row();
            int col = discPos.col();

            if (isWithinBounds(row, col + 1)) {
                if (board[row][col + 1].getOwner().equals(getFirstPlayer()))
                    board[row][col + 1].setOwner(getSecondPlayer());
                else
                    board[row][col + 1].setOwner(getFirstPlayer());
            }

            if (isWithinBounds(row + 1, col)) {
                if (board[row + 1][col].getOwner().equals(getFirstPlayer()))
                    board[row + 1][col].setOwner(getSecondPlayer());
                else
                    board[row + 1][col].setOwner(getFirstPlayer());
            }

            if (isWithinBounds(row + 1, col + 1)) {
                if (board[row + 1][col + 1].getOwner().equals(getFirstPlayer()))
                    board[row + 1][col + 1].setOwner(getSecondPlayer());
                else
                    board[row + 1][col + 1].setOwner(getFirstPlayer());
            }

            if (isWithinBounds(row, col - 1)) {
                if (board[row][col - 1].getOwner().equals(getFirstPlayer()))
                    board[row][col - 1].setOwner(getSecondPlayer());
                else
                    board[row][col - 1].setOwner(getFirstPlayer());
            }

            if (isWithinBounds(row - 1, col)) {
                if (board[row - 1][col].getOwner().equals(getFirstPlayer()))
                    board[row - 1][col].setOwner(getSecondPlayer());
                else
                    board[row - 1][col].setOwner(getFirstPlayer());
            }

            if (isWithinBounds(row + 1, col - 1)) {
                if (board[row + 1][col - 1].getOwner().equals(getFirstPlayer()))
                    board[row + 1][col - 1].setOwner(getSecondPlayer());
                else
                    board[row + 1][col - 1].setOwner(getFirstPlayer());
            }

            if (isWithinBounds(row - 1, col + 1)) {
                if (board[row - 1][col + 1].getOwner().equals(getFirstPlayer()))
                    board[row - 1][col + 1].setOwner(getSecondPlayer());
                else
                    board[row - 1][col + 1].setOwner(getFirstPlayer());
            }

            if (isWithinBounds(row - 1, col - 1)) {
                if (board[row - 1][col - 1].getOwner().equals(getFirstPlayer()))
                    board[row - 1][col - 1].setOwner(getSecondPlayer());
                else
                    board[row - 1][col - 1].setOwner(getFirstPlayer());
            }
        }
    }

    private Move placeDisc(Position position, Disc disc) {
        int row = position.row();
        int col = position.col();

        if(!isValidMove(row, col)) return null;

        board[row][col] = disc;

        List<Disc> discsToFlip = flippableDiscsAt(position);

        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8  ; j++)
            {
                if (discsToFlip.contains(board[i][j]))
                    flipDisc(board[i][j], new Position(i,j));
            }
        }

        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();
        return new Move(player, position, disc, discsToFlip);
    }

    private List<Disc> flippableDiscsAt(Position position) {
        List<Disc> flippableDiscs = new ArrayList<>();
        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();
        int row = position.row();
        int col = position.col();

        if (!isValidMove(row, col))
            return flippableDiscs;

        boolean isOpponentDiscRight = true;

        //row check for "sandwich"    B W W B

        for (int j = col; j < 8 && isOpponentDiscRight; j++) {
            if (board[row][j].getOwner().equals(player))
                isOpponentDiscRight = false;

            else if (!board[row][j].getType().equals("⭕"))
                flippableDiscs.add(board[row][j]);
        }


        boolean isOpponentDiscLeft = true;

        for (int j = col; j >= 0 && isOpponentDiscLeft; j--) {
            if (board[row][j].getOwner().equals(player))
                isOpponentDiscLeft = false;
            else if (!board[row][j].getType().equals("⭕"))
                flippableDiscs.add(board[row][j]);
        }


        boolean isOpponentTop = true;

        for (int i = row; i >= 0 && isOpponentTop; i--) {
            if (board[i][col].getOwner().equals(player))
                isOpponentTop = false;
            else if (!board[i][col].getType().equals("⭕"))
                flippableDiscs.add(board[i][col]);
        }


        boolean isOpponentBottom = true;

        for (int i = row; i < 8 && isOpponentBottom; i++) {
            if (board[i][col].getOwner().equals(player))
                isOpponentBottom = false;
            else if (!board[i][col].getType().equals("⭕"))
                flippableDiscs.add(board[i][col]);
        }


        boolean diaganolRightTop = true;

        for (int i = row, j = col; i >= 0 && j < 8 && diaganolRightTop; i--, j++) {
            if (board[i][j].getOwner().equals(player))
                diaganolRightTop = false;
            else if (!board[i][j].getType().equals("⭕"))
                flippableDiscs.add(board[i][j]);
        }


        boolean diaganolLeftBottom = true;

        for (int i = row, j = col; i < 8 && j >= 0 && diaganolLeftBottom; i++, j--) {
            if (board[i][j].getOwner().equals(player))
                diaganolLeftBottom = false;
            else if (!board[i][j].getType().equals("⭕"))
                flippableDiscs.add(board[i][j]);
        }


        boolean diaganolLeftTop = true;

        for (int i = row, j = col; i >= 0 && j >= 0 && diaganolLeftTop; i--, j--) {
            if (board[i][j].getOwner().equals(player))
                diaganolLeftTop = false;
            else if (!board[i][j].getType().equals("⭕"))
                flippableDiscs.add(board[i][j]);
        }


        boolean diaganolRightBottom = true;

        for (int i = row, j = col; i < 8 && j < 8 && diaganolRightBottom; i++, j++) {
            if (board[i][j].getOwner().equals(player))
                diaganolRightBottom = false;
            else if (!board[i][j].getType().equals("⭕"))
                flippableDiscs.add(board[i][j]);
        }

        return flippableDiscs;
    }

    private boolean isWithinBounds(Position a)
    {
        int row = a.row();
        int col = a.row();

        return row >=0 && row < 8 && col >= 0 && col < 8;
    }

    private boolean isWithinBounds(int row, int col)
    {
        return row >=0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if(!isWithinBounds(a))
            return false;


        int row = a.row();
        int col = a.col();


        return  board[row][col] != null && board[row][col].equals(disc) ;
    }

    @Override
    public Disc getDiscAtPosition(Position position)
    {
      int row = position.row();
      int col = position.col();

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

        List<Position> move = new ArrayList<>();

        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8  ; j++)
            {
             if(isValidMove(i, j))
                 move.add(new Position(i,j));
            }
        }

        return move;
    }

    private boolean isValidMove(int rowIndex, int colIndex)
    {
        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();

        if(board[rowIndex][colIndex] != null)
            return false;


        boolean isOpponentDiscRight = true;

        //row check for "sandwich"    B W W B

        for (int j = colIndex; j < 8 && isOpponentDiscRight; j++)
        {
            if (board[rowIndex][j].getOwner().equals(player))
                isOpponentDiscRight = false;
        }

        if(!isOpponentDiscRight)
            return true;



       boolean  isOpponentDiscLeft = true;

       for (int j = colIndex ; j >= 0 && isOpponentDiscLeft; j--)
       {
           if (board[rowIndex][j].getOwner().equals(player))
               isOpponentDiscLeft = false;
       }

       if(!isOpponentDiscLeft)
           return true;

;
       boolean isOpponentTop = true;

       for (int i = rowIndex; i >= 0 && isOpponentTop ; i--)
       {
         if (board[i][colIndex].getOwner().equals(player))
             isOpponentTop = false;
        }

       if (!isOpponentTop)
           return true;


       boolean isOpponentBottom = true;

       for (int i = rowIndex; i < 8 && isOpponentBottom ; i++)
       {
           if(board[i][colIndex].getOwner().equals(player))
               isOpponentBottom = false;
        }

       if (!isOpponentBottom)
           return true;


       boolean diaganolRightTop = true;

       for (int i = rowIndex , j = colIndex; i >= 0 && j < 8 && diaganolRightTop; i-- , j++)
       {
           if(board[i][j].getOwner().equals(player))
               diaganolRightTop = false;
        }

       if(!diaganolRightTop)
           return true;


       boolean diaganolLeftBottom = true;

       for (int i = rowIndex , j = colIndex; i < 8 && j >= 0 && diaganolLeftBottom; i++ , j--)
       {
           if (board[i][j].getOwner().equals(player))
               diaganolLeftBottom = false;
       }

       if(!diaganolLeftBottom)
           return true;


       boolean diaganolLeftTop = true;

       for (int i = rowIndex , j = colIndex; i >=0 && j >= 0 && diaganolLeftTop; i-- , j--)
       {
           if (board[i][j].getOwner().equals(player))
               diaganolLeftTop = false;
       }

       if(!diaganolLeftTop)
           return true;


       boolean diaganolRightBottom = true;

       for (int i = rowIndex , j = colIndex; i < 8 && j < 8 && diaganolRightBottom; i++ , j++)
       {
           if (board[i][j].getOwner().equals(player))
               diaganolRightBottom = false;
       }

       if(!diaganolRightBottom)
           return true;

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
    public boolean isFirstPlayerTurn()
    {
        return firstPlayer.isPlayerOne;
    }

    @Override
    public boolean isGameFinished() {
        return false;
    }

    @Override
    public void reset() {
        board = new Disc[8][8];
    }

    @Override
    public void undoLastMove()
    {

    }

}

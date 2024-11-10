import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    private Player firstPlayer = new HumanPlayer(true);
    private Player secondPlayer = new HumanPlayer(false);
    private Disc[][] board;


    public GameLogic()
    {
        board = new Disc[8][8];


    }
    public GameLogic(Player firstPlayer , Player secondPlayer)
    {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public Move placeDisc(Position position, Disc disc) {

        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();

        if (disc instanceof BombDisc)
        {
            if (player.number_of_bombs > 0 )
                player.number_of_bombs--;

            else
                return null;
        }

        if (disc instanceof UnflippableDisc)
        {

            if ( player.number_of_unflippedable > 0)
                player.number_of_unflippedable--;

            else
                return null;

        }

        int row = position.row();
        int col = position.col();

        if (!isValidMove(row, col))
            return null;

        board[row][col] = disc;

        List<Disc> discsToFlip = flippableDiscsAt(position);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (discsToFlip.contains(board[i][j]))
                    flipDiscs(board[i][j], new Position(i, j));
            }
        }


        Move move = new Move(player, position, disc, discsToFlip);


        Move.getTracker().add(move);


        return move;
    }

    public void flipDiscs(Disc disc, Position position) {
        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();

        int row = position.row();
        int col = position.col();


        if (!isWithinBounds(row, col) || board[row][col] == null) {
            return;
        }


        disc.setOwner(player);


        if (disc instanceof BombDisc) {
            triggerExplosion(row, col, player);
        }
    }

    private void triggerExplosion(int row, int col, Player player) {
        Disc disc = board[row][col];

        int[] rowDirections = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] colDirections = {-1, 0, 1, 1, 1, 0, -1, -1};

        // Recursively call flipDiscs on all 8 adjacent cells
        for (int i = 0; i < 8; i++) {
            int newRow = row + rowDirections[i];
            int newCol = col + colDirections[i];
            flipDiscs(disc, new Position(newRow, newCol));
        }
    }

    private List<Disc> counterExplosions(int row, int col, Player player) {
        List<Disc> explodedDiscs = new ArrayList<>();
        Disc disc = board[row][col];

        int[] rowDirections = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] colDirections = {-1, 0, 1, 1, 1, 0, -1, -1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + rowDirections[i];
            int newCol = col + colDirections[i];

            if (isWithinBounds(newRow, newCol)) {
                Disc adjacentDisc = board[newRow][newCol];

                if (adjacentDisc != null) {
                    explodedDiscs.add(disc);
                }

                if (adjacentDisc instanceof BombDisc) {
                    explodedDiscs.addAll(counterExplosions(newRow, newCol, player));
                }


            }

        }
        return explodedDiscs;
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

            if (board[row][j] != null && board[row][j].getOwner().equals(player))
                isOpponentDiscRight = false;

            else if (board[row][j] != null && !board[row][j].getType().equals("⭕"))
                flippableDiscs.add(board[row][j]);


            if (board[row][j] instanceof BombDisc)
                flippableDiscs.addAll(counterExplosions(row, j, player));


        }


        boolean isOpponentDiscLeft = true;

        for (int j = col; j >= 0 && isOpponentDiscLeft; j--) {

            if (board[row][j] != null && board[row][j].getOwner().equals(player))
                isOpponentDiscLeft = false;


            else if (board[row][j] != null && !board[row][j].getType().equals("⭕"))
                flippableDiscs.add(board[row][j]);


            if (board[row][j] instanceof BombDisc)
                flippableDiscs.addAll(counterExplosions(row, j, player));
        }


        boolean isOpponentTop = true;

        for (int i = row; i >= 0 && isOpponentTop; i--) {

            if (board[i][col] != null &&  board[i][col].getOwner().equals(player))
                isOpponentTop = false;

            else if (board[i][col] != null && !board[i][col].getType().equals("⭕"))
                flippableDiscs.add(board[i][col]);

            if (board[i][col] instanceof BombDisc)
                flippableDiscs.addAll(counterExplosions(i, col, player));
        }


        boolean isOpponentBottom = true;

        for (int i = row; i < 8 && isOpponentBottom; i++) {

            if (board[i][col]!= null && board[i][col].getOwner().equals(player))
                isOpponentBottom = false;

            else if (board[i][col]!= null && !board[i][col].getType().equals("⭕"))
                flippableDiscs.add(board[i][col]);

            if (board[i][col] instanceof BombDisc)
                flippableDiscs.addAll(counterExplosions(i, col, player));
        }


        boolean diaganolRightTop = true;

        for (int i = row, j = col; i >= 0 && j < 8 && diaganolRightTop; i--, j++) {

            if (board[i][j] != null && board[i][j].getOwner().equals(player))
                diaganolRightTop = false;

            else if (board[i][j] != null && !board[i][j].getType().equals("⭕"))
                flippableDiscs.add(board[i][j]);

            if (board[i][j] instanceof BombDisc)
                flippableDiscs.addAll(counterExplosions(i, j, player));
        }


        boolean diaganolLeftBottom = true;

        for (int i = row, j = col; i < 8 && j >= 0 && diaganolLeftBottom; i++, j--) {


            if (board[i][j] != null && board[i][j].getOwner().equals(player))
                diaganolLeftBottom = false;


            else if (board[i][j] != null && !board[i][j].getType().equals("⭕"))
                flippableDiscs.add(board[i][j]);

            if (board[i][j] instanceof BombDisc)
                flippableDiscs.addAll(counterExplosions(i, j, player));
        }


        boolean diaganolLeftTop = true;

        for (int i = row, j = col; i >= 0 && j >= 0 && diaganolLeftTop; i--, j--) {

            if (board[i][j] != null && board[i][j].getOwner().equals(player))
                diaganolLeftTop = false;

            else if (board[i][j] != null && board[i][j].getType().equals("⭕"))
                flippableDiscs.add(board[i][j]);

            if (board[i][j] instanceof BombDisc)
                flippableDiscs.addAll(counterExplosions(i, j, player));
        }


        boolean diaganolRightBottom = true;

        for (int i = row, j = col; i < 8 && j < 8 && diaganolRightBottom; i++, j++) {

            if (board[i][j]!= null && board[i][j].getOwner().equals(player))
                diaganolRightBottom = false;

            else if (board[i][j]!= null && !board[i][j].getType().equals("⭕"))
                flippableDiscs.add(board[i][j]);

            if (board[i][j] instanceof BombDisc)
                flippableDiscs.addAll(counterExplosions(i, j, player));

        }

        return flippableDiscs;
    }

    private boolean isWithinBounds(Position a) {
        int row = a.row();
        int col = a.col();

        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if (!isWithinBounds(a))
            return false;


        int row = a.row();
        int col = a.col();


        return board[row][col] != null && board[row][col].equals(disc);
    }

    @Override
    public Disc getDiscAtPosition(Position position) {
        int row = position.row();
        int col = position.col();

        if (!isWithinBounds(position))
            return null;

        return board[row][col];


    }

    @Override
    public int getBoardSize() {
        return 8;
    }

    @Override
    public List<Position> ValidMoves() {

        List<Position> move = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j))
                    move.add(new Position(i, j));
            }
        }

        return move;
    }

    private boolean isValidMove(int rowIndex, int colIndex) {
        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();

        if (board[rowIndex][colIndex] != null)
            return false;


        boolean isOpponentDiscRight = true;

        //row check for "sandwich"    B W W B

        for (int j = colIndex; j < 8 && isOpponentDiscRight; j++) {
            if (board[rowIndex][j] != null && board[rowIndex][j].getOwner().equals(player))
                isOpponentDiscRight = false;
        }

        if (!isOpponentDiscRight)
            return true;


        boolean isOpponentDiscLeft = true;

        for (int j = colIndex; j >= 0 && isOpponentDiscLeft; j--) {
            if (board[rowIndex][j] != null && board[rowIndex][j].getOwner().equals(player))
                isOpponentDiscLeft = false;
        }

        if (!isOpponentDiscLeft)
            return true;

        ;
        boolean isOpponentTop = true;

        for (int i = rowIndex; i >= 0 && isOpponentTop; i--) {
            if (board[i][colIndex] != null && board[i][colIndex].getOwner().equals(player))
                isOpponentTop = false;
        }

        if (!isOpponentTop)
            return true;


        boolean isOpponentBottom = true;

        for (int i = rowIndex; i < 8 && isOpponentBottom; i++) {
            if (board[i][colIndex] != null && board[i][colIndex].getOwner().equals(player))
                isOpponentBottom = false;
        }

        if (!isOpponentBottom)
            return true;


        boolean diaganolRightTop = true;

        for (int i = rowIndex, j = colIndex; i >= 0 && j < 8 && diaganolRightTop; i--, j++) {
            if (board[i][j] != null && board[i][j].getOwner().equals(player))
                diaganolRightTop = false;
        }

        if (!diaganolRightTop)
            return true;


        boolean diaganolLeftBottom = true;

        for (int i = rowIndex, j = colIndex; i < 8 && j >= 0 && diaganolLeftBottom; i++, j--) {
            if (board[i][j] != null && board[i][j].getOwner().equals(player))
                diaganolLeftBottom = false;
        }

        if (!diaganolLeftBottom)
            return true;


        boolean diaganolLeftTop = true;

        for (int i = rowIndex, j = colIndex; i >= 0 && j >= 0 && diaganolLeftTop; i--, j--) {
            if (board[i][j] != null && board[i][j].getOwner().equals(player))
                diaganolLeftTop = false;
        }

        if (!diaganolLeftTop)
            return true;


        boolean diaganolRightBottom = true;

        for (int i = rowIndex, j = colIndex; i < 8 && j < 8 && diaganolRightBottom; i++, j++) {
            if (board[i][j] != null && board[i][j].getOwner().equals(player))
                diaganolRightBottom = false;
        }

        return !diaganolRightBottom;
    }

    @Override
    public int countFlips(Position a) {
        return flippableDiscsAt(a).size();
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
    public void setPlayers(Player player1, Player player2) {
        this.firstPlayer = player1;
        this.secondPlayer = player2;
    }

    @Override
    public boolean isFirstPlayerTurn() {
        return firstPlayer.isPlayerOne();
    }

    @Override
    public boolean isGameFinished() {
        return ValidMoves().isEmpty();
    }

    @Override
    public void reset() {
        board = new Disc[8][8];

        int midRow = board.length / 2;
        int midCol = board[0].length / 2;

        board[midRow - 1][midCol - 1] = new SimpleDisc(getSecondPlayer());
        board[midRow - 1][midCol] = new SimpleDisc(getFirstPlayer());
        board[midRow][midCol - 1] = new SimpleDisc(getFirstPlayer());
        board[midRow][midCol] = new SimpleDisc(getSecondPlayer());

    }

    @Override
    public void undoLastMove() {
        if (Move.getTracker().isEmpty())
            return;

        if (!getFirstPlayer().isHuman() || !getSecondPlayer().isHuman())
            return;


        Move temp = Move.getTracker().pop();
        int row = temp.position().row();
        int col = temp.position().col();

        board[row][col] = null;

        List<Disc> modified = temp.getWereFlipped();

        for (Disc disc : modified) {
            if (disc.getOwner().equals(getFirstPlayer()))
                disc.setOwner((getSecondPlayer()));
            else
                disc.setOwner(getFirstPlayer());
        }


    }

}

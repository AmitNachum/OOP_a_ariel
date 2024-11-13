import java.util.ArrayList;
import java.util.List;

public class GameLogic implements PlayableLogic {
    private Player firstPlayer;
    private Player secondPlayer;
    private Disc[][] board;
    boolean isFirst = true;


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
        int row = position.row();
        int col = position.col();

        if (!isValidMove(row, col))
            return null;

        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();

        if (disc instanceof BombDisc)
        {
            if (player.getNumber_of_bombs() > 0 )
                player.reduce_bomb();

            else
                return null;
        }

        if (disc instanceof UnflippableDisc)
        {
            if ( player.getNumber_of_unflippedable() > 0)
                player.reduce_unflippedable();

            else
                return null;

        }

        board[row][col] = disc;

        if (player.equals(getFirstPlayer()))
            System.out.println("Player 1 placed a " + disc.getType() + " in (" + row + ", " + col + ")");

        else
            System.out.println("Player 2 placed a " + disc.getType() + " in (" + row + ", " + col + ")");

        List<Disc> discsToFlip = flippableDiscsAt(position);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (discsToFlip.contains(board[i][j])) {
                    flipDiscs(board[i][j], new Position(i, j));
                }
            }
        }


        Move move = new Move(player, position, disc, discsToFlip);

        Move.getTracker().add(move);

        System.out.println();

        isFirst = !isFirst;

        return move;
    }

    private void flipDiscs(Disc disc, Position position) {
        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();

        int row = position.row();
        int col = position.col();


        if (!isWithinBounds(row, col) || board[row][col] == null) {
            return;
        }


        if (disc instanceof SimpleDisc || disc instanceof BombDisc) {
            disc.setOwner(player);

            if (player.equals(getFirstPlayer()))
                System.out.println("Player 1 flipped the " + disc.getType() + " in (" + row + ", " + col + ")");

            else
                System.out.println("Player 2 flipped the " + disc.getType() + " in (" + row + ", " + col + ")");
        }


        if (disc instanceof BombDisc) {
            triggerExplosion(row, col);
        }
    }

    private void triggerExplosion(int row, int col) {
        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();
        Disc disc = board[row][col];

        int[] rowDirections = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] colDirections = {-1, 0, 1, 1, 1, 0, -1, -1};

        // Recursively call flipDiscs on all 8 adjacent cells
        for (int i = 0; i < 8; i++) {
            int newRow = row + rowDirections[i];
            int newCol = col + colDirections[i];

            Disc adjacentDisc = board[newRow][newCol];

            if (adjacentDisc != null && !adjacentDisc.getOwner().equals(player))
                flipDiscs(disc, new Position(newRow, newCol));
        }
    }

    private List<Disc> counterExplosions(int row, int col) {
        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();
        List<Disc> explodedDiscs = new ArrayList<>();
        Disc disc = board[row][col];

        int[] rowDirections = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] colDirections = {-1, 0, 1, 1, 1, 0, -1, -1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + rowDirections[i];
            int newCol = col + colDirections[i];

            if (isWithinBounds(newRow, newCol)) {
                Disc adjacentDisc = board[newRow][newCol];

                if (adjacentDisc != null && !adjacentDisc.getOwner().equals(player)) {
                    explodedDiscs.add(disc);
                }

                if (adjacentDisc instanceof BombDisc) {
                    explodedDiscs.addAll(counterExplosions(newRow, newCol));
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


        // Define directions as row and column increments
        int[][] directions = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0},  // Horizontal and Vertical
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // Diagonals
        };

        // Iterate over each direction
        for (int[] dir : directions) {
            int i = row + dir[0];
            int j = col + dir[1];
            List<Disc> potentialFlips = new ArrayList<>();

            while (i >= 0 && i < 8 && j >= 0 && j < 8) {
                Disc currentDisc = board[i][j];
                if (currentDisc == null) {
                    break; // Empty cell, stop searching in this direction
                }

                if (currentDisc.getOwner().equals(player) && !(currentDisc instanceof UnflippableDisc)) {
                    flippableDiscs.addAll(potentialFlips); // Valid sandwich, add all potential flips
                    break;

                } else if (!(currentDisc instanceof UnflippableDisc)) {
                    // Opponent disc, add to potential flips
                    potentialFlips.add(currentDisc);

                    // Check for bomb disc and add explosions
                    if (currentDisc instanceof BombDisc) {
                        potentialFlips.addAll(counterExplosions(i, j));
                    }
                }

                i += dir[0];
                j += dir[1];
            }
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
        return placeDisc(a, disc) != null;
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


        if (board[rowIndex][colIndex] != null) {
            return false;
        }


        int[][] directions = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };


        for (int[] dir : directions) {
            int i = rowIndex + dir[0];
            int j = colIndex + dir[1];
            boolean hasOpponent = false;


            while (i >= 0 && i < 8 && j >= 0 && j < 8) {
                Disc currentDisc = board[i][j];

                if (currentDisc == null) {
                    break;
                } else if (currentDisc.getOwner().equals(player)) {
                    if (hasOpponent)
                    {
                        return true;
                    } else {
                        break;
                    }
                } else if (!(currentDisc instanceof UnflippableDisc)){

                    hasOpponent = true;
                }


                i += dir[0];
                j += dir[1];
            }
        }

        return false;
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
        return isFirst;
    }

    @Override
    public boolean isGameFinished() {
        return ValidMoves().isEmpty();
    }

    private Player theWinnerIs(){
        int firstPlayerCounter = 0, secondPlayerCounter = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {

                    if (board[i][j].getOwner().equals(getFirstPlayer()))
                        firstPlayerCounter++;

                    else
                        secondPlayerCounter++;
                }
            }
        }

        if (firstPlayerCounter > secondPlayerCounter) {
            System.out.println("Player 1 wins with " + firstPlayerCounter + " discs! Player 2 had " + secondPlayerCounter + " discs.");
            return getFirstPlayer();
        }

        else if (firstPlayerCounter < secondPlayerCounter) {
            System.out.println("Player 2 wins with " + secondPlayerCounter + " discs! Player 1 had " + firstPlayerCounter + " discs.");
            return getSecondPlayer();
        }

        else
            return null;
    }

    @Override
    public void reset() {
        board = new Disc[8][8];
        Player winner = theWinnerIs();

        if (winner != null)
            winner.addWin();

        getFirstPlayer().reset_bombs_and_unflippedable();
        getSecondPlayer().reset_bombs_and_unflippedable();

        int midRow = board.length / 2;
        int midCol = board[0].length / 2;

        board[midRow - 1][midCol - 1] = new SimpleDisc(getFirstPlayer());
        board[midRow - 1][midCol] = new SimpleDisc(getSecondPlayer());
        board[midRow][midCol - 1] = new SimpleDisc(getSecondPlayer());
        board[midRow][midCol] = new SimpleDisc(getFirstPlayer());

    }

    @Override
    public void undoLastMove() {
        Player player = isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer();
        System.out.println("Undoing last move:");

        if (Move.getTracker().isEmpty()) {
            System.out.println("    No previous move available to undo.\n");
            return;
        }

        if (!getFirstPlayer().isHuman() || !getSecondPlayer().isHuman())
            return;

        int numBombs = player.number_of_bombs;
        int numUnFlipp = player.number_of_unflippedable;
        Move temp = Move.getTracker().pop();
        int row = temp.position().row();
        int col = temp.position().col();

        System.out.println("    Undo: removing " + temp.disc().getType() + " from (" + row + ", " + col + ")");

        board[row][col] = null;

        List<Disc> modified = temp.getWereFlipped();

        for (Disc disc : modified) {
            if (disc.getOwner().equals(getFirstPlayer()))
                disc.setOwner((getSecondPlayer()));
            else
                disc.setOwner(getFirstPlayer());

            boolean found = false;
            for (int i = 0; i < 8 && !found; i++) {
                for (int j = 0; j < 8 && !found; j++) {
                    if (board[i][j] == disc) {
                        System.out.println("    Undo: flipping back " + disc.getType() + " in (" + i + ", " + j + ")");
                        found = true;
                    }
                }
            }
        }

        if (temp.disc() instanceof BombDisc && numBombs < 3)
            player.number_of_bombs++;

        if (temp.disc() instanceof UnflippableDisc && numUnFlipp < 2)
            player.number_of_unflippedable++;

        isFirst = !isFirst;

        System.out.println();
    }

}

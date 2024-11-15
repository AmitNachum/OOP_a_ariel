import java.awt.*;
import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer {

    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }


    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        Random random = new Random();

        GameLogic temp = new GameLogic();

        // Retrieve the list of valid moves from the interface
        List<Position> validMoves = gameStatus.ValidMoves();
        if (validMoves.isEmpty()) {
            return null; // No valid moves available
        }

        // Select a random position from the valid moves
        Position chosenPosition = validMoves.get(random.nextInt(validMoves.size()));

        // Choose a random disc type
        Disc[] discTypes = {new BombDisc(this), new UnflippableDisc(this), new SimpleDisc(this)};
        Disc chosenDisc = discTypes[random.nextInt(discTypes.length)];

        // Retrieve the player making the move
        Player currentPlayer = gameStatus.isFirstPlayerTurn() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();

        // Determine the discs to flip at the chosen position
        List<Disc> discsToFlip = temp.flippableDiscsAt(chosenPosition);

        // Create and return the move
        return new Move(currentPlayer, chosenPosition, chosenDisc, discsToFlip);
    }
}
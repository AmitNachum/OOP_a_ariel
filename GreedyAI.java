import java.util.List;
import java.util.Random;

public class GreedyAI extends AIPlayer {

        public GreedyAI(boolean isPlayerOne) {
            super(isPlayerOne);
        }

        @Override
        public Move makeMove(PlayableLogic gameStatus) {
            GameLogic temp = new GameLogic();
            Player player = gameStatus.isFirstPlayerTurn() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();
            List<Position> positions = gameStatus.ValidMoves();
            int maxFlipp = 0;

            Disc[] type = {new BombDisc(this), new UnflippableDisc(this), new SimpleDisc(this)};
            Random rand = new Random();
            Position bestPos = null;

            for (Position position : positions) {
                int candidate = gameStatus.countFlips(position);
                if (candidate > maxFlipp) {
                    maxFlipp = candidate;
                    bestPos = position;
                }
            }

            if (bestPos == null) {
                return null; // No valid moves available
            }

            Disc chosenDisc = type[rand.nextInt(type.length)];

            return new Move(player, bestPos, chosenDisc, temp.flippableDiscsAt(bestPos));
        }
    }



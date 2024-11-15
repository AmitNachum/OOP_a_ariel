import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GreedyAI extends AIPlayer {

    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    private List<Position> topMoves(PlayableLogic gameStatus, List<Position> positions) {
        List<Position> topMoves = new ArrayList<>();
        int maxFlip = maxFlip(gameStatus, positions);

        for (Position position : positions) {
            if (gameStatus.countFlips(position) == maxFlip)
                topMoves.add(position);
        }

        return topMoves;
    }

    private int maxFlip(PlayableLogic gameStatus, List<Position> positions) {
        int maxFlip = 0;

        for (Position position : positions) {
            int candidate = gameStatus.countFlips(position);
            if (candidate > maxFlip) {
                maxFlip = candidate;
            }
        }

        return maxFlip;
    }

    private List<Position> topRightMoves(List<Position> topMoves) {
        List<Position> topRightMoves = new ArrayList<>();
        int rightCol = rightCol(topMoves);

        for (Position position : topMoves) {
            if (position.col() == rightCol)
                topRightMoves.add(position);
        }

        return topRightMoves;
    }

    private int rightCol(List<Position> topMoves) {
        int rightCol = 0;

        for (Position position : topMoves) {
            if (position.col() > rightCol) {
                rightCol = position.col();
            }
        }

        return rightCol;
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        GameLogic temp = new GameLogic();
        Player player = gameStatus.isFirstPlayerTurn() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();
        List<Position> positions = gameStatus.ValidMoves();

        List<Position> topMoves = topMoves(gameStatus, positions);
        Position bestPos = topMoves.getFirst();


        if (topMoves.size() > 1) {
            List<Position> topRightMoves = topRightMoves(topMoves);
            bestPos = topRightMoves.getFirst();

            if (topRightMoves.size() > 1) {
                int bottomRow = 0;
                for (Position position : topRightMoves) {
                    if (position.row() > bottomRow) {
                        bottomRow = position.row();
                        bestPos = position;
                    }
                }
            }
        }

        if (bestPos == null) {
            return null; // No valid moves available
        }

        return new Move(player, bestPos, new SimpleDisc(this), temp.flippableDiscsAt(bestPos));
    }
}



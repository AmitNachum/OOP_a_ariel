import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedyAI extends AIPlayer {

    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    private List<Position> topMoves(PlayableLogic gameStatus, List<Position> positions) {
        List<Position> topMoves = new ArrayList<>();
        int maxFlip = gameStatus.countFlips(positions.getLast());

        for (int i = positions.size()-1 ; i >= 0 && gameStatus.countFlips(positions.get(i)) == maxFlip ; i--) {
            topMoves.add(positions.get(i));
        }

        return topMoves;
    }

    private List<Position> topRightMoves(List<Position> topMoves) {
        List<Position> topRightMoves = new ArrayList<>();
        int rightCol = topMoves.getLast().col();

        for (int i = topMoves.size()-1 ; i >= 0 && topMoves.get(i).col() == rightCol ; i--) {
            topRightMoves.add(topMoves.get(i));
        }

        return topRightMoves;
    }

    private List<Position> topBottomMoves(List<Position> topRightMoves) {
        List<Position> topBottomMoves = new ArrayList<>();
        int bottomRow = topRightMoves.getLast().row();

        for (int i = topRightMoves.size()-1 ; i >= 0 && topRightMoves.get(i).row() == bottomRow ; i--) {
            topBottomMoves.add(topRightMoves.get(i));
        }

        return topBottomMoves;
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        GameLogic temp = new GameLogic();
        Player player = gameStatus.isFirstPlayerTurn() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();
        List<Position> positions = gameStatus.ValidMoves();

        positions.sort(Comparator.comparingInt(gameStatus::countFlips));
        List<Position> topMoves = topMoves(gameStatus, positions);
        Position bestPos = topMoves.getLast();


        if (topMoves.size() > 1) {
            topMoves.sort(Comparator.comparingInt(Position::col));
            List<Position> topRightMoves = topRightMoves(topMoves);
            bestPos = topRightMoves.getLast();

            if (topRightMoves.size() > 1) {
                topRightMoves.sort(Comparator.comparingInt(Position::row));
                List<Position> topBottomMoves = topBottomMoves(topRightMoves);
                bestPos = topBottomMoves.getLast();
            }
        }

        if (bestPos == null) {
            return null; // No valid moves available
        }

        return new Move(player, bestPos, new SimpleDisc(this), temp.flippableDiscsAt(bestPos));
    }
}



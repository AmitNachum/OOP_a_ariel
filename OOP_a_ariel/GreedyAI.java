import java.util.List;
import java.util.Random;

public class GreedyAI extends AIPlayer {


    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        Move bestMove = null;
        int maxFlipp = -1;

        List<Position> positions = gameStatus.ValidMoves();
        GameLogic temp = new GameLogic(new GreedyAI(false), new HumanPlayer((true)));

        Disc[] type = {new BombDisc(this), new UnflippableDisc(this), new SimpleDisc(this)};
        Random rand = new Random();


        for (Position position : positions) {
            int candidate = gameStatus.countFlips(position);

            if (candidate > maxFlipp) {
                maxFlipp = candidate;
                int randIndex = rand.nextInt(0, 3);
                bestMove = temp.placeDisc(position, type[randIndex]);
            }


        }

        return bestMove;


    }
}

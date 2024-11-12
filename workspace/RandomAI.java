import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer {

    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }


    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> list = gameStatus.ValidMoves();
        Random random = new Random();
        int randomNum = random.nextInt(list.size());
        Position runPos = list.get(randomNum);

        GameLogic temp = new GameLogic(new RandomAI(false) , new HumanPlayer(true));

        Disc[] type = {new BombDisc(this), new UnflippableDisc(this), new SimpleDisc(this)};

        int index = random.nextInt(type.length);

        Disc randomDisc = type[index];

        return temp.placeDisc(runPos, randomDisc);
    }
}

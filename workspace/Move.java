import java.util.List;
import java.util.Stack;

public class Move {

    private final Player playedBy;
    private final Position position;
    private final Disc disc;
    private final List<Disc> wereFlipped;
    private static Stack<Move> tracker = new Stack<>();


    public Move(Player playedBy, Position position, Disc disc, List<Disc> wereFlipped ) {
        this.playedBy = playedBy;
        this.position = position;
        this.disc = disc;
        this.wereFlipped = wereFlipped;

    }

    public Player player() {
        return playedBy;
    }

    public Position position() {
        return position;
    }

    public Disc disc() {
        return disc;
    }

    public List<Disc> getWereFlipped() {
        return wereFlipped;
    }

    public static Stack<Move> getTracker()
    {
        return tracker;
    }
}

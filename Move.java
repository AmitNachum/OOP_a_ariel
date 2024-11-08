import java.util.List;

public class Move {

    private final Player playedBy;
    private final Position position;
    private final Disc disc;
    private final List<Disc> wereFlipped;

    public Move(Player playedBy, Position position, Disc disc, List<Disc> wereFlipped) {
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
}

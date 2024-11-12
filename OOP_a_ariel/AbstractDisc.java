import java.awt.*;

public abstract class AbstractDisc implements Disc
{
    private Player owner;



    public AbstractDisc(Player owner)
    {
        this.owner = owner;

    }


    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Player player) {
         this.owner = player;
    }

    @Override
    public abstract String getType();

    @Override
    public String toString() {
        return "The disc placed is : "+ getType();
    }
}

import java.awt.*;

public class SimpleDisc extends AbstractDisc implements Disc
{
    public SimpleDisc(Player owner)
    {
        super( owner );
    }
    @Override
    public Player getOwner()
    {
        return null;
    }

    @Override
    public void setOwner(Player player)
    {

    }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public boolean canFlip() {
        return false;
    }

    @Override
    public boolean canExploed() {
        return false;
    }
}

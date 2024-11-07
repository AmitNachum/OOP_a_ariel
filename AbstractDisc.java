import java.awt.*;

public abstract class AbstractDisc
{
    private String color;
    private Player owner;


    public AbstractDisc(Player owner)
    {
        this.owner = owner;

    }

    public String getColor()
    {}

    public abstract boolean canFlip();
    public abstract boolean canExploed();

}

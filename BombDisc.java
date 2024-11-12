public class BombDisc extends AbstractDisc
{
    public BombDisc(Player owner)
    {
        super(owner);
    }

    @Override
    public String getType()
    {
        return "💣";
    }
}

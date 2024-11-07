public class ExplosiveDisc extends AbstractDisc
{
    public ExplosiveDisc(Player owner)
    {
        super(owner);
    }

    @Override
    public String getType()
    {
        return  "\"\uD83D\uDCA3\"";
    }
}

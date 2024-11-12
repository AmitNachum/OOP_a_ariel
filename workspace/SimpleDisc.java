import java.awt.*;

public class SimpleDisc extends AbstractDisc
{

    public SimpleDisc(Player owner)
    {
        super( owner );
    }


    @Override
    public String getType()
    {
        return  "⬤";
    }


}

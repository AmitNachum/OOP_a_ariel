public class Position
{
    private int row;
    private int col;

    public Position(int row , int col)
    {
        this.row = row;
        this.col = col;

    }

    public int row() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int col()
    {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString()
    {
        return "Position{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}

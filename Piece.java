public abstract class Piece
{
    protected int x, y;

    public Piece(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public abstract boolean canMoveOn(Board board, int x, int y) throws OutOfBoardException;

    public int getX()
    {
        return x;
    }

    public int getY() 
    {
        return y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }
}
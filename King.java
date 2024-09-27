public class King extends ChessPiece
{
    boolean moved;

    public King(Color color)
    {
        super(Type.King, color);
    }

    public King(Color color, int x, int y)
    {
        super(Type.King, color, x, y);
    }

    public boolean hasMoved()
    {
        return moved;
    }

    public void setMoved(boolean moved)
    {
        this.moved = moved;
    }

    @Override
    public boolean canMoveOn(Board board, int x, int y) throws OutOfBoardException
    {
        return false;
    }

    @Override
    public boolean canTargetAt(Board board, int x, int y) throws OutOfBoardException
    {
        final int[] pos = { -1, -1, 
                             0, -1, 
                             1, -1,
                            -1,  0,
                             1,  0,
                            -1,  1,
                             0,  1,
                             1,  1
                            };

        for(int i = 0; i < pos.length; i += 2)
            if(pos[i] + this.x == x && pos[i+1] + this.y == y)
                return true;
        return false;
    }
}
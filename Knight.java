public class Knight extends ChessPiece
{
    public static Knight fromPawn(Pawn pawn)
    {
        return new Knight(pawn.getColor(), pawn.getX(), pawn.getY());
    }

    public Knight(Color color)
    {
        super(Type.Knight, color);
    }

    public Knight(Color color, int x, int y)
    {
        super(Type.Knight, color, x, y);
    }

    @Override
    public boolean canMoveOn(Board board, int x, int y) throws OutOfBoardException
    {
        return false;
    }

    @Override
    public boolean canTargetAt(Board board, int x, int y) throws OutOfBoardException
    {
        final int[] pos = { -2, -1, 
                            -1, -2, 
                             1, -2,
                             2, -1,
                             2,  1,
                             1,  2,
                            -1,  2,
                            -2,  1
                            };

        for(int i = 0; i < pos.length; i += 2)
            if(pos[i] + this.x == x && pos[i+1] + this.y == y)
                return true;
        return false;
    }
}
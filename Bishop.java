public class Bishop extends ChessPiece
{
    public static Bishop fromPawn(Pawn pawn)
    {
        return new Bishop(pawn.getColor(), pawn.getX(), pawn.getY());
    }

    public Bishop(Color color)
    {
        super(Type.Bishop, color);
    }

    public Bishop(Color color, int x, int y)
    {
        super(Type.Bishop, color, x, y);
    }

    @Override
    public boolean canMoveOn(Board board, int x, int y)
    {
        return false;
    }

    @Override
    public boolean canTargetAt(Board board, int x, int y) throws OutOfBoardException
    {
        int dx = x - this.x;
        int dy = y - this.y;

        if (Math.abs(dx) == Math.abs(dy) && dx != 0) 
        {
            int kx = Utils.sign(dx);
            int ky = Utils.sign(dy);

            for(int k = 1; k < Math.abs(dx); ++k)
            {
                int nx = x + k * kx;
                int ny = y + k * ky;
                
                if(!board.isSquareEmpty(nx, ny)) //Is there a Piece blocking on the diagonal ?
                    return false;
            }

            return true;
        }

        return false;
    }
}
public class Queen extends ChessPiece
{
    public static Queen fromPawn(Pawn pawn)
    {
        return new Queen(pawn.getColor(), pawn.getX(), pawn.getY());
    }

    public Queen(Color color)
    {
        super(Type.Queen, color);
    }

    public Queen(Color color, int x, int y)
    {
        super(Type.Queen, color, x, y);
    }

    @Override
    public boolean canMoveOn(Board board, int x, int y) throws OutOfBoardException
    {
        return false;
    }

    @Override
    public boolean canTargetAt(Board board, int x, int y) throws OutOfBoardException
    {
        int dx = x - this.x;
        int dy = y - this.y;
        
        if(dx == 0 && dy == 0)
            return false;

        if (Math.abs(dx) == Math.abs(dy) || dx == 0 || dy == 0) 
        {
            int kx = Utils.sign(dx);
            int ky = Utils.sign(dy);

            final int BOUND = Math.max(Math.abs(dx), Math.abs(dy));

            for(int k = 1; k < BOUND; ++k)
            {
                int nx = x + k * kx;
                int ny = y + k * ky;

                if(!board.isSquareEmpty(nx, ny)) //Is there a Piece blocking on the diagonal/row/column ?
                    return false;
            }

            return true;
        }

        return false;
    }
}
public class Rook extends ChessPiece 
{
    boolean moved;

    public static Rook fromPawn(Pawn pawn)
    {
        return new Rook(pawn.getColor(), pawn.getX(), pawn.getY());
    }

    public Rook(Color color)
    {
        super(Type.Rook, color);
    }

    public Rook(Color color, int x, int y)
    {
        super(Type.Rook, color, x, y);
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
        int dx = x - this.x;
        int dy = y - this.y;
        
        if(dx == 0 && dy == 0)
            return false;

        if(dx == 0)
        {
            int ky = Utils.sign(dy);

            for(int k = 1; k < Math.abs(dy); ++k)
            {
                int ny = y + k * ky;
            
                if(!board.isSquareEmpty(x, ny)) //Is there a Piece blocking on the same row/column ?
                    return false;
            }
            return true;
        }
        else if(dy == 0)
        {
            int kx = Utils.sign(dx);

            for(int k = 1; k < Math.abs(dx); ++k)
            {
                int nx = x + k * kx;
            
                if(!board.isSquareEmpty(nx, y)) //Is there a Piece blocking on the same row/column ?
                    return false;
            }
            return true;
        }
        
        return false;
    }
}
public class Pawn extends ChessPiece
{
    public Pawn(Color color)
    {
        super(Type.Pawn, color);
    }
    
    public Pawn(Color color, int x, int y)
    {
        super(Type.Pawn, color, x, y);
    }

    @Override
    public boolean canMoveOn(Board board, int x, int y)
    {
        return false;
    }

    @Override
    public boolean canTargetAt(Board board, int x, int y)
    {
        return Math.abs(this.x - x) == 1 && (color == Color.Black && this.y + 1 == y || color == Color.White && this.y == y + 1);
    }

    public boolean canPromote()
    {
        return color == Color.Black && y == 7 || color == Color.White && y == 0;
    }
}
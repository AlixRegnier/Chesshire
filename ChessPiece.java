public abstract class ChessPiece extends Piece
{
    enum Color { Black, White };
    enum Type { Bishop, King, Knight, Pawn, Queen, Rook }

    protected Type type;
    protected Color color;

    public ChessPiece(Type type, Color color)
    {
        this(type, color, -1, -1);
    }

    public ChessPiece(Type type, Color color, int x, int y)
    {
        super(x, y);
        this.type = type;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public abstract boolean canTargetAt(Board board, int x, int y) throws GameException;
    
    public Color getColor()
    {
        return color;
    }

    public Type getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return String.format("%s %s (%d, %d)", color.name(), type.name(), x, y);
    }
}
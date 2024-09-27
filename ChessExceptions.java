class NoKingWasFoundException extends GameException
{
    public NoKingWasFoundException()
    {
        super("No king was found on board");
    }
}

class IllegalPromotionTypeException extends GameException
{
    public IllegalPromotionTypeException(ChessPiece.Type type)
    {
        super(String.format("Can't promote a pawn to a %s", type.name()));
    }
}

class WrongPlayerMoving extends GameException
{
    public WrongPlayerMoving(ChessPiece.Color color)
    {
        super(String.format("%s can't play, it is not his turn", color.name()));
    }
}
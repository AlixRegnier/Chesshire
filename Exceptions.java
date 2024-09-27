abstract class GameException extends Exception
{
    public GameException(String msg)
    {
        super(msg);
    }
}

class CallbackException extends GameException
{
    public CallbackException(Exception e)
    {
        super(String.format("Exception %s was catched during a callback, message: %s", e.getClass().getName(), e.getMessage()));
    }
}
class OutOfBoardException extends GameException
{
    public OutOfBoardException(int x, int y, int width, int height)
    {
        super(String.format("Asked a position out of board. Board size: %d x %d, position out of board: (%d, %d)", width, height, x, y));
    }
}

class MovingGhostException extends GameException
{
    public MovingGhostException(int x, int y)
    {
        super(String.format("Tried to move a something from empty square(%d, %d)", x, y));
    }
}

class IllegalMoveException extends GameException
{
    public IllegalMoveException(int x, int y, int new_x, int new_y)
    {
        super(String.format("Moving piece on (%d, %d) to (%d, %d) is an illegal move", x, y, new_x, new_y));
    }
}

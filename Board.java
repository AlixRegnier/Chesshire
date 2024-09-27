import java.util.Arrays;

public abstract class Board
{
    private Piece[][] board;
    protected final int WIDTH, HEIGHT;

    public Board(int width, int height)
    {
        WIDTH = width;
        HEIGHT = height;
        board = new Piece[WIDTH][HEIGHT];
    }

    public Piece getPiece(int x, int y) throws OutOfBoardException
    {
        if(isPositionValid(x, y))
            return board[x][y];
        else
            throw new OutOfBoardException(x, y, WIDTH, HEIGHT);
    }

    public Piece[] getPieces()
    {
        return Arrays.stream(board)            // Stream<Piece[]>
                     .flatMap(Arrays::stream)  // Flatten into Stream<Piece>
                     .filter(s -> s != null)   // Filter out null Piece (empty cases)
                     .toArray(Piece[]::new);

    }

    public void setPiece(Piece piece, int x, int y) throws OutOfBoardException
    {
        if(isPositionValid(x, y))
        {
            piece.setX(x);
            piece.setY(y);
            board[x][y] = piece;
        }
        else
            throw new OutOfBoardException(x, y, WIDTH, HEIGHT);
    }

    public void clearBoard() throws OutOfBoardException
    {
        for(int x = 0; x < WIDTH; ++x)
            for(int y = 0; y < HEIGHT; ++y)
                clearSquare(x, y);
    }

    public void clearSquare(int x, int y) throws OutOfBoardException
    {
        setPiece(null, x, y);
    }

    //As WIDTH and HEIGHT are declared as final, could use public instead of getters
    public int getWidth()
    {
        return WIDTH;
    }

    public int getHeight()
    {
        return HEIGHT;
    }

    public boolean isPositionValid(int x, int y)
    {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    public boolean isSquareEmpty(int x, int y) throws OutOfBoardException
    {
        return getPiece(x, y) == null;
    }

    public abstract void movePiece(int x, int y, int new_x, int new_y) throws GameException;
}
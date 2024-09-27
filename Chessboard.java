import java.util.Arrays;

public class Chessboard extends Board
{
    private Pawn dashedPawn;
    private ChessGame game;

    public Chessboard(ChessGame game) throws GameException
    {
        super(8,8);

        this.game = game;

        //Rooks
        super.setPiece(new Rook(ChessPiece.Color.Black), 0, 0);
        super.setPiece(new Rook(ChessPiece.Color.Black), 7, 0);
        super.setPiece(new Rook(ChessPiece.Color.White), 0, 7);
        super.setPiece(new Rook(ChessPiece.Color.White), 7, 7);

        //Knight
        super.setPiece(new Knight(ChessPiece.Color.Black), 1, 0);
        super.setPiece(new Knight(ChessPiece.Color.Black), 6, 0);
        super.setPiece(new Knight(ChessPiece.Color.White), 1, 7);
        super.setPiece(new Knight(ChessPiece.Color.White), 6, 7);

        //Knight
        super.setPiece(new Bishop(ChessPiece.Color.Black), 2, 0);
        super.setPiece(new Bishop(ChessPiece.Color.Black), 5, 0);
        super.setPiece(new Bishop(ChessPiece.Color.White), 2, 7);
        super.setPiece(new Bishop(ChessPiece.Color.White), 5, 7);

        //Queen
        super.setPiece(new Queen(ChessPiece.Color.Black), 3, 0);
        super.setPiece(new Queen(ChessPiece.Color.White), 3, 7);

        //King
        super.setPiece(new King(ChessPiece.Color.Black), 4, 0);
        super.setPiece(new King(ChessPiece.Color.White), 4, 7);

        //Pawn
        for(int i = 0; i < super.getWidth(); ++i)
        {
            super.setPiece(new Pawn(ChessPiece.Color.Black), i, 1);
            super.setPiece(new Pawn(ChessPiece.Color.White), i, 6);
        }
    }

    private void castle(King king, Rook rook) throws OutOfBoardException
    {
        if(rook.getX() == 0)
        {
            setPiece(rook, king.getX() - 1, rook.getY());
            setPiece(king, king.getX() - 2, king.getY());
        }
        else
        {
            setPiece(rook, king.getX() + 1, rook.getY());
            setPiece(king, king.getX() + 2, king.getY());
        }
    }

    private void promote(Pawn pawn, ChessPiece.Type type) throws IllegalPromotionTypeException, OutOfBoardException
    {
        switch (type) 
        {
            case Pawn:
            case King:
                throw new IllegalPromotionTypeException(type);
        
            case Bishop:
                setPiece(Bishop.fromPawn(pawn), pawn.getX(), pawn.getY());
                break;

            case Knight:
                setPiece(Knight.fromPawn(pawn), pawn.getX(), pawn.getY());
                break;

            case Rook:
                setPiece(Rook.fromPawn(pawn), pawn.getX(), pawn.getY());
                break;

            case Queen:
                setPiece(Queen.fromPawn(pawn), pawn.getX(), pawn.getY());
                break;
        }
    }

    @Override
    public void movePiece(int x, int y, int new_x, int new_y) throws GameException
    {
        //is there a piece on (x,y) ?
        //from=king, to=rook, from and to same color => castle ? function rock
        //is there a piece of different color on (nx, ny) ? 
        //can the moving piece go to this location ?
        //is there a piece on the path ?
        //when moving, is my king checked ?
        //check promotion
        ChessPiece movingPiece = getPiece(x, y);
        ChessPiece targetPiece = getPiece(new_x, new_y);

        if(movingPiece == null)
            throw new MovingGhostException(x, y); //error no piece
        
        if(game.getColorToPlay() != movingPiece.getColor())
            throw new WrongPlayerMoving(movingPiece.getColor());

        //Castling
        if(targetPiece != null && movingPiece.getColor() == targetPiece.getColor() && movingPiece.getType() == ChessPiece.Type.King && targetPiece.getType() == ChessPiece.Type.Rook)
        {
            King k = (King)movingPiece;
            Rook q = (Rook)targetPiece;

            if(!k.hasMoved() && !q.hasMoved())
            {
                castle(k, q);
                return;
            }
        }

        if(movingPiece.canMoveOn(this, new_x, new_y))
            setPiece(movingPiece, new_x, new_y);
        else
            throw new IllegalMoveException(x, y, new_x, new_y);

        dashedPawn = null;

        switch(movingPiece.getType())
        {
            case Pawn:
                if(Math.abs(y - new_y) == 2)
                    dashedPawn = (Pawn)movingPiece;
                else if(((Pawn)movingPiece).canPromote())
                    promote((Pawn)movingPiece, game.callback(ChessGame.Action.Promote));
                break;

            //Set that piece can no more rock
            case King:
                ((King)movingPiece).setMoved(true);
                break;

            case Rook:
                ((Rook)movingPiece).setMoved(true);
                break;
                
            default:
                break;
        }
    }

    public ChessPiece getPiece(int x, int y) throws OutOfBoardException
    {
        return (ChessPiece)super.getPiece(x, y);
    }

    public King getKing(ChessPiece.Color color) throws OutOfBoardException, NoKingWasFoundException
    {
        for(int x = 0; x < WIDTH; ++x)
            for(int y = 0; y < HEIGHT; ++y)
            {
                ChessPiece k = getPiece(x, y);
                
                if(k != null && k.getType() == ChessPiece.Type.King && k.getColor() == color)
                    return (King)k;
            }
        
        throw new NoKingWasFoundException();
    }

    public ChessPiece[] getPieces()
    {
        return Arrays.stream(super.getPieces())
                     .map(x -> (ChessPiece)x)
                     .toArray(ChessPiece[]::new);
    }

    public boolean isKingChecked(ChessPiece.Color color) throws GameException
    {
        King k = getKing(color);

        for(ChessPiece p : getPieces())
            if(p.getColor() != k.getColor() && p.canTargetAt(this, k.getX(), k.getY()))
                return true;

        return false;
    }
}
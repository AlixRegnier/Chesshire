import java.util.concurrent.Callable;
import java.util.Scanner;

public class ChessGame
{
    enum Action { Promote, End }
    
    private CallbackHandler callbackHandler;
    private Chessboard board;
    private String winner;
    private boolean gameEnded;
    private ChessPiece.Color colorTurn = ChessPiece.Color.White;

    public ChessGame() throws GameException
    {
        callbackHandler = new CallbackHandler();
        board = new Chessboard(this);
    }

    public Chessboard getChessboard()
    {
        return board;
    }

    public String getWinnerColor()
    {
        if(gameEnded)
            return winner;

        return null;
    }

    public ChessPiece.Color getColorToPlay()
    {
        return colorTurn;
    }

    public void endGame()
    {
        gameEnded = true;
    }

    public void bindPromotion(Callable<ChessPiece.Type> function)
    {
        callbackHandler.addCallback(Action.Promote.name(), function);
    }

    public void bindEnd(Callable<Void> function)
    {
        callbackHandler.addCallback(Action.End.name(), () -> { endGame(); return function.call(); });
    }

    public <T> T callback(Action action) throws GameException
    {
        return callbackHandler.callback(action.name());
    }

    public boolean isGameEnded()
    {
        return gameEnded;
    }

    public static Void end(String color)
    {
        if(color != null)
            System.out.println(color + " wins !");
        else
            System.out.println("No winner.");

        return null;
    }

    public static void main(String[] args)
    {
        try
        {
            ChessGame game = new ChessGame();
            Chessboard board = game.getChessboard();

            game.bindPromotion(ChessGame::selectPromotingPiece);
            game.bindEnd(() -> end(game.getWinnerColor()));

            for(ChessPiece p : board.getPieces())
            {
                System.out.println(p);
            }

            while(!game.isGameEnded())
            {
                /*Game loop, get next move to play*/
            }
        }
        catch(GameException e)
        {
            e.printStackTrace();
        }
    }

    public static ChessPiece.Type selectPromotingPiece()
    { 
        Scanner sc = new Scanner(System.in);
        System.out.println("Select new type for promoted pawn (B/N/Q/R): ");

        try
        {
            switch (sc.nextLine()) 
            {
                case "B":
                    return ChessPiece.Type.Bishop;
                case "N":
                    return ChessPiece.Type.Knight;
                case "Q":
                    return ChessPiece.Type.Queen;
                case "R":
                    return ChessPiece.Type.Rook;
                default:
                    throw new Exception("Invalid piece selected for a promotion");
                
            }
        }
        catch(Exception e)
        {
            return selectPromotingPiece();
        }
        finally
        {
            sc.close();
        }
    }
}

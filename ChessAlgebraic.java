import java.util.HashMap;
import java.util.Scanner;

public class ChessAlgebraic {
    enum Action { Nothing, PieceType, StartLoc, Loc, Eat, EndLoc }

    private Automata<Action> automata;
    private ChessGame game;
    private ChessPiece.Type type = ChessPiece.Type.Pawn;
    private boolean eat;

    public static int getLocationFromChar(char c)
    {
        if(c >= 'a' && c <= 'h')
            return c - 'a';
        else if(c >= '1' && c <= '8')
            return 7 - c + '1';

        throw new RuntimeException(String.format("Unexpected characted given when trying to parse location index: ", c));
    }

    public ChessAlgebraic(ChessGame game)
    {
        //Define automata
        automata = new Automata<>();

        State<Action> start = new State<>(Action.Nothing);
        automata.setStartState(start);

        State<Action> pawnCol = new State<>(Action.StartLoc);
        
        State<Action> pieceType = new State<>(Action.PieceType);

        State<Action> pieceCol = new State<>(Action.Loc);
        State<Action> pieceRow = new State<>(Action.StartLoc);

        State<Action> eat = new State<>(Action.Eat);

        State<Action> endCol = new State<>(Action.EndLoc);
        State<Action> endRow = new State<>(Action.EndLoc);

        //Piece
        start.addFromString("BKNQR", pieceType);
        pieceType.add('x', eat);
        pieceType.addFromString("abcdefgh", pieceCol);
        pieceType.addFromString("12345678", pieceRow);

        pieceCol.add('x', eat);
        pieceCol.addFromString("abcdefgh", endCol);
        pieceCol.addFromString("12345678", endRow);

        pieceRow.add('x', eat);
        
        //Pawn
        start.addFromString("abcdefgh", pawnCol);
        pawnCol.add('x', eat);
        pawnCol.addFromString("12345678", endRow);

        //Eat
        eat.addFromString("abcdefgh", endCol);

        //End
        endCol.addFromString("12345678", endRow);
    
        this.game = game;
    }

    public void setPieceType(ChessPiece.Type type)
    {
        this.type = type;
    }

    public int[] getStartAndEndLocation(String move) throws GameException
    {
        King k = game.getChessboard().getKing(game.getColorToPlay());

        if(move.equals("O-O"))
            return new int[] {k.getX(), k.getY(), 7, k.getY()}; //Tells to king castle
        else if(move.equals("O-O-O"))
            return new int[] {k.getX(), k.getY(), 0, k.getY()}; //Tells to queen castle
    
        automata.reset();
        int[] movePos = new int[]{-1, -1, -1, -1};

        for(char c : move.toCharArray())
        {
            State<Action> s = automata.followRule(c);
            
            switch(s.getAction())
            {
                case Nothing:
                    break;
                    
                case Loc:
                    if(c >= 'a' && c <= 'h') //Ambiguish location, later in automata execution, it will be resolved
                        movePos[0] = movePos[2] = getLocationFromChar(c);
                    break;
                    
                case StartLoc:
                    if(c >= 'a' && c <= 'h')
                        movePos[0] = getLocationFromChar(c);
                    else
                        movePos[1] = getLocationFromChar(c);
                    break;

                case EndLoc:
                    if(c >= 'a' && c <= 'h')
                        movePos[2] = getLocationFromChar(c);
                    else
                        movePos[3] = getLocationFromChar(c);
                    break;

                case Eat:
                    eat = true;
                    break;

                case PieceType:
                    switch(c)
                    {
                        case 'B':
                            setPieceType(ChessPiece.Type.Bishop);
                            break;
                        case 'K':
                            setPieceType(ChessPiece.Type.King);
                            break;
                        case 'N':
                            setPieceType(ChessPiece.Type.Knight);
                            break;
                        case 'Q':
                            setPieceType(ChessPiece.Type.Queen);
                            break;
                        case 'R':
                            setPieceType(ChessPiece.Type.Rook);
                            break;
                    }
                    break;
            }
        }

        if(type != ChessPiece.Type.Pawn)
        {
            if(movePos[0] == -1 && movePos[1] == -1)
            {
                //Search only piece that can play on (movePos[2], movePos[3])
                for(ChessPiece piece : game.getChessboard().getPieces())
                {
                    if(piece.getType() == type && piece.getColor() == game.getColorToPlay() && piece.canMoveOn(null, 0, 0))
                    {    
                        movePos[0] = piece.getX();
                        movePos[1] = piece.getY();
                        break;
                    }
                }

                if(movePos[0] == -1 && movePos[1] == -1)
                {
                    System.err.println(String.format("No piece could make a move to (%d, %d)", movePos[2], movePos[3]));
                    throw new IllegalMoveException(movePos[0], movePos[1], movePos[2], movePos[3]);
                }
            }
            else
            {
                if(movePos[0] == -1)
                    for(int i = 0; i < 7; ++i)
                    {
                        ChessPiece piece = game.getChessboard().getPiece(i, movePos[1]);
                        if(piece.getType() == type && piece.getColor() == game.getColorToPlay())
                        {
                            movePos[0] = i;
                            break;
                        }
                    }

                if(movePos[1] == -1)
                    for(int i = 0; i < 7; ++i)
                    {
                        ChessPiece piece = game.getChessboard().getPiece(movePos[0], i);
                        if(piece.getType() == type && piece.getColor() == game.getColorToPlay())
                        {
                            movePos[1] = i;
                            break;
                        }
                    }
            }
        }    

        return movePos;
    }

    class Automata<T>
    {
        private State<T> currentState;
        private State<T> startState;

        public void setStartState(State<T> n)
        {
            currentState = startState = n;
        }

        public void reset()
        {
            currentState = startState;
        }

        public State<T> followRule(char c)
        {
            if(currentState == null)
                throw new RuntimeException("State is undefined");
            
            currentState = currentState.next(c);
            return currentState;
        }
    }

    class State<T>
    {
        boolean finalState;
        T action;
        private HashMap<Character, State<T>> outStates;

        public State(T action)
        {
            this(action, false);
            
        }

        public State(T action, boolean finalState)
        {
            this.action = action;
            this.finalState = finalState;
            outStates = new HashMap<>();
        }

        public void add(char c, State<T> state)
        {
            outStates.put(Character.valueOf(c), state);
        }

        public void add(Character c, State<T> state)
        {
            outStates.put(c, state);
        }

        public void addFromString(String s, State<T> state)
        {
            for(char c : s.toCharArray())
                add(c, state);
        }

        public State<T> next(char c)
        {
            Character _c = Character.valueOf(c);

            if(outStates.containsKey(_c))
                return outStates.get(_c);
            else if(outStates.containsKey(null)) //Default rule
                return outStates.get(null).next(_c);
            
            //No default rule
            throw new RuntimeException(String.format("No rule for '%c'", _c.charValue()));
        }

        public T getAction()
        {
            return action;
        }
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        try
        {
            ChessGame game = new ChessGame();
            ChessAlgebraic ca = new ChessAlgebraic(game);

            while(!game.isGameEnded())
            {
                System.out.print("Move: ");
                int[] move = ca.getStartAndEndLocation(sc.nextLine());
                game.getChessboard().movePiece(move[0], move[1], move[2], move[3]);
            }
        }
        catch(GameException e)
        {
            e.printStackTrace();
        }
        finally
        {
            sc.close();
        }
    }
}
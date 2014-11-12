package jump61;

/** A Player that gets its moves from manual input.
 *  @author Daehwan Jung
 */
class HumanPlayer extends Player {

    /** A new player initially playing COLOR taking manual input of
     *  moves from GAME's input source. */
    HumanPlayer(Game game, Color color) {
        super(game, color);
        _nextMove = new int[2];
    }

    @Override
    void makeMove() {
        Game game = getGame();
        Board board = getBoard();
        if (game.getMove(_nextMove)) {
            game.makeMove(_nextMove[0], _nextMove[1]);
        }
    }

    /** Player's next move. */
    private int[] _nextMove;
}

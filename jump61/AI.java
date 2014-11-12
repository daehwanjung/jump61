package jump61;

import java.util.ArrayList;

/** An automated Player.
 *  @author Daehwan Jung
 */
class AI extends Player {

    /** A new player of GAME initially playing COLOR that chooses
     *  moves automatically.
     */
    AI(Game game, Color color) {
        super(game, color);
    }

    @Override
    void makeMove() {
        ArrayList<Integer> moves = new ArrayList<Integer>();
        Game game = getGame();
        MutableBoard board = new MutableBoard(getBoard(), false);
        int n = board.size();
        int move;
        generateMoves(moves, game, board, n);
        if (moves.size() > 0) {
            move = moves.get(game.randInt(moves.size()));
        } else {
            move = lastResort(game, board, n);
        }
        game.message("%s moves %d %d", getColor(),
            board.row(move), board.col(move));
        game.makeMove(move);
    }

    /** Prioritizes taking empty corners and edges and adds possible
     *  actions into MOVES, given a GAME, BOARD, and size N. */
    private void generateMoves(ArrayList<Integer> moves,
        Game game, MutableBoard board, int n) {
        if (board.spots(1) == 0) {
            moves.add(1);
        }
        if (board.spots(n) == 0) {
            moves.add(n);
        }
        if (board.spots(n * n - n + 1) == 0) {
            moves.add(n * n - n + 1);
        }
        if (board.spots(n * n) == 0) {
            moves.add(n * n);
        }
        if (moves.size() > 0) {
            return;
        }
        for (int r = 1; r <= n; r += 1) {
            if (r == 1 || r == n) {
                for (int c = 2; c < n; c += 1) {
                    if (board.spots(r, c) == 0) {
                        if (checkNeighbors(board.sqNum(r, c), board, n)) {
                            moves.add(board.sqNum(r, c));
                        }
                    }
                }
            } else {
                if (board.color(r, 1) != getColor().opposite()
                    && checkNeighbors(board.sqNum(r, 1), board, n)) {
                    moves.add(board.sqNum(r, 1));
                }
                if (board.color(r, n) != getColor().opposite()
                    && checkNeighbors(board.sqNum(r, n), board, n)) {
                    moves.add(board.sqNum(r, n));
                }
            }
        }
    }

    /** Returns the best calculated move given a GAME,
     *  BOARD, and size N. */
    private int lastResort(Game game, MutableBoard board, int n) {
        int best = countColors(board, n);
        int newMove = 0;
        for (int i = 1; i <= n * n; i += 1) {
            if (board.color(n) != getColor().opposite()) {
                board.addSpot(getColor(), i);
                int current = countColors(board, n);
                if (current >= best) {
                    newMove = n;
                }
                board.undo();
            }
        }
        if (newMove > 0) {
            return newMove;
        } else {
            while (!board.isLegal(getColor(), newMove)) {
                newMove = game.randInt(n * n);
            }
            return newMove;
        }
    }

    /** Returns TRUE if empty square S has no neighbors with the opposite
     *  color of the player. If S is the player's color,
     *  return TRUE only if S is closer to overflowing than its neighboring
     *  squares of the opponent's color. Otherwise return FALSE, given
     *  a BOARD of size N. */
    private boolean checkNeighbors(int s, MutableBoard board, int n) {
        int[] neighbors = new int[]{s - 1, s + 1, s - n, s + n};
        if (board.color(s) == getColor()) {
            int x = board.neighbors(s) - board.spots(s);
            for (int e : neighbors) {
                if (board.exists(e)
                    && board.color(e) == getColor().opposite()) {
                    if ((board.neighbors(e) - board.spots(e)) >= x) {
                        return true;
                    }
                }
            }
        } else {
            for (int e : neighbors) {
                if (board.exists(e)
                    && board.color(e) != getColor().opposite()) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns how many squares on the BOARD of size N are the player's
     *  color. */
    private int countColors(MutableBoard board, int n) {
        int sum = 0;
        for (int i = 1; i < n * n; i += 1) {
            if (board.color(n) == getColor()) {
                sum += 1;
            }
        }
        return sum;
    }
}



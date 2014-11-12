package jump61;


import static jump61.Color.*;
import java.util.ArrayList;

/** A Jump61 board state.
 *  @author Daehwan Jung
 */
class MutableBoard extends Board {

    /** An N x N board in initial configuration. */
    MutableBoard(int N) {
        _N = N;
        _moves = 0;
        _spots = new int[N * N];
        for (int i = 0; i < _spots.length; i += 1) {
            _spots[i] = 0;
        }
        _colors = new Color[N * N];
        for (int j = 0; j < _colors.length; j += 1) {
            _colors[j] = WHITE;
        }
        _history = new ArrayList<Board>();
    }

    /** A board whose initial contents are copied from BOARD0. Clears the
     *  undo history if not KEEP. */
    MutableBoard(Board board0, boolean keep) {
        copy(board0);
        if (!keep) {
            _history = new ArrayList<Board>();
        }
    }

    @Override
    void clear(int N) {
        _N = N;
        _moves = 0;
        _spots = new int[N * N];
        for (int i = 0; i < _spots.length; i += 1) {
            _spots[i] = 0;
        }
        _colors = new Color[N * N];
        for (int j = 0; j < _colors.length; j += 1) {
            _colors[j] = WHITE;
        }
        _history.clear();
    }

    @Override
    void copy(Board board) {
        _N = board.size();
        _moves = board.numMoves();
        _spots = new int[_N * _N];
        for (int i = 0; i < _spots.length; i += 1) {
            _spots[i] = board.spots(i + 1);
        }
        _colors = new Color[_N * _N];
        for (int j = 0; j < _colors.length; j += 1) {
            _colors[j] = board.color(j + 1);
        }
        _history = board.getHistory();
    }

    @Override
    int size() {
        return _N;
    }

    @Override
    int spots(int r, int c) {
        return _spots[sqNum(r, c) - 1];
    }

    @Override
    int spots(int n) {
        return _spots[n - 1];
    }

    @Override
    Color color(int r, int c) {
        return _colors[sqNum(r, c) - 1];
    }

    @Override
    Color color(int n) {
        return _colors[n - 1];
    }

    @Override
    int numMoves() {
        return _moves;
    }

    @Override
    int numOfColor(Color color) {
        int total = 0;
        for (int i = 0; i < _colors.length; i += 1) {
            if (_colors[i] == color) {
                total += 1;
            }
        }
        return total;
    }

    @Override
    void addSpot(Color player, int r, int c) {
        _history.add(new MutableBoard(this, true));
        _spots[sqNum(r, c) - 1] += 1;
        if (color(r, c) == WHITE) {
            _colors[sqNum(r, c) - 1] = player;
        }
        _moves += 1;
        jump(sqNum(r, c));
    }

    @Override
    void addSpot(Color player, int n) {
        _history.add(new MutableBoard(this, true));
        _spots[n - 1] += 1;
        if (color(n) == WHITE) {
            _colors[n - 1] = player;
        }
        _moves += 1;
        jump(n);
    }

    @Override
    void set(int r, int c, int num, Color player) {
        if (exists(r, c)) {
            if (num == 0) {
                _spots[sqNum(r, c) - 1] = 0;
                _colors[sqNum(r, c) - 1] = WHITE;
            } else {
                _spots[sqNum(r, c) - 1] = num;
                _colors[sqNum(r, c) - 1] = player;
            }
        }
    }

    @Override
    void set(int n, int num, Color player) {
        if (exists(n)) {
            if (num == 0) {
                _spots[n - 1] = 0;
                _colors[n - 1] = WHITE;
            } else {
                _spots[n - 1] = num;
                _colors[n - 1] = player;
            }
        }
    }

    @Override
    void setMoves(int num) {
        assert num > 0;
        _moves = num;
    }

    @Override
    void undo() {
        if (_history.size() > 0) {
            copy(_history.remove(_history.size() - 1));
        }
    }

    /** Return true if S is overfull. Do all jumping on this board, assuming
     *  that initially, S is the only square that might be over-full. */
    private boolean jump(int S) {
        if (exists(S) && spots(S) > neighbors(S)) {
            _spots[S - 1] -= neighbors(S);
            Color player = _colors[S - 1];
            int[] neighbors = new int[]{S - 1, S + 1, S - size(), S + size()};
            for (int i = 0; i < neighbors.length; i += 1) {
                if (exists(neighbors[i])) {
                    set(neighbors[i], spots(neighbors[i]) + 1, player);
                }
            }
            return true;
        }
        return false;
    }

    /** Recursively jumps all the overfull neighbors of S. */
    private void jumpHelper(int S) {
        if (getWinner() != null) {
            return;
        }
        int[] neighbors = new int[]{S - 1, S + 1, S - size(), S + size()};
        for (int j = 0; j < neighbors.length; j += 1) {
            if (exists(neighbors[j]) && jump(neighbors[j])) {
                jumpHelper(neighbors[j]);
            }
        }
    }

    /** Returns the array containing spots. */
    public int[] getSpots() {
        return _spots;
    }

    /** Returns the array containing colors. */
    public Color[] getColors() {
        return _colors;
    }

    /** Returns the ArrayList containing the history. */
    public ArrayList<Board> getHistory() {
        return _history;
    }

    /** Total combined number of moves by both sides. */
    protected int _moves;
    /** Convenience variable: size of board (squares along one edge). */
    private int _N;
    /** An array of spots on the board. */
    private int[] _spots;
    /** An array of colors on the board. */
    private Color[] _colors;
    /** A history of changes to the board. */
    private ArrayList<Board> _history;

}

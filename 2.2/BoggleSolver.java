
public class BoggleSolver
{
    private Trie<Integer> dict;
    private boolean[][] marked;
    private int m, n;
    private SET<String> q;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new Trie<Integer>();
        for (String s : dictionary)
            dict.put(s, 0);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        m = board.rows();
        n = board.cols();
        marked = new boolean[m][n];
        q = new SET<String>();

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                findValidWords(board, i, j, ""); //find valid words starting at board[i][j]
            }
        }
        return q;
    }
    //dfs
    private void findValidWords(BoggleBoard board, int i, int j, String pre) {
        if (!isValidPos(i, j) || marked[i][j]) return;
        marked[i][j] = true;
        char c = board.getLetter(i, j);
        String s = pre + ((c == 'Q') ? "QU" : c);

        if (!dict.containsPrefix(s)) { 
            marked[i][j] = false; 
            return; 
        }

        if (s.length() >= 3 && dict.contains(s)) q.add(s); //find one word
        
        //now do it for 8 neighbors:
        findValidWords(board, i,   j+1, s); //E
        findValidWords(board, i-1, j+1, s); //NE
        findValidWords(board, i-1, j,   s); //N
        findValidWords(board, i-1, j-1, s); //NW
        findValidWords(board, i,   j-1, s); //W
        findValidWords(board, i+1, j-1, s); //SW
        findValidWords(board, i+1, j,   s); //S
        findValidWords(board, i+1, j+1, s); //SE

        marked[i][j] = false;
    }

    private boolean isValidPos(int i, int j) {
        return 0 <= i && i < m && 0 <= j && j < n;
    }
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dict.contains(word)) return score(word.length());
        else return 0;
    }

    private int score(int length) {
        if      (length <= 2) return 0;
        else if (length <= 4) return 1;
        else if (length == 5) return 2;
        else if (length == 6) return 3;
        else if (length == 7) return 5;
        else                  return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}

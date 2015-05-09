public class Solver {

    private MinPQ<Node> pq1, pq2; //pq1 for board, pq2 for swapped
    private Stack<Board> sol; //solution
    private int minCount = -1;
    private Node goal = null; //final node
    private boolean solvable = false;

    private class Node implements Comparable<Node> {
        private final Board board; //board configuration
        private final int count; //number of moves
        private final Node prev; //previous node

        public Node(Board board, int count, Node prev) {
            this.board = board;
            this.count = count;
            this.prev = prev;
        }

        private int priority() {
            return board.manhattan() + count;
            //return board.hamming() + count;
        }

        public int compareTo(Node that)
        {
            int pri = this.priority();
            int pri2 = that.priority();
            if (pri < pri2) return -1;
            else if (pri > pri2) return 1;
            else return 0;
        }
    }

    private Board getBoard(Node node) {
        if(node == null) return null;
        return node.board;
    }

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        //two synchronized PQs, one for original board, one for swapped board
        pq1 = new MinPQ<Node>();
        pq2 = new MinPQ<Node>();

        pq1.insert(new Node(initial, 0, null));               
        pq2.insert(new Node(initial.twin(), 0, null));

        Node curr1 = null, curr2 = null;
        while (!pq1.isEmpty() && !pq2.isEmpty()) {
            curr1 = pq1.delMin();
            curr2 = pq2.delMin();

            if (curr1.board.isGoal()) { 
                solvable = true;
                minCount = curr1.count;
                goal = curr1;             
                return;
            } else if (curr2.board.isGoal()) {
                solvable = false;
                return;
            }

            for (Board bd : curr1.board.neighbors()) {
                if (!bd.equals(getBoard(curr1.prev))) //avoid already searched
                    pq1.insert(new Node(bd, curr1.count+1, curr1));
            }
            for (Board bd : curr2.board.neighbors()) {
                if (!bd.equals(getBoard(curr2.prev))) //avoid already searched
                    pq2.insert(new Node(bd, curr2.count+1, curr2));
            }
        }       
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return solvable;
    }
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        return minCount;
    }
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        //construct solution
        if (goal == null) return null;

        sol = new Stack<Board>();     
        Node n = goal;
        while (n != null) {
            sol.push(n.board);
            n = n.prev;
        }
        return sol;
    }
    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

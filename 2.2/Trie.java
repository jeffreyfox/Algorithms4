import java.util.LinkedList;

public class Trie<Value> {
    
    private static class Node { //has to add "static", otherwise runtime error. Don't know why
        private Object val;
        private Node[] links = new Node[R];
    }
    
    private static int R = 26;
    private Node root = new Node();

    // put key-value pair into the table
    public void put(String key, Value val) {
        root = put(root, key, 0, val);
    }

    // update the node associated with Key, searching from subtree x and starting at Key[d ..], return updated node x
    private Node put(Node x, String key, int d, Value val) {
        if (x == null) x = new Node();  //no node exist, create
        if (d == key.length()) x.val = val; //update node value
        else {
            int c = key.charAt(d) - 'A';
            x.links[c] = put(x.links[c], key, d+1, val);
        }
        return x; //return updated node
    }

    // get value associated with key
    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null; //node does not exist
        else return (Value) x.val; //found node, but key could be null
    }

    // return the node associated with Key, searching from subtree x and starting at Key[d ..] 
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x; //examined all characters in key, return node
        int c = key.charAt(d) - 'A';
        return get(x.links[c], key, d+1);
    }

    // whether table contains key?
    public boolean contains(String key) {
        return get(key) != null;
    }

    // whether table contains any key with prefix pre?
    public boolean containsPrefix(String pre) {
        return get(root, pre, 0) != null;
    }
}

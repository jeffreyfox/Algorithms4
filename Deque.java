import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int N; //size
    private Item[] a; //array
    private int head, tail; //inclusive
    
    public Deque()                           // construct an empty deque
    { 
        N = 0;
        a = (Item[]) new Object[1];
        head = -1;
        tail = -1;
    }
    public boolean isEmpty()                 // is the deque empty?
    {      return N == 0;  }
    public int size()                        // return the number of items on the deque
    {      return N;    }
    private boolean isFull()
    {      return N == a.length;    }
    private boolean isQuarterFull()
    {      return N > 0 && N == a.length/4;    }
    private void resize(int cap)
    {
        Item[] tmp = (Item[]) new Object[cap];
        int k, l;
        for (k = 0, l = head; k < N; k = k+1, l = (l+1) % a.length) {
            tmp[k] = a[l];
        }
        a = tmp;
        head = 0;
        tail = N-1;
    }
    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null) throw new java.lang.NullPointerException();
        if (isEmpty()) {
            head = 0;
            tail = 0;
        } else {        
            if (isFull()) resize(2*a.length);
            head = (head+a.length-1) % a.length;
        }
        a[head] = item;
        N++;
    }
    public void addLast(Item item)           // add the item to the end
    {
        if (item == null) throw new java.lang.NullPointerException();
        if (isEmpty()) {
            head = 0;
            tail = 0;
        } else {  
            if (isFull()) resize(2*a.length);
            tail = (tail+1) % a.length;
        }
        a[tail] = item;
        N++;
    }
    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = a[head];
        a[head] = null;
        N--;
        head = (head+1) % a.length;
        if (isQuarterFull()) resize(a.length/2);
        if (isEmpty()) {
            head = -1;
            tail = -1;
        }
        return item;
    }
    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = a[tail];
        a[tail] = null;
        N--;
        tail = (tail+a.length-1) % a.length;
        if (isQuarterFull()) resize(a.length/2);
        if (isEmpty()) {
            head = -1;
            tail = -1;
        }
        return item;
    }
    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new ForwardIterator();
    }
    private class ForwardIterator implements Iterator<Item>
    {
        private int i = head; //head of iterator
        private int c = N; //number of remaining elements
        public boolean hasNext() { return c > 0; }
        public Item next() { 
            if (!hasNext()) throw new java.util.NoSuchElementException();
            c--; 
            Item item = a[i];
            i = (i+1) % a.length;
            return item;
        }
        public void remove() 
        {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public static void main(String[] args)   // unit testing
    {
        Deque<Integer> dq = new Deque<Integer>();
        dq.addLast(1);
        dq.addLast(2);
        dq.addLast(3);
        dq.addFirst(4);
        dq.addFirst(5);
        dq.removeLast();

        for (int k : dq) 
            StdOut.print(k + " ");
        StdOut.println();
    }
}

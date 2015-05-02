import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N;
    private Item[] a; 
    private int head, tail;

    public RandomizedQueue()                 // construct an empty randomized queue
    {
        N = 0;
        a = (Item[]) new Object[1];
        head = -1;
        tail = -1;
    }
    public boolean isEmpty()                 // is the queue empty?
    {   return N == 0;   }
    public int size()                        // return the number of items on the queue
    {   return N;       }
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
    public void enqueue(Item item)           // add the item
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
    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        //find a random element and swap with head
        int idx = head + StdRandom.uniform(0, N);
        idx = idx % a.length;
        swap(idx, head);
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
    public Item sample()                     // return (but do not remove) a random item
    {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        //find a random element and swap with head
        int idx = head + StdRandom.uniform(0, N);
        idx = idx % a.length;
        return a[idx];
    }
    private void swap(int i, int j) 
    {
        if (i == j) return;
        Item t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new RandomIterator();
    }
    private class RandomIterator implements Iterator<Item> 
    {
        private int[] permute;
        private int i;
        public RandomIterator() 
        {
            permute = new int[N];
            for (int k = 0; k < N; ++k) 
                permute[k] = k;
            StdRandom.shuffle(permute);
            i = 0;
        }
        public boolean hasNext() { return i < N; }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            int idx = (head+permute[i]) % N;
            i++;
            return a[idx];
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
    public static void main(String[] args)   // unit testing
    {
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
      //  q.removeLast();

        for (int k : q) 
            StdOut.print(k + " ");
        StdOut.println();
    }
}

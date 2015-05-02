public class Subset {
   public static void main(String[] args)
   {
       RandomizedQueue<String> q = new RandomizedQueue<String>();
       
       int k = Integer.parseInt(args[0]);
       while (!StdIn.isEmpty()) {
           String s = StdIn.readString();
           q.enqueue(s);
       }
       while (k > 0) {
           StdOut.println(q.dequeue());
           k--;
       }
   }
}

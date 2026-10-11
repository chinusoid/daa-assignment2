public interface IntQueue {
    void enqueue(int value);
    int dequeue();
    int peek();
    int size();
    boolean isEmpty();
}

public interface IntDeque {
    void addFirst(int value);
    void addLast(int value);
    int removeFirst();
    int removeLast();
    int peekFirst();
    int peekLast();
    int size();
    boolean isEmpty();
}

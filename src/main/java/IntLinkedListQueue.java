import java.util.NoSuchElementException;

public class IntLinkedListQueue implements IntQueue {
    private final IntLinkedList elements;

    public IntLinkedListQueue() {
        elements = new IntLinkedList();
    }

    @Override
    public void enqueue(int value) {
        elements.add(value);
    }

    @Override
    public int dequeue() {
        checkNotEmpty();
        return elements.remove(0);
    }

    @Override
    public int peek() {
        checkNotEmpty();
        return elements.get(0);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    private void checkNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
    }
}

import java.util.NoSuchElementException;

public class IntLinkedListDeque implements IntDeque {
    private final IntLinkedList elements;

    public IntLinkedListDeque() {
        elements = new IntLinkedList();
    }

    @Override
    public void addFirst(int value) {
        elements.add(0, value);
    }

    @Override
    public void addLast(int value) {
        elements.add(value);
    }

    @Override
    public int removeFirst() {
        checkNotEmpty();
        return elements.remove(0);
    }

    @Override
    public int removeLast() {
        checkNotEmpty();
        return elements.remove(elements.size() - 1);
    }

    @Override
    public int peekFirst() {
        checkNotEmpty();
        return elements.get(0);
    }

    @Override
    public int peekLast() {
        checkNotEmpty();
        return elements.get(elements.size() - 1);
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
            throw new NoSuchElementException("Deque is empty");
        }
    }
}

import java.util.NoSuchElementException;

public class IntArrayStack implements IntStack {
    private final IntArray elements;

    public IntArrayStack() {
        elements = new IntArray();
    }

    @Override
    public void push(int value) {
        elements.add(value);
    }

    @Override
    public int pop() {
        checkNotEmpty();
        return elements.remove(elements.size() - 1);
    }

    @Override
    public int peek() {
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
            throw new NoSuchElementException("Stack is empty");
        }
    }
}

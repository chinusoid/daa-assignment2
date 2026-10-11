import java.util.NoSuchElementException;

public class IntArrayDeque implements IntDeque {
    private IntArray elements;
    private int head;
    private int size;

    public IntArrayDeque() {
        elements = new IntArray();
        elements.add(new int[8]);
    }

    @Override
    public void addFirst(int value) {
        ensureCapacity();
        head = (head - 1 + elements.size()) % elements.size();
        elements.set(head, value);
        size++;
    }

    @Override
    public void addLast(int value) {
        ensureCapacity();
        elements.set((head + size) % elements.size(), value);
        size++;
    }

    @Override
    public int removeFirst() {
        checkNotEmpty();
        int value = elements.get(head);
        elements.set(head, 0);
        head = (head + 1) % elements.size();
        size--;
        return value;
    }

    @Override
    public int removeLast() {
        checkNotEmpty();
        int index = (head + size - 1) % elements.size();
        int value = elements.get(index);
        elements.set(index, 0);
        size--;
        return value;
    }

    @Override
    public int peekFirst() {
        checkNotEmpty();
        return elements.get(head);
    }

    @Override
    public int peekLast() {
        checkNotEmpty();
        return elements.get((head + size - 1) % elements.size());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(elements.get((head + i) % elements.size()));
        }
        return result.append(']').toString();
    }

    private void ensureCapacity() {
        if (size < elements.size()) {
            return;
        }

        int capacity = elements.size();
        IntArray resized = new IntArray();
        for (int i = 0; i < size; i++) {
            resized.add(elements.get((head + i) % capacity));
        }
        resized.add(new int[capacity]);
        elements = resized;
        head = 0;
    }

    private void checkNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
    }
}

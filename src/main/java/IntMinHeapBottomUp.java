import java.util.NoSuchElementException;

public class IntMinHeapBottomUp implements IntQueue {
    private final IntArray elements;

    public IntMinHeapBottomUp() {
        elements = new IntArray();
    }

    public IntMinHeapBottomUp(int[] values) {
        this();
        elements.add(values);
        for (int i = elements.size() / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override
    public void enqueue(int value) {
        elements.add(value);
        siftUp(elements.size() - 1);
    }

    @Override
    public int dequeue() {
        checkNotEmpty();
        int minimum = elements.get(0);
        int last = elements.remove(elements.size() - 1);
        if (!elements.isEmpty()) {
            elements.set(0, last);
            siftDown(0);
        }
        return minimum;
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

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (elements.get(parent) <= elements.get(index)) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (index < elements.size() / 2) {
            int child = index * 2 + 1;
            int right = child + 1;
            if (right < elements.size() && elements.get(right) < elements.get(child)) {
                child = right;
            }
            if (elements.get(index) <= elements.get(child)) {
                break;
            }
            swap(index, child);
            index = child;
        }
    }

    private void swap(int first, int second) {
        int value = elements.get(first);
        elements.set(first, elements.get(second));
        elements.set(second, value);
    }

    private void checkNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
    }
}

public class IntArray {
    private int[] elements;
    private int size;

    public IntArray() {
        elements = new int[8];
    }

    public void add(int value) {
        ensureCapacity(size + 1);
        elements[size++] = value;
    }

    public void add(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }

        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = value;
        size++;
    }

    public int get(int index) {
        checkIndex(index);
        return elements[index];
    }

    public void set(int index, int value) {
        checkIndex(index);
        elements[index] = value;
    }

    public int remove(int index) {
        checkIndex(index);
        int value = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = 0;
        return value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int[] values) {
        ensureCapacity(size + values.length);
        for (int value : values) {
            elements[size++] = value;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(elements[i]);
        }
        return result.append(']').toString();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
    }

    private void ensureCapacity(int requiredCapacity) {
        if (requiredCapacity <= elements.length) {
            return;
        }

        int capacity = Math.max(requiredCapacity, elements.length * 2);
        int[] resized = new int[capacity];
        for (int i = 0; i < size; i++) {
            resized[i] = elements[i];
        }
        elements = resized;
    }
}

public class IntLinkedList {
    Node head;
    Node tail;
    int size;

    static class Node {
        int value;
        Node next;
        Node prev;

        Node() {
            this(0);
        }

        Node(int value) {
            this(value, null, null);
        }

        Node(int value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    public IntLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void add(int value) {
        add(size, value);
    }

    public void add(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }

        if (index == size) {
            Node node = new Node(value, tail, null);
            if (tail == null) {
                head = node;
            } else {
                tail.next = node;
            }
            tail = node;
        } else {
            Node next = nodeAt(index);
            Node node = new Node(value, next.prev, next);
            if (next.prev == null) {
                head = node;
            } else {
                next.prev.next = node;
            }
            next.prev = node;
        }
        size++;
    }

    public int get(int index) {
        return nodeAt(index).value;
    }

    public void set(int index, int value) {
        nodeAt(index).value = value;
    }

    public int remove(int index) {
        Node node = nodeAt(index);
        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }
        if (node.next == null) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }
        size--;
        return node.value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (Node node = head; node != null; node = node.next) {
            if (node != head) {
                result.append(", ");
            }
            result.append(node.value);
        }
        return result.append(']').toString();
    }

    private Node nodeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }

        if (index < size / 2) {
            Node node = head;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        }

        Node node = tail;
        for (int i = size - 1; i > index; i--) {
            node = node.prev;
        }
        return node;
    }
}

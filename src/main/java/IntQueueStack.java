import java.util.NoSuchElementException;

public class IntQueueStack implements IntStack {
    private IntLinkedListQueue queue;
    private IntLinkedListQueue buffer;

    public IntQueueStack() {
        queue = new IntLinkedListQueue();
        buffer = new IntLinkedListQueue();
    }

    @Override
    public void push(int value) {
        queue.enqueue(value);
    }

    @Override
    public int pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }

        while (queue.size() > 1) {
            buffer.enqueue(queue.dequeue());
        }
        int value = queue.dequeue();
        IntLinkedListQueue temporary = queue;
        queue = buffer;
        buffer = temporary;
        return value;
    }

    @Override
    public int peek() {
        int value = pop();
        push(value);
        return value;
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}

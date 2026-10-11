import java.util.NoSuchElementException;

public class IntStackQueue implements IntQueue {
    private final IntArrayStack incoming;
    private final IntArrayStack outgoing;

    public IntStackQueue() {
        incoming = new IntArrayStack();
        outgoing = new IntArrayStack();
    }

    @Override
    public void enqueue(int value) {
        incoming.push(value);
    }

    @Override
    public int dequeue() {
        prepareOutgoing();
        return outgoing.pop();
    }

    @Override
    public int peek() {
        prepareOutgoing();
        return outgoing.peek();
    }

    @Override
    public int size() {
        return incoming.size() + outgoing.size();
    }

    @Override
    public boolean isEmpty() {
        return incoming.isEmpty() && outgoing.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        appendStack(result, outgoing, false);
        appendStack(result, incoming, true);
        return result.append(']').toString();
    }

    private void prepareOutgoing() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        if (outgoing.isEmpty()) {
            while (!incoming.isEmpty()) {
                outgoing.push(incoming.pop());
            }
        }
    }

    private void appendStack(StringBuilder result, IntArrayStack stack, boolean bottomFirst) {
        int[] values = new int[stack.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = stack.pop();
        }
        for (int i = values.length - 1; i >= 0; i--) {
            stack.push(values[i]);
        }
        for (int i = 0; i < values.length; i++) {
            if (result.length() > 1) {
                result.append(", ");
            }
            int index = bottomFirst ? values.length - 1 - i : i;
            result.append(values[index]);
        }
    }
}

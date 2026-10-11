import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntQueueTest {
    static Stream<Arguments> queues() {
        return Stream.of(
                Arguments.of("IntLinkedListQueue", new IntLinkedListQueue()),
                Arguments.of("IntStackQueue", new IntStackQueue())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("queues")
    void startsEmptyAndRejectsReads(String name, IntQueue queue) {
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertEquals("[]", queue.toString());
        assertThrows(NoSuchElementException.class, queue::dequeue);
        assertThrows(NoSuchElementException.class, queue::peek);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("queues")
    void removesValuesInFifoOrder(String name, IntQueue queue) {
        int[] values = {7, 8, 6, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 7};
        for (int value : values) {
            queue.enqueue(value);
        }

        assertEquals(values.length, queue.size());
        assertFalse(queue.isEmpty());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], queue.dequeue());
            assertEquals(values.length - i - 1, queue.size());
        }
        assertTrue(queue.isEmpty());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("queues")
    void peekAndToStringPreserveOrderAfterMoreEnqueues(String name, IntQueue queue) {
        queue.enqueue(7);
        queue.enqueue(8);
        queue.enqueue(6);
        assertEquals(7, queue.peek());
        assertEquals(7, queue.dequeue());
        queue.enqueue(9);
        queue.enqueue(0);

        for (int i = 0; i < 5; i++) {
            assertEquals(8, queue.peek());
            assertEquals("[8, 6, 9, 0]", queue.toString());
            assertEquals(4, queue.size());
        }
        for (int value : new int[] {8, 6, 9, 0}) {
            assertEquals(value, queue.dequeue());
        }
        assertEquals("[]", queue.toString());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("queues")
    void supportsMixedOperationsAndReuse(String name, IntQueue queue) {
        for (int round = 0; round < 64; round++) {
            int base = round * 64;
            for (int i = 0; i < 32; i++) {
                queue.enqueue(base + i);
            }
            for (int i = 0; i < 16; i++) {
                assertEquals(base + i, queue.dequeue());
            }
            for (int i = 32; i < 64; i++) {
                queue.enqueue(base + i);
            }
            assertEquals(48, queue.size());
            for (int i = 16; i < 64; i++) {
                assertEquals(base + i, queue.peek());
                assertEquals(base + i, queue.dequeue());
            }
            assertTrue(queue.isEmpty());
        }
        assertThrows(NoSuchElementException.class, queue::dequeue);
        assertThrows(NoSuchElementException.class, queue::peek);
        queue.enqueue(17);
        assertEquals(17, queue.dequeue());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("queues")
    void keepsAllValuesAfterLargeEnqueues(String name, IntQueue queue) {
        int[] values = new int[4096];
        Random random = new Random(58);
        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextInt();
            queue.enqueue(values[i]);
        }

        for (int value : values) {
            assertEquals(value, queue.peek());
            assertEquals(value, queue.dequeue());
        }
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        assertEquals("[]", queue.toString());
    }
}

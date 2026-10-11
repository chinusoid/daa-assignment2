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

class IntDequeTest {
    static Stream<Arguments> deques() {
        return Stream.of(
                Arguments.of("IntArrayDeque", new IntArrayDeque()),
                Arguments.of("IntLinkedListDeque", new IntLinkedListDeque())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("deques")
    void startsEmptyAndRejectsReads(String name, IntDeque deque) {
        assertEquals(0, deque.size());
        assertTrue(deque.isEmpty());
        assertEquals("[]", deque.toString());
        assertThrows(NoSuchElementException.class, deque::removeFirst);
        assertThrows(NoSuchElementException.class, deque::removeLast);
        assertThrows(NoSuchElementException.class, deque::peekFirst);
        assertThrows(NoSuchElementException.class, deque::peekLast);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("deques")
    void supportsOperationsAtBothEnds(String name, IntDeque deque) {
        deque.addFirst(7);
        deque.addLast(8);
        deque.addFirst(6);
        deque.addLast(0);
        deque.addFirst(Integer.MIN_VALUE);
        deque.addLast(Integer.MAX_VALUE);

        assertEquals(6, deque.size());
        assertFalse(deque.isEmpty());
        assertEquals(Integer.MIN_VALUE, deque.removeFirst());
        assertEquals(Integer.MAX_VALUE, deque.removeLast());
        assertEquals(6, deque.removeFirst());
        assertEquals(0, deque.removeLast());
        assertEquals(7, deque.removeFirst());
        assertEquals(8, deque.removeLast());
        assertTrue(deque.isEmpty());

        deque.addLast(-17);
        assertEquals(-17, deque.removeFirst());
        deque.addFirst(17);
        assertEquals(17, deque.removeLast());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("deques")
    void peekAndToStringPreserveBothEnds(String name, IntDeque deque) {
        deque.addLast(7);
        deque.addLast(8);
        deque.addLast(6);
        assertEquals(7, deque.removeFirst());
        deque.addFirst(5);
        deque.addLast(9);

        for (int i = 0; i < 5; i++) {
            assertEquals(5, deque.peekFirst());
            assertEquals(9, deque.peekLast());
            assertEquals("[5, 8, 6, 9]", deque.toString());
            assertEquals(4, deque.size());
        }
        for (int value : new int[] {5, 8, 6, 9}) {
            assertEquals(value, deque.removeFirst());
        }
        assertEquals("[]", deque.toString());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("deques")
    void preservesOrderThroughWraparoundGrowthAndReuse(String name, IntDeque deque) {
        for (int round = 0; round < 8; round++) {
            for (int i = 0; i < 8; i++) {
                deque.addLast(i);
            }
            for (int i = 0; i < 6; i++) {
                assertEquals(i, deque.removeFirst());
            }
            for (int i = 8; i < 512; i++) {
                deque.addLast(i);
            }
            for (int i = 0; i < 300; i++) {
                deque.addFirst(-i - 1);
            }
            assertEquals(806, deque.size());
            for (int i = 299; i >= 0; i--) {
                assertEquals(-i - 1, deque.removeFirst());
            }
            for (int i = 511; i >= 6; i--) {
                assertEquals(i, deque.removeLast());
            }
            assertTrue(deque.isEmpty());
            assertEquals(0, deque.size());
        }
        assertThrows(NoSuchElementException.class, deque::removeFirst);
        assertThrows(NoSuchElementException.class, deque::removeLast);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("deques")
    void keepsAllValuesWhenAlternatingBetweenBothEnds(String name, IntDeque deque) {
        int[] values = new int[2048];
        Random random = new Random(71);
        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextInt();
            if (i % 2 == 0) {
                deque.addFirst(values[i]);
            } else {
                deque.addLast(values[i]);
            }
        }

        for (int i = values.length - 2; i >= 0; i -= 2) {
            assertEquals(values[i], deque.peekFirst());
            assertEquals(values[i], deque.removeFirst());
        }
        for (int i = values.length - 1; i >= 1; i -= 2) {
            assertEquals(values[i], deque.peekLast());
            assertEquals(values[i], deque.removeLast());
        }
        assertTrue(deque.isEmpty());
        assertEquals("[]", deque.toString());
    }
}

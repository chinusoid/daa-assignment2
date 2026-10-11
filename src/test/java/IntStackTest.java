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

class IntStackTest {
    static Stream<Arguments> stacks() {
        return Stream.of(
                Arguments.of("IntArrayStack", new IntArrayStack()),
                Arguments.of("IntQueueStack", new IntQueueStack())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("stacks")
    void startsEmptyAndRejectsReads(String name, IntStack stack) {
        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());
        assertEquals("[]", stack.toString());
        assertThrows(NoSuchElementException.class, stack::pop);
        assertThrows(NoSuchElementException.class, stack::peek);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("stacks")
    void removesValuesInLifoOrder(String name, IntStack stack) {
        int[] values = {7, 8, 6, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 7};
        for (int value : values) {
            stack.push(value);
        }

        assertEquals(values.length, stack.size());
        assertFalse(stack.isEmpty());
        for (int i = values.length - 1; i >= 0; i--) {
            assertEquals(values[i], stack.pop());
            assertEquals(i, stack.size());
        }
        assertTrue(stack.isEmpty());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("stacks")
    void peekAndToStringPreserveTheStack(String name, IntStack stack) {
        stack.push(7);
        stack.push(8);
        stack.push(6);

        for (int i = 0; i < 5; i++) {
            assertEquals(6, stack.peek());
            assertEquals("[7, 8, 6]", stack.toString());
            assertEquals(3, stack.size());
        }
        assertEquals(6, stack.pop());
        assertEquals(8, stack.pop());
        assertEquals(7, stack.pop());
        assertEquals("[]", stack.toString());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("stacks")
    void supportsMixedOperationsAndReuse(String name, IntStack stack) {
        for (int round = 0; round < 256; round++) {
            stack.push(round);
            stack.push(-round);
            assertEquals(-round, stack.pop());
            stack.push(round + 7);
            assertEquals(round + 7, stack.peek());
            assertEquals(round + 7, stack.pop());
            assertEquals(round, stack.pop());
            assertTrue(stack.isEmpty());
            assertEquals(0, stack.size());
        }
        assertThrows(NoSuchElementException.class, stack::pop);
        assertThrows(NoSuchElementException.class, stack::peek);
        stack.push(17);
        assertEquals(17, stack.pop());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("stacks")
    void keepsAllValuesAfterLargePushes(String name, IntStack stack) {
        int[] values = new int[2048];
        Random random = new Random(43);
        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextInt();
            stack.push(values[i]);
        }

        for (int i = values.length - 1; i >= 0; i--) {
            if (i % 31 == 0) {
                assertEquals(values[i], stack.peek());
            }
            assertEquals(values[i], stack.pop());
        }
        assertTrue(stack.isEmpty());
        assertEquals("[]", stack.toString());
    }
}

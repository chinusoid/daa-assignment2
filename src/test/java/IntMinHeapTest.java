import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntMinHeapTest {
    static Stream<Arguments> heaps() {
        return Stream.of(
                Arguments.of("IntMinHeapTopDown", new IntMinHeapTopDown(),
                        (Function<int[], IntQueue>) IntMinHeapTopDown::new),
                Arguments.of("IntMinHeapBottomUp", new IntMinHeapBottomUp(),
                        (Function<int[], IntQueue>) IntMinHeapBottomUp::new)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("heaps")
    void startsEmptyAndRejectsReads(String name, IntQueue heap, Function<int[], IntQueue> factory) {
        assertEquals(0, heap.size());
        assertTrue(heap.isEmpty());
        assertEquals("[]", heap.toString());
        assertThrows(NoSuchElementException.class, heap::dequeue);
        assertThrows(NoSuchElementException.class, heap::peek);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("heaps")
    void removesMinimumValuesIncludingDuplicates(String name, IntQueue heap,
                                                Function<int[], IntQueue> factory) {
        int[] values = {7, 8, 6, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 7, -1, 0};
        for (int value : values) {
            heap.enqueue(value);
            assertHeapProperty(heap);
        }

        int size = heap.size();
        String representation = heap.toString();
        for (int i = 0; i < 5; i++) {
            assertEquals(Integer.MIN_VALUE, heap.peek());
            assertEquals(size, heap.size());
            assertEquals(representation, heap.toString());
        }
        assertSortedRemoval(heap, values);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("heaps")
    void constructorCopiesValuesAndProducesAHeap(String name, IntQueue emptyHeap,
                                                Function<int[], IntQueue> factory) {
        int[] values = {9, 4, 7, 1, 6, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
        int[] original = values.clone();
        IntQueue heap = factory.apply(values);

        assertArrayEquals(original, values);
        assertHeapProperty(heap);
        Arrays.fill(values, 42);
        assertSortedRemoval(heap, original);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("heaps")
    void supportsInterleavedInsertionsRemovalsAndReuse(String name, IntQueue heap,
                                                     Function<int[], IntQueue> factory) {
        Random random = new Random(89);
        for (int round = 0; round < 10; round++) {
            int[] first = new int[200];
            for (int i = 0; i < first.length; i++) {
                first[i] = random.nextInt();
                heap.enqueue(first[i]);
            }
            Arrays.sort(first);
            for (int i = 0; i < 100; i++) {
                assertEquals(first[i], heap.peek());
                assertEquals(first[i], heap.dequeue());
            }

            int[] remaining = new int[300];
            System.arraycopy(first, 100, remaining, 0, 100);
            for (int i = 100; i < remaining.length; i++) {
                remaining[i] = random.nextInt();
                heap.enqueue(remaining[i]);
            }
            assertHeapProperty(heap);
            assertSortedRemoval(heap, remaining);
        }
        assertThrows(NoSuchElementException.class, heap::dequeue);
        assertThrows(NoSuchElementException.class, heap::peek);
        heap.enqueue(17);
        assertEquals(17, heap.dequeue());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("heaps")
    void buildsHeapsFromSortedReverseAndRandomArrays(String name, IntQueue emptyHeap,
                                                    Function<int[], IntQueue> factory) {
        Random random = new Random(103);
        for (int length : new int[] {0, 1, 2, 3, 7, 8, 9, 31, 32, 33, 1024}) {
            for (int pattern = 0; pattern < 4; pattern++) {
                int[] values = new int[length];
                for (int i = 0; i < length; i++) {
                    values[i] = pattern == 0 ? i : pattern == 1 ? length - i
                            : pattern == 2 ? random.nextInt() : 7;
                }
                IntQueue heap = factory.apply(values);
                assertHeapProperty(heap);
                assertSortedRemoval(heap, values);
            }
        }
    }

    private void assertSortedRemoval(IntQueue heap, int[] values) {
        int[] sorted = values.clone();
        Arrays.sort(sorted);
        assertEquals(sorted.length, heap.size());
        for (int value : sorted) {
            assertEquals(value, heap.peek());
            assertEquals(value, heap.dequeue());
        }
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
        assertEquals("[]", heap.toString());
    }

    private void assertHeapProperty(IntQueue heap) {
        String representation = heap.toString();
        assertTrue(representation.startsWith("[") && representation.endsWith("]"));
        String body = representation.substring(1, representation.length() - 1);
        String[] parts = body.isEmpty() ? new String[0] : body.split(", ");
        assertEquals(heap.size(), parts.length);

        int[] values = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            values[i] = Integer.parseInt(parts[i]);
            if (i > 0) {
                assertTrue(values[(i - 1) / 2] <= values[i]);
            }
        }
    }
}

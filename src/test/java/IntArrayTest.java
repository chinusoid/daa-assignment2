import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntArrayTest {
    @Test
    void startsEmpty() {
        IntArray array = new IntArray();

        assertEquals(0, array.size());
        assertTrue(array.isEmpty());
        assertEquals("[]", array.toString());
    }

    @Test
    void supportsIndexedInsertionUpdateAndRemoval() {
        IntArray array = new IntArray();
        array.add(7);
        array.add(8);
        array.add(6);
        array.add(0, 3);
        array.add(2, 5);
        array.add(array.size(), 9);
        array.set(3, -8);

        assertEquals("[3, 7, 5, -8, 6, 9]", array.toString());
        assertEquals(-8, array.get(3));
        assertEquals(3, array.remove(0));
        assertEquals(-8, array.remove(2));
        assertEquals(9, array.remove(array.size() - 1));
        assertEquals("[7, 5, 6]", array.toString());
        assertEquals(3, array.size());
        assertFalse(array.isEmpty());
    }

    @Test
    void growsAndCopiesBulkValues() {
        IntArray array = new IntArray();
        array.add(new int[0]);
        assertTrue(array.isEmpty());

        int[] values = {Integer.MIN_VALUE, -1, 0, Integer.MAX_VALUE};
        array.add(values);
        values[0] = 17;
        assertEquals(Integer.MIN_VALUE, array.get(0));

        int[] batch = new int[4096];
        for (int i = 0; i < batch.length; i++) {
            batch[i] = i * 7;
        }
        array.add(batch);
        array.add(new int[0]);
        array.add(-99);

        assertEquals(4101, array.size());
        for (int i = 0; i < batch.length; i++) {
            assertEquals(batch[i], array.get(i + 4));
        }
        assertEquals(-99, array.get(array.size() - 1));
    }

    @Test
    void rejectsInvalidIndexesWithoutChangingValues() {
        IntArray array = new IntArray();
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.set(0, 5));

        array.add(new int[] {7, 8, 6});
        for (int index : new int[] {-1, 3, 4}) {
            assertThrows(IndexOutOfBoundsException.class, () -> array.get(index));
            assertThrows(IndexOutOfBoundsException.class, () -> array.set(index, 5));
            assertThrows(IndexOutOfBoundsException.class, () -> array.remove(index));
        }
        assertThrows(IndexOutOfBoundsException.class, () -> array.add(-1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> array.add(4, 5));
        assertEquals("[7, 8, 6]", array.toString());
    }

    @Test
    void preservesValuesDuringRepeatedEditsAndCanBeReused() {
        IntArray array = new IntArray();
        int[] expected = new int[1024];
        Random random = new Random(12);
        for (int i = 0; i < expected.length; i++) {
            expected[i] = random.nextInt();
            array.add(expected[i]);
        }
        for (int i = 0; i < 5000; i++) {
            int index = random.nextInt(expected.length);
            int value = random.nextInt();
            array.set(index, value);
            expected[index] = value;
        }

        int first = 0;
        int last = expected.length - 1;
        while (first <= last) {
            assertEquals(expected[first++], array.remove(0));
            if (first <= last) {
                assertEquals(expected[last--], array.remove(array.size() - 1));
            }
        }
        assertTrue(array.isEmpty());
        assertEquals("[]", array.toString());
        array.add(17);
        assertEquals(17, array.remove(0));
        assertEquals(0, array.size());
    }
}

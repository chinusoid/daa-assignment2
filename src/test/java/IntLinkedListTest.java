import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntLinkedListTest {
    @Test
    void startsEmpty() {
        IntLinkedList list = new IntLinkedList();

        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        assertEquals("[]", list.toString());
        assertNull(list.head);
        assertNull(list.tail);
    }

    @Test
    void supportsInsertionAndUpdateAtBothEndsAndInTheMiddle() {
        IntLinkedList list = new IntLinkedList();
        list.add(7);
        list.add(8);
        list.add(6);
        list.add(0, Integer.MIN_VALUE);
        list.add(2, 0);
        list.add(list.size(), Integer.MAX_VALUE);
        list.set(3, -8);

        assertEquals("[-2147483648, 7, 0, -8, 6, 2147483647]", list.toString());
        assertEquals(Integer.MIN_VALUE, list.get(0));
        assertEquals(-8, list.get(3));
        assertEquals(Integer.MAX_VALUE, list.get(5));
        assertEquals(6, list.size());
        assertFalse(list.isEmpty());
        assertNull(list.head.prev);
        assertNull(list.tail.next);
    }

    @Test
    void removesHeadMiddleAndTailAndCanBeReused() {
        IntLinkedList list = new IntLinkedList();
        for (int value : new int[] {7, 8, 6, 5}) {
            list.add(value);
        }

        assertEquals(7, list.remove(0));
        assertEquals(6, list.remove(1));
        assertEquals(5, list.remove(list.size() - 1));
        assertEquals("[8]", list.toString());
        assertSame(list.head, list.tail);
        assertEquals(8, list.remove(0));
        assertTrue(list.isEmpty());
        assertNull(list.head);
        assertNull(list.tail);

        list.add(0, 17);
        assertEquals(17, list.get(0));
        assertSame(list.head, list.tail);
        assertEquals(17, list.remove(0));
        assertEquals("[]", list.toString());
    }

    @Test
    void rejectsInvalidIndexesWithoutChangingValues() {
        IntLinkedList list = new IntLinkedList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(0, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));

        list.add(7);
        list.add(8);
        for (int index : new int[] {-1, 2, 3}) {
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
            assertThrows(IndexOutOfBoundsException.class, () -> list.set(index, 5));
            assertThrows(IndexOutOfBoundsException.class, () -> list.remove(index));
        }
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(3, 5));
        assertEquals("[7, 8]", list.toString());
    }

    @Test
    void maintainsForwardAndBackwardLinksAfterManyChanges() {
        IntLinkedList list = new IntLinkedList();
        int[] expected = new int[1024];
        Random random = new Random(27);
        for (int i = 0; i < expected.length; i++) {
            expected[i] = random.nextInt();
            list.add(expected[i]);
        }
        for (int i = 0; i < expected.length; i += 17) {
            expected[i] = -i;
            list.set(i, expected[i]);
        }

        IntLinkedList.Node node = list.head;
        IntLinkedList.Node previous = null;
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], list.get(i));
            assertEquals(expected[i], node.value);
            assertSame(previous, node.prev);
            previous = node;
            node = node.next;
        }
        assertNull(node);
        assertSame(previous, list.tail);

        node = list.tail;
        IntLinkedList.Node next = null;
        for (int i = expected.length - 1; i >= 0; i--) {
            assertEquals(expected[i], node.value);
            assertSame(next, node.next);
            next = node;
            node = node.prev;
        }
        assertNull(node);
        assertSame(next, list.head);

        for (int value : expected) {
            assertEquals(value, list.remove(0));
            if (!list.isEmpty()) {
                assertNull(list.head.prev);
                assertNull(list.tail.next);
            }
        }
        assertNull(list.head);
        assertNull(list.tail);
    }
}

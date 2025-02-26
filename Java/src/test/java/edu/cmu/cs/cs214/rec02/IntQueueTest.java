package edu.cmu.cs.cs214.rec02;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for IntQueue implementations.
 */
public class IntQueueTest {

    private ArrayIntQueue ArrayIntQueue;
    private List<Integer> testList;

    @Before
    public void setUp() {
        // Uncomment to test LinkedIntQueue
        // ArrayIntQueue = new LinkedIntQueue();
        
        // Uncomment to test ArrayIntQueue
        ArrayIntQueue = new ArrayIntQueue();

        testList = new ArrayList<>(List.of(1, 2, 3));
    }

    @Test
    public void testIsEmpty() {
        assertTrue("Queue should be empty initially", 
        ArrayIntQueue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        ArrayIntQueue.enqueue(5);
        assertFalse("Queue should not be empty after enqueue", 
        ArrayIntQueue.isEmpty());
    }

    @Test
    public void testPeekEmptyQueue() {
        assertTrue(ArrayIntQueue.isEmpty());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> ArrayIntQueue.peek());
        assertEquals("Queue is empty", thrown.getMessage());
    }

    @Test
    public void testPeekNoEmptyQueue() {
        ArrayIntQueue.enqueue(10);
        assertEquals("Peek should return first element", 10, (int) ArrayIntQueue.peek());
        ArrayIntQueue.enqueue(20);
        assertEquals("Peek should still return first element", 10, (int) ArrayIntQueue.peek()); // FIFO
    }

    @Test
    public void testEnqueue() {
        for (int i = 0; i < testList.size(); i++) {
            ArrayIntQueue.enqueue(testList.get(i));
            assertEquals("Peek should return first element", testList.get(0), ArrayIntQueue.peek());
            assertEquals("Size should increase after enqueue", i + 1, ArrayIntQueue.size());
        }
    }

    @Test
    public void testDequeue() {
        ArrayIntQueue.enqueue(1);
        ArrayIntQueue.enqueue(2);
        ArrayIntQueue.enqueue(3);

        assertEquals("Dequeue should return first element", 1, (int) ArrayIntQueue.dequeue());
        assertEquals("Dequeue should return next element", 2, (int) ArrayIntQueue.dequeue());
        assertEquals("Dequeue should return next element", 2, (int) ArrayIntQueue.dequeue());

        assertTrue("Queue should be empty after dequeuing all elements", ArrayIntQueue.isEmpty());
    }

    @Test
    public void testDequeueEmptyQueue() {
        assertNull("Dequeue on empty queue should return null", ArrayIntQueue.dequeue());
    }

    @Test
    public void testClear() {
        ArrayIntQueue.enqueue(1);
        ArrayIntQueue.enqueue(2);
        ArrayIntQueue.enqueue(3);

        assertFalse("Queue should not be empty before clearing", 
        ArrayIntQueue.isEmpty());

        ArrayIntQueue.clear();

        assertTrue("Queue should be empty after clearing", 
        ArrayIntQueue.isEmpty());
        assertEquals("Size should be 0 after clearing", 0,
        ArrayIntQueue.size());

        assertNull("Dequeue on cleared queue should return null", 
        ArrayIntQueue.dequeue());
    }

    @Test
    public void testOrderPreserved() {
        // ArrayIntQueue.enqueue(10);
        // ArrayIntQueue.enqueue(20);
        // ArrayIntQueue.enqueue(30);

        // assertEquals("First dequeued should be first enqueued", 10, (int) ArrayIntQueue.dequeue());
        // assertEquals("Second dequeued should be second enqueued", 20, (int) ArrayIntQueue.dequeue());
        // assertEquals("Third dequeued should be third enqueued", 30, (int) ArrayIntQueue.dequeue());
    }

    @Test
    public void testEnqueueResize() {
        // Assuming initial capacity is 10
        for (int i = 1; i <= 15; i++) {
            ArrayIntQueue.enqueue(i);
        }

        assertEquals("Size should be 15 after 15 enqueues", 15, ArrayIntQueue.size());
        assertEquals("Peek should return first enqueued element", 1, (int) ArrayIntQueue.peek());

        for (int i = 1; i <= 15; i++) {
            assertEquals("Dequeue should return elements in order", i, (int) ArrayIntQueue.dequeue());
        }

        assertTrue("Queue should be empty", ArrayIntQueue.isEmpty());
    }

    @Test
    public void testContent() throws IOException {
        InputStream in = new FileInputStream("src/test/resources/data.txt");
        try (Scanner scanner = new Scanner(in)) {
            scanner.useDelimiter("\\s*fish\\s*");

            List<Integer> correctResult = new ArrayList<>();
            while (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                correctResult.add(input);
                ArrayIntQueue.enqueue(input);
            }

            for (Integer result : correctResult) {
                assertEquals("Dequeued values should match enqueued values", result, ArrayIntQueue.dequeue());
            }
        }
    }
}

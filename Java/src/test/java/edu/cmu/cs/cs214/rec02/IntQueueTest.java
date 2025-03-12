package edu.cmu.cs.cs214.rec02;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for IntQueue implementations.
 */
public class IntQueueTest {

    private ArrayIntQueue arrayIntQueue; // Corrected variable name
    private List<Integer> testList;

    @Before
    public void setUp() {
        // Uncomment to test LinkedIntQueue
        // arrayIntQueue = new LinkedIntQueue();
        
        // Uncomment to test ArrayIntQueue
        arrayIntQueue = new ArrayIntQueue(); // Corrected variable name

        testList = new ArrayList<>(List.of(1, 2, 3));
    }

    @Test
    public void testIsEmpty() {
        assertTrue("Queue should be empty initially", arrayIntQueue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        arrayIntQueue.enqueue(5);
        assertFalse("Queue should not be empty after enqueue", arrayIntQueue.isEmpty());
    }

    @Test
    public void testPeekEmptyQueue() {
        assertTrue(arrayIntQueue.isEmpty());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> arrayIntQueue.peek());
        assertEquals("Queue is empty", thrown.getMessage());
    }

    @Test
    public void testPeekNoEmptyQueue() {
        arrayIntQueue.enqueue(10);
        assertEquals("Peek should return first element", 10, (int) arrayIntQueue.peek());
        arrayIntQueue.enqueue(20);
        assertEquals("Peek should still return first element", 10, (int) arrayIntQueue.peek()); // FIFO
    }

    @Test
    public void testEnqueue() {
        for (int i = 0; i < testList.size(); i++) {
            arrayIntQueue.enqueue(testList.get(i));
            assertEquals("Peek should return first element", testList.get(0), arrayIntQueue.peek());
            assertEquals("Size should increase after enqueue", i + 1, arrayIntQueue.size());
        }
    }

    @Test
    public void testDequeue() {
        arrayIntQueue.enqueue(1);
        arrayIntQueue.enqueue(2);
        arrayIntQueue.enqueue(3);

        assertEquals("Dequeue should return first element", 1, (int) arrayIntQueue.dequeue());
        assertEquals("Dequeue should return next element", 2, (int) arrayIntQueue.dequeue());
        assertEquals("Dequeue should return next element", 3, (int) arrayIntQueue.dequeue()); // Fixed this

        assertTrue("Queue should be empty after dequeuing all elements", arrayIntQueue.isEmpty());
    }

    @Test
    public void testDequeueEmptyQueue() {
        assertThrows(IllegalStateException.class, () -> arrayIntQueue.dequeue()); // Enforce exception
    }

    @Test
    public void testClear() {
        arrayIntQueue.enqueue(1);
        arrayIntQueue.enqueue(2);
        arrayIntQueue.enqueue(3);

        assertFalse("Queue should not be empty before clearing", arrayIntQueue.isEmpty());

        arrayIntQueue.clear();

        assertTrue("Queue should be empty after clearing", arrayIntQueue.isEmpty());
        assertEquals("Size should be 0 after clearing", 0, arrayIntQueue.size());

        assertThrows(IllegalStateException.class, () -> arrayIntQueue.dequeue()); // Enforce exception
    }

    @Test
    public void testOrderPreserved() {
        arrayIntQueue.enqueue(10);
        arrayIntQueue.enqueue(20);
        arrayIntQueue.enqueue(30);

        assertEquals("First dequeued should be first enqueued", 10, (int) arrayIntQueue.dequeue());
        assertEquals("Second dequeued should be second enqueued", 20, (int) arrayIntQueue.dequeue());
        assertEquals("Third dequeued should be third enqueued", 30, (int) arrayIntQueue.dequeue());
    }

    @Test
    public void testEnqueueResize() {
        // Assuming initial capacity is 10
        for (int i = 1; i <= 15; i++) {
            arrayIntQueue.enqueue(i);
        }

        assertEquals("Size should be 15 after 15 enqueues", 15, arrayIntQueue.size());
        assertEquals("Peek should return first enqueued element", 1, (int) arrayIntQueue.peek());

        for (int i = 1; i <= 15; i++) {
            assertEquals("Dequeue should return elements in order", i, (int) arrayIntQueue.dequeue());
        }

        assertTrue("Queue should be empty", arrayIntQueue.isEmpty());
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
                arrayIntQueue.enqueue(input);
            }

            for (Integer result : correctResult) {
                assertEquals("Dequeued values should match enqueued values", result, arrayIntQueue.dequeue());
            }
        }
    }
}

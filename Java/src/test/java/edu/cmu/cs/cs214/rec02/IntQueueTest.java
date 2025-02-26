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

    private IntQueue mQueue;
    private List<Integer> testList;

    @Before
    public void setUp() {
        // Uncomment to test LinkedIntQueue
        // mQueue = new LinkedIntQueue();
        
        // Uncomment to test ArrayIntQueue
        mQueue = new ArrayIntQueue();

        testList = new ArrayList<>(List.of(1, 2, 3));
    }

    @Test
    public void testIsEmpty() {
        assertTrue("Queue should be empty initially", 
        mQueue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        mQueue.enqueue(5);
        assertFalse("Queue should not be empty after enqueue", 
        mQueue.isEmpty());
    }

    @Test
    public void testPeekEmptyQueue() {
        assertTrue(mQueue.isEmpty());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> mQueue.peek());
        assertEquals("Queue is empty", thrown.getMessage());
    }

    @Test
    public void testPeekNoEmptyQueue() {
        mQueue.enqueue(10);
        assertEquals("Peek should return first element", 10, (int) mQueue.peek());
        mQueue.enqueue(20);
        assertEquals("Peek should still return first element", 10, (int) mQueue.peek()); // FIFO
    }

    @Test
    public void testEnqueue() {
        for (int i = 0; i < testList.size(); i++) {
            mQueue.enqueue(testList.get(i));
            assertEquals("Peek should return first element", testList.get(0), mQueue.peek());
            assertEquals("Size should increase after enqueue", i + 1, mQueue.size());
        }
    }

    @Test
    public void testDequeue() {
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.enqueue(3);

        assertEquals("Dequeue should return first element", 1, (int) mQueue.dequeue());
        assertEquals("Dequeue should return next element", 2, (int) mQueue.dequeue());
        assertEquals("Dequeue should return next element", 3, (int) mQueue.dequeue());

        assertTrue("Queue should be empty after dequeuing all elements", mQueue.isEmpty());
    }

    @Test
    public void testDequeueEmptyQueue() {
        assertNull("Dequeue on empty queue should return null", mQueue.dequeue());
    }

    @Test
    public void testClear() {
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.enqueue(3);

        assertFalse("Queue should not be empty before clearing", 
        mQueue.isEmpty());

        mQueue.clear();

        assertTrue("Queue should be empty after clearing", 
        mQueue.isEmpty());
        assertEquals("Size should be 0 after clearing", 0,
        mQueue.size());

        assertNull("Dequeue on cleared queue should return null", 
        mQueue.dequeue());
    }

    @Test
    public void testOrderPreserved() {
        mQueue.enqueue(10);
        mQueue.enqueue(20);
        mQueue.enqueue(30);

        assertEquals("First dequeued should be first enqueued", 10, (int) mQueue.dequeue());
        assertEquals("Second dequeued should be second enqueued", 20, (int) mQueue.dequeue());
        assertEquals("Third dequeued should be third enqueued", 30, (int) mQueue.dequeue());
    }

    @Test
    public void testEnqueueResize() {
        // Assuming initial capacity is 10
        for (int i = 1; i <= 15; i++) {
            mQueue.enqueue(i);
        }

        assertEquals("Size should be 15 after 15 enqueues", 15, mQueue.size());
        assertEquals("Peek should return first enqueued element", 1, (int) mQueue.peek());

        for (int i = 1; i <= 15; i++) {
            assertEquals("Dequeue should return elements in order", i, (int) mQueue.dequeue());
        }

        assertTrue("Queue should be empty after dequeuing all elements", mQueue.isEmpty());
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
                mQueue.enqueue(input);
            }

            for (Integer result : correctResult) {
                assertEquals("Dequeued values should match enqueued values", result, mQueue.dequeue());
            }
        }
    }
}

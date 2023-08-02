/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int count;

    private class Node {
        Item item;
        Node next;
        Node before;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        count = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (first == null && last == null);
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument!");
        }
        count++;
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        if (oldFirst == null) {
            last = first;
            return;
        }
        first.next = oldFirst;
        oldFirst.before = first;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument!");
        }
        count++;
        Node oldLast = last;
        last = new Node();
        last.item = item;
        if (oldLast == null) {
            first = last;
            return;
        }
        last.before = oldLast;
        oldLast.next = last;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("no elements in deque!");
        }
        Item item = first.item;
        first = first.next;
        if (first == null) {
            last = null;
        }
        else {
            first.before = null;
        }
        count--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("no elements in deque!");
        }
        Item item = last.item;
        last = last.before;
        if (last == null) {
            first = null;
        }
        else {
            last.next = null;
        }
        count--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no elements!");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("no remove function!");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        StdOut.println(deque.isEmpty());
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(3);
        deque.addLast(4);
        StdOut.println(deque.size());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());
        Iterator<Integer> iterator = deque.iterator();
        while (iterator.hasNext()) {
            StdOut.println(iterator.next());
        }
    }
}

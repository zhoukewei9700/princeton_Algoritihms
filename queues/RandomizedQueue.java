/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument!");
        }
        if (n == items.length) {
            resize(2 * items.length);
        }
        items[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("no element in queue!");
        }
        int index = StdRandom.uniformInt(0, n);
        Item res = items[index];
        items[index] = items[--n];
        items[n] = null;
        if (n > 0 && n <= items.length / 4) {
            resize(items.length / 2);
        }
        return res;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("no element in queue!");
        }
        return items[StdRandom.uniformInt(0, n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Item[] temp = (Item[]) new Object[n];
        private int size = n;

        public ListIterator() {
            for (int i = 0; i < n; i++) {
                temp[i] = items[i];
            }
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no elements!");
            }
            int index = StdRandom.uniformInt(0, size);
            Item item = temp[index];
            temp[index] = temp[--size];
            temp[size] = null;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("no remove function!");
        }

        public boolean hasNext() {
            return size > 0;
        }
    }

    private void resize(int size) {
        Item[] newArray = (Item[]) new Object[size];
        for (int i = 0; i < n; i++) {
            newArray[i] = items[i];
        }
        items = newArray;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("1");
        rq.enqueue("2");
        rq.enqueue("3");
        rq.enqueue("4");
        System.out.println(rq.size());
        System.out.println(rq.sample());
        rq.dequeue();
        System.out.println(rq.isEmpty());
        Iterator<String> it = rq.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}

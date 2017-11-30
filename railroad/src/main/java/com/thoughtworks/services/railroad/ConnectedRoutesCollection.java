package com.thoughtworks.services.railroad;

import java.util.Iterator;
import java.util.NoSuchElementException;

//Collection implemented as connection nodes
public class ConnectedRoutesCollection<Item> implements Iterable<Item> {
    
	//Helper linked list node inner class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

	private Node<Item> first;    // beginning of Collection
    private int n;               // number of elements in collection


    /**
     * Initializes an empty collection.
     */
    public ConnectedRoutesCollection() {
        first = null;
        n = 0;
    }

    /**
     * Returns true if this collection is empty.
     *
     * @return {@code true} if this collection is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in this collection.
     *
     * @return the number of items in this collection.
     */
    public int size() {
        return n;
    }

    /**
     * Adds the item to this collection.
     *
     * @param  item the item to add to this collection
     */
    public void add(Item item) {
        Node<Item> currentHead = first;
        first = new Node<Item>();
        first.item = item;
        first.next = currentHead;
        n++;
    }


    /**
     * Returns an iterator that iterates over the items in this collection in arbitrary order.
     *
     * @return an iterator that iterates over the items in this collection in arbitrary order
     */
    public Iterator<Item> iterator()  {
        return new ListIterator<Item>(first);  
    }

    // an iterator of items
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
}
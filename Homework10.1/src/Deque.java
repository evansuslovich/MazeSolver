// Represents a Deque
class Deque<T> {
    Sentinel<T> header;

    Deque() {
        this.header = new Sentinel<T>();
    }

    Deque(Sentinel<T> s) {
        this.header = s;
    }

    // returns the number of nodes in the deque
    int size() {
        return this.header.next.size();
    }

    // adds a node to the begining of the deque
    void addAtHead(T t) {
        this.header.next = new Node<T>(t, this.header.next, this.header);
    }

    // adds a node to the end of the deque
    void addAtTail(T t) {
        this.header.prev = new Node<T>(t, this.header, this.header.prev);
    }

    // remove the last item of the deque
    T removeFromTail() {
        if (this.header.closedLoop()) {
            throw new RuntimeException("Can't remove from an empty list!");
        } 
        else {
            return this.header.prev.remove();
        }
    }

    // removes the first item of the deque
    T removeFromHead() {
        if (this.header.closedLoop()) {
            throw new RuntimeException("Can't remove from an empty list!");
        } 
        else {
            return this.header.next.remove();
        }
    }

    // Remove given Node from the Deque
    void removeNode(ANode<T> that) {
        this.header.next.removeNode(that);
    }
}

abstract class ANode<T> {
    ANode<T> next;
    ANode<T> prev;

    // returns the size of the deque
    abstract int size();

    // returns true if the next item is the same item and the previous item is
    // the same item, i.e. the deque loops over this Node
    boolean closedLoop() {
        return (this.next == this && this.prev == this);
    }

    // removes this node in the deque
    abstract void removeNode(ANode<T> that);

    // Remove a node
    abstract T remove();
}

// Represents a Sentinel in a Deque
class Sentinel<T> extends ANode<T> {

    Sentinel() {
        this.next = this;
        this.prev = this;
    }

    // Return size of Sentinel
    int size() {
        return 0;
    }

    // Remove a Sentinel from Deque
    void removeNode(ANode<T> that) {
        // the sentinel should never be removed
    }

    // Remove Sentenel from Deque
    T remove() {
        return null;
    }
}

// Represents a Node in a Deque
class Node<T> extends ANode<T> {
    T data;

    Node(T t) {
        this.data = t;
        this.next = null;
        this.prev = null;
    }

    Node(T t, ANode<T> next, ANode<T> previous) {
        this.data = t;
        this.next = next;
        this.prev = previous;
        if (next == null || previous == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }
        else {
            this.next.prev = this;
            this.prev.next = this;
        }
    }

    // Return size of the Deque
    int size() {
        return 1 + this.next.size();
    }

    // Remove specific node from the Deque
    void removeNode(ANode<T> that) {
        if (this == that) {
            this.remove();
        } 
        else {
            this.next.removeNode(that);
        }
    }

    // Remove element from the Deque
    T remove() {
        this.next.prev = this.prev;
        this.prev.next = this.next;
        return this.data;
    }
}

// Represents either a Stack or a Queue
abstract class Collection<T> {
    Deque<T> body;

    // Add element to collection
    abstract void add(T t);

    // Remove element from collection
    abstract T remove();

    // Checks if collection is empty
    boolean isEmpty() {
        return body.size() == 0;
    }

    // Resets the body of the Collection
    void empty() {
        body = new Deque<T>();
    }
}

// Repreents a Queue data structure
class Queue<T> extends Collection<T> {
    Queue() {
        this.body = new Deque<T>();
    }

    // Add new elements at tail
    void add(T t) {
        body.addAtTail(t);
    }

    // Remove elements from the head
    T remove() {
        return body.removeFromHead();
    }
}

// Represents a Stack data structure
class Stack<T> extends Collection<T> {
    Stack() {
        this.body = new Deque<T>();
    }

    // Add new elements at head
    void add(T t) {
        body.addAtHead(t);
    }

    // Remove elements from head
    T remove() {
        return body.removeFromHead();
    }
}
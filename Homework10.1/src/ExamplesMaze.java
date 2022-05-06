//Assignment 10
//Taylor, Jacob
//jrtaylor
//Murphy, Taylor
//tmurphy

import java.util.ArrayList;

import tester.*;

// Examples for the Maze game
public class ExamplesMaze {
    Maze game;

    
    // Deque and collection tests
    Deque<String> deque1 = new Deque<String>();
    Sentinel<String> sentinelA = new Sentinel<String>();
    Deque<String> deque2 = new Deque<String>(sentinelA);
    Node<String> node2a = new Node<String>("bcd");
    Node<String> node1a = new Node<String>("abc", node2a, 
            sentinelA);
    Node<String> node4a = new Node<String>("def", sentinelA, 
            new Node<String>("cde"));
    Node<String> node3a = new Node<String>("cde", node4a, node2a);
    Sentinel<String> sentinelB = new Sentinel<String>();
    Deque<String> deque3 = new Deque<String>(sentinelB);
    Node<String> node2b = new Node<String>("quail");
    Node<String> node1b = new Node<String>("duck", node2b, sentinelB);
    Node<String> node4b = new Node<String>("moose");
    Node<String> node3b = new Node<String>("y'all", node4b, node2b);
    Node<String> node5b = new Node<String>("frog", sentinelB, node4b);
    
    // these are to compare to the other nodes, and are identical copies of them
    Deque<String> oldDeque1 = new Deque<String>();
    Sentinel<String> oldSentinelA = new Sentinel<String>();
    Deque<String> oldDeque2 = new Deque<String>(oldSentinelA);
    Node<String> oldNode2a = new Node<String>("bcd");
    Node<String> oldNode1a = new Node<String>("abc", oldNode2a, 
            oldSentinelA);
    Node<String> oldNode4a = new Node<String>("def", oldSentinelA, 
            new Node<String>("cde"));
    Node<String> oldNode3a = new Node<String>("cde", oldNode4a, oldNode2a);
    Sentinel<String> oldSentinelB = new Sentinel<String>();
    Deque<String> oldDeque3 = new Deque<String>(oldSentinelB);
    Node<String> oldNode2b = new Node<String>("quail");
    Node<String> oldNode1b = new Node<String>("duck", oldNode2b, oldSentinelB);
    Node<String> oldNode4b = new Node<String>("moose");
    Node<String> oldNode3b = new Node<String>("y'all", oldNode4b, oldNode2b);
    Node<String> oldNode5b = new Node<String>("frog", oldSentinelB, oldNode4b);
    Node<String> bodeNode;
    Node<String> roadNode;
    Node<String> potateNode;
    Queue<String> que = new Queue<String>();
    Stack<String> stack = new Stack<String>();

    // Initialize the game
    void initGame() {
        game = new Maze();
        game.bigBang(Maze.BOARD_WIDTH + 1, Maze.BOARD_HEIGHT + 1, .001);
    }

    // Start the game
    void testGame(Tester t) {
        initGame();
    }

    void testGeneration(Tester t) {
         initGame();
         t.checkExpect(game.cells.size(), Maze.HEIGHT * Maze.WIDTH);
    }
    
    void testSearch(Tester t) {
        initGame();
        game.searchList = new Queue<Cell>();
        Cell startCell = game.cells.get(0);
        game.searchList.add(startCell);
        game.search();
        t.checkExpect(startCell.visited, true);
        game.search();
        t.checkExpect(startCell.bottom.visited || 
                startCell.right.visited, true);
        game.search();
        t.checkExpect(startCell.bottom.right.visited || 
                startCell.right.bottom.visited || 
                startCell.bottom.bottom.visited ||
                startCell.right.right.visited, true);
        t.checkExpect(game.cameFromEdge.size() > 2, true);
        t.checkExpect(startCell.colorVal, 0);
    }
    
    /*
     * 
     * DEQUE AND COLLECTION TESTS
     * 
     */
    void testConstruct(Tester t) {
        resetDeques();
        t.checkExpect(deque2.size(), 4);
        t.checkExpect(deque2.header, sentinelA);
        t.checkExpect(((Node<String>)deque2.header.next).data, "abc");
        t.checkExpect(((Node<String>)deque2.header.next.next).data, "bcd");
        t.checkExpect(((Node<String>)deque2.header.next.next.next).data, "cde");
        t.checkExpect(((Node<String>)deque2.header.next.next.next.next).data, 
                "def");
        t.checkExpect(deque2.header.next.next.next.next.next, sentinelA);
        t.checkExpect(deque2.header.prev.prev.prev.prev.prev, sentinelA);
        t.checkExpect(((Node<String>)deque2.header.prev.prev.prev.prev).data, 
                "abc");
        t.checkExpect(((Node<String>)deque2.header.prev.prev.prev).data, "bcd");
        t.checkExpect(((Node<String>)deque2.header.prev.prev).data, "cde");
        t.checkExpect(((Node<String>)deque2.header.prev).data, "def");
    }

    void resetDeques() {
        deque1 = new Deque<String>();
        sentinelA = new Sentinel<String>();
        deque2 = new Deque<String>(sentinelA);
        node2a = new Node<String>("bcd");
        node1a = new Node<String>("abc", node2a, 
                sentinelA);
        node4a = new Node<String>("def", sentinelA, 
                new Node<String>("cde"));
        node3a = new Node<String>("cde", node4a, node2a);
        sentinelB = new Sentinel<String>();
        deque3 = new Deque<String>(sentinelB);
        node2b = new Node<String>("quail");
        node1b = new Node<String>("duck", node2b, sentinelB);
        node4b = new Node<String>("moose");
        node3b = new Node<String>("y'all", node4b, node2b);
        node5b = new Node<String>("frog", sentinelB, node4b);
        oldDeque1 = new Deque<String>();
        oldSentinelA = new Sentinel<String>();
        oldDeque2 = new Deque<String>(oldSentinelA);
        oldNode2a = new Node<String>("bcd");
        oldNode1a = new Node<String>("abc", oldNode2a, 
                oldSentinelA);
        oldNode4a = new Node<String>("def", oldSentinelA, 
                new Node<String>("cde"));
        oldNode3a = new Node<String>("cde", oldNode4a, oldNode2a);
        oldSentinelB = new Sentinel<String>();
        oldDeque3 = new Deque<String>(oldSentinelB);
        oldNode2b = new Node<String>("quail");
        oldNode1b = new Node<String>("duck", oldNode2b, oldSentinelB);
        oldNode4b = new Node<String>("moose");
        oldNode3b = new Node<String>("y'all", oldNode4b, oldNode2b);
        oldNode5b = new Node<String>("frog", oldSentinelB, oldNode4b);
    }

    // NOTE: Supposed to fail!
    ANode<String> constructNullNode() {
        return new Node<String>("", new Node<String>(""), null);
    }
    void testConstructingNullNodes(Tester t) {
        t.checkException(new IllegalArgumentException("Node cannot be null"), 
                this, "constructNullNode");
    }
    
    // checks to see if the deques have all the correct data
    void testData(Tester t) {
        resetDeques();
        t.checkExpect(deque1.header, new Sentinel<String>());
        t.checkExpect(deque2.header, sentinelA);
        t.checkExpect(((Node<String>)deque2.header.next).data, "abc");
        t.checkExpect(((Node<String>)deque2.header.next.next).data, "bcd");
        t.checkExpect(((Node<String>)deque2.header.next.next.next).data, "cde");
        t.checkExpect(((Node<String>)deque2.header.next.next.next.next).data, 
                "def");
        t.checkExpect(deque2.header.next.next.next.next.next, sentinelA);
        t.checkExpect(deque2.header.prev.prev.prev.prev.prev, sentinelA);
        t.checkExpect(((Node<String>)deque2.header.prev.prev.prev.prev).data, 
                "abc");
        t.checkExpect(((Node<String>)deque2.header.prev.prev.prev).data, "bcd");
        t.checkExpect(((Node<String>)deque2.header.prev.prev).data, "cde");
        t.checkExpect(((Node<String>)deque2.header.prev).data, "def");
        t.checkExpect(deque2.header.prev.prev.prev.prev.prev.prev.next.next.next
                .next.next.next, sentinelA);
        
        t.checkExpect(deque3.header, sentinelB);
        t.checkExpect(((Node<String>)deque3.header.next).data, "duck");
        t.checkExpect(((Node<String>)deque3.header.next.next).data, "quail");
        t.checkExpect(((Node<String>)deque3.header.next.next.next).data, 
                "y'all");
        t.checkExpect(((Node<String>)deque3.header.next.next.next.next).data, 
                "moose");
        t.checkExpect(((Node<String>)deque3.header.next.next.next.next.next)
                .data, "frog");
        t.checkExpect(deque3.header.next.next.next.next.next.next, sentinelB);
        t.checkExpect(deque3.header.prev.prev.prev.prev.prev.prev, sentinelB);
        t.checkExpect(((Node<String>)deque3.header.prev.prev.prev.prev.prev)
                .data, "duck");
        t.checkExpect(((Node<String>)deque3.header.prev.prev.prev.prev).data,
                "quail");
        t.checkExpect(((Node<String>)deque3.header.prev.prev.prev).data,
                "y'all");
        t.checkExpect(((Node<String>)deque3.header.prev.prev).data, "moose");
        t.checkExpect(((Node<String>)deque3.header.prev).data, "frog");
    }

    void testSize(Tester t) {
        resetDeques();
        t.checkExpect(deque1.size(), 0);
        t.checkExpect(deque2.size(), 4);
        t.checkExpect(deque3.size(), 5);
    }

    void testAddAtHead(Tester t) {
        resetDeques();
        deque1.addAtHead("bode");
        deque2.addAtHead("road");
        deque3.addAtHead("potate");
        bodeNode = new Node<String>("bode", oldDeque1.header, oldDeque1.header);
        roadNode = new Node<String>("road", oldNode1a, oldSentinelA);
        potateNode = new Node<String>("potate", oldNode1b, oldSentinelB);
        
        // do all of the links work
        t.checkExpect(((Node<String>)deque1.header.next).data, "bode");
        t.checkExpect(deque1.header, oldDeque1.header);
        t.checkExpect(deque1.header.next, bodeNode);
        t.checkExpect(deque1.header.prev, bodeNode);
        t.checkExpect(deque1.header.next.next, oldDeque1.header);
        t.checkExpect(deque1.header.next.prev, oldDeque1.header);
        
        t.checkExpect(((Node<String>)deque2.header.next).data, "road");
        t.checkExpect(deque2.header, oldSentinelA);
        t.checkExpect(deque2.header.next, roadNode);
        t.checkExpect(node1a.prev, roadNode);
        t.checkExpect(deque2.header.next.prev, oldSentinelA);
        t.checkExpect(deque2.header.next.next, oldNode1a);
        
        t.checkExpect(((Node<String>)deque3.header.next).data, "potate");
        t.checkExpect(deque3.header, oldSentinelB);
        t.checkExpect(node1b.prev, potateNode);
        t.checkExpect(deque3.header.next, potateNode);
        t.checkExpect(deque3.header.next.prev, oldSentinelB);
        t.checkExpect(deque3.header.next.next, oldNode1b);
    }

    void testAddAtTail(Tester t) {
        resetDeques();
        deque1.addAtTail("bode");
        deque2.addAtTail("road");
        deque3.addAtTail("potate");
        bodeNode = new Node<String>("bode", oldDeque1.header, oldDeque1.header);
        roadNode = new Node<String>("road", oldSentinelA, oldNode4a);
        potateNode = new Node<String>("potate", oldSentinelB, oldNode5b);
        t.checkExpect(deque1.header, oldDeque1.header);
        t.checkExpect(deque1.header.next, bodeNode);
        t.checkExpect(deque1.header.prev, bodeNode);
        t.checkExpect(deque1.header.next.next, oldDeque1.header);
        t.checkExpect(deque1.header.next.prev, oldDeque1.header);
        
        t.checkExpect(deque2.header, oldSentinelA);
        t.checkExpect(deque2.header.prev, roadNode);
        t.checkExpect(node4a.next, roadNode);
        t.checkExpect(deque2.header.prev.next, oldSentinelA);
        t.checkExpect(deque2.header.prev.prev, oldNode4a);
        
        t.checkExpect(deque3.header, oldSentinelB);
        t.checkExpect(node5b.next, potateNode);
        t.checkExpect(deque3.header.prev, potateNode);
        t.checkExpect(deque3.header.prev.next, oldSentinelB);
        t.checkExpect(deque3.header.prev.prev, oldNode5b);
    }

    void testRemoveFromHead(Tester t) {
        resetDeques();
        t.checkException(new RuntimeException("Can't remove from "
                + "an empty list!"), deque1, "removeFromHead");
        t.checkExpect(deque2.size(), 4);
        t.checkExpect(((Node<String>)deque2.header.next).data, "abc");
        
        t.checkExpect(deque2.removeFromHead(), "abc");
        t.checkExpect(deque2.size(), 3);
        
        t.checkExpect(deque2.removeFromHead(), "bcd");
        t.checkExpect(deque2.size(), 2);
        
        t.checkExpect(((Node<String>)deque2.header.next.next).data, "def");
        t.checkExpect(((Node<String>)deque2.header.next.prev.prev).data, "def");
        t.checkExpect(((Node<String>)deque2.header.next.prev.prev.prev).data, 
                "cde");
    }

    void testRemoveFromTail(Tester t) {
        resetDeques();
        deque2.addAtTail("blah");
        deque3.addAtTail("blah blah blah");
        deque2.removeFromTail();
        deque3.removeFromTail();
        t.checkException(new RuntimeException("Can't remove from "
                + "an empty list!"), deque1, "removeFromTail");
        t.checkExpect(deque2, oldDeque2);
        t.checkExpect(deque3, oldDeque3);
    }

    void testRemoveNode(Tester t) {
        resetDeques();
        deque1.removeNode(node1a);
        t.checkExpect(deque1, oldDeque1);
        deque1.removeNode(sentinelA);
        t.checkExpect(deque2, oldDeque2);
        deque2.removeNode(node1a);
        oldDeque2.removeFromHead();
        t.checkExpect(deque2, oldDeque2);
        resetDeques();
        Deque<String> newDeque2 = deque2;
        deque2.removeNode(node4a);
        t.checkExpect(deque2, newDeque2);
        deque2.removeNode(node2a);
        deque2.removeNode(node3a);
        deque2.removeNode(node1a);
        t.checkExpect(deque2, deque1);
    }
    
    void testQueue(Tester t) {
        resetDeques();
        que.body = deque2; 
        t.checkExpect(que.isEmpty(), false);
        t.checkExpect(que.remove(), "abc");
        que.add("abc");
        t.checkExpect(que.remove(), "bcd");
        que.empty();
        t.checkExpect(que.isEmpty(), true);
    }
    
    void testStack(Tester t) {
        resetDeques();
        stack.body = deque2;
        t.checkExpect(stack.isEmpty(), false);
        t.checkExpect(stack.remove(), "abc");
        stack.add("qed");
        t.checkExpect(stack.remove(), "qed");
        stack.empty();
        t.checkExpect(stack.isEmpty(), true);
    }
}

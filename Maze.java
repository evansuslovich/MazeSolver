import java.util.*;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// this interface represents a collection of items
interface ICollection<T> {

  // checks if this collection has no items in it
  boolean empty();

  // adds the given item to this collection
  void add(T item);

  // removes the first item of this collection and returns its value
  T remove();
}

// represents a stack that implements ICollection
class StackCollection<T> implements ICollection<T> {
  ArrayDeque<T> contents;

  StackCollection() {
    this.contents = new ArrayDeque<T>();
  }

  // checks if the deque in this stack is empty 
  public boolean empty() {
    return this.contents.isEmpty();
  }

  // removes the first item of the deque and returns its value
  public T remove() {
    return this.contents.removeFirst();
  }

  // adds the given item to the beginning of this deque 
  public void add(T item) {
    this.contents.addFirst(item);
  }
}

// represents a queue that implements ICollection
class QueueCollection<T> implements ICollection<T> {
  ArrayDeque<T> contents;

  QueueCollection() {
    this.contents = new ArrayDeque<T>();
  }

  //checks if the deque in this queue is empty 
  public boolean empty() {
    return this.contents.isEmpty();
  }

  // removes the first item of the queue and returns its value
  public T remove() {
    return this.contents.removeFirst();
  }

  // adds the given item to the end of this queue 
  public void add(T item) {
    this.contents.addLast(item);
  }
}

// this class compares two edges by their weights 
class CompareEdge implements Comparator<Edge> {

  public int compare(Edge edge1, Edge edge2) {
    return edge1.valWeight - edge2.valWeight;
  }
}

// this class represents a Utils which contains functions needed throughout the code
// with some additional variables which are used 
class Utils {
  Cell nextCell;

  ArrayDeque<Cell> floodCells = new ArrayDeque<Cell>();
  HashMap<Cell, Cell> path = new HashMap<Cell, Cell>();
  ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();

  // finds the integer in the hash and returns that integer 
  Integer find(HashMap<Integer, Integer> hash, Integer i) {
    if (hash.get(i) != i) {
      return this.find(hash, hash.get(i));
    }
    else {
      return hash.get(i);
    }
  }

  // changes all the cells that have been searched to pink and then changes the 
  // final path to red 
  void floodTheCells() {
    if (!this.floodCells.isEmpty()) {
      this.floodCells.pop().color = Color.pink;
      int x = this.cells.size() - 1;
      int y = this.cells.get(0).size() - 1;
      this.nextCell = this.cells.get(x).get(y);
    }
    if (this.floodCells.isEmpty() && !this.path.isEmpty()) {
      if (!nextCell.equals(this.cells.get(0).get(0))) {
        this.path.get(nextCell).updateColor(Color.red);
        nextCell = this.path.get(nextCell);
      }
    }
  }

  // this method draws a horizontal edge 
  WorldImage drawXEdge(int length) {
    WorldImage edge = new LineImage(new Posn(length, 0), Color.black).movePinhole(0, length / -2);
    return edge;
  }

  // this method draws a vertical edge 
  WorldImage drawYEdge(int length) {
    WorldImage edge = new LineImage(new Posn(0, length), Color.black).movePinhole(length / -2, 0);
    return edge;
  }
}

// this class represents a cell on a graph  
class Cell {
  int num;
  ArrayList<Cell> cellNeighbors;
  Color color; 


  Cell(int num) {
    this.num = num;
    this.cellNeighbors = new ArrayList<Cell>();
    this.color = Color.LIGHT_GRAY; 
  }

  // this method draws the cell given a certain size
  WorldImage drawCell(int size) { 
    return new RectangleImage(size, size, OutlineMode.SOLID, this.color);  
  }

  // changes this cell's color to the given color 
  void updateColor(Color c) {
    this.color = c;
  }

  // adds the given cell to this cell's ArrayList of neighbors
  void addCell(Cell c) { 
    if (!this.cellNeighbors.contains(c)) {
      this.cellNeighbors.add(c);
    }
  }

  // checks if the given cell can be found within this cell's ArrayList of neighbors
  boolean containsCell(Cell c) {
    return this.cellNeighbors.contains(c);
  }

  // removes the given cell from this cell's ArrayList of neighbors
  void removeCell(Cell c) {
    this.cellNeighbors.remove(c);
  }
}

// this class represents a graph with an nested array of cell and an array of edges 
class Graph {
  static int width = 500; // width of the canvas
  static int height = Graph.width * 6 / 10; // height of the canvas

  ArrayList<ArrayList<Cell>> graphCells;
  ArrayList<Edge> edges;
  Random rand;

  WorldScene scene = new WorldScene(Graph.width, Graph.height);

  // main constructor 
  Graph() {
    this.graphCells = new ArrayList<ArrayList<Cell>>();
    this.edges = new ArrayList<Edge>();
    this.rand = new Random();
  }

  // constructor that takes in a random - used for testing purposes 
  Graph(Random rand) {
    this.graphCells = new ArrayList<ArrayList<Cell>>();
    this.edges = new ArrayList<Edge>();
    this.rand = rand;
  }

  // creates a nested array of cells with the given number of rows and columns
  void makeGraph(int row, int column) {
    for (int i = 0; i < row; i++) {
      this.graphCells.add(this.createGraphRows(column, i * column));
    }
  }

  // helper method for makeGraph that creates each row 
  ArrayList<Cell> createGraphRows(int column, int val) {
    ArrayList<Cell> row = new ArrayList<Cell>();
    for (int y = column; y > 0; y--) {
      row.add(new Cell(val));
      val = val + 1;
    }
    return row;
  }

  // implements kruskal's algorithm to create a random maze based on edge weights 
  ArrayList<Edge> kruskalsAlgorithm(ArrayList<Edge> givenEdges) {
    Utils util = new Utils();
    givenEdges.sort(new CompareEdge());
    ArrayList<Edge> edges = new ArrayList<Edge>();
    HashMap<Integer, Integer> representatives = new HashMap<Integer, Integer>();

    for (ArrayList<Cell> arr : this.graphCells) {
      for (Cell v : arr) {
        representatives.put(v.num, v.num);
      }
    }
    while (givenEdges.size() > 0) {
      Edge e = givenEdges.remove(0);

      if (util.find(representatives, e.cell1.num) != (util.find(representatives, e.cell2.num))) {
        edges.add(e);
        representatives.replace(util.find(representatives, e.cell1.num),
            util.find(representatives, e.cell2.num));
      }
      else {
        e.cell1.removeCell(e.cell2);
        e.cell2.removeCell(e.cell1);
      }
    }
    return edges;
  }

  // creates an edge between the given cell and the bottom cell
  void addEdges() {
    for (int i = 0; i < this.graphCells.size(); i++) {
      if (i < this.graphCells.size() - 1) {
        for (int j = 0; j < this.graphCells.get(0).size(); j++) {
          Cell cell1 = this.graphCells.get(i).get(j);
          Cell cell2 = this.graphCells.get(i + 1).get(j);
          this.edges.add(new Edge(cell1, cell2, this.rand.nextInt(100)));
          cell1.cellNeighbors.add(cell2);
          cell2.cellNeighbors.add(cell1);
        }
      }
    }
    this.addEdgesHelper();
  }

  //creates an edge between the given cell and the next cell 
  void addEdgesHelper() {
    for (int i = 0; i < this.graphCells.size(); i++) {
      for (int j = 0; j < this.graphCells.get(0).size(); j++) {
        if (j < this.graphCells.get(0).size() - 1) {
          Cell cell1 = this.graphCells.get(i).get(j);
          Cell cell2 = this.graphCells.get(i).get(j + 1);
          this.edges.add(new Edge(cell1, cell2, this.rand.nextInt(100)));
          cell1.cellNeighbors.add(cell2);
          cell2.cellNeighbors.add(cell1);

        }
      }
    }
    this.edges = this.kruskalsAlgorithm(this.edges); // runs kruskals algorith 
  }

  // draws the graph and will add edges throughout the graph as needed
  WorldScene draw() {

    WorldImage background = new RectangleImage(Graph.width, Graph.height,
        OutlineMode.SOLID, Color.LIGHT_GRAY);

    scene.placeImageXY(background, Graph.width / 2, Graph.height / 2);

    int xMax = graphCells.size();
    int yMax = graphCells.get(0).size();


    int xLength = Graph.width / (yMax);
    int yLength = Graph.height / (xMax);


    WorldImage start = new RectangleImage(xLength, yLength, OutlineMode.SOLID, Color.green);
    WorldImage end = new RectangleImage(xLength, yLength, OutlineMode.SOLID, Color.magenta);


    for (int i = 0; i < xMax; i++) {
      for (int j = 0; j < yMax; j++) {
        WorldImage cell = this.graphCells.get(i).get(j).drawCell(xLength); // gets the drawn cell 
        scene.placeImageXY(cell, (j * xLength) + xLength / 2, (i * yLength) + yLength / 2); 
      }
    }

    scene.placeImageXY(start, xLength / 2, yLength / 2);
    scene.placeImageXY(end, Graph.width - xLength / 2, Graph.height - yLength / 2);

    return this.drawEdges(xMax, yMax, xLength, yLength);

  }

  // draws the edges for the graph onto the scene 
  WorldScene drawEdges(int xMax, int yMax, int xLength, int yLength) {
    for (int i = 0; i < xMax; i++) {
      for (int j = 0; j < yMax; j++) {
        if (j < this.graphCells.get(0).size() - 1
            && !this.graphCells.get(i).get(j).containsCell(this.graphCells.get(i).get(j + 1))) {
          scene.placeImageXY(new Utils().drawYEdge(xLength),
              (j * xLength) + xLength / 2, (i * yLength) + yLength / 2);
        }
        if (i < this.graphCells.size() - 1
            && !this.graphCells.get(i).get(j).containsCell(this.graphCells.get(i + 1).get(j))) {
          scene.placeImageXY(new Utils().drawXEdge(yLength),
              (j * xLength) + xLength / 2, (i * yLength) + yLength / 2);
        }
      }
    }
    return scene;
  }

  // searches the graph using breadth first search 
  boolean breadthFirstSearch(Utils u) {
    return searchHelper(new QueueCollection<Cell>(), u);
  }

  // searches the graph using depth first search 
  boolean depthFirstSearch(Utils u) {
    return searchHelper(new StackCollection<Cell>(), u);
  }

  // searches the graph with a given ICollection, either StackCollection or 
  // QueueCollection, and returns true if found 
  boolean searchHelper(ICollection<Cell> cells, Utils utils) {

    int xMax = this.graphCells.size() - 1;
    int yMax = this.graphCells.get(0).size() - 1; 

    Cell cell1 = this.graphCells.get(0).get(0); 
    Cell cell2 = this.graphCells.get(xMax).get(yMax);

    HashMap<Cell, Cell> backtrack = new HashMap<Cell, Cell>();
    ArrayDeque<Cell> history = new ArrayDeque<Cell>();
    cells.add(cell1);

    while (!cells.empty()) {
      Cell next = cells.remove();

      if (next.equals(cell2)) {
        utils.floodCells.addAll(history); 
        utils.path = backtrack;
        utils.cells = this.graphCells;
        return true; 
      }
      else if (!history.contains(next)) {
        for (int i = 0; i <  next.cellNeighbors.size(); i++) {
          cells.add(next.cellNeighbors.get(i));
          backtrack.putIfAbsent(next.cellNeighbors.get(i), next);
        }
        history.addLast(next);
      }
    }
    return false;
  }

}

// this class represents an edge between two cells (from cell1 to cell2) and assigns 
// it a random weight 
class Edge {
  Cell cell1; 
  Cell cell2;

  Random rand;
  int valWeight;

  Edge(Cell cell1, Cell cell2) {
    this.cell1 = cell1;
    this.cell2 = cell2;
    this.rand = new Random();
    this.valWeight = this.rand.nextInt(250); // randomly selects weight 
  }

  Edge(Cell cell1, Cell cell2, int valWeight) {
    this.cell1 = cell1;
    this.cell2 = cell2;
    this.rand = new Random();
    this.valWeight = valWeight;
  }

}

// this class represents the maze which is created through the graph
class Maze extends World {
  int row;
  int column;

  Graph graph;
  Random rand;
  Utils utils;

  // Maze takes only in numRows and calculates the number of columns 
  Maze(int row) {
    this.row = row;
    this.column = row * 10 / 6;

    this.graph = new Graph();
    this.graph.makeGraph(this.row, this.column);
    this.graph.addEdges();

    this.rand = new Random();
    this.utils = new Utils();
  }

  // this method returns a scene with the graph drawn 
  @Override
  public WorldScene makeScene() {
    return this.graph.draw();
  }

  // for every tick the cells that are followed will be flooded and the 
  // scene will be made 
  public void onTick() { 
    this.utils.floodTheCells();
    this.makeScene(); 
  }

  //this method takes in a String of a key and if it is b , 
  // the program will run a breadth first search, if it is d, 
  // the program will run a depth first search, and anything else 
  // will not change the graph
  public void onKeyEvent(String key) { 
    if (key.equals("b")) { 
      this.graph.breadthFirstSearch(this.utils); 
    }
    if (key.equals("d")) { 
      this.graph.depthFirstSearch(this.utils); 
    }
  }

}

// this class contains examples of the classes Maze, Graph, Cell, and Edge 
// while testing all of the methods in each class 
class ExamplesMaze {

  // runs the game (maze in this case)
  void testGame(Tester t) {
    Maze m = new Maze(20);
    m.bigBang(Graph.width, Graph.height, 1.0 / 100.0);
  }



  Utils utils; 
  Cell cell1; 
  Cell cell2; 
  Cell cell3; 
  Cell cell4; 
  Cell cell5; 
  Cell cell6; 
  Cell cell7; 
  Cell cell8; 
  Cell cell9;
  Cell cell10; 

  Edge edge1; 
  Edge edge2; 
  Edge edge3; 
  Edge edge4; 
  Edge edge5; 


  Edge edgeConnect1; 
  Edge edgeConnect2; 
  Edge edgeConnect3; 
  Edge edgeConnect4; 
  Edge edgeConnect5; 
  Edge edgeConnect6; 
  Edge edgeConnect7; 
  Edge edgeConnect8; 

  ArrayList<Edge> empty; 

  ArrayList<Edge> list1; 
  ArrayList<Edge> list2; 
  ArrayList<Edge> list3; 
  ArrayList<Edge> list4; 
  ArrayList<Edge> list5; 

  ArrayList<Edge> MtList;
  ArrayList<Edge> finalList;

  HashMap<Integer, Integer> representative; 


  Graph g;

  Cell cell1Extra;

  Cell cell8Extra;

  void init() {

    this.empty = new ArrayList<Edge>();

    this.representative = new HashMap<Integer, Integer>();
    this.representative.put(0, 2);
    this.representative.put(1, 3);
    this.representative.put(2, 4);
    this.representative.put(3, 8);
    this.representative.put(10, 3);
    this.representative.put(6, 5);



    this.cell1 = new Cell(1);
    this.cell1Extra = new Cell(1);
    this.cell2 = new Cell(2);
    this.cell3 = new Cell(3);
    this.cell4 = new Cell(4);
    this.cell5 = new Cell(5);
    this.cell6 = new Cell(6);
    this.cell7 = new Cell(7);
    this.cell8 = new Cell(8);
    this.cell8Extra = new Cell(8);
    this.cell9 = new Cell(9);
    this.cell10 = new Cell(10);

    this.edge1 = new Edge(cell1,cell2, 10);
    this.edge2 = new Edge(cell2,cell3, 11);
    this.edge3 = new Edge(cell3,cell4, 12);
    this.edge4 = new Edge(cell4,cell5, 13);
    this.edge5 = new Edge(cell5,cell6, 14);

    this.edgeConnect1 = new Edge(cell6,cell7, 15);
    this.edgeConnect2 = new Edge(cell7,cell9, 16);
    this.edgeConnect3 = new Edge(cell9,cell8, 17);
    this.edgeConnect4 = new Edge(cell8,cell10, 18);
    this.edgeConnect5 = new Edge(cell10,cell9, 19);

    this.list1 = new ArrayList<Edge>(Arrays.asList(edge1, edge2, edge3));
    this.list2 = new ArrayList<Edge>(Arrays.asList(edge2, edge3, edge4));
    this.list3 = new ArrayList<Edge>(Arrays.asList(edge1, edge3, edge2));
    this.list4 = new ArrayList<Edge>(Arrays.asList(edge4, edge5, edge1));
    this.list5 = new ArrayList<Edge>(Arrays.asList(edge4, edge3, edge2));

    this.finalList = new ArrayList<Edge>(Arrays.asList(edge1, edge2, edge3));


  }





  // tests the comparator CompareEdge
  void testCompareEdge(Tester t) {
    this.init();
    t.checkExpect(new CompareEdge().compare(edge1, edge2), -1);
    t.checkExpect(new CompareEdge().compare(edge2, edge3), -1);
    t.checkExpect(new CompareEdge().compare(edge3, edge4), -1);
    t.checkExpect(new CompareEdge().compare(edge5, edge4), 1);
    t.checkExpect(new CompareEdge().compare(edge3, edge1), 2);
    t.checkExpect(new CompareEdge().compare(edge5, edge5), 0);

  }




  //tests the method addCell in the Cell class
  void testAddCell(Tester t) {
    this.init();

    this.cell1.cellNeighbors = new ArrayList<Cell>(Arrays.asList(cell1));
    this.cell1Extra.cellNeighbors = new ArrayList<Cell>(Arrays.asList(cell1,cell2));

    cell1.addCell(cell2);

    for (int i = 0; i < cell1.cellNeighbors.size(); i++) {
      t.checkExpect(cell1.cellNeighbors.get(i) == cell1Extra.cellNeighbors.get(i), true);
    }


    this.cell5.cellNeighbors = new ArrayList<Cell>(Arrays.asList(cell1,cell2,cell3));
    this.cell6.cellNeighbors = new ArrayList<Cell>(Arrays.asList(cell4, cell5, cell6));

    for (int x = 0; x < this.cell5.cellNeighbors.size(); x++) {
      t.checkExpect(cell5.cellNeighbors.get(x) == cell6.cellNeighbors.get(x), false);
    }


  }

  //tests the method containsCell in the Cell class
  void testContainsCell(Tester t) {
    this.init();
    this.cell1.cellNeighbors = new ArrayList<Cell>(Arrays.asList(cell2,cell3,cell4));
    this.cell2.cellNeighbors = new ArrayList<Cell>(Arrays.asList(cell5,cell6));

    t.checkExpect(cell1.containsCell(cell1), false);
    t.checkExpect(cell1.containsCell(cell2), true);
    t.checkExpect(cell1.containsCell(cell3), true);
    t.checkExpect(cell1.containsCell(cell4), true);
    t.checkExpect(cell1.containsCell(cell5), false);


    t.checkExpect(cell2.containsCell(cell1), false);
    t.checkExpect(cell2.containsCell(cell2), false);
    t.checkExpect(cell2.containsCell(cell3), false);
    t.checkExpect(cell2.containsCell(cell4), false);
    t.checkExpect(cell2.containsCell(cell5), true);
    t.checkExpect(cell2.containsCell(cell6), true);
  }

  //tests the method removeCell in the Cell class
  void testRemoveCell(Tester t) {
    this.init(); 
    this.cell8.cellNeighbors = new ArrayList<Cell>(Arrays.asList(cell1, cell2));
    this.cell8.removeCell(cell2);
    this.cell8Extra.cellNeighbors = new ArrayList<Cell>(Arrays.asList(cell1));

    for (int i = 0; i < this.cell8.cellNeighbors.size(); i++) {
      t.checkExpect(cell8.cellNeighbors.get(i) == cell8Extra.cellNeighbors.get(i), true);
    }
  }

  // tests the method drawCell in the Cell class
  void testDrawCell(Tester t) {
    init();
    t.checkExpect(this.cell1.drawCell(2), 
        new RectangleImage(2, 2, OutlineMode.SOLID, Color.lightGray));
    t.checkExpect(this.cell2.drawCell(4), 
        new RectangleImage(4, 4, OutlineMode.SOLID, Color.lightGray));
    t.checkExpect(this.cell3.drawCell(6), 
        new RectangleImage(6, 6, OutlineMode.SOLID, Color.lightGray));
    t.checkExpect(this.cell4.drawCell(8), 
        new RectangleImage(8, 8, OutlineMode.SOLID, Color.lightGray));
    t.checkExpect(this.cell5.drawCell(10), 
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.lightGray));
    t.checkExpect(this.cell6.drawCell(15), 
        new RectangleImage(15, 15, OutlineMode.SOLID, Color.lightGray));
    t.checkExpect(this.cell7.drawCell(20), 
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.lightGray));
    t.checkExpect(this.cell8.drawCell(100), 
        new RectangleImage(100, 100, OutlineMode.SOLID, Color.lightGray));
    t.checkExpect(this.cell9.drawCell(150),
        new RectangleImage(150, 150, OutlineMode.SOLID, Color.lightGray));
    t.checkExpect(this.cell10.drawCell(200), 
        new RectangleImage(200, 200, OutlineMode.SOLID, Color.lightGray));
  }

  //tests the method updateColor in the Cell class
  void testUpdateColor(Tester t) {
    init();
    t.checkExpect(this.cell1.color, Color.lightGray);
    this.cell1.updateColor(Color.black);
    t.checkExpect(this.cell1.color, Color.black);

    t.checkExpect(this.cell2.color, Color.lightGray);
    this.cell2.updateColor(Color.blue);
    t.checkExpect(this.cell2.color, Color.blue);

    t.checkExpect(this.cell3.color, Color.lightGray);
    this.cell3.updateColor(Color.cyan);
    t.checkExpect(this.cell3.color, Color.cyan);

    t.checkExpect(this.cell4.color, Color.lightGray);
    this.cell4.updateColor(Color.green);
    t.checkExpect(this.cell4.color, Color.green);

    t.checkExpect(this.cell5.color, Color.lightGray);
    this.cell5.updateColor(Color.magenta);
    t.checkExpect(this.cell5.color, Color.magenta);

    t.checkExpect(this.cell6.color, Color.lightGray);
    this.cell6.updateColor(Color.orange);
    t.checkExpect(this.cell6.color, Color.orange);

    t.checkExpect(this.cell7.color, Color.lightGray);
    this.cell7.updateColor(Color.pink);
    t.checkExpect(this.cell7.color, Color.pink);

    t.checkExpect(this.cell8.color, Color.lightGray);
    this.cell8.updateColor(Color.red);
    t.checkExpect(this.cell8.color, Color.red);

    t.checkExpect(this.cell9.color, Color.lightGray);
    this.cell9.updateColor(Color.white);
    t.checkExpect(this.cell9.color, Color.white);

    t.checkExpect(this.cell10.color, Color.lightGray);
    this.cell10.updateColor(Color.yellow);
    t.checkExpect(this.cell10.color, Color.yellow);
  }



}
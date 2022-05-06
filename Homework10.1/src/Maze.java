//Assignment 10
//Taylor, Jacob
//jrtaylor
//Murphy, Taylor
//tmurphy

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import javalib.colors.Black;
import javalib.colors.White;
import javalib.impworld.World;
import javalib.worldimages.FrameImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;


// Represents the maze game
class Maze extends JPanel extends World implements KeyListener {
    ArrayList<Cell> cells; // the list of all cells in the maze
    ArrayList<Edge> edges; // the spanning tree of all edges in the maze
    HashMap<Cell, Cell> cameFromEdge;
    Player player;
    Cell goal; // the end goal that the player must reach
    Cell endPath;
    Collection<Cell> searchList;
    boolean won; // has the player won
    boolean searching;      // is the game searching for the goal?
    boolean drawWalls;      // draw the walls
    boolean rainbows;       // draw rainbow trails?
    boolean verticalBias;   // bias towards vertical corridors
    boolean horizontalBias; // bias towards horizontal corridors
    boolean spectator;      // don't display the player or the end goal,
                            // and don't stop searching once goal is reached
    boolean foundEnd;
    static final int WIDTH = 30; // Width of board (in cells)
    static final int HEIGHT = 30; // Height of board (in cells)
    static final int TILE_SIZE = 20; // Size of tiles (in pixels)
    static final int BOARD_WIDTH = WIDTH * TILE_SIZE; // Width of board (in
                                                      // pixels)
    static final int BOARD_HEIGHT = HEIGHT * TILE_SIZE; // Height of board (in
                                                        // pixels)

    Maze() {
        resetMaze();
        goal = cells.get(cells.size() - 1);
        drawWalls = true;
        rainbows = true;
    }

    // Draw entire game
    public WorldImage makeImage() {
        // makes a white background
        WorldImage background = new RectangleImage(new Posn(BOARD_WIDTH / 2,
                BOARD_HEIGHT / 2), BOARD_WIDTH, BOARD_HEIGHT, new White());
        // shows the win screen
        if (won) {
            background = new OverlayImages(background,
                    new TextImage(new Posn(BOARD_WIDTH / 2, BOARD_HEIGHT / 2),
                            "You win!", new Black()));
        } 
        else {
            // displays all the cells
            for (Cell n : cells) {
                background = new OverlayImages(background, n.drawAt(rainbows,
                        spectator));
            }
            if (!spectator) {
                // adds the player
                background = new OverlayImages(background,
                        player.draw(!rainbows && (searching)));
            }
            // draws the edges if they are supposed to be drawn
            if (drawWalls) {
                for (Edge e : edges) {
                    if (!e.path) {
                        background = new OverlayImages(background, e.drawEdge());
                    }
                }
                // adds a frame around the maze to show the outside walls
                background = new OverlayImages(background, new FrameImage(
                        new Posn(BOARD_WIDTH / 2, BOARD_HEIGHT / 2),
                        BOARD_WIDTH, BOARD_HEIGHT, new White()));
            }
        }
        return background;
    }

    // Create 2D Arraylist of cells
    ArrayList<ArrayList<Cell>> createCellList() {
        ArrayList<ArrayList<Cell>> cellList = new ArrayList<ArrayList<Cell>>();
        ArrayList<Cell> temp;
        int val = 0;
        for (int y = 0; y < HEIGHT; y++) {
            temp = new ArrayList<Cell>();
            for (int x = 0; x < WIDTH; x++) {
                temp.add(new Cell(val, x, y));
                val++;
            }
            cellList.add(temp);
        }
        return cellList;
    }

    // Create a grid of edges
    void createEdges(ArrayList<ArrayList<Cell>> cellList) {
        Random rand = new Random();
        Cell curCell;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                curCell = cellList.get(y).get(x);
                // add the right edge and have the left edge point to this cell
                int weakenedWeight = 150;
                int baseWeight = 25;
                if (x == 0) {
                    if (verticalBias) {
                        edges.add(new Edge(curCell, cellList.get(y).get(x + 1),
                                rand.nextInt(weakenedWeight)));
                        edges.add(new Edge(curCell, curCell, rand
                                .nextInt(weakenedWeight)));
                    }
                    // if there is no bias
                    else {
                        // add the edge to the right
                        edges.add(new Edge(curCell, cellList.get(y).get(x + 1),
                                rand.nextInt(baseWeight)));
                        // add the edge to the left
                        edges.add(new Edge(curCell, curCell, rand
                                .nextInt(baseWeight)));
                    }
                }
                // add the right edge, which points back to this cell
                else if (x == WIDTH - 1) {
                    if (verticalBias) {
                        edges.add(new Edge(curCell, curCell, rand
                                .nextInt(weakenedWeight)));
                    }
                    // if there is no bias
                    else {
                        edges.add(new Edge(curCell, curCell, rand
                                .nextInt(baseWeight)));
                    }
                }
                // add only the right edge
                else {
                    if (verticalBias) {
                        edges.add(new Edge(curCell, cellList.get(y).get(x + 1),
                                rand.nextInt(weakenedWeight)));
                    }
                    // if there is no bias
                    else {
                        edges.add(new Edge(curCell, cellList.get(y).get(x + 1),
                                rand.nextInt(baseWeight)));
                    }
                }
                // add the top and bottom edges, and have the top edge point
                // back to this cell
                if (y == 0) {
                    if (horizontalBias) {
                        edges.add(new Edge(curCell, curCell, rand
                                .nextInt(weakenedWeight)));
                        edges.add(new Edge(curCell, cellList.get(y + 1).get(x),
                                rand.nextInt(weakenedWeight)));
                    } 
                    else {
                        edges.add(new Edge(curCell, curCell, rand
                                .nextInt(baseWeight)));
                        edges.add(new Edge(curCell, cellList.get(y + 1).get(x),
                                rand.nextInt(baseWeight)));
                    }
                }
                // add the bottom edge, which points back to this cell
                else if (y == HEIGHT - 1) {
                    if (horizontalBias) {
                        edges.add(new Edge(curCell, curCell, rand
                                .nextInt(weakenedWeight)));
                    } 
                    else {
                        edges.add(new Edge(curCell, curCell, rand
                                .nextInt(baseWeight)));
                    }
                } 
                else {
                    if (horizontalBias) {
                        edges.add(new Edge(curCell, cellList.get(y + 1).get(x),
                                rand.nextInt(weakenedWeight)));
                    } 
                    else {
                        edges.add(new Edge(curCell, cellList.get(y + 1).get(x),
                                rand.nextInt(baseWeight)));
                    }
                }
                cells.add(curCell);
            }
        }
    }

    // generates the maze and all it's edges
    void genMaze() {
        // Sort edges by their weight
        Collections.sort(edges, new EdgeComparator());
        // Initalize variables
        UnionFind unionFind = new UnionFind();
        HashMap<Cell, Cell> representatives = new HashMap<Cell, Cell>();
        // Set all cell representatives as themselves
        for (Cell n : cells) {
            representatives.put(n, n);
        }
        // While there's more than one tree
        for (Edge e : edges) {
            Cell x = e.first;
            Cell y = e.second;
            // If representatives aren't equal
            if (unionFind.find(representatives, x).equals(
                    unionFind.find(representatives, y))
                    && !x.equals(y)) {
                // Set it as an invalid path
                e.path = false;
            }
            else {
                x.setNeighbor(y);
            }
            // Then union the representatives of x and y
            unionFind.union(representatives,
                    unionFind.find(representatives, x),
                    unionFind.find(representatives, y));
        }
    }

    // resets the maze
    void resetMaze() {
        cells = new ArrayList<Cell>();
        edges = new ArrayList<Edge>();
        cameFromEdge = new HashMap<Cell, Cell>();
        createEdges(createCellList());
        genMaze();
        player = new Player(cells.get(0));
        won = false;
        endPath = goal;
        foundEnd = false;
    }

    // set all the cells as unvisited
    void unvisitCells() {
        for (Cell n : cells) {
            n.visited = false;
        }
    }

    // Key events
    public void onKeyEvent(String key) {
        player.onKey(key);
        // check to see if the player has won upon moving
        if (player.location.equals(goal)) {
            // Check if player finished the maze
            won = true;
        }
        if (key.equals("r")) {
            // Generate a new maze
            resetMaze();
        }
        // search through the maze using breadth first search
        else if (key.equals("b")) {
            if (searching) {
                searching = false;
            }
            searching = true;
            unvisitCells();
            searchList = new Queue<Cell>();
            searchList.add(cells.get(0));
        }
        // search through the maze using depth first search
        else if (key.equals("d")) {
            if (searching) {
                searching = false;
            }
            searching = true;
            unvisitCells();
            searchList = new Stack<Cell>();
            searchList.add(cells.get(0));
        }
        // toggle drawing the walls
        else if (key.equals("w")) {
            if (drawWalls) {
                drawWalls = false;
            } 
            else {
                drawWalls = true;
            }
        }
        // toggle drawing the path in color or greyscale
        else if (key.equals("c")) {
            if (rainbows) {
                rainbows = false;
            } 
            else {
                rainbows = true;
            }
        }
        // generate a new maze and navigate through it using
        // depth first search
        else if (key.equals("u")) {
            resetMaze();
            onKeyEvent("d");
        }
        // generate a new maze and navigate through it using
        // breadth first search
        else if (key.equals("i")) {
            resetMaze();
            onKeyEvent("b");
        }
        // toggles spectator mode
        else if (key.equals("s")) {
            if (spectator) {
                spectator = false;
            } 
            else {
                spectator = true;
                drawWalls = false;
            }
        }
        // set the maze generation to prefer vertical corridors
        // and resets the maze
        else if (key.equals("v")) {
            if (verticalBias) {
                verticalBias = false;
            }
            else {
                horizontalBias = false;
                verticalBias = true;
            }
            resetMaze();
        }
        // set the maze generation to prefer horizontal corridors
        // and resets the maze
        else if (key.equals("h")) {
            if (horizontalBias) {
                horizontalBias = false;
            } 
            else {
                verticalBias = false;
                horizontalBias = true;
            }
            resetMaze();
        }
    }

    public void onTick() {
        for (Cell n : cells) {
            if (n.visited) {
                n.colorVal++;
            }
        }
        if (searching) {
            search();
        }
        if (foundEnd) {
            reconstructPath();
        }
    }

    // search through the maze for the next unvisited node,
    // either breadth or depth first search
    void search() {
        if (!searchList.isEmpty()) {
            Cell curCell = searchList.remove();
            if (curCell.equals(goal) & !spectator) {
                foundEnd = true;
                endPath = curCell;
                searchList.empty();
            } 
            else if (!curCell.visited) {
                curCell.visited = true;
                curCell.colorVal = 0;
                if (!curCell.top.visited) {
                    this.searchList.add(curCell.top);
                    cameFromEdge.put(curCell.top, curCell);
                }
                if (!curCell.bottom.visited) {
                    this.searchList.add(curCell.bottom);
                    cameFromEdge.put(curCell.bottom, curCell);
                }
                if (!curCell.left.visited) {
                    this.searchList.add(curCell.left);
                    cameFromEdge.put(curCell.left, curCell);
                }
                if (!curCell.right.visited) {
                    this.searchList.add(curCell.right);
                    cameFromEdge.put(curCell.right, curCell);
                }
            }
        }
    }

    void reconstructPath() {
        endPath = cameFromEdge.get(endPath);
        endPath.onPath = true;
        endPath.visited = false;
        if (endPath.val == 0) {
            foundEnd = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
}

// Represents a union/find data structure
class UnionFind {
    void union(HashMap<Cell, Cell> unions, Cell n1, Cell n2) {
        if (!find(unions, n1).equals(find(unions, n2))) {
            unions.put(n1, find(unions, n2));
        }
    }

    Cell find(HashMap<Cell, Cell> unions, Cell n) {
        if (!unions.get(n).equals(n)) {
            unions.put(n, find(unions, unions.get(n)));
        }
        return unions.get(n);
    }
}

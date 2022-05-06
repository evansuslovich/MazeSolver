import java.awt.Color;
import java.awt.Graphics;

// Represents a Cell
class Cell {
    Cell top;
    Cell bottom;
    Cell left;
    Cell right;
    int val;
    int x;
    int y;
    int colorVal;
    boolean visited;
    boolean onPath;

    Cell(int v, int x, int y) {
        this.val = v;
        this.x = x;
        this.y = y;
        this.top = this;
        this.bottom = this;
        this.left = this;
        this.right = this;
    }

    // Checks if a Cell is equal to given Object
    public boolean equals(Object o) {
        if (!(o instanceof Cell)) {
            return false;
        }
        else {
            return this.val == ((Cell) o).val;
        }
    }

    // Return node hash code
    public int hashCode() {
        return this.val;
    }

    // Return WorldImage form of given Cell (unvisited)
    public void draw(Graphics g, boolean rainbow, boolean spectator) {
        int xStart = this.x * Maze.TILE_SIZE;
        int yStart = this.y * Maze.TILE_SIZE;
        Color c = Color.black;
        // if the node has been visited
        if (visited && rainbow) {
            colorVal++;
            c = getColor();
        }
        else if (visited) {
            colorVal++;
           c = getGreyscaleColor();
        }
        // if it is the ending Cell
        else if (this.x == Maze.WIDTH - 1 && this.y == Maze.HEIGHT - 1
                && !spectator) {
            c = Color.red;
        }
        else if (onPath) {
           c = new Color(0, 153, 0);
        }
        g.setColor(c);
        g.fillRect(xStart, yStart, Maze.TILE_SIZE, Maze.TILE_SIZE);
    }

    // calculate the color of the wall
    Color getColor() {
        // Calculate rainbow color using sine wave
        double frequency = .01;
        double red = Math.sin(frequency * colorVal + 0) * 127 + 128;
        double green = Math.sin(frequency * colorVal + 2) * 127 + 128;
        double blue = Math.sin(frequency * colorVal + 4) * 127 + 128;
        return new Color((int) red, (int) green, (int) blue);
    }

    // Set color for greyscale mode
    Color getGreyscaleColor() {
        int grey = Math.min(240, colorVal);
        return new Color(grey, grey, grey);
    }

    // Set cell neighbors
    void setNeighbor(Cell that) {
        if (this.x < that.x) {
            this.right = that;
            that.left = this;
        } 
        else if (this.x > that.x) {
            this.left = that;
            that.right = this;
        } 
        else if (this.y < that.y) {
            this.bottom = that;
            that.top = this;
        } 
        else if (this.y < that.y) {
            this.top = that;
            that.bottom = this;
        }
    }
}
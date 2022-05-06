//Assignment 10
//Taylor, Jacob
//jrtaylor
//Murphy, Taylor
//tmurphy

import java.awt.*;

class Player {
    Cell location;

    Player(Cell n) {
        this.location = n;
    }

    // Player movement key events
    void onKey(String key) {
        if (key.equals("up")) {
            this.location.visited = true;
            this.location = this.location.top;
            this.location.colorVal = 0;
        } 
        else if (key.equals("down")) {
            this.location.visited = true;
            this.location = this.location.bottom;
            this.location.colorVal = 0;
        } 
        else if (key.equals("right")) {
            this.location.visited = true;
            this.location = this.location.right;
            this.location.colorVal = 0;
        } 
        else if (key.equals("left")) {
            this.location.visited = true;
            this.location = this.location.left;
            this.location.colorVal = 0;
        }
    }

    // Draw the player
    public void draw(Graphics g, boolean red) {
        if (red) {
            g.setColor(Color.red);
        }
        g.fillOval(this.location.x, this.location.y, 
                Maze.TILE_SIZE, Maze.TILE_SIZE);
    }
}

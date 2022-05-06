import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;

// Represents an Edge
class Edge {
    Cell first;
    Cell second;
    boolean path;
    int weight;

    Edge(Cell f, Cell s, int w) {
        this.first = f;
        this.second = s;
        this.weight = w;
        this.path = true;
    }

    // Draw this edge
    public void drawEdge(Graphics g) {
        int x1,x2, y1, y2;
        int tS = Maze.TILE_SIZE;
        int halfTile = tS / 2;
        int xa = this.first.x * tS;
        int ya = this.first.y * tS;
        int xb = this.second.x * tS;
        int yb = this.second.y * tS;
        if (this.first.y == this.second.y) { // Horizontal Line
            x1 = ((xa + xb) / 2);
            x2 = x1;
            y1 = (ya) - halfTile;
            y2 = (ya) + halfTile;
        } 
        else { // Vertical line
            x1 = (xa) + halfTile;
            x2 = (xa) - halfTile;
            y1 = ((ya + yb) / 2);
            y2 = y1;
        }
        g.setColor(Color.white);
        g.drawLine(x1, y1, x2, y2);
    }
}

// Comparator for edges
class EdgeComparator implements Comparator<Edge> {
    public int compare(Edge e1, Edge e2) {
        return e1.weight - e2.weight;
    }
}
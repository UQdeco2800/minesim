package minesim.pathfinder;

public class Node implements Cloneable {
    public Node parent;
    /* type = 0 normal node type = 1 Transportation node */
    public int x, y, cost, type;
    public NodeList linked;

    public Node(int x, int y, int cost, Node parent) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.type = 0;
        linked = new NodeList();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        Node other = (Node) o;
        if (other.x == this.x && other.y == this.y) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.x * 19 + this.y * 31;
    }

    @Override
    public String toString() {
        String s;
        s = "Node " + Integer.toString(this.x) + " " + Integer.toString(this.y) + " Linked to ";
        for (Node n : linked) {
            s = s + "(" + Integer.toString(n.x) + "," + Integer.toString(n.y) + ") ";
        }
        return s;
    }

    public Node clone() {
        Node node = new Node(this.x, this.y, this.cost, this.parent);
        node.linked = this.linked;
        return node;
    }
}

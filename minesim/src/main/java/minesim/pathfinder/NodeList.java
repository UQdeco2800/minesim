package minesim.pathfinder;

import java.util.ArrayList;

public class NodeList extends ArrayList<Node> implements Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public NodeList() {
        super();
    }


    @Override
    public boolean add(Node token) {
        if (token == null) {
            return false;
        }
        for (Node node : this) {
            if (node.equals(token)) {
                for (Node n : token.linked) {
                    if (!node.linked.contains(n)) {
                        node.linked.add(n);
                    }
                }
                return true;
            }
        }
        return super.add(token);
    }

    public Node getByPos(int x, int y) {
        for (Node node : this) {
            if (node.x == x && node.y == y) {
                return node;
            }
        }
        return null;
    }

    public Node getByNode(Node node) {
        for (Node n : this) {
            if (node.equals(n)) {
                return n;
            }
        }
        return null;
    }

    public NodeList clone() {
        NodeList list = new NodeList();
        for (Node node : this) {
            list.add(node.clone());
        }
        return list;
    }
}

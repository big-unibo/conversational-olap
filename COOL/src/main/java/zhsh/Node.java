package zhsh;

import java.util.ArrayList;

/**
 * A node in a tree.
 */
public class Node {
    public String label; // node label
    public int index; // preorder index
    public ArrayList<Node> children = new ArrayList<Node>();
    public Node leftmost; // used by the recursive O(n) leftmost() function

    /**
     * A node in a tree.
     */
    public Node() {
    }

    /**
     * A node in tree.
     *
     * @param label label
     */
    public Node(final String label) {
        this.label = label;
    }
}

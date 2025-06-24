package btree;

import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {
    private static int globalId = 0;

    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int count;
    protected final int idNode;

    public BNode(int n) {
        this.keys = new ArrayList<>(n);
        this.childs = new ArrayList<>(n + 1);
        this.count = 0;
        this.idNode = globalId++;

        for (int i = 0; i < n; i++) {
            this.keys.add(null);
        }

        for (int i = 0; i < n + 1; i++) {
            this.childs.add(null);
        }
    }

    public boolean nodeFull(int maxKeys) {
        return count == maxKeys;
    }

    public boolean nodeEmpty() {
        return count == 0;
    }

    public boolean searchNode(E key, int[] pos) {
        pos[0] = 0;
        while (pos[0] < count && keys.get(pos[0]).compareTo(key) < 0) {
            pos[0]++;
        }

        if (pos[0] < count && keys.get(pos[0]).compareTo(key) == 0) {
            return true;
        }

        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node ID: ").append(idNode).append(" | Keys: [");

        for (int i = 0; i < count; i++) {
            sb.append(keys.get(i));
            if (i < count - 1) sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }
}
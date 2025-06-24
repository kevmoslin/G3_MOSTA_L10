package btree;

public class BTree<E extends Comparable<E>> {
    private BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden){ 
        this.orden = orden;
        this.root = null;
    }
    
    public boolean isEmpty() {
        return this.root == null;
    }

    public void insert(E cl){ 
        up = false;
        E mediana;
        BNode<E> pnew;
        mediana = push(this.root, cl);
        if (up){ 
            pnew = new BNode<E>(this.orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, this.root);
            pnew.childs.set(1, nDes);
            this.root = pnew;
        }
    }


    private E push(BNode<E> current,E cl){
        int pos[] = new int[1];
        E mediana;
        if(current == null){
            up = true;
            nDes = null;
            return cl;
        } else{
            boolean fl;
            fl = current.searchNode(cl, pos);
            if(fl){
                System.out.println("Item duplicado\n");
                up = false;
                return null;
            }
            mediana = push(current.childs.get(pos[0]),cl);
            if(up){
                if(current.nodeFull(this.orden-1)){
                    mediana = dividedNode(current,mediana,pos[0]);
                } else{
                    up = false;
                    putNode(current,mediana,nDes,pos[0]);
                }
            }
            return mediana;
        }
    }

    private void putNode(BNode<E> current,E cl,BNode<E> rd,int k){
        int i;
        for(i = current.count-1; i >= k; i--){ 
            current.keys.set(i+1,current.keys.get(i));
            current.childs.set(i+2,current.childs.get(i+1));
        }
        current.keys.set(k,cl);
        current.childs.set(k+1,rd);
        current.count++;
    }

    private E dividedNode(BNode<E> current,E cl,int k){ 
        BNode<E> rd = nDes;
        int i, posMdna;
        posMdna = (k <= this.orden/2) ? this.orden/2 : this.orden/2+1;
        nDes = new BNode<E>(this.orden);
        for(i = posMdna; i < this.orden-1; i++){ 
            nDes.keys.set(i - posMdna,current.keys.get(i));
            nDes.childs.set(i-posMdna+1,current.childs.get(i+1));
        }
        nDes.count = (this.orden - 1) - posMdna;
        current.count = posMdna;
        if(k <= this.orden/2){
            putNode(current,cl,rd,k);
        } else{
            putNode(nDes,cl,rd,k-posMdna);
        }
        E median = current.keys.get(current.count-1);
        nDes.childs.set(0,current.childs.get(current.count));
        current.count--;
        return median;
    }

    public boolean search(E c1){
        return searchRecursive(this.root, c1);
    }

    public boolean searchRecursive(BNode<E> current, E cl){
        if (current == null) {
            return false;
        }

        int pos[] = new int[1];
        boolean found = current.searchNode(cl, pos);

        if (found) {
            System.out.println(cl + " se encuentra en el nodo " + current.idNode + " en la poscicion " + pos[0]);
            return true;
        } else{
            return searchRecursive(current.childs.get(pos[0]), cl);
        }
    }

    public void remove(E c1){

    }

    private void delete(BNode<E> node, E cl){
        int[] pos = new int[1];
        boolean found = node.searchNode(cl, pos);

        if (found) {
            if (node.childs.get(pos[0]) == null) {
                removeFromLeaf(node, orden);;
            }
        }
    }

    private void removeFromLeaf(BNode<E> node, int idx){
        for (int i = idx; i < node.count; i++) {
            node.keys.set(i, node.keys.get( + 1));
        }
        node.keys.set(node.count - 1, null);
        node.count--;
    }

    private void removeFromInternal(BNode<E> node, int idx){
        E key = node.keys.get(idx);

        //hijo izquierdo no esta vacio
        if (node.childs.get(idx).count >= minKeys() + 1) {
            E pred = getPredecessor(node, idx);
            node.keys.set(idx, pred);
            delete(node.childs.get(idx), pred);
        }

        //hijo derecho no esta vacio
        else if(node.childs.get( + 1).count >= minKeys() + 1) {
            E succ = getSuccessor(node, idx);
            node.keys.set(idx, succ);
            delete(node.childs.get(idx + 1), succ);
        }
    }

    private E getPredecessor(BNode<E> node, int idx){

    }

    private E getSuccessor(BNode<E> node, int idx){

    }

    private void fixUnderflow(BNode<E> parent, int idx){

    }

    private void borrowFromLeft(BNode<E> parent, int idx){

    }

    private void borrowFromRight(BNode<E> parent, int idx){

    }

    private void merge(BNode<E> parent, int idx){

    }

    private int minKeys(){

    }

    public String toString(){
        String s = " ";
        if (isEmpty()) {
            s += "BTree is empty";
        } else{
            s = writeTree(this.root, 0);
        }
        return s;
    }

    private String writeTree(BNode<E> current, int level){
        StringBuilder sb = new StringBuilder();
        if (current != null) {
            sb.append("Nivel ").append(level).append("[");
            for (int i = 0; i < current.count; i++) {
                sb.append(current.keys.get(i));
                if (i < current.count - 1) {
                    sb.append(" | ");
                }
            }
            sb.append("]\n");

            for (int i = 0; i < current.count; i++) {
                if (current.childs.get(i) != null) {
                    sb.append(writeTree(current.childs.get(i), level + 1));
                }
            }
        }
        return sb.toString();
    }
}
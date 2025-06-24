package btree;

import java.io.*;
import java.util.*;

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
        if (!isEmpty()) {
            delete(root, c1);
            if (root.count == 0 && root.childs.get(0) != null) {
                root = root.childs.get(0);
            }
        }
    }

    private void delete(BNode<E> node, E cl){
        int[] pos = new int[1];
        boolean found = node.searchNode(cl, pos);

        if (found) {
            if (node.childs.get(pos[0]) == null) {
                removeFromLeaf(node, orden);;
            } else{
                removeFromInternal(node, pos[0]);
            }
        } else{
            if (node.childs.get(pos[0]) == null) {
                System.out.println("Clave " + cl + " no encontrada");
                return;
            }

            delete(node.childs.get(pos[0]), cl);

            if (node.childs.get(pos[0]).count < minKeys()) {
                fixUnderflow(node, pos[0]);
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
        E pred;

        //hijo izquierdo no esta vacio
        if (node.childs.get(idx).count >= minKeys() + 1) {
            pred = getPredecessor(node, idx);
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
        BNode<E> current = node.childs.get(idx);
        while (current.childs.get(current.count) != null) {
            current = current.childs.get(current.count);
        }

        return current.keys.get(current.count - 1);
    }

    private E getSuccessor(BNode<E> node, int idx){
        BNode<E> current = node.childs.get(idx + 1);
        while (current.childs.get(0) != null) {
            current = current.childs.get(0);
        }
        return current.keys.get(0);
    }

    private void fixUnderflow(BNode<E> parent, int idx){
        if (idx > 0 && parent.childs.get(idx - 1).count > minKeys()){
            borrowFromLeft(parent, idx);
        } else if (idx < parent.count && parent.childs.get(idx + 1).count > minKeys()){
            borrowFromRight(parent, idx);
        } else{
            if (idx < parent.count) {
                merge(parent, idx);
            } else{
                merge(parent, idx - 1);
            }
        }
    }

    private void borrowFromLeft(BNode<E> parent, int idx){
        BNode<E> child = parent.childs.get(idx);
        BNode<E> left = parent.childs.get(idx - 1);

        for (int i = child.count; i > 0; i--) {
            child.keys.set(i, child.keys.get(i - 1));
            child.childs.set(i + 1, child.childs.get(i));
        }
        child.childs.set(1, child.childs.get(0));

        child.keys.set(0, parent.keys.get(idx - 1));
        child.childs.set(0, left.childs.get(left.count));

        parent.keys.set(idx - 1, left.keys.get(left.count - 1));

        child.count++;
        left.count--;
    }

    private void borrowFromRight(BNode<E> parent, int idx){
        BNode<E> child = parent.childs.get(idx);
        BNode<E> right = parent.childs.get(idx + 1);

        child.keys.set(child.count, parent.keys.get(idx));
        child.childs.set(child.count + 1, right.childs.get(0));

        parent.keys.set(idx, right.keys.get(0));

        for (int i = 0; i < right.count - 1; i++) {
            right.keys.set(i, right.keys.get(i + 1));
            right.childs.set(i, right.childs.get(i + 1));
        }

        right.childs.set(right.count - 1, right.childs.get(right.count));
        right.childs.set(right.count, null);
        right.keys.set(right.count - 1, null);
        right.count--;

        child.count--;
    }

    private void merge(BNode<E> parent, int idx){
        BNode<E> child = parent.childs.get(idx);
        BNode<E> sibling = parent.childs.get(idx + 1);

        child.keys.set(child.count, parent.keys.get(idx));
        for (int i = 0; i < sibling.count; i++) {
            child.keys.set(child.count + 1 + i, sibling.keys.get(i));
            child.childs.set(child.count + 1 + i, sibling.childs.get(i));
        }
        child.childs.set(child.count + sibling.count + 1, sibling.childs.get(sibling.count));

        child.count += sibling.count + 1;

        for (int i = idx; i < parent.count - 1; i++) {
            parent.keys.set(i, parent.keys.get(i + 1));
            parent.childs.set(i + 1, parent.childs.get(i + 2));
        }

        parent.keys.set(parent.count - 1, null);
        parent.childs.set(parent.count, null);
        parent.count--;
    }

    private int minKeys(){
        return (orden - 1) / 2;
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

    public static BTree<Integer> building_BTree(String path) throws ItemNoFound{
        Map<Integer, BNode<Integer>> nodos = new HashMap<>();
        Map<Integer, Integer> niveles = new HashMap<>();
        List<String> lineas = new ArrayList<>();
        int orden = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea.trim());
            }
        } catch (IOException e) {
            throw new ItemNoFound("Error al leer el archivo: " + e.getMessage());
        }

        try {
            orden = Integer.parseInt(lineas.get(0));
        } catch (NumberFormatException e) {
            throw new ItemNoFound("Orden invalido");
        }

        BTree<Integer> tree = new BTree<>(orden);

        for (int i = 1; i < lineas.size(); i++) {
            String[] partes = lineas.get(i).split(",");
            if (partes.length < 3) {
                throw new ItemNoFound("Formato invalido en linea: " + lineas.get(i));
            }

            int nivel = Integer.parseInt(partes[0]);
            int id = Integer.parseInt(partes[1]);
            BNode<Integer> node = new BNode<>(orden);
            node.count = partes.length - 2;

            for (int j = 2; j < partes.length; j++){
                node.keys.set(j - 2, Integer.parseInt(partes[j]));
            }

            nodos.put(id, node);
            niveles.put(id, nivel);
        }

        for(Map.Entry<Integer, BNode<Integer>> entry : nodos.entrySet()){
            int id = entry.getKey();
            BNode<Integer> node = entry.getValue();
            int nivel = niveles.get(id);

            if (nivel == 0) {
                tree.root = node;
            } else{
                for (Map.Entry<Integer, BNode<Integer>> posiblePadre : nodos.entrySet()){
                    int idPadre = posiblePadre.getKey();
                    BNode<Integer> padre = posiblePadre.getValue();
                    int nivelPadre = niveles.get(idPadre);

                    if (nivelPadre == nivel - 1 && padre.childs.contains(null)) {
                        for (int i = 0; i < padre.childs.size(); i++){
                            if (padre.childs.get(i) == null) {
                                padre.childs.set(i, node);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }

        if (!validateBTree(tree.root, orden)) {
            throw new ItemNoFound("El arbol no cumple con las propiedades de un B-Tree.");
        }
        return tree;
    }

    private static boolean validateBTree(BNode<Integer> node, int orden){
        if (node == null) {
            return true;
        }

        if (node.count > orden - 1) {
            return false;
        }
        if (node.count < (orden - 1) / 2 && node != node.childs.get(0)) {
            return false;
        }

        for (int i = 0; i < node.count; i++) {
            if (node.childs.get(i) != null) {
                if (!validateBTree(node.childs.get(i), orden)) {
                    return false;
                }
            }
        }

        return true;
    }

    public String buscarNombre(int codigo){
        return buscarNombreRecursivo(this.root, codigo);
    }

    private String buscarNombreRecursivo(BNode<RegistroEstudiante> current, int codigo){
        if (current == null) {
            return "no encontrado. ";
        }

        int[] pos = new int[1];
        RegistroEstudiante buscado = new RegistroEstudiante(codigo, "");

        boolean found = current.searchNode(buscado, pos);
        
    }
}
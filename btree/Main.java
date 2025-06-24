package btree;

public class Main {
    public static void main(String[] args) {
        BTree<Integer> arbol = new BTree<>(4);

        int[] elementos = {10,20,5,6,12,30,7,17};

        for(int elem : elementos){
            arbol.insert(elem);
        }

        System.out.println("Contenido del arbol B: ");
        System.out.println(arbol.toString());
    }
}

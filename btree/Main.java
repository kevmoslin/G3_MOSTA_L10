package btree;

public class Main {
    public static void main(String[] args) {
        BTree<Integer> btree = new BTree<>(4);

        int[] datos = {25, 10, 16, 30, 40, 1, 3, 8, 15, 18, 19, 21, 27, 28, 33, 36, 39, 42, 45};

        for (int dato : datos) {
            btree.insert(dato);
        }

        // Mostrar el árbol
        System.out.println("Árbol B luego de insertar los elementos:");
        System.out.println(btree);

        // Eliminar algunos elementos
        btree.remove(8);
        btree.remove(30);
        btree.remove(25);

        System.out.println("\nÁrbol B luego de eliminar 8, 30 y 25:");
        System.out.println(btree);

        // Probar construcción desde archivo
        try {
            BTree<Integer> fromFile = BTree.building_BTree("arbolB.txt");
            System.out.println("\nÁrbol B cargado desde archivo:");
            System.out.println(fromFile);
        } catch (ItemNoFound e) {
            System.err.println("Error al construir el árbol desde archivo: " + e.getMessage());
        }
    }
}

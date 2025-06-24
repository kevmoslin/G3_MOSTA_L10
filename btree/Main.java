package btree;

public class Main {
    public static void main(String[] args) {
        BTree<Integer> arbol = new BTree<>(4); // Orden 4

        // Insertar elementos
        int[] insertar = {10, 20, 5, 6, 12, 30, 7, 17, 3, 8, 15, 2};
        for (int clave : insertar) {
            arbol.insert(clave);
        }

        // Mostrar árbol después de inserciones
        System.out.println("Árbol después de insertar:");
        System.out.println(arbol.toString());

        // Eliminar claves una por una
        int[] eliminar = {6, 7, 5, 8, 10, 12};

        for (int clave : eliminar) {
            System.out.println("\nEliminando clave: " + clave);
            arbol.remove(clave);
            System.out.println("Árbol después de eliminar " + clave + ":");
            System.out.println(arbol.toString());
        }
    }
}

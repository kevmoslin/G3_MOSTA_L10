package btree;

public class Main {
    public static void main(String[] args) {
        BTree<RegistroEstudiante> arbol = new BTree<>(4);

        // Insertar estudiantes
        arbol.insert(new RegistroEstudiante(103, "Ana"));
        arbol.insert(new RegistroEstudiante(110, "Luis"));
        arbol.insert(new RegistroEstudiante(101, "Carlos"));
        arbol.insert(new RegistroEstudiante(120, "Lucía"));
        arbol.insert(new RegistroEstudiante(115, "David"));
        arbol.insert(new RegistroEstudiante(125, "Jorge"));
        arbol.insert(new RegistroEstudiante(140, "Camila"));
        arbol.insert(new RegistroEstudiante(108, "Rosa"));
        arbol.insert(new RegistroEstudiante(132, "Ernesto"));
        arbol.insert(new RegistroEstudiante(128, "Denis"));
        arbol.insert(new RegistroEstudiante(145, "Enrique"));
        arbol.insert(new RegistroEstudiante(122, "Karina"));
        arbol.insert(new RegistroEstudiante(108, "Juan")); // Duplicado (verificación)

        // Mostrar árbol después de inserciones
        System.out.println("Árbol B luego de insertar estudiantes:");
        System.out.println(arbol);

        // Búsquedas
        System.out.println("Buscar código 115: " + arbol.buscarNombre(115)); // David
        System.out.println("Buscar código 132: " + arbol.buscarNombre(132)); // Ernesto
        System.out.println("Buscar código 999: " + arbol.buscarNombre(999)); // No encontrado

        // Eliminar estudiante con código 101
        arbol.remove(new RegistroEstudiante(101, ""));
        System.out.println("Árbol B luego de eliminar al estudiante con código 101:");
        System.out.println(arbol);

        // Insertar nuevo estudiante
        arbol.insert(new RegistroEstudiante(106, "Sara"));
        System.out.println("Árbol B luego de insertar a Sara:");
        System.out.println(arbol);

        // Buscar nuevo estudiante
        System.out.println("Buscar código 106: " + arbol.buscarNombre(106)); // Sara
    }
}

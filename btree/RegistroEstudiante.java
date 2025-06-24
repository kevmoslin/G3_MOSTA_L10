package btree;

public class RegistroEstudiante implements Comparable<RegistroEstudiante>{
    private int codigo;
    private String nombre;

    public RegistroEstudiante(int codigo, String nombre){
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public int getCodigo(){
        return codigo;
    }

    public String getNombre(){
        return nombre;
    }

    public int compareTo(RegistroEstudiante otro){
        return Integer.compare(this.codigo, otro.codigo);
    }

    public String toString(){
        return codigo + " - " + nombre;
    }
}

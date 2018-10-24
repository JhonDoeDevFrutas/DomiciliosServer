package jhondoe.com.domiciliosserver.data.model.entities;

public class Categoria {
    private String id;
    private String descripcion;
    private String imagen;

    public Categoria() {
    }

    public Categoria(String id, String descripcion, String imagen) {
        this.id = id;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    // Get & Set

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

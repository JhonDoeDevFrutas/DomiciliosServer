package jhondoe.com.domiciliosserver.data.model.entities;

public class Presupuesto {
    private String id;
    private Proveedor proveedor;
    private String precio;

    public Presupuesto() {
    }

    public Presupuesto(String id, Proveedor proveedor, String precio) {
        this.id = id;
        this.proveedor = proveedor;
        this.precio = precio;
    }

    // Get & Set
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}

package jhondoe.com.domiciliosserver.data.model.entities;

import java.io.Serializable;

public class Proveedor implements Serializable {
    private String id;
    private String nombre;
    private String direccion;
    private String movil;
    private String trabajo;

    public Proveedor() {
    }

    public Proveedor(String id, String nombre, String direccion, String movil, String trabajo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.movil = movil;
        this.trabajo = trabajo;
    }

    // Get & Set
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(String trabajo) {
        this.trabajo = trabajo;
    }
}

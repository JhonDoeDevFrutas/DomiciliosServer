package jhondoe.com.domiciliosserver.data.model.entities;

import java.util.List;

public class Solicitud {
    private String id;
    private String phone;
    private String name;
    private String address;
    private String total;
    private String status;
    private List<OrdenCompra> products;

    public Solicitud() {
    }

    public Solicitud(String phone, String name, String address, String total, List<OrdenCompra> products) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.products = products;
        this.status = "0"; // Default is 0 , 0: Placed, 1: Shipping, 2: Shipped
    }

    // Get & Set

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<OrdenCompra> getProducts() {
        return products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProducts(List<OrdenCompra> products) {
        this.products = products;
    }
}

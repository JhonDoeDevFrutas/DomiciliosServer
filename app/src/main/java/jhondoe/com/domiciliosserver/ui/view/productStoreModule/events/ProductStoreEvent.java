package jhondoe.com.domiciliosserver.ui.view.productStoreModule.events;

import java.util.List;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;

public class ProductStoreEvent {
    public static final  int SUCCESS = 0;
    public static final  int ERROR_SERVER = 100;

    private List<Producto> productos;
    private int typeEvent;
    private int resMsg;

    public ProductStoreEvent() {
    }

    // Get & Set
    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }

    public int getResMsg() {
        return resMsg;
    }

    public void setResMsg(int resMsg) {
        this.resMsg = resMsg;
    }
}

package jhondoe.com.domiciliosserver.ui.productModule.events;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;

public class ProductEvent {
    public static final int SUCCESS_ADD = 0;
    public static final int SUCCESS_UPDATE = 1;
    public static final int SUCCESS_REMOVE = 2;
    public static final int ERROR_SERVER = 100;
    public static final int ERROR_TO_REMOVE = 101;

    private Producto producto;
    private int typeEvent;
    private int resMsg;

    public ProductEvent() {
    }

    // Get & Set
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
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

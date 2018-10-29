package jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.events;

import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;

public class OrderStatusEvent {
    public static final int SUCCESS_UPDATE = 1;
    public static final int ERROR_SERVER = 100;

    private Solicitud order;
    private int typeEvent;
    private int resMsg;

    public OrderStatusEvent() {
    }

    // Get & Set
    public Solicitud getOrder() {
        return order;
    }

    public void setOrder(Solicitud order) {
        this.order = order;
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

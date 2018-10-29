package jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.model;

import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;

public interface OrderStatusInteractor {
    void subscribeToOrderStatus();
    void unsubscribeToOrderStatus();

}

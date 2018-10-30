package jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.model.dataAccess;

import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;

public interface OrderStatusEventListener {
    void onChildUpdated(Solicitud solicitud);

    void onError(int resMsg);

}

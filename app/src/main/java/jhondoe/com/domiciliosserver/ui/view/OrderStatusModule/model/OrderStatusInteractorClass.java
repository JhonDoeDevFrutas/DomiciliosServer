package jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.model;

import org.greenrobot.eventbus.EventBus;

import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;
import jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.events.OrderStatusEvent;
import jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.model.dataAccess.OrderStatusEventListener;
import jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.model.dataAccess.RealtimeDatabase;

public class OrderStatusInteractorClass implements OrderStatusInteractor {
    private RealtimeDatabase mDatabase;

    public OrderStatusInteractorClass() {
        mDatabase = new RealtimeDatabase();
    }

    @Override
    public void subscribeToOrderStatus() {
        mDatabase.subscribeToOrderStatus(new OrderStatusEventListener() {
            @Override
            public void onChildUpdated(Solicitud solicitud) {
                post(solicitud, OrderStatusEvent.SUCCESS_UPDATE);
            }

            @Override
            public void onError(int resMsg) {
                post(OrderStatusEvent.ERROR_SERVER, resMsg);
            }
        });

    }

    @Override
    public void unsubscribeToOrderStatus() {
        mDatabase.unsubscribeToOrderStatus();
    }

    private void post(int typeEvent, int resMsg){
        post(null, typeEvent, resMsg);
    }

    private void post(Solicitud solicitud, int typeEvent){
        post(solicitud, typeEvent, 0);
    }

    private void post(Solicitud solicitud, int typeEvent, int resMsg) {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrder(solicitud);
        event.setTypeEvent(typeEvent);
        event.setResMsg(resMsg);
        EventBus.getDefault().post(event);
    }

}

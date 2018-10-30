package jhondoe.com.domiciliosserver.ui.view.OrderStatusModule;

import jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.events.OrderStatusEvent;

public interface OrderStatusPresenter {
    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();

    void onEventListener(OrderStatusEvent event);
}

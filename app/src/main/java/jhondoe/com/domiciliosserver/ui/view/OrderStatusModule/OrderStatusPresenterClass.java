package jhondoe.com.domiciliosserver.ui.view.OrderStatusModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.events.OrderStatusEvent;
import jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.model.OrderStatusInteractor;
import jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.model.OrderStatusInteractorClass;
import jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.view.OrderDetailView;

public class OrderStatusPresenterClass implements OrderStatusPresenter {
    private OrderDetailView mView;
    private OrderStatusInteractor mInteractor;

    public OrderStatusPresenterClass(OrderDetailView mView) {
        this.mView = mView;
        this.mInteractor = new OrderStatusInteractorClass();
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        mInteractor.unsubscribeToOrderStatus();
    }

    @Override
    public void onResume() {
        mInteractor.subscribeToOrderStatus();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mView = null;
    }

    private boolean setProgress(){
        if (mView != null){
            mView.showProgress();
            return true;
        }
        return false;
    }

    @Subscribe
    @Override
    public void onEventListener(OrderStatusEvent event) {
        if (mView != null){
            mView.hideProgress();

            switch (event.getTypeEvent()){

                case OrderStatusEvent.ERROR_SERVER:
                    mView.onShowError(event.getResMsg());
                    break;
            }
        }
    }
}

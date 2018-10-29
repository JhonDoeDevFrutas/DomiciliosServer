package jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.view;

import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;

public interface OrderDetailView {
    void showProgress();
    void hideProgress();

    void removeFail();
    void onShowError(int resMsg);
}


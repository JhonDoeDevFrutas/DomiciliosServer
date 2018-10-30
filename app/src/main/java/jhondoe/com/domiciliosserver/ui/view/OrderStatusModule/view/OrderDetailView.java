package jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.view;

public interface OrderDetailView {
    void showProgress();
    void hideProgress();

    void removeFail();
    void onShowError(int resMsg);
}


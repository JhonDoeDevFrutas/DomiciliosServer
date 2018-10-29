package jhondoe.com.domiciliosserver.ui.view.productStoreModule.view;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;

public interface ProductStoreView {
    void showProgress();
    void hideProgress();
    void enableUIElements();
    void disableUIElements();

    void showFab();
    void hideFab();

    void add(Producto producto);
    void update(Producto producto);
    void delete(Producto producto);

    void removeFail();
    void onShowError(int resMsg);

}

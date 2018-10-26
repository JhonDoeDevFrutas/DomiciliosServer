package jhondoe.com.domiciliosserver.ui.view;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;

public interface ProductStoreView {
    void showProgress();
    void hideProgress();

    void add(Producto producto);
    void update(Producto producto);
    void delete(Producto producto);

    void removeFail();
    void onShowError(int resMsg);
}

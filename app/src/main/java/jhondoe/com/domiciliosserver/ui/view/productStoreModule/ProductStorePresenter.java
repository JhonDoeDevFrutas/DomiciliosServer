package jhondoe.com.domiciliosserver.ui.view.productStoreModule;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.ui.view.productStoreModule.events.ProductStoreEvent;

public interface ProductStorePresenter {
    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();

    void updateProduct(Producto producto);

    void onEventListener(ProductStoreEvent event);

}

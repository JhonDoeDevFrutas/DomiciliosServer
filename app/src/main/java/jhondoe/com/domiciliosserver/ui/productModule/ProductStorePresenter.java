package jhondoe.com.domiciliosserver.ui.productModule;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.ui.productModule.events.ProductEvent;

public interface ProductStorePresenter {
    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();

    void remove(Producto producto);

    void onEventListener(ProductEvent event);
}

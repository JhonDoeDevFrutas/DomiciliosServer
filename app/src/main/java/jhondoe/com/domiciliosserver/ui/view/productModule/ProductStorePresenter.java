package jhondoe.com.domiciliosserver.ui.view.productModule;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.ui.view.productModule.events.ProductEvent;

public interface ProductStorePresenter {
    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();

    void remove(Producto producto);

    void onEventListener(ProductEvent event);
}

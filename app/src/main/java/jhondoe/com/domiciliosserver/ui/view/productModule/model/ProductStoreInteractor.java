package jhondoe.com.domiciliosserver.ui.view.productModule.model;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;

public interface ProductStoreInteractor {
    void subscribeToProducts();
    void unsubscribeToProducts();

    void removeProduct(Producto producto);
}

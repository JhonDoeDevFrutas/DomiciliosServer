package jhondoe.com.domiciliosserver.ui.view.productStoreModule.model;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;

public interface ProductStoreInteractor {
    void subscribeToProducts(String idCategory);
    void unsubscribeToProducts();

}

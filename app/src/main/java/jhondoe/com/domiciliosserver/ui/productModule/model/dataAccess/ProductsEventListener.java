package jhondoe.com.domiciliosserver.ui.productModule.model.dataAccess;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;

public interface ProductsEventListener {
    void onChildAdded(Producto producto);
    void onChildUpdated(Producto producto);
    void onChildRemoved(Producto producto);

    void onError(int resMsg);

}

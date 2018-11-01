package jhondoe.com.domiciliosserver.ui.view.productStoreModule.model.dataAccess;

import java.util.List;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;

public interface ProductStoreEventListener {
    void onDataChange(List<Producto> productoList);

    void onError(int resMsg);
}

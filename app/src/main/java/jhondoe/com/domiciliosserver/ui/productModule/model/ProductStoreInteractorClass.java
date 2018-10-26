package jhondoe.com.domiciliosserver.ui.productModule.model;

import jhondoe.com.domiciliosserver.common.BasicErrorEventCallback;
import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.ui.productModule.events.ProductEvent;
import jhondoe.com.domiciliosserver.ui.productModule.model.dataAccess.ProductsEventListener;
import jhondoe.com.domiciliosserver.ui.productModule.model.dataAccess.RealtimeDatabase;

public class ProductStoreInteractorClass implements ProductStoreInteractor {
    private RealtimeDatabase mDatabase;

    public ProductStoreInteractorClass() {
        mDatabase = new RealtimeDatabase();

    }

    @Override
    public void subscribeToProducts() {
        mDatabase.subscribeToProducts(new ProductsEventListener() {
            @Override
            public void onChildAdded(Producto producto) {
                post(producto, ProductEvent.SUCCESS_ADD);
            }

            @Override
            public void onChildUpdated(Producto producto) {
                post(producto, ProductEvent.SUCCESS_UPDATE);
            }

            @Override
            public void onChildRemoved(Producto producto) {
                post(producto, ProductEvent.SUCCESS_REMOVE);
            }

            @Override
            public void onError(int resMsg) {
                pos(ProductEvent.ERROR_SERVER, resMsg);
            }
        });
    }


    @Override
    public void unsubscribeToProducts() {
        mDatabase.unsubscribeToProducts();
    }

    @Override
    public void removeProduct(Producto producto) {
        mDatabase.removeProduct(producto, new BasicErrorEventCallback() {
            @Override
            public void onSuccess() {
                pos(ProductEvent.SUCCESS_REMOVE);

            }

            @Override
            public void onError(int typeEvent, int resMsg) {

            }
        });

    }

    private void pos(int typeEvent){
        post(null, typeEvent, 0);
    }

    private void pos(int typeEvent, int resMsg){
        post(null, typeEvent, resMsg);
    }

    private void post(Producto producto, int typeEvent){
        post(producto, typeEvent, 0);
    }

    private void post(Producto producto, int typeEvent, int resMsg) {
        ProductEvent event = new ProductEvent();
        event.setProducto(producto);;
        event.setTypeEvent(typeEvent);
        event.setResMsg(resMsg);

    }

}

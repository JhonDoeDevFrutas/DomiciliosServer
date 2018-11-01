package jhondoe.com.domiciliosserver.ui.view.productStoreModule.model;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.ui.view.productStoreModule.events.ProductStoreEvent;
import jhondoe.com.domiciliosserver.ui.view.productStoreModule.model.dataAccess.ProductStoreEventListener;
import jhondoe.com.domiciliosserver.ui.view.productStoreModule.model.dataAccess.RealtimeDatabase;

public class ProductStoreInteractorClass implements ProductStoreInteractor {
    private RealtimeDatabase mDatabase;

    public ProductStoreInteractorClass(){
        mDatabase = new RealtimeDatabase();
    }

    @Override
    public void subscribeToProducts(String idCategory) {
        mDatabase.subscribeToProducts(idCategory, new ProductStoreEventListener() {
            @Override
            public void onDataChange(List<Producto> productoList) {
                post(productoList, ProductStoreEvent.SUCCESS);
            }

            @Override
            public void onError(int resMsg) {
                post(ProductStoreEvent.ERROR_SERVER, resMsg);
            }
        });
    }

    @Override
    public void unsubscribeToProducts() {
        mDatabase.unsubscribeToProducts();
    }

    private void post(int typeEvent, int resMsg){
        post(null, typeEvent, resMsg);
    }

    private void post(List<Producto> productos, int typeEvent){
        post(productos, typeEvent, 0);
    }

    private void post(List<Producto> productos, int typeEvent, int resMsg){
        ProductStoreEvent event = new ProductStoreEvent();
        event.setProductos(productos);
        event.setTypeEvent(typeEvent);
        event.setResMsg(resMsg);
        EventBus.getDefault().post(event);
    }
}

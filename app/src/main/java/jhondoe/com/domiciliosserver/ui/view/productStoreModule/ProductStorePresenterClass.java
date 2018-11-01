package jhondoe.com.domiciliosserver.ui.view.productStoreModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import jhondoe.com.domiciliosserver.ui.view.productStoreModule.events.ProductStoreEvent;
import jhondoe.com.domiciliosserver.ui.view.productStoreModule.model.ProductStoreInteractor;
import jhondoe.com.domiciliosserver.ui.view.productStoreModule.model.ProductStoreInteractorClass;
import jhondoe.com.domiciliosserver.ui.view.productStoreModule.view.ProductStoreView;

/*
    ¿De que debería estar libre nuestro Presenter?
        Del framework de android.
        De conexiones directas a la base de datos
 */
public class ProductStorePresenterClass implements ProductStorePresenter{
    private ProductStoreView mView;
    private ProductStoreInteractor mInteractor;

    public ProductStorePresenterClass(ProductStoreView mView){
        this.mView = mView;
        this.mInteractor = new ProductStoreInteractorClass();
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        mInteractor.unsubscribeToProducts();
    }

    @Override
    public void onResume(String idCategory) {
        mInteractor.subscribeToProducts(idCategory);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mView = null;
    }

    @Subscribe
    @Override
    public void onEventListener(ProductStoreEvent event) {
        if (mView != null){
            mView.hideProgress();

            switch (event.getTypeEvent()){
                case ProductStoreEvent.SUCCESS:
                    mView.addDatas(event.getProductos());
                    break;
                case ProductStoreEvent.ERROR_SERVER :
                    mView.onShowError(event.getResMsg());
                    break;
            }
        }

    }
}

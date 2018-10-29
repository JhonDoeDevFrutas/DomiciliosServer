package jhondoe.com.domiciliosserver.ui.view.productModule;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.ui.view.productModule.events.ProductEvent;
import jhondoe.com.domiciliosserver.ui.view.productModule.model.ProductStoreInteractor;
import jhondoe.com.domiciliosserver.ui.view.productModule.model.ProductStoreInteractorClass;
import jhondoe.com.domiciliosserver.ui.view.ProductStoreView;

public class ProductStorePresenterClass implements ProductStorePresenter {
    private ProductStoreView mView;
    private ProductStoreInteractor mInteractor;

    public ProductStorePresenterClass(ProductStoreView mView) {
        this.mView = mView;
        this.mInteractor = new ProductStoreInteractorClass();
    }

    @Override
    public void onCreate() {


    }

    @Override
    public void onPause() {
        mInteractor.unsubscribeToProducts();
    }

    @Override
    public void onResume() {
        mInteractor.subscribeToProducts();
    }

    @Override
    public void onDestroy() {
        mView = null;
        
    }

    @Override
    public void remove(Producto producto) {
        if (setProgress()){
            mInteractor.removeProduct(producto);
        }
    }

    @Override
    public void onEventListener(ProductEvent event) {
        if (mView != null){
            mView.hideProgress();

            switch (event.getTypeEvent()){
                case ProductEvent.SUCCESS_ADD:
                    mView.add(event.getProducto());
                    break;
                case ProductEvent.SUCCESS_UPDATE:
                    mView.update(event.getProducto());
                    break;
                case ProductEvent.SUCCESS_REMOVE:

                    break;
                case ProductEvent.ERROR_SERVER:
                    mView.onShowError(event.getResMsg());
                    break;
                case ProductEvent.ERROR_TO_REMOVE:
                    mView.removeFail();
                    break;

            }
        }
    }

    private boolean setProgress() {
        if (mView != null){
            mView.showProgress();
            return true;
        }
        return false;

    }

}

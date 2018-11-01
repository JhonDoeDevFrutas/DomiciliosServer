package jhondoe.com.domiciliosserver.ui.view.productStoreModule.view;

import java.util.List;

import jhondoe.com.domiciliosserver.data.model.entities.Producto;

/*
    Tiene la responsabilidad de presentar los datos y de reaccionar ante los eventos de pulsación
    que ejecute el usuario
 */
public interface ProductStoreView {
    void showProgress();
    void hideProgress();

    void addDatas(List<Producto> datas);

    void onShowError(int resMsg);
}

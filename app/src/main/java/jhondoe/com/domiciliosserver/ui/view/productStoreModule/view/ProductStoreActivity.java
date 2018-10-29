package jhondoe.com.domiciliosserver.ui.view.productStoreModule.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.ui.view.productStoreModule.ProductStorePresenter;

public class ProductStoreActivity extends AppCompatActivity implements ProductStoreView{

    private ProductStorePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_store2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void enableUIElements() {

    }

    @Override
    public void disableUIElements() {

    }

    @Override
    public void showFab() {

    }

    @Override
    public void hideFab() {

    }

    @Override
    public void add(Producto producto) {

    }

    @Override
    public void update(Producto producto) {

    }

    @Override
    public void delete(Producto producto) {

    }

    @Override
    public void removeFail() {

    }

    @Override
    public void onShowError(int resMsg) {

    }
}

package jhondoe.com.domiciliosserver.ui.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.ui.adapter.ProductStoreAdapter;
import jhondoe.com.domiciliosserver.ui.productModule.ProductStorePresenter;
import jhondoe.com.domiciliosserver.ui.productModule.ProductStorePresenterClass;

public class ActivityProductStore extends AppCompatActivity implements ProductStoreView{

    private ProductStorePresenter mPresenter;
    private ProductStoreAdapter mAdapter;

    // Referencias UI
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_store);

        configToolbar();
        configAdapter();
        configRecyclerView();

        prepararFab();

        mPresenter = new ProductStorePresenterClass(this);
        mPresenter.onCreate();
    }

    private void configToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void configAdapter(){
        mAdapter = new ProductStoreAdapter(ActivityProductStore.this ,new ArrayList<Producto>(0));
    }

    private void configRecyclerView(){
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.columns));

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void prepararFab() {
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
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    /*ProductStoreView*/
    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

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

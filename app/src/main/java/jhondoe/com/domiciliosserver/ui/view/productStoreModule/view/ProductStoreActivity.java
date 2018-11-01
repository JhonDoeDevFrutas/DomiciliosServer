package jhondoe.com.domiciliosserver.ui.view.productStoreModule.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Categoria;
import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.ui.adapter.ProductStoreAdapter;
import jhondoe.com.domiciliosserver.ui.view.productStoreModule.ProductStorePresenter;
import jhondoe.com.domiciliosserver.ui.view.productStoreModule.ProductStorePresenterClass;

public class ProductStoreActivity extends AppCompatActivity implements ProductStoreView{

    private ProductStorePresenter mPresenter;
    private ProductStoreAdapter mAdapter;

    // Referencias UI
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_store2);

        Intent intentProductStore = getIntent();
        mId = intentProductStore.getStringExtra(Categoria.ID);

        // Preparar elementos UI
        prepararUI();
        configToolbar();
        prepararFab();
        configAdapter(new ArrayList<Producto>());
        configRecyclerView();

        mPresenter = new ProductStorePresenterClass(this);
        mPresenter.onCreate();
    }

    private void prepararUI() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    private void configToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void configAdapter(List<Producto> productsList) {
        mAdapter = new ProductStoreAdapter(this, productsList);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
    }

    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.columns));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
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
        mPresenter.onResume(mId);
        //Log.v(LOG_TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    /* ProductStoreView */
    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void addDatas(List<Producto> datas) {
        configAdapter(datas);
    }

    @Override
    public void onShowError(int resMsg) {

    }
}

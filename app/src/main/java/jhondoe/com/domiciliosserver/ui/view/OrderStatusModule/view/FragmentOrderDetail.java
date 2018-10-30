package jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.view;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;
import jhondoe.com.domiciliosserver.ui.adapter.OrderAdapter;
import jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.OrderStatusPresenter;
import jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.OrderStatusPresenterClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOrderDetail extends Fragment implements OrderDetailView{

    private OrderStatusPresenter mPresenter;
    private OrderAdapter mAdapter;

    // Referencias UI
    private View view;
    private RecyclerView mReciclador;
    private ProgressBar mProgressBar;

    public FragmentOrderDetail() {
        mPresenter = new OrderStatusPresenterClass(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        // Init view
        configAdapter();
        configRecyclerView();
        prepararUI();

        mPresenter.onCreate();

        return view;
    }

    private void prepararUI() {
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
    }

    private void configAdapter() {
        mAdapter = new OrderAdapter(getActivity(), new ArrayList<Solicitud>(0));
    }

    private void configRecyclerView() {
        mReciclador = (RecyclerView)view.findViewById(R.id.reciclador_orders);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }

    /*
    * OrderDetailView
    * */
    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void removeFail() {

    }

    @Override
    public void onShowError(int resMsg) {

    }

    /*
        if (getActivity() != null){
                getActivity().onBackPressed();
                }
    */

}

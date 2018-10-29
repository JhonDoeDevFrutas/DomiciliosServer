package jhondoe.com.domiciliosserver.ui.view;


import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.common.Common;
import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;
import jhondoe.com.domiciliosserver.data.preferences.SessionPrefs;
import jhondoe.com.domiciliosserver.provider.FirebaseReferences;
import jhondoe.com.domiciliosserver.ui.adapter.OrderAdapter;
import jhondoe.com.domiciliosserver.utilies.Uweb;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOrderStatus extends Fragment {

    // Referencias UI
    private View view;
    private RecyclerView mReciclador;
    private OrderAdapter mAdapter;

    MaterialSpinner spinner;

    FirebaseDatabase mDatabase;
    //our database reference object
    DatabaseReference mDbReference;

    //a list to store all the orders from firebase database
    List<Solicitud> solicitudList;

    String phone = SessionPrefs.get(getActivity()).getPhone();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_fragment_order_status, container, false);

        // Init service
        mReciclador = (RecyclerView)view.findViewById(R.id.reciclador_orders);
        mAdapter    = new OrderAdapter(getActivity(), new ArrayList<Solicitud>(0));

        mDatabase = FirebaseDatabase.getInstance();
        //getting the reference of node
        mDbReference = mDatabase.getReference(FirebaseReferences.ORDEN_COMPRA);

        //list to store
        solicitudList = new ArrayList<>();

        // Checks if the device has any active internet connection.
        if (!Uweb.isNetworkConnected(getActivity())){
            onShowNetWorkError(getString(R.string.error_network));
        }

        // If we start OrderSatus activity from home activity
        // We will not any extra, so we just loadOrder by phone from common
/*
        if (getActivity().getIntent() == null){
            phone = getActivity().getIntent().getStringExtra("userPhone");
        }
*/

        if (Common.NOTIFICATION == "request"){
            phone = getActivity().getIntent().getStringExtra("userPhone");
        }

        return view;
    }

    private void onShowNetWorkError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        //attaching value event listener
        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous orders list
                solicitudList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    //getting order
                    Solicitud solicitud = postSnapshot.getValue(Solicitud.class);
                    solicitud.setId(postSnapshot.getKey());
                    //adding order to the list
                    solicitudList.add(solicitud);

                    //adapter
                    mAdapter = new OrderAdapter(getActivity(), solicitudList);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.setOnItemSelectedListener(new OrderAdapter.OnItemSelectedListener() {
                        @Override
                        public void onMenuAction(Solicitud selectOrder, MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_custom_edit:
                                    showUpdateDialog(selectOrder);
                                    break;
                                case R.id.menu_custom_delete:
                                    deleteOrder(selectOrder);
                                    break;
                            }
                        }
                    });
                    mAdapter.setOnItemLongClickListener(new OrderAdapter.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(int position) {
                            return false;
                        }
                    });

                    mReciclador.setAdapter(mAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void onStartOld(){
        String phone = SessionPrefs.get(getActivity()).getPhone();
        Query myQuery = mDbReference.orderByChild("phone").equalTo(phone);

        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous orders list
                solicitudList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    //getting order
                    Solicitud solicitud = postSnapshot.getValue(Solicitud.class);
                    solicitud.setId(postSnapshot.getKey());
                    //adding order to the list
                    solicitudList.add(solicitud);
                }
                //adapter
                mAdapter = new OrderAdapter(getActivity(), solicitudList);
                mAdapter.notifyDataSetChanged();

                mAdapter.setOnItemSelectedListener(new OrderAdapter.OnItemSelectedListener() {
                    @Override
                    public void onMenuAction(Solicitud selectOrder, MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_custom_edit:
                                showUpdateDialog(selectOrder);
                                break;
                            case R.id.menu_custom_delete:
                                deleteOrder(selectOrder);
                                break;
                        }
                    }
                });

                mReciclador.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });

    }

    private void deleteOrder(Solicitud solicitud) {
        String id = solicitud.getId();
        mDbReference.child(id).removeValue();
    }

    private void showUpdateDialog(final Solicitud solicitud) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Orden de actualización");
        alertDialog.setMessage("Por favor elige el estado");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_order_layout, null);

        spinner = (MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Pedido Realizado","En camino", "Enviado");

        alertDialog.setView(view);

        alertDialog.setPositiveButton("Guadar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String id = solicitud.getId();
                solicitud.setStatus(String.valueOf(spinner.getSelectedIndex()));

                //Saving
                mDbReference.child(id).setValue(solicitud);

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

}

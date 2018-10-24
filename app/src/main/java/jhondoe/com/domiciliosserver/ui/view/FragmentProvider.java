package jhondoe.com.domiciliosserver.ui.view;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Proveedor;
import jhondoe.com.domiciliosserver.provider.FirebaseReferences;
import jhondoe.com.domiciliosserver.ui.adapter.ProviderAdapter;
import jhondoe.com.domiciliosserver.utilies.Uweb;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProvider extends Fragment {

    // Referencias UI
    private View view;
    private RecyclerView mReciclador;
    private ProviderAdapter mAdapter;

    // Referencia FireBase
    DatabaseReference mDbReference;//our database reference object
    //our database reference object

    //a list to provider all the section from firebase database
    List<Proveedor> providerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_provider, container, false);

        // Preparar elementos UI
        mReciclador = (RecyclerView)view.findViewById(R.id.recycler_providers);
        mAdapter    = new ProviderAdapter(getActivity(), new ArrayList<Proveedor>(0));

        // Firebase Init
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //getting the reference of node
        mDbReference = database.getReference(FirebaseReferences.PROVIDER_REFERENCE);

        /*Leer datos una unica vez*/
        /*
        mDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/

        // Checks if the device has any active internet connection.
        if (!Uweb.isNetworkConnected(getActivity())){
            onShowNetWorkError(getString(R.string.error_network));
        }

        //list to store
        providerList = new ArrayList<>();

        prepararFab();
        
        return view;
    }

    private void prepararFab() {
        FloatingActionButton fabProvider = (FloatingActionButton)view.findViewById(R.id.fab_provider);
        fabProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Nuevo Proveedor");
        alertDialog.setMessage("Por favor complete la informacion");

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View add_provider = inflater.inflate(R.layout.add_new_provider, null);

        final EditText edtName          = (EditText)add_provider.findViewById(R.id.edt_name);
        final EditText edtAddress       = (EditText)add_provider.findViewById(R.id.edt_address);
        final EditText edtPhone         = (EditText)add_provider.findViewById(R.id.edt_phone);
        final EditText edtLocalPhone    = (EditText)add_provider.findViewById(R.id.edt_local_phone);

        alertDialog.setView(add_provider);
        alertDialog.setIcon(R.drawable.ic_add);

        alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Set value for new provider
                //getting a unique id using push().getKey() method
                //it will create a unique id and we will use it as the Primary Key for our provider
                String id = mDbReference.push().getKey();

                String name         = edtName.getText().toString().trim();
                String address      = edtAddress.getText().toString().trim();
                String phone        = edtPhone.getText().toString().trim();
                String localPhone   = edtLocalPhone.getText().toString().trim();

                if (!TextUtils.isEmpty(name)){
                    //creating an provider Object
                    Proveedor proveedor = new Proveedor(id,name,address,phone,localPhone);

                    //Saving the Provider
                    mDbReference.child(id).setValue(proveedor);

                } else {
                    Toast.makeText(getActivity(), "Your should enter a name", Toast.LENGTH_LONG).show();
                }


                dialog.dismiss();
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
                //clearing the previous provider list
                providerList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    //getting provider
                    Proveedor proveedorTmp = postSnapshot.getValue(Proveedor.class);
                    proveedorTmp.setId(postSnapshot.getKey());
                    //adding provider to the list
                    providerList.add(proveedorTmp);

                    //adapter
                    mAdapter = new ProviderAdapter(getActivity(), providerList);
                    mAdapter.notifyDataSetChanged();
                    mReciclador.setAdapter(mAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

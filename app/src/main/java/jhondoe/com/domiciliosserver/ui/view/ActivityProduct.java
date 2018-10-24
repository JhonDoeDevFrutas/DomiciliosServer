package jhondoe.com.domiciliosserver.ui.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.data.model.entities.ProductoProveedor;
import jhondoe.com.domiciliosserver.data.model.entities.Proveedor;
import jhondoe.com.domiciliosserver.provider.FirebaseReferences;
import jhondoe.com.domiciliosserver.ui.adapter.PrecioProveedorAdapter;
import jhondoe.com.domiciliosserver.utilies.Uweb;

public class ActivityProduct extends AppCompatActivity {

    // Referencias UI
    ImageView imgProduct;
    TextView txtProductPrice;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView mReciclador;
    private PrecioProveedorAdapter mAdapter;

    private String mIdCategoria;
    private String mId;

    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceProduct;
    DatabaseReference mReferencePreciProvider;

    Query myProviderQuery;

    //a list to store all the section from firebase database
    List<Proveedor> mProveedorList;
    List<ProductoProveedor> mPreciProveedorList ;
    Proveedor mProveedor;

    Producto mProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intentProduct = getIntent();
        mIdCategoria    = intentProduct.getStringExtra(ActivityProducts.CATEGORIA_ID);
        mId             = intentProduct.getStringExtra(ActivityProducts.PRODUCT_ID);

        // Firebase Init
        mDatabase               = FirebaseDatabase.getInstance();
        mReferenceProduct       = mDatabase.getReference(FirebaseReferences.PRODUCT_REFERENCE).child(mIdCategoria).child(mId);
        mReferencePreciProvider = mDatabase.getReference(FirebaseReferences.PRECI_PROVIDER_REFERENCE).child(mId);

        //getting the reference of node
        myProviderQuery = mDatabase.getReference(FirebaseReferences.PROVIDER_REFERENCE).orderByChild("nombre");

        //list to provider
        mProveedorList      = new ArrayList<>();
        mPreciProveedorList = new ArrayList<>();

        // Init view
        prepararFab();
        prepararUI();

        onBringData();// Traer Datos
    }

    private void onBringData() {
        getDetailProduct();
        getPreciProvider();
    }

    private void prepararFab() {
        FloatingActionButton fabProvider = (FloatingActionButton)findViewById(R.id.fab_preci);
        fabProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
    }

    private void showAddDialog() {
        // Reset values
        mProveedor = null;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityProduct.this);
        View view = getLayoutInflater().inflate(R.layout.add_new_price_provider, null);

        alertDialog.setTitle("Nuevo");
        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_add);

        List<String> providerList = getProviderList();

        // Elements UI
        MaterialSpinner providerSpinner = (MaterialSpinner)view.findViewById(R.id.provider_Spinner) ;
        final EditText editPrice = (EditText)view.findViewById(R.id.edit_Price);

        providerSpinner.setItems(providerList);
        providerSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                for (Proveedor proveedorTmp : mProveedorList) {
                    if (proveedorTmp.getNombre().equals(item.toString().trim())){
                        mProveedor = proveedorTmp;
                        break;
                    }
                }

            }
        });

        alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProveedor == null){
                    Toast.makeText(ActivityProduct.this, "Debe seleccionar un proveedor", Toast.LENGTH_SHORT).show();
                } else {
                    String price = editPrice.getText().toString().trim();
                    if (!price.toString().isEmpty()){
                        String id = mReferencePreciProvider.push().getKey();

                        ProductoProveedor preciProvider = new ProductoProveedor(id, mProveedor, price);

                        // Saving
                        mReferencePreciProvider.child(id).setValue(preciProvider);

                        dialog.dismiss();
                    }else {
                        Toast.makeText(ActivityProduct.this, R.string.error_price_required, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private List<String> getProviderList() {
        List<String> providerList = new ArrayList<>();
        providerList.add("SELECCIONE PROVEEDOR");

        for (Proveedor proveedorTmp : mProveedorList) {
            providerList.add(proveedorTmp.getNombre().trim());
        }

        return  providerList;
    }

    private void prepararUI() {

        txtProductPrice = (TextView)findViewById(R.id.product_price);

        imgProduct = (ImageView)findViewById(R.id.img_product);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

        mReciclador = (RecyclerView)findViewById(R.id.recycler_preci_provider);
        mAdapter    = new PrecioProveedorAdapter(getBaseContext(), new ArrayList<ProductoProveedor>(0));
    }

    private void getDetailProduct() {
        mReferenceProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProducto = dataSnapshot.getValue(Producto.class);

                // Set Image
                Picasso.with(getBaseContext()).load(mProducto.getImagen()).into(imgProduct);

                collapsingToolbarLayout.setTitle(mProducto.getNombre());

                txtProductPrice.setText(mProducto.getPrecio());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void getPreciProvider(){
        mReferencePreciProvider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPreciProveedorList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    ProductoProveedor preciProviderTmp = postSnapshot.getValue(ProductoProveedor.class);
                    //adding preci provider to the list
                    mPreciProveedorList.add(preciProviderTmp);
                }

                //adapter
                mAdapter = new PrecioProveedorAdapter(getBaseContext(), mPreciProveedorList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new PrecioProveedorAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ProductoProveedor clickedProvider) {
                        showUpdateDialog(clickedProvider);
                    }
                });
                mReciclador.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final ProductoProveedor productoProveedor) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityProduct.this);
        alertDialog.setTitle("Modificar");
        alertDialog.setMessage("Ingrese Valor Costo");

        final EditText editPrice = new EditText(ActivityProduct.this);
        editPrice.setText(productoProveedor.getPrecio().trim());
        editPrice.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        editPrice.setLayoutParams(lp);
        alertDialog.setView(editPrice); // Add edit text to alert dialog
        alertDialog.setIcon(R.drawable.ic_edit);

        alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String price = editPrice.getText().toString().trim();
                if (!price.toString().isEmpty()){
                    // Checks if the device has any active internet connection.
                    if (Uweb.isNetworkConnected(ActivityProduct.this)){
                        //getting the reference of node
                        String id           = productoProveedor.getId();
                        Proveedor proveedor = productoProveedor.getProveedor();

                        ProductoProveedor productoProveedorTmp = new ProductoProveedor(id,proveedor, price);

                        //getting the specified provider reference
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(FirebaseReferences.PRECI_PROVIDER_REFERENCE).child(mId).child(id);

                        //updating
                        dR.setValue(productoProveedorTmp);

                        Toast.makeText(ActivityProduct.this, "Registro Actualizado", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }else {
                    Toast.makeText(ActivityProduct.this, R.string.error_price_required, Toast.LENGTH_SHORT).show();
                }
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

    @Override
    protected void onStart() {
        super.onStart();

        //attaching value event listener
        myProviderQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProveedorList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    Proveedor proveedorTmp = postSnapshot.getValue(Proveedor.class);

                    //adding provider to the list
                    mProveedorList.add(proveedorTmp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

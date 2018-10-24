package jhondoe.com.domiciliosserver.ui.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.data.model.entities.Tienda;
import jhondoe.com.domiciliosserver.provider.FirebaseReferences;
import jhondoe.com.domiciliosserver.ui.adapter.ProductAdapter;

public class ActivityProducts extends AppCompatActivity {
    public static final String CATEGORIA_ID = "categoriaid";
    public static final String PRODUCT_ID = "productoid";

    // Referencias UI
    private RecyclerView mReciclador;
    private ProductAdapter mAdapter;

    EditText edtName, edtDescription, edtCharacteristics, edtProperties, edtPrice;
    Button btnSelect, btnUpload;

    private String mId;

    //a list to store all the product from firebase database
    List<Producto> productList;

    //our database reference object
    DatabaseReference mDbReference;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    Producto mProducto;

    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Intent intentProducts = getIntent();
        mId = intentProducts.getStringExtra(ActivityCategory.CATEGORIA_ID);

        mReciclador = (RecyclerView)findViewById(R.id.recycler_products);
        mAdapter = new ProductAdapter(this, new ArrayList<Producto>(0));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //getting the reference of product node
        mDbReference = database.getReference(FirebaseReferences.PRODUCT_REFERENCE).child(mId);

        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        //list to store
        productList     = new ArrayList<>();

        prepararFab();
    }

    private void prepararFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                showDialog();
                /*
                Intent intentProduct = new Intent(getBaseContext(), ActivityProduct.class);
                intentProduct.putExtra(CATEGORIA_ID, mId);
                startActivity(intentProduct);
                */
            }
        });

    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityProducts.this);
        alertDialog.setTitle("Adicionar Nuevo Producto");
        alertDialog.setMessage("Por favor complete la información");

        LayoutInflater inflater = getLayoutInflater();
        View add_product = inflater.inflate(R.layout.add_new_product, null);

        edtName             = (EditText) add_product.findViewById(R.id.edt_name);
        edtDescription      = (EditText) add_product.findViewById(R.id.edt_description);
        edtCharacteristics  = (EditText) add_product.findViewById(R.id.edt_characteristics);
        edtProperties       = (EditText) add_product.findViewById(R.id.edt_properties);
        edtPrice            = (EditText) add_product.findViewById(R.id.edt_price);

        btnSelect           = (Button ) add_product.findViewById(R.id.btn_select);
        btnUpload           = (Button ) add_product.findViewById(R.id.btn_upload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); // let user select image from gallery and save url of this image
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        alertDialog.setView(add_product);
        alertDialog.setIcon(R.drawable.ic_add);
        // set button
        alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Here, just create new product
                if (mProducto != null){
                    // Saving
                    String id = mProducto.getId();
                    mDbReference.child(id).setValue(mProducto);
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

    private void uploadImage() {

        if (saveUri != null){
            final ProgressDialog mDialog = new ProgressDialog(ActivityProducts.this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            final StorageReference imageFolder = mStorageReference.child("images/"+ UUID.randomUUID().toString());
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Set value for new store
                                    //getting a unique id using push().getKey() method
                                    //it will create a unique id and we will use it as the Primary Key for our category

                                    String id               = mDbReference.push().getKey();
                                    String name             = edtName.getText().toString().trim();
                                    String description      = edtDescription.getText().toString().trim();
                                    String characteristic   = edtCharacteristics.getText().toString().trim();
                                    String properties       = edtProperties.getText().toString().trim();

                                    String price            = edtPrice.getText().toString().trim();

                                    //creating an product Object
                                    mProducto = new Producto(id, name, description, uri.toString(), price, "0");
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(ActivityProducts.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST) ;

        btnSelect.setText("Imagen Seleccionada");
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous categorias list
                productList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Producto producto = postSnapshot.getValue(Producto.class);

                    //adding product to the list
                    productList.add(producto);
                }

                // Adapter
                mAdapter = new ProductAdapter(getBaseContext(), productList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Producto clickedProduct) {
                        onProducto(clickedProduct);
                    }
                });

                mReciclador.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onProducto(Producto producto) {
        Intent intentProduct = new Intent(getBaseContext(), ActivityProduct.class);
        intentProduct.putExtra(CATEGORIA_ID, mId);
        intentProduct.putExtra(PRODUCT_ID, producto.getId());
        startActivity(intentProduct);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            saveUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(ActivityProducts.this.getContentResolver(), saveUri);
                //imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

}

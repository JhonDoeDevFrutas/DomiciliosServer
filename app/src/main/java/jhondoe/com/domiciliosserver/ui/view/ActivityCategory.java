package jhondoe.com.domiciliosserver.ui.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import jhondoe.com.domiciliosserver.data.model.entities.Categoria;
import jhondoe.com.domiciliosserver.provider.FirebaseReferences;
import jhondoe.com.domiciliosserver.ui.adapter.CategoryAdapter;

public class ActivityCategory extends AppCompatActivity {
    public static final String CATEGORIA_ID = "categoriaid";

    // Referencias UI
    private RecyclerView mReciclador;
    private CategoryAdapter mAdapter;

    EditText edtName;
    Button btnSelect, btnUpload;

    private String mId;

    //a list to store all the product from firebase database
    List<Categoria> categoriaList;

    //our database reference object
    DatabaseReference mDbReference;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    Categoria mCategoria;

    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intentCategory = getIntent();
        mId = intentCategory.getStringExtra(FragmentStore.STORE_ID);

        mReciclador = (RecyclerView)findViewById(R.id.recycler_category);
        mAdapter    = new CategoryAdapter(this, new ArrayList<Categoria>(0));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //getting the reference of category node
        mDbReference = database.getReference(FirebaseReferences.CATEGORY_REFERENCE).child(mId);
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        //list to store
        categoriaList = new ArrayList<>();

        prepararFab();
    }

    private void prepararUI() {
    }

    private void prepararFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_category);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                showDialog();
            }
        });
    }

    void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityCategory.this);
        alertDialog.setTitle("Adicionar Nuevo Producto");
        alertDialog.setMessage("Por favor complete la información");

        LayoutInflater inflater = getLayoutInflater();
        View add_category = inflater.inflate(R.layout.add_new_category, null);

        edtName     = (EditText)add_category.findViewById(R.id.edt_name);
        btnSelect   = (Button) add_category.findViewById(R.id.btn_select);
        btnUpload   = (Button) add_category.findViewById(R.id.btn_upload);

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

        alertDialog.setView(add_category);
        alertDialog.setIcon(R.drawable.ic_add);
        // set button
        alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Here, just create new category
                if (mCategoria != null){
                    // Saving
                    String id = mCategoria.getId();
                    mDbReference.child(id).setValue(mCategoria);
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
            final ProgressDialog mDialog = new ProgressDialog(ActivityCategory.this);
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

                                    //creating an product Object
                                    mCategoria = new Categoria(id, name, uri.toString());
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(ActivityCategory.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        //attaching value event listener
        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoriaList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    Categoria categoria = postSnapshot.getValue(Categoria.class);
                    //adding section to the list
                    categoriaList.add(categoria);
                }

                //adapter
                mAdapter = new CategoryAdapter(ActivityCategory.this, categoriaList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Categoria clickedCategoria) {
                        onCategory(clickedCategoria);
                    }
                });

                mReciclador.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onCategory(Categoria categoria) {
        Intent intentProducts = new Intent(ActivityCategory.this, ActivityProducts.class);
        intentProducts.putExtra(CATEGORIA_ID, categoria.getId());
        startActivity(intentProducts);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            saveUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(ActivityCategory.this.getContentResolver(), saveUri);
                //imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

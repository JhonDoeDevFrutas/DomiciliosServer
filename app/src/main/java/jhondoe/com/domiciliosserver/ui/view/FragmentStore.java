package jhondoe.com.domiciliosserver.ui.view;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import jhondoe.com.domiciliosserver.data.model.entities.Tienda;
import jhondoe.com.domiciliosserver.provider.FirebaseReferences;
import jhondoe.com.domiciliosserver.ui.adapter.StoreAdapter;
import jhondoe.com.domiciliosserver.utilies.Uweb;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStore extends Fragment {
    public static final String STORE_ID = "storeId";

    // Referencias UI
    private View view;
    private RecyclerView mReciclador;
    private StoreAdapter mAdapter;

    EditText edtDescription;
    Button btnSelect, btnUpload;
    ImageView imageView;

    //a list to store all the section from firebase database
    List<Tienda> storeList;


    DatabaseReference mDbReference;//our database reference object
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    Tienda mTienda;
    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store, container, false);

        // Preparar elementos UI
        mReciclador = (RecyclerView)view.findViewById(R.id.recycler_store);
        mAdapter    = new StoreAdapter(getActivity(), new ArrayList<Tienda>(0));

        // Firebase Init
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //getting the reference of node
        mDbReference = database.getReference(FirebaseReferences.STORE_REFERENCE);

        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        // Checks if the device has any active internet connection.
        if (!Uweb.isNetworkConnected(getActivity())){
            onShowNetWorkError(getString(R.string.error_network));
        }

        //list to store
        storeList = new ArrayList<>();

        prepararFab();

        return view;
    }

    private void prepararFab() {
        FloatingActionButton fabStore = (FloatingActionButton)view.findViewById(R.id.fab_store);
        fabStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void showDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Adicionar Nueva Tienda");
        alertDialog.setMessage("Por favor complete la informacion");

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View add_store = inflater.inflate(R.layout.add_new_store, null);

        edtDescription  = (EditText) add_store.findViewById(R.id.edt_description);
        btnSelect       = (Button) add_store.findViewById(R.id.btn_select);
        btnUpload       = (Button) add_store.findViewById(R.id.btn_upload);

        //Event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(); // let user select image from gallery and save url of this image
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        alertDialog.setView(add_store);
        alertDialog.setIcon(R.drawable.ic_add);

        // set button
        alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //Here, just create new store
                if (mTienda != null){
                    //Saving
                    String id = mTienda.getId();
                    mDbReference.child(id).setValue(mTienda);

                }

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void uploadImage() {
        if (saveUri != null){
            final ProgressDialog mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Uploading...");
            mDialog.show();

            final StorageReference imageFolder = mStorageReference.child("images/"+ UUID.randomUUID().toString());
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Set value for new store
                                    //getting a unique id using push().getKey() method
                                    //it will create a unique id and we will use it as the Primary Key for our category
                                    String id = mDbReference.push().getKey();
                                    String descripcion = edtDescription.getText().toString().trim();

                                    //creating an store Object
                                    mTienda = new Tienda(id,descripcion, uri.toString());

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    private void onShowNetWorkError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        Query myStoreQuery = mDbReference.orderByChild("descripcion");
        myStoreQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    Tienda storeTmp = postSnapshot.getValue(Tienda.class);

                    //adding section to the list
                    storeList.add(storeTmp);
                }

                //adapter
                mAdapter = new StoreAdapter(getActivity(), storeList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tienda clickedStore) {
                        showAlertDialog(clickedStore);
                    }

                    @Override
                    public void onTiendaClick(Tienda clickedStore) {
                        onStore(clickedStore);
                    }
                });

                mReciclador.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //attaching value event listener
/*        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    Tienda storeTmp = postSnapshot.getValue(Tienda.class);

                    //adding section to the list
                    storeList.add(storeTmp);
                }

                //adapter
                mAdapter = new StoreAdapter(getActivity(), storeList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tienda clickedStore) {
                        showAlertDialog(clickedStore);
                    }

                    @Override
                    public void onTiendaClick(Tienda clickedStore) {
                        onStore(clickedStore);
                    }
                });

                mReciclador.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    private void onStore(Tienda tienda) {
        Intent intentCategory = new Intent(getActivity(), ActivityCategory.class);
        intentCategory.putExtra(STORE_ID, tienda.getId());
        startActivity(intentCategory);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            saveUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), saveUri);
                //imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    private void showAlertDialog(final Tienda tienda) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Modificar");
        alertDialog.setMessage("Ingrese Descripción");

        final EditText edtDescription = new EditText(getActivity());
        edtDescription.setText(tienda.getDescripcion().toString());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtDescription.setLayoutParams(lp);
        alertDialog.setView(edtDescription); // Add edit text to alert dialog
        alertDialog.setIcon(R.drawable.ic_edit);

        alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String description = edtDescription.getText().toString();

                if (!TextUtils.isEmpty(description)){
                    // Checks if the device has any active internet connection.
                    if (Uweb.isNetworkConnected(getActivity())){
                        //getting the specified store reference
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //getting the reference of node
                        String id           = tienda.getId();
                        String imagen       = tienda.getImagen().toString();
                        mDbReference = database.getReference(FirebaseReferences.STORE_REFERENCE).child(id);

                        //updating
                        Tienda tiendaTmp = new Tienda(id, description, imagen);
                        mDbReference.setValue(tiendaTmp);
                        Toast.makeText(getActivity(), "Registro Actualizado", Toast.LENGTH_SHORT).show();
                        //finish();
                        dialog.dismiss();

                    }
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

}

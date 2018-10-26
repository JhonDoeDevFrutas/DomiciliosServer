package jhondoe.com.domiciliosserver.ui.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.data.model.entities.Tienda;
import jhondoe.com.domiciliosserver.provider.FirebaseReferences;
import jhondoe.com.domiciliosserver.ui.adapter.ProductAdapter;

public class ActivityProducts extends AppCompatActivity {
    private static final int RC_GALLERY = 21;
    private static final int RC_CAMERA = 22;

    private static final int RP_CAMERA = 121;
    private static final int RP_STORAGE = 122;

    private static final String IMAGE_DIRECTORY = "/MyPhotoApp";
    private static final String MY_PHOTO = "my_photo";

    private static final String PATH_PROFILE = "profile";
    private static final String PATH_PHOTO_URL = "photoUrl";

    private String mCurrentPhotoPath;
    private Uri mPhotoSelectedUri;

    public static final String CATEGORIA_ID = "categoriaid";
    public static final String PRODUCT_ID = "productoid";

    // Referencias UI
    private RecyclerView mReciclador;
    private ProductAdapter mAdapter;

    EditText edtName, edtDescription, edtCharacteristics, edtProperties, edtPrice;
    ImageView imgPhoto;
    Button btnGallery, btnCamera, btnUpload;

    private String mId;

    //a list to store all the product from firebase database
    List<Producto> productList;

    //our database reference object
    DatabaseReference mDbReference;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    Producto mProducto;

    Uri saveUri;

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

        imgPhoto            = (ImageView) add_product.findViewById(R.id.img_photo);

        btnGallery           = (Button ) add_product.findViewById(R.id.btn_gallery);
        btnCamera           = (Button ) add_product.findViewById(R.id.btn_camera);
        btnUpload           = (Button ) add_product.findViewById(R.id.btn_upload);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chooseImage(); // let user select image from gallery and save url of this image

                //fromGallery();
                checkPermissionToApp(Manifest.permission.READ_EXTERNAL_STORAGE, RP_STORAGE);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fromCamera();
                //dispatchTakePictureIntent();

                checkPermissionToApp(Manifest.permission.CAMERA, RP_CAMERA);
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
        alertDialog.setPositiveButton("Guardar.", new DialogInterface.OnClickListener() {
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

    private void checkPermissionToApp(String permissionStr, int requestPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permissionStr) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{permissionStr}, requestPermission);
                return;
            }
        }

        switch (requestPermission){
            case RP_STORAGE:
                fromGallery();
                break;
            case RP_CAMERA:
                dispatchTakePictureIntent();
                break;
        }
    }


    private void uploadImage() {

        if (mPhotoSelectedUri != null){
            final ProgressDialog mDialog = new ProgressDialog(ActivityProducts.this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            final StorageReference imageFolder = mStorageReference.child(MY_PHOTO+ UUID.randomUUID().toString());
            imageFolder.putFile(mPhotoSelectedUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(ActivityProducts.this, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(ActivityProducts.this, "Error al subir la imagen intente mas tarde "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RC_GALLERY) ;

        btnGallery.setText("Imagen Seleccionada");
    }

    private void fromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RC_GALLERY);
        btnGallery.setText("Imagen Seleccionada");
    }

    private void fromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RC_CAMERA);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile;
            photoFile = createImageFile();

            if (photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this,
                        "jhondoe.com.domiciliosserver", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, RC_CAMERA);
            }
        }
    }

    private File createImageFile() {
        final String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HHmmss", Locale.ROOT)
                .format(new Date());
        final String imageFileName = MY_PHOTO + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
            mCurrentPhotoPath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    private void onProducto(Producto producto) {
        Intent intentProduct = new Intent(getBaseContext(), ActivityProduct.class);
        intentProduct.putExtra(CATEGORIA_ID, mId);
        intentProduct.putExtra(PRODUCT_ID, producto.getId());
        startActivity(intentProduct);
    }

    private Uri addPicGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        mCurrentPhotoPath = null;
        return contentUri;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case RC_GALLERY:
                    if (data != null && data.getData() != null){
                        mPhotoSelectedUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                    mPhotoSelectedUri);
                            imgPhoto.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case RC_CAMERA:
                    /*Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap)extras.get("data");*/
                    mPhotoSelectedUri = addPicGallery();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mPhotoSelectedUri);
                        imgPhoto.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            switch (requestCode){
                case RP_STORAGE:
                    fromGallery();
                    break;
                case RP_CAMERA:
                    dispatchTakePictureIntent();
                    break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}

package jhondoe.com.domiciliosserver.ui.view.productStoreModule.model.dataAccess;

/**
 *   ¿En que forma nos ayuda SRP(Single Responsibility Principle)?
 *       Auxiliar en definir que tipo de acciones en concreto va a tratar una clase.
 *       Cada clase o modulo debe de resolver un solo tipo de problema.
 *   ¿De las siguientes ventajas, ¿Cual es gracias a el Patrón Singleton?
 *       Obtener una única instancia de una clase para todo el proyecto.
 *       Garantizar que una clase sea instanciada solo una vez y que solo se tenga un acceso a ella.
 *
 *  Selecciona cual de los siguientes casos, serían un correcto nombramiento de acuerdo a lo
 *   explicado en la clase: Listeners vs Callbacks.
 *       Listener: Interface añadida a un método que permita estar escuchando todo el tiempo por un
 *       evento que podría o no, ocurrir.
 *       Callback: Interface instanciada dentro de un método que si o si, devolverá una respuesta.
 *   MVP
 *   Model: Es la parte responsable de manejar los datos. Su objetivo es conectarse a la fuentes
 *       de datos.
 *   Presenter: Es la parte intermedia entre la vista y el modelo, Su funcion es de controlar la
 *       logica de la aplicacion que necesita el interfaz de usuario.
 *   View: Aqui la logica se reduce considerablemente, ya que solo tiene la responsabilida de
 *       presentar los datos y de reaccionar antes los eventos de pulsacion que ejecute el usuario.
 */

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import jhondoe.com.domiciliosserver.data.model.entities.Producto;

public class RealtimeDatabase {
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
    private ValueEventListener mProductsValueEventListener;

    public RealtimeDatabase(){
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void subscribeToProducts(String idCategory, final ProductStoreEventListener listener){
        mDatabaseAPI.getProductsReference().child(idCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Producto> productos = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    productos.add(getProduct(postSnapshot));
                }
                listener.onDataChange(productos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                switch (databaseError.getCode()){
                    case DatabaseError.PERMISSION_DENIED:
                        listener.onError(R.string.error_permission_denied);
                        break;
                    default:
                        listener.onError(R.string.error_server);
                }

            }
        });

/*        if (mProductsChildEventListener == null){
            mProductsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listener.onChildAdded(getProduct(dataSnapshot));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    listener.onChildUpdated(getProduct(dataSnapshot));
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }

        mDatabaseAPI.getProductsReference().addChildEventListener(mProductsChildEventListener);
        */
    }

    private Producto getProduct(DataSnapshot dataSnapshot){
        Producto producto = dataSnapshot.getValue(Producto.class);
        if (producto != null){
            producto.setId(dataSnapshot.getKey());
        }
        return producto;
    }

    public void unsubscribeToProducts(){
        if (mProductsValueEventListener != null){
            mDatabaseAPI.getProductsReference().removeEventListener(mProductsValueEventListener);
        }
    }

}

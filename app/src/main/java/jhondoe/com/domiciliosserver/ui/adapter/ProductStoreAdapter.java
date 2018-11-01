package jhondoe.com.domiciliosserver.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Producto;

public class ProductStoreAdapter extends RecyclerView.Adapter<ProductStoreAdapter.ViewHolder>{
    private Context mContext;
    private List<Producto> mItems;
    // Instancia de escucha
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public ProductStoreAdapter(Context context, List<Producto> items) {
        this.mContext = context;
        this.mItems = items;
    }

    public interface OnItemClickListener{
        void onItemClick(Producto clickedProduct);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongSelected){
        mOnItemLongClickListener = onItemLongSelected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public ImageView imgPhoto;
        public TextView txtData;

        ViewHolder(View view) {
            super(view);

            imgPhoto = (ImageView)itemView.findViewById(R.id.img_photo);
            txtData = (TextView)itemView.findViewById(R.id.txt_data);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION){
                mOnItemClickListener.onItemClick(mItems.get(position));
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION){
                mOnItemLongClickListener.onItemLongClick(position);
                return true;
            }

            return false;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_product_store, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = mItems.get(position);
        String imagen = producto.getImagen().trim();

        Picasso.with(mContext).load(imagen).into(holder.imgPhoto);
        holder.txtData.setText(mContext.getString(R.string.item_product_data, producto.getNombre(), producto.getCantidad()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}

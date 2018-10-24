package jhondoe.com.domiciliosserver.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Tienda;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder>{
    private Context mContext;
    private List<Tienda> mItems;

    // Instancia de escucha
    private OnItemClickListener mOnItemClickListener;

    public StoreAdapter(Context context, List<Tienda> items) {
        this.mContext = context;
        this.mItems = items;
    }

    public interface OnItemClickListener{
        void onItemClick(Tienda clickedStore);
        void onTiendaClick(Tienda clickedStore);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.item_store, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tienda store = mItems.get(position);

        String descripcion  = store.getDescripcion().toString().trim();
        String image        = store.getImagen().toString().trim();

        holder.txtDescripcion.setText(descripcion);
        if (image != null){
            Picasso.with(mContext).load(image).into(holder.imgView);
        }holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_crop_original));

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imgView;
        public TextView txtDescripcion;
        public ImageButton imgBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            imgView         = (ImageView)itemView.findViewById(R.id.store_image);
            txtDescripcion  = (TextView) itemView.findViewById(R.id.text_descripcion);
            imgBtn          = (ImageButton)itemView.findViewById(R.id.image_button);

            itemView.setOnClickListener(this);
            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mOnItemClickListener.onTiendaClick(mItems.get(position));
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION){
                mOnItemClickListener.onItemClick(mItems.get(position));
            }
        }

    }
}

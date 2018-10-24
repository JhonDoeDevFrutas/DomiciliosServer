package jhondoe.com.domiciliosserver.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.ProductoProveedor;
import jhondoe.com.domiciliosserver.data.model.entities.Proveedor;

public class PrecioProveedorAdapter extends RecyclerView.Adapter<PrecioProveedorAdapter.ViewHolder>{
    private Context mContext;
    private List<ProductoProveedor> mItems;

    // Instancia de escucha
    private OnItemClickListener mOnItemClickListener;

    public PrecioProveedorAdapter(Context context, List<ProductoProveedor> items) {
        mItems = items;
        mContext = context;
    }

    public interface OnItemClickListener{
        void onItemClick(ProductoProveedor clickedProvider);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtName;
        public TextView txtPreci;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView)itemView.findViewById(R.id.txt_name_provider);
            txtPreci = (TextView)itemView.findViewById(R.id.txt_preci);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION){
                mOnItemClickListener.onItemClick(mItems.get(position));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_provider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductoProveedor proveedor = mItems.get(position);

        holder.txtName.setText(proveedor.getProveedor().getNombre().trim());
        holder.txtPreci.setText(proveedor.getPrecio().trim());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


}

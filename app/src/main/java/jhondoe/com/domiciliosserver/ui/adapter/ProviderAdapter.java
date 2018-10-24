package jhondoe.com.domiciliosserver.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Proveedor;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ViewHolder>{
    private Context mContext;
    private List<Proveedor> mItems;

    // Instancia de escucha
    private OnItemClickListener mOnItemClickListener;

    public ProviderAdapter(Context context, List<Proveedor> items) {
        this.mContext = context;
        this.mItems = items;
    }

    public interface OnItemClickListener{
        void onItemClick(Proveedor clickedStore);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.item_providers, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Proveedor provider = mItems.get(position);

        String name  = provider.getNombre().trim();
        holder.txtName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName  = (TextView) itemView.findViewById(R.id.txtName);

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
}

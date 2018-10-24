package jhondoe.com.domiciliosserver.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private Context mContext;
    private List<Solicitud> mItems;

    // Instancia de escucha
    private OnItemSelectedListener mOnItemSelectedListener;


    public OrderAdapter(Context context, List<Solicitud> items){
        this.mContext = context;
        this.mItems = items;
    }

    public interface OnItemClickListener{
        void onItemClick(Solicitud clickedOrder);
    }

    public interface  OnItemSelectedListener{
        void onMenuAction(Solicitud selectOrder, MenuItem item);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener){
        mOnItemSelectedListener = onItemSelectedListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.order_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Solicitud order = mItems.get(position);

        holder.txtOrderId.setText(order.getId());
        holder.txtOrderStatus.setText(convertCodeStatus(order.getStatus() != null ? order.getStatus() : "0") );
        holder.txtOrderPhone.setText(order.getPhone());
        holder.txtOrderAddress.setText(order.getAddress());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener{

        public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
            txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
            txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);
            txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.custom_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION){
                mOnItemSelectedListener.onMenuAction(mItems.get(position), item);
            }

            return false;
        }
    }

    private String convertCodeStatus(String status){
        if (status.equals("0"))
            return "Pedido Realizado";
        else if (status.equals("1"))
            return "En camino";
        else
            return "Enviado";

    }
}

package materialtest.vivz.slidenerd.materialtest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import materialtest.vivz.slidenerd.materialtest.R;
import materialtest.vivz.slidenerd.materialtest.pojo.Information;

/**
 * Creado por soft12 el 10/08/2015.
 */
public class AdapterDrawer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    List<Information> data = Collections.emptyList();
    private LayoutInflater inflater;
    //private Context context;

    public AdapterDrawer(Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        //this.context = context;
    }

    /*public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.drawer_header, parent, false);
            //Log.d("VIVZ", "OnCreatedHolder called");
            return new HeaderHolder(view);
        } else {
            View view = inflater.inflate(R.layout.custom_row, parent, false);
            //Log.d("VIVZ", "OnCreatedHolder called");
            return new ItemHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            Log.d("VIVZ", "no Instance" + position);
        } else {
            ItemHolder itemHolder = (ItemHolder) holder;
            Information current = data.get(position - 1);//como tenemos un item de más ahora tenemos que poner -1
            //Log.d("VIVZ", "OnBindViewHolder called " + position);
            itemHolder.title.setText(current.getTitle());
            itemHolder.icon.setImageResource(current.getIconId());
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;// +1 porque ahora está el header de drawer_header
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView icon;

        public ItemHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            //title.setTextColor(Color.GRAY);
            //icon.setOnClickListener(this);
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {


        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }
}

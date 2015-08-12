package materialtest.vivz.slidenerd.materialtest.materialtest;

import android.content.Context;
import android.graphics.Color;
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

/**
 * Creado por soft12 el 10/08/2015.
 */
public class VivzAdapter extends RecyclerView.Adapter<VivzAdapter.MyViewHolder> {

    List<Information> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;

    public VivzAdapter(Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        Log.d("VIVZ", "OnCreatedHolder called");
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        Log.d("VIVZ", "OnBindViewHolder called " + position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            title.setTextColor(Color.GRAY);
            //icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context, "item selected " + getAdapterPosition(),
            // Toast.LENGTH_SHORT).show();
            //delete(getAdapterPosition());
            //context.startActivity(new Intent(context, SubActivity.class));
            if (clickListener != null) {
                clickListener.itemClicked(v, getAdapterPosition());
            }
        }
    }
}

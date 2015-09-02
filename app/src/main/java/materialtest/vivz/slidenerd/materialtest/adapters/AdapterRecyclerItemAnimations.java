package materialtest.vivz.slidenerd.materialtest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import materialtest.vivz.slidenerd.materialtest.R;

/**
 * Creado por soft12 el 27/08/2015.
 */
public class AdapterRecyclerItemAnimations extends RecyclerView.Adapter<AdapterRecyclerItemAnimations.Holder> {

    private ArrayList<String> mListaData = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public AdapterRecyclerItemAnimations(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View row = mLayoutInflater.inflate(R.layout.custom_row_item_animations, parent, false);

        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        String data = mListaData.get(position);
        holder.textDataItem.setText(data);
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });
    }

    public void addItem(String item) {
        mListaData.add(item);
        notifyItemInserted(mListaData.size());
    }

    /*public void removeItem(String item) {
        int position = mListaData.indexOf(item);
        if (position != -1) {
            mListaData.remove(item);
            notifyItemRemoved(position);
        }
    }*/

    public void removeItem(int position) {
        mListaData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mListaData.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView textDataItem;
        ImageButton buttonDelete;

        public Holder(View itemView) {
            super(itemView);
            textDataItem = (TextView) itemView.findViewById(R.id.text_item);
            buttonDelete = (ImageButton) itemView.findViewById(R.id.button_delete);

        }
    }
}

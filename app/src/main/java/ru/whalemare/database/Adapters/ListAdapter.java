package ru.whalemare.database.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.whalemare.database.ItemClickListener;
import ru.whalemare.database.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<String> names = new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView;

    public ListAdapter(Context context, ArrayList<String> names, RecyclerView recyclerView){
        this.names = names;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView_name);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener){
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // создаем новое view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        holder.name.setText(names.get(position)); // установка текста

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick)
                {
                    Toast.makeText(context, "#" + position + " - " + names.get(position), Toast.LENGTH_SHORT).show();
                    recyclerView.scrollBy(50, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }
}

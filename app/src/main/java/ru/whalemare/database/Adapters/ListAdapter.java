package ru.whalemare.database.Adapters;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.whalemare.database.ItemClickListener;
import ru.whalemare.database.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<String> names = new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView;

    public ListAdapter(Context context, ArrayList<String> names, RecyclerView recyclerView) {
        this.names = names;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    ArrayList<Integer> ids = new ArrayList<>(names.size());

    private void getId() {
        for (int i = 0; i < names.size(); i++)
            ids.add(i);
    }

    public void shuffle() {
        List<String> shuffled = new ArrayList<>(names);
        Collections.shuffle(shuffled);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new SimpleCallback(names, shuffled));
        diffResult.dispatchUpdatesTo(this);

        setItems(shuffled);
    }

    public void setItems(List<String> list) {
        this.names = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name; // key
        public TextView id; // p
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            getId(); // создадим список ID
            name = (TextView) itemView.findViewById(R.id.textView_name);
            id = (TextView) itemView.findViewById(R.id.textView_id);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        holder.name.setText(names.get(position)); // установка текста
        holder.id.setText(ids.get(position).toString()); // установка номера

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Toast.makeText(context, "#" + position + " - " + "Данные: " + names.get(position), Toast.LENGTH_SHORT).show();
                    recyclerView.scrollBy(50, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class SimpleCallback extends DiffUtil.Callback {

        private final List<String> old;
        private final List<String> fresh;

        public SimpleCallback(List<String> old, List<String> fresh) {
            this.old = old;
            this.fresh = fresh;
        }

        @Override
        public int getOldListSize() {
            return old.size();
        }

        @Override
        public int getNewListSize() {
            return fresh.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return old.get(i).hashCode() == fresh.get(i1).hashCode();
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return !old.get(i).equals(fresh.get(i1));
        }
    }
}

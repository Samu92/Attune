package es.app.attune.attune.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import es.app.attune.attune.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private ArrayList<String> filterList;

    public FilterAdapter(ArrayList<String> filterList, Context context) {
        this.filterList = filterList;
    }

    @NonNull
    @Override
    public FilterAdapter.FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_list_item,parent,false);
        return new FilterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.FilterViewHolder holder, int position) {
        holder.text.setText(filterList.get(position));
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public static class FilterViewHolder extends RecyclerView.ViewHolder{

        protected TextView text;

        FilterViewHolder(View itemView) {
            super(itemView);
            text= itemView.findViewById(R.id.text_id);
        }
    }
}

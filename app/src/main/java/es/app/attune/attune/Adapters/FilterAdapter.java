package es.app.attune.attune.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import es.app.attune.attune.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    ArrayList<String> filterList;

    public FilterAdapter(ArrayList<String> filterList, Context context) {
        this.filterList = filterList;
    }

    @Override
    public FilterAdapter.FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_list_item,parent,false);
        FilterViewHolder viewHolder=new FilterViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterAdapter.FilterViewHolder holder, int position) {
        holder.text.setText(filterList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public static class FilterViewHolder extends RecyclerView.ViewHolder{

        protected TextView text;

        public FilterViewHolder(View itemView) {
            super(itemView);
            text= itemView.findViewById(R.id.text_id);
        }
    }
}

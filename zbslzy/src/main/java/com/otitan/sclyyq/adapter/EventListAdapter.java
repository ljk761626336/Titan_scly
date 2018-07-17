package com.otitan.sclyyq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otitan.sclyyq.R;
import com.otitan.sclyyq.entity.EventList;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyHolder> {

    private List<EventList> items;
    private Context context;

    public EventListAdapter(Context context, List<EventList> items) {
        this.context = context;
        this.items = items;
    }

    public void setItems(List<EventList> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_eventlist, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.textView.setText(items.get(position).getXJ_SJMC());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_sjlb);
        }
    }
}

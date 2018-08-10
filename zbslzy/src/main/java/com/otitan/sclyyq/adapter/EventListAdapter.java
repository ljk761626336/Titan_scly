package com.otitan.sclyyq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otitan.sclyyq.R;
import com.otitan.sclyyq.entity.EventList;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyHolder> {

    private List<EventList> items;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

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
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.textView.setText(items.get(position).getXJ_SJMC());
        holder.sjTv.setText(items.get(position).getXJ_SCRQ().replace("T", " "));
        boolean flag = TextUtils.isEmpty(items.get(position).getXJ_SCRQ());
        holder.uploadImg.setVisibility(flag ? View.VISIBLE : View.GONE);
        if (mOnItemClickListener != null) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.uploadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.upLoad(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);

        void upLoad(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView textView;
        TextView sjTv;
        ImageView uploadImg;

        MyHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_linear);
            textView = itemView.findViewById(R.id.item_sjlb);
            sjTv = itemView.findViewById(R.id.item_sjsj);
            uploadImg = itemView.findViewById(R.id.item_upload);
        }
    }
}

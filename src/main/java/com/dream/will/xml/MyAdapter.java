package com.dream.will.xml;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Author：Will on 2016/12/2 10:33
 * Mail：heheheqin.will@gmail.com
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewMHolder> {

    List<JokeBean> data;
    LayoutInflater inflater;
    Context context;

    public MyAdapter(List<JokeBean> data, Context context) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewMHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewMHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewMHolder holder, final int position) {
        JokeBean jokeBean = data.get(position);
        holder.content.setText(jokeBean.content);
        holder.userName.setText(jokeBean.userName);
        Glide.with(context)
                .load(jokeBean.userImage)
                .error(R.drawable.anony)
                .into(holder.userImage);
//        Log.i("TAG", "JokeBean: image---------" + jokeBean.type);
        if (jokeBean.type==1){
//            Log.i("TAG", "JokeBean: image---------" + jokeBean.image);
            holder.contentImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(jokeBean.image).into(holder.contentImage);
        }else {
            holder.contentImage.setVisibility(View.GONE);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: holder-onclick--------" );
                clickListener.OnClickListener(v,position);
            }
        });
    }

    public void addData(int position) {
//        data.add(position, new JokeBean());
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public interface ContentSetOnClickItem {
        void OnClickListener(View view,int position);
    }
    ContentSetOnClickItem clickListener;
    public void setOnItemClick(ContentSetOnClickItem clickListener) {
        this.clickListener = clickListener;
    }

    class ViewMHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView content, userName;
       public   ImageView userImage, contentImage;
        public  View root;
        public ViewMHolder(View itemView) {
            super(itemView);
            root = itemView;
            content = (TextView) itemView.findViewById(R.id.content);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userImage = (ImageView) itemView.findViewById(R.id.userImage);
            contentImage = (ImageView) itemView.findViewById(R.id.contentImage);
        }

        @Override
        public void onClick(View v) {

        }
    }
}

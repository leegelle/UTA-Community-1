package com.example.utacommunityapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utacommunityapp.Activities.PostInfo;
import com.example.utacommunityapp.Models.Forum;
import com.example.utacommunityapp.R;

import java.util.List;

public class PostConnector extends RecyclerView.Adapter<PostConnector.TheView> {

    Context mContext;
    List<Forum> mData;

    public PostConnector(Context mContext, List<Forum> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public TheView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View r = LayoutInflater.from(mContext).inflate(R.layout.post_setup,parent,false);




        return new TheView(r);
    }

    @Override
    public void onBindViewHolder(@NonNull TheView holder, int position) {

        holder.post.setText(mData.get(position).getPost());
        holder.title.setText(mData.get(position).getTitle());
        holder.comm.setText(mData.get(position).getCommunity());

        holder.post.getText();
        holder.title.getText();
        holder.comm.getText();


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class TheView extends RecyclerView.ViewHolder{


        TextView post;
        TextView title;
        TextView comm;


        public TheView(@NonNull View itemView) {
            super(itemView);

            post = itemView.findViewById(R.id.post_View);
            title = itemView.findViewById(R.id.title_View);
            comm = itemView.findViewById(R.id.comm_View);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postInfo = new Intent(mContext, PostInfo.class);
                    int spot = getAdapterPosition();

                    postInfo.putExtra("title", mData.get(spot).getTitle());

                    postInfo.putExtra("post",mData.get(spot).getPost());
                    postInfo.putExtra("postKey",mData.get(spot).getPostID());

                    long timestamp = (long) mData.get(spot).getTime();
                    postInfo.putExtra("postDate", timestamp);

                    postInfo.putExtra("community",mData.get(spot).getCommunity());
                    mContext.startActivity(postInfo);





                }
            });
        }
    }
}

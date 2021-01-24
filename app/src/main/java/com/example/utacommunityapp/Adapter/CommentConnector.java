package com.example.utacommunityapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utacommunityapp.Models.Comment_Add;
import com.example.utacommunityapp.R;
import java.util.List;


public class CommentConnector extends RecyclerView.Adapter<CommentConnector.CommentViewHolder> {


    private Context comment_context;
    private List<Comment_Add> data;

    public CommentConnector(Context comment_context, List<Comment_Add> data) {
        this.comment_context = comment_context;
        this.data = data;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View r = LayoutInflater.from(comment_context).inflate(R.layout.comment_layout,parent,false);

        return new CommentViewHolder(r);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int spot) {

        holder.userN.setText(data.get(spot).getUsername());
        holder.commentN.setText(data.get(spot).getContent());



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView userN, commentN, dateN;
        public CommentViewHolder(View itemView){
            super(itemView);
            userN = itemView.findViewById(R.id.user_Name);
            commentN = itemView.findViewById(R.id.text_comment);



        }

    }

}

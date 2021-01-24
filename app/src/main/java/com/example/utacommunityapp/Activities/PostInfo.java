package com.example.utacommunityapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utacommunityapp.Adapter.CommentConnector;
import com.example.utacommunityapp.Models.Comment_Add;
import com.example.utacommunityapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostInfo extends AppCompatActivity {
    EditText comment;
    TextView post, postDate, postTitle;
    Button button;
    ImageView userPhoto;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseDatabase db;
    String postID;
    RecyclerView commentN;
    CommentConnector commentConnector;
    List<Comment_Add> Lcom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();

        commentN = findViewById(R.id.rvC);
        post = findViewById(R.id.post_info);
        postTitle = findViewById(R.id.post_info_title);
        postDate = findViewById(R.id.post_date_user);

        //userPhoto = findViewById(R.id.post_user_image);
        comment = findViewById(R.id.post_comment);

        button = findViewById(R.id.add_post_btn);



        //Comment button listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference c = db.getReference("Comment").child(postID).push();
                String comment_info = comment.getText().toString();
                String userID = user.getUid();
                String userN = user.getDisplayName();
                Comment_Add comment_add = new Comment_Add(comment_info, userID, userN);

                c.setValue(comment_add).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showError("comment added");
                        comment.setText("");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError("Failure to add comment :  "+e.getMessage());
                    }
                });


                rvComment();




            }
        });


        //Binding all data

        String postText = getIntent().getExtras().getString("post");
        post.setText(postText);
        String title = getIntent().getExtras().getString("title");
        postTitle.setText(title);
        String postD = getIntent().getExtras().getString("community");
        postDate.setText(postD);

        postID = getIntent().getExtras().getString("postKey");





    }

    private void rvComment() {

        commentN.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference cm = db.getReference("Comment").child(postID);
        cm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Lcom = new ArrayList<>();
                for(DataSnapshot info: snapshot.getChildren()){
                    Comment_Add comment_add = info.getValue(Comment_Add.class);
                    Lcom.add(comment_add);
                }
                commentConnector = new CommentConnector(getApplicationContext(), Lcom);
                commentN.setAdapter(commentConnector);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showError(String m) {
        Toast.makeText(this,m, Toast.LENGTH_LONG).show();

    }

}
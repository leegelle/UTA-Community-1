package com.example.utacommunityapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.utacommunityapp.Models.Forum;
import com.example.utacommunityapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListForum extends AppCompatActivity {
    SearchView searchView;
    private FirebaseAuth fAuth;
    ListView listView;
    public static String text;
    DatabaseReference User;
    DatabaseReference Foorum;
    ArrayList<String> arraylist;
    com.example.utacommunityapp.Models.Forum forum;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_forum);
        searchView = findViewById(R.id.seach_fbar);
        listView = findViewById(R.id.listfview);
        arraylist = new ArrayList();
        Foorum = FirebaseDatabase.getInstance().getReference("Posts");

        Foorum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                     Forum forum = new Forum();
                    forum.setTitle(ds.getValue(Forum.class).getTitle());
                    arraylist.add(forum.getTitle());
                }
                arrayAdapter = new ArrayAdapter<String>(ListForum.this, android.R.layout.simple_list_item_1,arraylist);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(ListForum.this, "You click " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        ListForum.this.arrayAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        ListForum.this.arrayAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
package com.example.utacommunityapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.utacommunityapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListUser extends AppCompatActivity {
    SearchView searchView;
    String Selected;
    private FirebaseAuth fAuth;
    ListView listView;
    public static String text;
    DatabaseReference User;
    DatabaseReference Forum;
    ArrayList<String> arraylist;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        searchView = findViewById(R.id.seach_bar);
        listView = findViewById(R.id.listview);
        arraylist = new ArrayList();

        User = FirebaseDatabase.getInstance().getReference("User");

        User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    member member = new member();
                    member.setEmail(ds.getValue(member.class).getEmail());
                    arraylist.add(member.getEmail());
                }
                arrayAdapter = new ArrayAdapter<String>(ListUser.this, android.R.layout.simple_list_item_1,arraylist);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Toast.makeText(ListUser.this, "You click " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                        Selected = parent.getItemAtPosition(position).toString();
                        Intent intent = new Intent(getApplicationContext(),Messaging.class);
                        intent.putExtra("Selected",Selected);
                        startActivity(intent);


                    }
                });
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        ListUser.this.arrayAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        ListUser.this.arrayAdapter.getFilter().filter(newText);
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
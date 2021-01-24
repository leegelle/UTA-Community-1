package com.example.utacommunityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.utacommunityapp.R;

public class SearchActivity extends AppCompatActivity {
    private CheckBox UserCheck, ForumCheck;
    SearchView searchView;
    ListView listView;
    Button buttn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        UserCheck = findViewById(R.id.box);
        ForumCheck = findViewById(R.id.box2);
        buttn = findViewById(R.id.buttn);
        buttn.setBackgroundColor(Color.BLACK);
        buttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserCheck.isChecked() && ForumCheck.isChecked())
                {
                    Toast.makeText(SearchActivity.this, "Only one selection is allowed", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(UserCheck.isChecked())
                {
                    Intent intent = new Intent(getApplicationContext(),ListUser.class);
                    startActivity(intent);

                }
                else if (ForumCheck.isChecked())
                {
                    Intent intent = new Intent(getApplicationContext(),ListForum.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SearchActivity.this, "Please select one of the search checkbox ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
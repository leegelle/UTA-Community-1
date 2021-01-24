package com.example.utacommunityapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.utacommunityapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Messaging extends AppCompatActivity {
    private DatabaseReference myDatabase;
    Button button;
    message message;
    FirebaseAuth mAuth;
    String Selected;
    FirebaseUser user;
    ArrayList<String> arraylist;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        final TextView mytext = findViewById(R.id.textView5);

        button = findViewById(R.id.sendbutton);
        myDatabase = FirebaseDatabase.getInstance().getReference("Message");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myDatabase.addValueEventListener(new ValueEventListener()
        {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mytext.setText("");

                    mytext.setText("\n\n"+dataSnapshot.getValue().toString()+"\n\n");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError dataSnapshot) {

                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage(v);
                }
            });
        }
        public void sendMessage(View view)
        {
            EditText myEditText = findViewById((R.id.textbox));
            final String Text = myEditText.getText().toString();
            if(Text.isEmpty())
            {
                return;
            }
            Selected = getIntent().getStringExtra("Selected");
            message = new message();
            message.setReceiver(Selected);
            message.setUser(user.getEmail());
            message.setMessage(Text);
            myDatabase.child(Long.toString(System.currentTimeMillis())).setValue(message);
            myEditText.setText("");
            return;
        }


}

package com.example.utacommunityapp.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.utacommunityapp.Activities.ui.Chat.ChatFragment;
import com.example.utacommunityapp.Activities.ui.Community.SearchFragment;

import com.example.utacommunityapp.Fragments.Home_Fragment;
import com.example.utacommunityapp.Fragments.LogOut;
import com.example.utacommunityapp.Models.Forum;
import com.example.utacommunityapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Home2Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    FirebaseAuth mAuth;
    FirebaseUser user;
    Dialog add;

    DatabaseReference data;
    TextView title, community, post;
    ImageView Btn;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        user.getEmail();


        //initiate the popup
        popup();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle t = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(t);
        t.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_forum, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        updateNav();

        //setting home fragement as main page
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,new Home_Fragment()).commit();

    }

    private void popup() {
        add = new Dialog(this);
        add.setContentView(R.layout.popup_add_post);
        add.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        add.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        add.getWindow().getAttributes().gravity = Gravity.TOP;
        //initializing title, community and post
        title = add.findViewById(R.id.edit_Title);
        community = add.findViewById(R.id.edit_Comm);
        post = add.findViewById(R.id.edit_compose);
        Btn = add.findViewById(R.id.add_post);
        progress = add.findViewById(R.id.progressBar);

        //Add post listener


        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn.setVisibility(View.INVISIBLE);


                //testing title, community and post
                 if(title.getText().toString().isEmpty() || community.getText().toString().isEmpty() || post.getText().toString().isEmpty())
                {
                    showError("Make sure you fill out all fields");

                    Btn.setVisibility(View.VISIBLE);
                    return;
                }
                else
                {
                    //TODO: Everything is ok to be posted and need to add it to database
                    //Lets upload post
                    //Access to database

                    Forum forum = new Forum();

                    forum.setTitle(title.getText().toString());
                    forum.setCommunity(community.getText().toString());
                    forum.setPost(post.getText().toString());
                    forum.setUserID(user.getUid());
                    // Written by David Trimino
                    // CHECK TO SEE IF foul language is in forum
                    if ( !isFoul(forum) ) {
                        //Adding post to database
                        addingP(forum);

                    }else{
                        showError("Post contains foul language");
                        progress.setVisibility(View.INVISIBLE);
                        Btn.setVisibility(View.VISIBLE);
                        return;
                    }




                    //Adding post to database




                }

            }
        });


    }
    // Written by David Trimino
    // reads in a text file that contains vulgar language and returns true if text contains foul lang
    private boolean isFoul(Forum forum){
        ArrayList<String> lines = new ArrayList<>();

        // read in the file
        try {
            InputStream is = getAssets().open("foul.txt");
            if ( is != null ){
                InputStreamReader read = new InputStreamReader( is );
                BufferedReader b_read = new BufferedReader( read );
                String line = "";

                try {
                    while( (line = b_read.readLine() ) != null ){
                        // Split words anything other than alphanumeric or underscores
                        String array[] = line.split("\\W+");
                        for ( String x : array )
                            lines.add( x );
                    }
                }catch ( Exception e ){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            showError("foul.txt was not found");
        }

        // get words from title, community, and post
        String title [] = forum.getTitle().toString().toLowerCase().split("\\W+");
        String comms [] = forum.getCommunity().toString().toLowerCase().split("\\W+");
        String posts [] = forum.getPost().toString().toLowerCase().split("\\W+");

        // Compare user text to foul.txt
        for( String x : lines ){
            // check for title
            for ( String z : title)
                if ( z.equals( x.toLowerCase() ) )
                    return true;

            // check community
            for ( String z : comms )
                if ( z.equals( x.toLowerCase() ) )
                    return true;

            // check post
            for ( String z : posts )
                if ( z.equals( x.toLowerCase() ) )
                    return true;
        }

        // none of the strings have foul language
        return false;
    }
    private void addingP(Forum forum) {

        FirebaseDatabase d = FirebaseDatabase.getInstance();


        DatabaseReference ref = d.getReference("Posts").push();

        //get a unique post ID and set that as the postID
        String postID = ref.getKey();
        forum.setPostID(postID);



        ref.setValue(forum).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showError("Post has been added successfully");
                Btn.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);
                add.dismiss();
            }
        });

    }

    private void showError(String s) {

        Toast.makeText(Home2Activity.this,s,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home2, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //This is for the navigation page to set those buttons up with the intended page

        if(id == R.id.nav_logout)
        {

            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChatFragment()).commit();
        }
        else if(id == R.id.nav_home)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new Home_Fragment()).commit();

        }
        else if(id == R.id.nav_forum)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SearchFragment()).commit();

        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        //return super.onOptionsItemSelected(item);
    }

    public void updateNav(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        View head = navigationView.getHeaderView(0);
        TextView name = head.findViewById(R.id.nav_user_name);
        TextView email = head.findViewById(R.id.nav_user_email);
        ImageView photo = head.findViewById(R.id.nav_user_photo);


        name.setText(user.getDisplayName());
        email.setText(user.getEmail());

        //TODO: Using gilde to load in user image
        Glide.with(this).load(user.getPhotoUrl()).into(photo);

    }

}
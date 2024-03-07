package com.example.activeamigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;


import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.activeamigo.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DAO {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userEmail = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        //userRedirection(userEmail);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        // Set matches text to be clickable
        binding.appBarMain.toolbar.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_matches, R.id.nav_calender, R.id.nav_preferences, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void userRedirection(String email){
        Task<DocumentSnapshot> res = checkAccount(email, "Accounts", this.db);
        res.addOnCompleteListener(task->{
           DocumentSnapshot ds = task.getResult();
           if(Objects.requireNonNull(ds.getString("location")).isEmpty() || Objects.requireNonNull(ds.getString("exercise")).isEmpty()
                   || Objects.requireNonNull(ds.getString("gender")).isEmpty() || Objects.requireNonNull(ds.getString("dob")).isEmpty()){

               Toast.makeText(MainActivity.this, "Set all preferences first", Toast.LENGTH_LONG).show();
               startActivity(new Intent(MainActivity.this, PreferenceActivity.class));
               finish();
               return;
           }
          HashMap<Object, ArrayList<Long>> temp = (HashMap<Object,ArrayList<Long> >) ds.get("calendar");
          if(!nonEmptySchedule(temp)){ // calendar is empty
              Toast.makeText(MainActivity.this, "Input some time into your schedule first",
                      Toast.LENGTH_LONG).show();
              startActivity(new Intent(MainActivity.this, CalendarActivity.class));
              finish();
          }

        });
    }

    private boolean nonEmptySchedule(HashMap<Object, ArrayList<Long>> schedule) {
        boolean res = false;
        for (ArrayList<Long> val : schedule.values()) {
            if (val.contains(1L)) {
                res = true;
                break;
            }
        }
        return res;
    }


}
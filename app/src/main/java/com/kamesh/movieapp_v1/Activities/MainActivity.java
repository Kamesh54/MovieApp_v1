package com.kamesh.movieapp_v1.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.content.SharedPreferences;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kamesh.movieapp_v1.Adapters.FilmListAdapter;
import com.kamesh.movieapp_v1.Adapters.SlidersAdapter;
import com.kamesh.movieapp_v1.Domains.Film;
import com.kamesh.movieapp_v1.Domains.SliderItems;
import com.kamesh.movieapp_v1.R;
import com.kamesh.movieapp_v1.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseDatabase database;
    private TextView userName;
    private TextView emailText;

    private Handler sliderHandler = new Handler();

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database =  FirebaseDatabase.getInstance();
        userName = findViewById(R.id.userName);
        emailText = findViewById(R.id.textView2);
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        userName.setText("Hello, " + username + "!");
        emailText.setText(email);
        Window w=getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initBanner();
        initTopMovies();
        initUpcoming();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Clear login data when the app is paused
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false); // Clear login status
        editor.putString("username", null);    // Clear username
        editor.putString("email", null);       // Clear email
        editor.apply();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,2000);
    }

    private void initUpcoming(){
        DatabaseReference myRef=database.getReference("Upcomming");
        binding.progressBarUpcoming.setVisibility(View.VISIBLE);
        ArrayList<Film> items=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(Film.class));
                    }
                    if(!items.isEmpty()){
                        binding.recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerViewUpcoming.setAdapter(new FilmListAdapter(items));
                    }
                    binding.progressBarUpcoming.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initTopMovies(){
        DatabaseReference myRef=database.getReference("Items");
        binding.progressBarTop.setVisibility(View.VISIBLE);
        ArrayList<Film> items=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(Film.class));
                    }
                    if(!items.isEmpty()){
                        binding.recyclerViewTopMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerViewTopMovies.setAdapter(new FilmListAdapter(items));
                    }
                    binding.progressBarTop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initBanner(){
        DatabaseReference myRef=database.getReference("Banners");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banner(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void banner(ArrayList<SliderItems> items){
        binding.viewPager2.setAdapter(new SlidersAdapter(items,binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer= new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });

        binding.viewPager2.setPageTransformer(compositePageTransformer);
        binding.viewPager2.setCurrentItem(1);
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
            }
        });
    }

}
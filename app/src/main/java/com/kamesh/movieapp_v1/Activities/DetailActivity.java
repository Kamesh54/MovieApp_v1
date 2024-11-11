package com.kamesh.movieapp_v1.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kamesh.movieapp_v1.Adapters.CastListAdapter;
import com.kamesh.movieapp_v1.Adapters.CategoryEachFilmAdapter;
import com.kamesh.movieapp_v1.Domains.Film;
import com.kamesh.movieapp_v1.databinding.ActivityDetailBinding;

import eightbitlab.com.blurview.RenderScriptBlur;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVarialbe();
        Window w=getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    private void setVarialbe(){
        Film film = (Film) getIntent().getSerializableExtra("film");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions=requestOptions.transform(new CenterCrop(),new GranularRoundedCorners(0,0,50,50));

        Glide.with(this).load(film.getPoster()).apply(requestOptions).into(binding.filmPic);

        binding.titleTxt.setText(film.getTitle());
        binding.imdbTxt.setText("IMDB "+film.getImdb());
        binding.movieTimesTxt.setText(film.getYear()+" - "+film.getTime());
        binding.movieSummary.setText(film.getDescription());

        binding.watchTrailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = film.getTrailer().replace("https://www.youtube.com/watch?v=","");
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));

                Intent webIntent =  new Intent(Intent.ACTION_VIEW , Uri.parse(film.getTrailer()));

                try{
                    startActivity(appIntent);
                }catch (ActivityNotFoundException e){
                    startActivity(webIntent);
                }
            }
        });

        binding.backing.setOnClickListener(v -> finish());

        float radius=10f;
        View decorView = getWindow().getDecorView();

        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        binding.blurView.setupWith(rootView, new RenderScriptBlur(this))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radius);
        binding.blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        binding.blurView.setClipToOutline(true);

        if(film.getGenre()!=null){
            binding.genreView.setAdapter(new CategoryEachFilmAdapter(film.getGenre()));
            binding.genreView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
        //System.out.println(film.getCast());
        if(film.getCasts()!=null){
            //System.out.println(film.getCast());
            binding.castView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.castView.setAdapter(new CastListAdapter(film.getCasts()));

        }
    }
}
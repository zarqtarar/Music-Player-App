package com.app.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inspector.StaticInspectionCompanionProvider;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class player_activity extends AppCompatActivity {
    static MediaPlayer myMediaPlayer;
Button btnNext,btnPaus,btnPrevious;
TextView SongName;
String sName;

    int position;
ArrayList<File> mySongs;
Thread updateseekbar;
SeekBar sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_activity);
getSupportActionBar().setTitle("Now Playing");
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
getSupportActionBar().setDisplayShowHomeEnabled(true);


        btnNext=findViewById(R.id.next);
        btnPaus=findViewById(R.id.paus);
        btnPrevious=findViewById(R.id.previous);

        SongName=findViewById(R.id.tv);
        sb=findViewById(R.id.seekbar);
updateseekbar=new Thread(){
    @Override
    public void run(){

        super.run();
        int totalduration=myMediaPlayer.getDuration();
        int currentposition=0;
        while (currentposition<totalduration){
            try{
                sleep(500);
                currentposition=myMediaPlayer.getCurrentPosition();
                sb.setProgress(currentposition);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
};

if(myMediaPlayer!=null){
    myMediaPlayer.stop();
    myMediaPlayer.release();
}

Intent i= getIntent();
Bundle bundle=i.getExtras();
mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
sName=mySongs.get(position).getName().toString();
String songName =i.getStringExtra("songname");
SongName.setText(songName);
SongName.setSelected(true);


position= bundle.getInt("pos",0);
Uri u= Uri.parse(mySongs.get(position).toString());
myMediaPlayer= MediaPlayer.create(getApplicationContext(),u);

myMediaPlayer.start();
sb.setMax(myMediaPlayer.getDuration());

updateseekbar.start();
sb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.design_default_color_primary), PorterDuff.Mode.MULTIPLY);
sb.getThumb().setColorFilter(getResources().getColor(R.color.design_default_color_primary),PorterDuff.Mode.SRC_IN);



sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
myMediaPlayer.seekTo(seekBar.getProgress());
    }
});




btnPaus.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        sb.setMax(myMediaPlayer.getDuration());
        if(myMediaPlayer.isPlaying()){
            btnPaus.setBackgroundResource(R.drawable.iconplay);
            myMediaPlayer.pause();
        }
        else{
            btnPaus.setBackgroundResource(R.drawable.iconpaus);
            myMediaPlayer.start();
        }
    }
});

btnNext.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        myMediaPlayer.stop();
        myMediaPlayer.release();
        position= ((position+1)%mySongs.size());
        Uri u =Uri.parse(mySongs.get(position).toString());
        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        sName=mySongs.get(position).getName().toString();
        myMediaPlayer.start();
    }
});

btnPrevious.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        myMediaPlayer.stop();
        myMediaPlayer.release();
        position= ((position-1)>0)?(mySongs.size()-1):(position-1);
        Uri u =Uri.parse(mySongs.get(position).toString());
        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        sName=mySongs.get(position).getName().toString();
        myMediaPlayer.start();




    }
});


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}
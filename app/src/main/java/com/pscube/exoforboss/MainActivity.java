package com.pscube.exoforboss;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pscube.exoforboss.R;

import java.io.IOException;

import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static com.google.android.exoplayer2.Player.STATE_READY;


@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity  {


    private String hlsVideoUri = "http://winbeesolutions.livebox.co.in/BodhayanAcademyhls/LiveClasses.m3u8";
   // private String hlsVideoUri = "https://r2---sn-qxaeen7l.c.drive.google.com/videoplayback?expire=1592508366&ei=jofrXs2UMdPHuAWEn4ywAQ&ip=2405:205:1209:d0e2:640b:9183:ddfc:2908&cp=QVNOVEZfV1NUR1hOOlRJb0ZJTGlHZTNIaHpycXYzSnhKYWxJcE9UQVNVZUVIRWpsckRVUl9KeG8&id=72ea60dbb8916d31&itag=18&source=webdrive&requiressl=yes&mh=Oh&mm=32&mn=sn-qxaeen7l&ms=su&mv=m&mvi=1&pl=45&ttl=transient&susc=dr&driveid=1pc_0tMyXs67N-YxUQSmPNEtzgFfD-py7&app=explorer&mime=video/mp4&vprv=1&prv=1&dur=735.050&lmt=1587735688974171&mt=1592493866&sparams=expire,ei,ip,cp,id,itag,source,requiressl,ttl,susc,driveid,app,mime,vprv,prv,dur,lmt&sig=AOq0QJ8wRgIhALIhKqiQ7heKRfexzIT8imTs2NctbF_d8VruQshKf3BtAiEAnQAN6w0CcWeYBgnipcoTdnHoU4X_m6jwK1SSA1kUMew=&lsparams=mh,mm,mn,ms,mv,mvi,pl&lsig=AG3C_xAwRQIgeilxlDj1Fxt-B0DcTIBSwouPUJ9gqVCO85-VZqntoa8CIQCw0U_bFhvwF3KkHckTj1Thb5KudmmIwq8ocH1OZ9VBkg==&cpn=TP8RA6N2aoG4dGQu&c=WEB_EMBEDDED_PLAYER&cver=20200617";
    private SimpleExoPlayer player;
    PlayerView simpleExoPlayerView;
    boolean fullscreen = false;
    private String TAG ="tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
  BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();


        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

     simpleExoPlayerView =  findViewById(R.id.exoplayer_view);
        simpleExoPlayerView.setPlayer(player);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Exo2"), defaultBandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                .setAllowChunklessPreparation(true)
                .createMediaSource(Uri.parse(hlsVideoUri));

        player.prepare(hlsMediaSource);
        simpleExoPlayerView.requestFocus();
        player.setPlayWhenReady(true);
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState){

                    case STATE_BUFFERING:
                        Toast.makeText(MainActivity.this, "buffering", Toast.LENGTH_SHORT).show();
                        break;
                    case STATE_READY:
                        Toast.makeText(MainActivity.this, "ready", Toast.LENGTH_SHORT).show();

                        break;
                    case STATE_ENDED:
                        Toast.makeText(MainActivity.this, "Ended", Toast.LENGTH_SHORT).show();

                        break;
                    case STATE_IDLE:
                        Toast.makeText(MainActivity.this, "Ideal", Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        });


       final ImageView fullscreenButton = simpleExoPlayerView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_open));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    Log.d(TAG, "onClick: going to normal");
                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = (int) ( 200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    simpleExoPlayerView.setLayoutParams(params);
                    fullscreen = false;
                }else{
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_fullscreen_exit_24));
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    simpleExoPlayerView.setLayoutParams(params);
                    fullscreen = true;
                    Log.d(TAG, "onClick: going to land");
                }
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false); //to pause a video because now our video player is not in focus
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
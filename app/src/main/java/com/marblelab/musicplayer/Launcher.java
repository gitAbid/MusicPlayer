package com.marblelab.musicplayer;

import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by Abid Hasan on 4/17/2017.
 */

public class Launcher extends AwesomeSplash {
    @Override
    public void initSplash(ConfigSplash configSplash) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_TOP); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setOriginalHeight(400);
        configSplash.setOriginalWidth(400);
        configSplash.setLogoSplash(R.drawable.music_default); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);



        //Customize Title
        configSplash.setTitleSplash("Music Player");
        configSplash.setTitleTextColor(R.color.icons);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(500);
        configSplash.setAnimTitleTechnique(Techniques.BounceInDown);
        configSplash.setTitleFont("fonts/Raleway-ExtraLight.ttf");

    }

    @Override
    public void animationsFinished() {

        Intent intent=new Intent(this, MusicListActivity.class);
        startActivity(intent);
    }
}


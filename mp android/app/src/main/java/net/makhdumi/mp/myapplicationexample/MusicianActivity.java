package net.makhdumi.mp.myapplicationexample;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.BufferUnderflowException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fazlay on 7/26/2015.
 */
public  class MusicianActivity extends Fragment implements SurfaceHolder.Callback {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Bitmap bMap=null;
    private RelativeLayout musicPlayerLayout;
    private SurfaceView video;
    private SurfaceHolder holder;
    boolean stopThread = false;
    private ImageView profilePic;
    private WebView wv1, facebookFeed, instagramFeed, twitterFeed;
    private RelativeLayout biographyMain, facebookMain,instagramMain,twitterMain,localMain,mainLayout,tempLayout;
    private String name,genre,agent,phone,biography,profilePicUrl,fbUrl,fbProfileurl,localUrl,instaUrl;
    private String twitterUrl,soundCloudID;
    private Button bio;
    private Button soundcloud;
    private Button twitter;
    private Button local;
    private Button instagram;
    private Button facebook;

    private Button next,stop,play,previous;
    public static String user="";
    private MediaPlayer mediaPlayer;
    private double startTime = 0,finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000,backwardTime = 5000;
    private SeekBar seekbar;
    private TextView timePassed,timeLeft,songName;
    private int nowPlaying=0;
    private View rootView;
    private String prevTitle;
    ArrayList<String> songTypeList;
    ArrayList<String> fileNameList;
    boolean loadBio= false;
    boolean loadLocal= false;
    boolean loadFacebook= false;
    boolean loadInsta= false;
    boolean loadTwitter= false;
    boolean loadSC= false;
    public final static String Extra_Message = "My message";
    public static int oneTimeOnly = 0;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MusicianActivity newInstance(String temp) {
        MusicianActivity fragment = new MusicianActivity();
        Bundle args = new Bundle();
        user= temp;
        fragment.setArguments(args);
        return fragment;
    }

    public MusicianActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).current= 0;
        ((MainActivity) getActivity()).searchItem.setVisible(true);
    //    ((MainActivity) getActivity()).searchItem.setShowAsActionFlags( MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | (MenuItem.SHOW_AS_ACTION_IF_ROOM));
        ((MainActivity) getActivity()).item.setVisible(false);
    //    ((MainActivity) getActivity()).settings.setVisible(true);
        ((MainActivity) getActivity()).settings.setVisible(false);
        ((MainActivity) getActivity()).searchItem.setVisible(false);
        if (savedInstanceState != null) {

            user = savedInstanceState.getString("curUser");
        }

        ((MainActivity) getActivity()).musicianView= true;
        rootView = inflater.inflate(R.layout.activity_musician, container, false);
      //  ((MainActivity) getActivity()).restoreActionBar("");
        musicPlayerLayout= (RelativeLayout) rootView.findViewById(R.id.musicPlayerLayout);
        musicPlayerLayout.setVisibility(View.INVISIBLE);
        prevTitle= ((MainActivity) getActivity()).getCurrentTitle();
        ((MainActivity) getActivity()).setDisableItem();
        tempLayout= (RelativeLayout) rootView.findViewById(R.id.tempLayout);
        tempLayout.setVisibility(View.VISIBLE);
        mainLayout= (RelativeLayout) rootView.findViewById(R.id.mainLayout);
        biographyMain= (RelativeLayout) rootView.findViewById(R.id.biographyMain);
        biographyMain.setVisibility(View.VISIBLE);
        facebookMain= (RelativeLayout) rootView.findViewById(R.id.facebookMain);
        facebookMain.setVisibility(View.INVISIBLE);
        instagramMain= (RelativeLayout) rootView.findViewById(R.id.instagramMain);
        instagramMain.setVisibility(View.INVISIBLE);
        twitterMain= (RelativeLayout) rootView.findViewById(R.id.twitterMain);
        twitterMain.setVisibility(View.INVISIBLE);
        localMain= (RelativeLayout) rootView.findViewById(R.id.localMain);
        localMain.setVisibility(View.INVISIBLE);
        wv1=(WebView)rootView.findViewById(R.id.webView);
        wv1.setVisibility(View.INVISIBLE);
        video= (SurfaceView) rootView.findViewById(R.id.video);
        // video.setVisibility(View.INVISIBLE);
        songTypeList= new ArrayList<>();
        fileNameList= new ArrayList<>();
        profilePic= (ImageView) rootView.findViewById(R.id.profilePic);
        facebookMain= (RelativeLayout) rootView.findViewById(R.id.facebookMain);
        localMain= (RelativeLayout) rootView.findViewById(R.id.localMain);
        next = (Button) rootView.findViewById(R.id.next);
        stop = (Button) rootView.findViewById(R.id.stop);
        play=(Button)rootView.findViewById(R.id.play);
        previous=(Button)rootView.findViewById(R.id.previous);

        timePassed=(TextView)rootView.findViewById(R.id.timePassed);
        timeLeft=(TextView)rootView.findViewById(R.id.timeLeft);
        songName=(TextView)rootView.findViewById(R.id.songName);
        mediaPlayer= new MediaPlayer();

        seekbar=(SeekBar)rootView.findViewById(R.id.seekBar);
        seekbar.setClickable(true);
        stop.setEnabled(false);
        wv1= (WebView) rootView.findViewById(R.id.webView);

        wv1.setWebViewClient(new MyBrowser(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        facebookFeed= (WebView) rootView.findViewById(R.id.facebookFeed);
        facebookFeed.setWebChromeClient(new WebChromeClient());
        facebookFeed.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        facebookFeed.getSettings().setLoadsImagesAutomatically(true);
        facebookFeed.getSettings().setJavaScriptEnabled(true);
        facebookFeed.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        facebookFeed.getSettings().setAllowFileAccess(true);

        instagramFeed= (WebView) rootView.findViewById(R.id.instagramFeed);
        instagramFeed.setWebChromeClient(new WebChromeClient());
        instagramFeed.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        instagramFeed.getSettings().setLoadsImagesAutomatically(true);
        instagramFeed.getSettings().setJavaScriptEnabled(true);
        instagramFeed.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        instagramFeed.getSettings().setAllowFileAccess(true);

        twitterFeed= (WebView) rootView.findViewById(R.id.twitterFeed);
        twitterFeed.setWebChromeClient(new WebChromeClient());
        twitterFeed.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        twitterFeed.getSettings().setLoadsImagesAutomatically(true);
        twitterFeed.getSettings().setJavaScriptEnabled(true);
        twitterFeed.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        twitterFeed.getSettings().setAllowFileAccess(true);

        wv1.setWebChromeClient(new WebChromeClient());
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        bio = (Button) rootView.findViewById(R.id.bio);

        soundcloud= (Button) rootView.findViewById(R.id.soundcloud);

        twitter= (Button) rootView.findViewById(R.id.twitter);

        local= (Button) rootView.findViewById(R.id.local);

        instagram= (Button) rootView.findViewById(R.id.instagram);

        facebook= (Button) rootView.findViewById(R.id.facebook);
        bio.setEnabled(false);
        twitter.setEnabled(true);
        soundcloud.setEnabled(true);
        facebook.setEnabled(true);
        local.setEnabled(true);
        instagram.setEnabled(true);


        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!loadFacebook){
                    loadFacebook= true;
                    new FBProfileTask().execute("http://mp.makhdumi.net/getdata.asmx/getFBProfile?user="+user);
                }
                enableFB();
                wv1.setVisibility(View.INVISIBLE);
                biographyMain.setVisibility(View.INVISIBLE);
                facebookMain.setVisibility(View.VISIBLE);
                instagramMain.setVisibility(View.INVISIBLE);
                twitterMain.setVisibility(View.INVISIBLE);
                localMain.setVisibility(View.INVISIBLE);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!loadInsta){
                    loadInsta= true;
                    new InstaFeedTask().execute("http://mp.makhdumi.net/getdata.asmx/getInstagram?user="+user);
                }
                enableInsta();
                wv1.setVisibility(View.INVISIBLE);
                biographyMain.setVisibility(View.INVISIBLE);
                facebookMain.setVisibility(View.INVISIBLE);
                instagramMain.setVisibility(View.VISIBLE);
                twitterMain.setVisibility(View.INVISIBLE);
                localMain.setVisibility(View.INVISIBLE);
            }
        });
        local.setOnClickListener(

                new View.OnClickListener() {

            public void onClick(View v) {
                if(!loadLocal){
                    loadLocal= true;
                    new LocalFeedTask().execute("http://mp.makhdumi.net/getdata.asmx/getLocalMedia?user="+user);
                }
                enableLocal();
                wv1.setVisibility(View.INVISIBLE);
                biographyMain.setVisibility(View.INVISIBLE);
                facebookMain.setVisibility(View.INVISIBLE);
                instagramMain.setVisibility(View.INVISIBLE);
                twitterMain.setVisibility(View.INVISIBLE);
                localMain.setVisibility(View.VISIBLE);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!loadTwitter){
                    loadTwitter= true;
                    new TwitterFeedTask().execute("http://mp.makhdumi.net/getdata.asmx/getTwitter?user="+user);
                }
                enableTwitter();
                wv1.setVisibility(View.INVISIBLE);
                biographyMain.setVisibility(View.INVISIBLE);
                facebookMain.setVisibility(View.INVISIBLE);
                instagramMain.setVisibility(View.INVISIBLE);
                twitterMain.setVisibility(View.VISIBLE);
                localMain.setVisibility(View.INVISIBLE);
            }
        });
        bio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enableBio();
                wv1.setVisibility(View.INVISIBLE);
                biographyMain.setVisibility(View.VISIBLE);
                facebookMain.setVisibility(View.INVISIBLE);
                instagramMain.setVisibility(View.INVISIBLE);
                twitterMain.setVisibility(View.INVISIBLE);
                localMain.setVisibility(View.INVISIBLE);
            }
        });
        soundcloud.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!loadSC){
                    loadSC= true;
                    new SoundCloudTask().execute("http://mp.makhdumi.net/getdata.asmx/getSoundCloud?user="+user);
                }
                enableSC();
                wv1.setVisibility(View.VISIBLE);
                biographyMain.setVisibility(View.INVISIBLE);
                facebookMain.setVisibility(View.INVISIBLE);
                instagramMain.setVisibility(View.INVISIBLE);
                twitterMain.setVisibility(View.INVISIBLE);
                localMain.setVisibility(View.INVISIBLE);
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMedia();
            }

        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.pause();
                    stop.setEnabled(false);
                    play.setEnabled(true);
                }
                catch (Exception e){}
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nextMedia();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    previousMedia();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        String url= "http://mp.makhdumi.net/getdata.asmx/getBio?user="+user;
        new RequestTask().execute(url);/*
        url= "http://mp.makhdumi.net/getdata.asmx/getFBProfile?user="+user;
        new FBProfileTask().execute(url);

        url= "http://mp.makhdumi.net/getdata.asmx/getInstagram?user="+user;
        new InstaFeedTask().execute(url);
        url= "http://mp.makhdumi.net/getdata.asmx/getTwitter?user="+user;
        new TwitterFeedTask().execute(url);
        url= "http://mp.makhdumi.net/getdata.asmx/getSoundCloud?user="+user;
        new SoundCloudTask().execute(url);
        url= "http://mp.makhdumi.net/getdata.asmx/getLocalMedia?user="+user;
        new LocalFeedTask().execute(url);
        */

        return rootView;
    }
    void enableBio(){
        bio.setEnabled(false);
        twitter.setEnabled(true);
        soundcloud.setEnabled(true);
        facebook.setEnabled(true);
        local.setEnabled(true);
        instagram.setEnabled(true);
    }
    void enableTwitter(){
        bio.setEnabled(true);
        twitter.setEnabled(false);
        soundcloud.setEnabled(true);
        facebook.setEnabled(true);
        local.setEnabled(true);
        instagram.setEnabled(true);
    }
    void enableSC(){
        bio.setEnabled(true);
        twitter.setEnabled(true);
        soundcloud.setEnabled(false);
        facebook.setEnabled(true);
        local.setEnabled(true);
        instagram.setEnabled(true);
    }
    void enableFB(){
        bio.setEnabled(true);
        twitter.setEnabled(true);
        soundcloud.setEnabled(true);
        facebook.setEnabled(false);
        local.setEnabled(true);
        instagram.setEnabled(true);
    }
    void enableLocal(){
        bio.setEnabled(true);
        twitter.setEnabled(true);
        soundcloud.setEnabled(true);
        facebook.setEnabled(true);
        local.setEnabled(false);
        instagram.setEnabled(true);
    }
    void enableInsta(){
        bio.setEnabled(true);
        twitter.setEnabled(true);
        soundcloud.setEnabled(true);
        facebook.setEnabled(true);
        local.setEnabled(true);
        instagram.setEnabled(false);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void resetMedia(){
        try {
            mediaPlayer.start();

            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();

            //  if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
            //  }
            timeLeft.setText(String.format("%d : %02d",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
            );

            timePassed.setText(String.format("%d : %02d",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
            );

            seekbar.setProgress((int) startTime);
            //while(!stopThread) {
            myHandler.postDelayed(UpdateSongTime, 100);
            //}
            stop.setEnabled(true);
            play.setEnabled(false);
        }
        catch(Exception e){}

    }
    private void nextMedia() throws IOException {

        if(nowPlaying<fileNameList.size()-1 && fileNameList.size()>0){
            nowPlaying++;
            Uri uri= Uri.parse(fileNameList.get(nowPlaying));

            mediaPlayer.reset();
            if(songTypeList.get(nowPlaying).contains("audio"))
                tempLayout.setVisibility(View.INVISIBLE);
            else if(songTypeList.get(nowPlaying).contains("video"))
                tempLayout.setVisibility(View.VISIBLE);
            mediaPlayer.setDataSource(getActivity(),uri);
            mediaPlayer.prepare();
            resetMedia();

        }
        else if(nowPlaying>=fileNameList.size()-1  && fileNameList.size()>0){
            nowPlaying= 0;
            Uri uri= Uri.parse(fileNameList.get(0));
            mediaPlayer.reset();
            if(songTypeList.get(0).contains("audio"))
                tempLayout.setVisibility(View.INVISIBLE);
            else if(songTypeList.get(0).contains("video"))
                tempLayout.setVisibility(View.VISIBLE);
            mediaPlayer.setDataSource(getActivity(),uri);
            mediaPlayer.prepare();
            resetMedia();
        }
    }

    private void previousMedia() throws IOException {

        if(nowPlaying==0 && fileNameList.size()>0){
            nowPlaying= fileNameList.size()-1;
            Uri uri= Uri.parse(fileNameList.get(nowPlaying));

            mediaPlayer.reset();
            if(songTypeList.get(nowPlaying).contains("audio"))
                tempLayout.setVisibility(View.INVISIBLE);
            else if(songTypeList.get(nowPlaying).contains("video"))
                tempLayout.setVisibility(View.VISIBLE);
            mediaPlayer.setDataSource(getActivity(),uri);
            mediaPlayer.prepare();
            resetMedia();

        }
        else if(nowPlaying>0  && fileNameList.size()>0){
            nowPlaying --;
            Uri uri= Uri.parse(fileNameList.get(nowPlaying));
            mediaPlayer.reset();
            if(songTypeList.get(nowPlaying).contains("audio"))
                tempLayout.setVisibility(View.INVISIBLE);
            else if(songTypeList.get(nowPlaying).contains("video"))
                tempLayout.setVisibility(View.VISIBLE);
            mediaPlayer.setDataSource(getActivity(),uri);
            mediaPlayer.prepare();
            resetMedia();
        }
    }
    private Runnable UpdateSongTime = new Runnable() {

        public void run() {
            try {
                startTime = mediaPlayer.getCurrentPosition();
                timePassed.setText(String.format("%d : %02d",

                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                toMinutes((long) startTime)))
                );
                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(this, 100);
            }
            catch(IllegalStateException e){
            }
        }


    };
    private void setTwitter() throws JSONException {
        try {

            JSONArray jsonArr = new JSONArray(twitterUrl);
            // System.out.println(jsonArr);
            //  JSONObject jsonArr= new JSONObject(twitterUrl);
            // System.out.println(jsonArr);/*
            String temp = "<html><head><style>#tweetlist{\n" +
                    "    list-style-type: none;\n" +
                    "    background-color: #FFFFFF;\n" +
                    "    \n" +
                    "    margin: auto;\n" +
                    "}.fblike {\n" +
                    "    font-size: 12px;\n" +
                    "    margin: 8px;\n" +
                    "}</style></head><body> <div id=\"tweetlist\">";
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject data= jsonArr.getJSONObject(i);
                //  System.out.println(data);
                String profileImageUrl = "";
                String screenName = "";
                String createdDate= "";
                String text="";
                try {
                    profileImageUrl = data.getString("profileImageUrl");
                    //  System.out.println(picture);
                } catch (JSONException j) {
                }
                try {
                    screenName = data.getString("ScreenName");
                    //  System.out.println(picture);
                } catch (JSONException j) {
                }
                try {
                    createdDate = data.getString("CreatedDate");
                    //  System.out.println(picture);
                } catch (JSONException j) {
                }
                try {
                    text = data.getString("Text");
                    //  System.out.println(picture);
                } catch (JSONException j) {
                }
                temp = temp + "<li><img style=\"clear: both; float: left;\" src=\"" + profileImageUrl + "\"/>";
                temp = temp + "<p style=\"float: left;\" class=\"fblike\"><strong>@" + screenName + "</strong><br>" + createdDate + "</p><br>";
                temp = temp + ("<p style=\"clear: both;\"></p>");
                temp = temp + "<p>" + text + "</p>";
                temp = temp + "</li><hr>";

            }
            temp = temp + "</div></body></html>";
            twitterFeed.loadData(temp, "text/html", null);
        }
        catch (NullPointerException n){

        }
    }
    private void setInsta() throws JSONException {
        try {
            JSONObject jObj = new JSONObject(instaUrl);
            JSONArray jsonArr = jObj.getJSONArray("data");
            String temp = "<html><head><style>.instapics{\n" +
                    "    list-style-type: none;\n" +
                    "    background-color: #F0F0F0;\n" +
                    "}</style></head><body><div id=\"insta\" align=\"center\">\n" +
                    "\n" +
                    "                <div class=\"ScrollStyle\">\n" +
                    "                    <div class=\"instapics\"></div>";
            for (int i = 0; i < jsonArr.length(); i++) {
                String link = "";
                String instaPicUrl = "";
                JSONObject data = jsonArr.getJSONObject(i);
                try {
                    link = data.getString("link");
                    //  System.out.println(picture);
                } catch (JSONException j) {
                }
                try {
                    JSONObject images= data.getJSONObject("images");
                    JSONObject low_resolution= images.getJSONObject("low_resolution");
                    instaPicUrl = low_resolution.getString("url");
                    //  System.out.println(picture);
                } catch (JSONException j) {
                }
                temp = temp + "<li style=\"list-style-type: none;\"><img class=\"thumbnails\"   src='" + instaPicUrl + "'/></li><br>";
            }
            temp = temp + "</div></div></body></html>";
            instagramFeed.loadData(temp, "text/html", null);
        }
        catch (NullPointerException n){

        }
    }
    private void setFB() throws JSONException {
        try {
            JSONObject jObj = new JSONObject(fbUrl);
            JSONArray jsonArr = jObj.getJSONArray("data");
            JSONObject fbProfile = new JSONObject(fbProfileurl);
            String fbName = fbProfile.getString("name");

            String temp="";
            temp = "<html><head><style>.fbstatus{\n" +
                    "    list-style-type: none;\n" +
                    "    background-color: #FFFFFF;\n" +
                    "    \n" +
                    "    margin: auto;\n" +
                    "}#fblist{\n" +
                    "\n" +
                    "     margin-top: 4px;\n" +
                    "     margin-bottom: 4px;\n" +
                    "}.fblike {\n" +
                    "    font-size: 12px;\n" +
                    "    margin: 8px;\n" +
                    "}.fblink{\n" +
                    "    margin-right: 5px;\n" +
                    "    width: 100px;\n" +
                    "    height: 100px;\n" +
                    "} </style></head><body><div class=\"fbstatus\">";
            for (int i = 0; i < jsonArr.length(); i++) {

                String story = "";
                String picture = "";
                String video = "";
                String source = "";
                String status = "";
                String type = "";
                String linkName = "";
                String description = "";
                Integer likes = -1;
                Integer comments = -1;
                String since="";
                boolean hasLikes = false;
                boolean hasLinkName = false;
                boolean hasDescription = false;
                boolean hasComments = false;
                boolean hasStory = false;
                boolean hasPicture = false;
                boolean hasSource = false;
                boolean hasVideo = false;
                boolean hasStatus = false;
                JSONObject data = jsonArr.getJSONObject(i);
                JSONObject from= data.getJSONObject("from");
                String fbID= from.getString("id");
                String picURL="https://graph.facebook.com/v2.3/"+fbID+"/picture";
                try {
                    picture = data.getString("picture");
                    //  System.out.println(picture);
                    hasPicture = true;
                } catch (JSONException j) {
                    System.out.println("picture not found");
                }
                try {
                    type = data.getString("type");
                    //  System.out.println(picture);

                } catch (JSONException j) {
                }
                try {
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String getDate = data.getString("updated_time");

                    Date date= parse(getDate);
                    since= df.format(date);
                    //  System.out.println(picture);

                } catch (Exception j) {
                    //    System.out.println("updated time not found");
                }
                try {
                    story = data.getString("story");
                    // System.out.println(story);
                    hasStory = true;
                } catch (JSONException j) {
                    //     System.out.println("story not found");
                }
                try {
                    linkName = data.getString("name");
                    // System.out.println(story);
                    hasLinkName = true;
                } catch (JSONException j) {
                    //    System.out.println("link not found");
                }
                try {
                    description = data.getString("description");
                    // System.out.println(story);
                    hasDescription = true;
                } catch (JSONException j) {
                }
                try {
                    status = data.getString("message");
                    hasStatus = true;
                    // System.out.println(story);
                } catch (JSONException j) {
                    //     System.out.println("message not found");
                }
                try {
                    source = data.getString("source");
                    hasSource = true;
                    //     System.out.println(source);
                } catch (JSONException j) {
                }
                try {

                    JSONObject obj1 = data.getJSONObject("likes");
                    JSONArray obj = obj1.getJSONArray("data");
                    likes = obj.length();
                    hasLikes = true;
                    // System.out.println(story);
                } catch (JSONException j) {
                    //     System.out.println("likes not found");
                }
                try {

                    JSONObject obj1 = data.getJSONObject("comments");
                    JSONArray obj = obj1.getJSONArray("data");
                    comments = obj.length();
                    hasComments = true;
                    // System.out.println(story);
                } catch (JSONException j) {
                    //   System.out.println("comments not found");
                }

                temp = temp + "<li id=\"fblist\">";

                if (((story.contains("updated")) || (story.contains("changed")) || (story.contains("shared"))))
                    temp = temp + "<strong><p>" + story + "</p></strong>";
                temp = temp + "<img style=\"float: left; margin-bottom: 3px; \" src='" + picURL + "'/>";
                temp = temp + "<p style=\"float: left;\" class=\"fblike\"><strong>" + fbName + "</strong><br>" + since + "</p><br>";
                temp = temp + "<p style=\"clear: both;\"></p>";

                if (hasStatus == true)
                    temp = temp + "<p id=\"scrollpara\">" + status + "</p>";
                if (type.contains("link")) {
                    if (hasPicture)
                        temp = temp + "<img style=\"clear: both; height: 70px; width: 70px; float: left;\" class=\"fblink\" src='" + picture + "'/>";
                    temp = temp + "<p style=\"float: left;\">";
                    if (hasLinkName)
                        temp = temp + "<strong>" + linkName + "</strong><br>";
                    if (hasDescription)
                        temp = temp + description + "<br>";

                    temp = temp + "</p><p style=\"clear: both;\"></p>";
                } else {
                    if (hasStory && story.contains("shared") && !story.contains("shared a link")) {
                        temp = temp + "<br>";
                        temp = temp + "<p style=\"float: left;\" class=\"fblike\"><strong>" + linkName + "</strong></p><br>";
                        temp = temp + "<p style=\"clear: both;\"></p>";
                        if (hasDescription)
                            temp = temp + "<p id=\"scrollpara\">" + description + "</p>";
                    }

                    if (hasPicture && !type.contains("video")) {
                        temp = temp + "<img align=\"middle\"  src=\"" + picture + "\"/>";
                    }
                    if (type.contains("video"))
                        temp = temp + "<video width=\"300\" height=\"200\" controls><source  src='" + source + "'></video>";

                    if (hasStory && story.contains("shared") && !story.contains("shared a link"))
                        temp = temp + "<hr>";


                }
                if (hasLikes && hasComments)
                    temp = temp + "<p class=\"fblike\">Likes: " + likes.toString() + "  Comments: " + comments.toString() + "</p>";
                else if (!hasLikes && !hasComments)
                    temp = temp + "<p class=\"fblike\">Likes: 0  Comments: 0</p>";
                else if (hasLikes && !hasComments)
                    temp = temp + "<p class=\"fblike\">Likes: " + likes.toString() + " Comments: 0</p>";
                if (!hasLikes && hasComments)
                    temp = temp + "<p class=\"fblike\">Likes: 0  Comments: " + comments.toString() + "</p>";
                temp = temp + "<hr class\"hrbreak\"></li>";
            }
            temp = temp + "</div></body></html>";
            facebookFeed.loadData(temp, "text/html", null);
        }
        catch (NullPointerException n){

        }
    }
    private void setLocal() throws JSONException{
        try {
            JSONArray jsonArray = new JSONArray(localUrl);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String url = "";
                String type = "";
                try {
                    url = jsonObject.getString("filename");
                    fileNameList.add(url);
                } catch (Exception e) {
                    fileNameList.add("");
                }
                try {
                    type = jsonObject.getString("mediatype");
                    songTypeList.add(type);

                } catch (Exception e) {
                    songTypeList.add("");
                }
                if(i==0){
                    if(type.contains("audio")) {
                        tempLayout.setVisibility(View.INVISIBLE);
                    }
                    else if(type.contains("video")){
                        tempLayout.setVisibility(View.VISIBLE);
                    }
                    Uri uri = Uri.parse(url);
                    nowPlaying = 0;
                    mediaPlayer= MediaPlayer.create(getActivity(), uri);
                    holder= video.getHolder();
                    holder.addCallback(this);
                    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    mediaPlayer.setDisplay(holder);

                    musicPlayerLayout.setVisibility(View.VISIBLE);
                }
            }
        }
        catch (Exception e){
        }
    }
    private void setImage(Drawable drawable)
    {
        profilePic.setBackgroundDrawable(drawable);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public class DownloadImage extends AsyncTask<String, Integer, Drawable> {

        @Override
        protected Drawable doInBackground(String... arg0) {
            // This is done in a background thread
            return downloadImage(arg0[0]);
        }

        protected void onPostExecute(Drawable image)
        {
            setImage(image);
        }

        private Drawable downloadImage(String _url)
        {
            //Prepare to download image
            URL url;
            BufferedOutputStream out;
            InputStream in;
            BufferedInputStream buf;

            try {
                url = new URL(_url);
                in = url.openStream();

                buf = new BufferedInputStream(in);

                // Convert the BufferedInputStream to a Bitmap
                bMap = BitmapFactory.decodeStream(buf);
                if (in != null) {
                    in.close();
                }
                if (buf != null) {
                    buf.close();
                }

                return new BitmapDrawable(bMap);

            } catch (Exception e) {
                Log.e("Error reading file", e.toString());
            }

            return null;
        }

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    class RequestTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                    try{
                        //   System.out.println(responseString);
                        StringBuilder sb = new StringBuilder(responseString);
                        sb.delete(0,76);
                        sb.delete(sb.length()-9,sb.length());
                        responseString= sb.toString();
                        //  System.out.println(responseString);

                        JSONArray jsonObj = new JSONArray(responseString);

                        for (int i=0; i<jsonObj.length(); i++) {
                            JSONObject data = jsonObj.getJSONObject(i);
                            name = data.getString("displayname");
                            biography= data.getString("biography");

                            genre = data.getString("genre");

                            profilePicUrl= data.getString("profilepic");
                            agent = data.getString("agent");

                            phone= data.getString("phone");
                        }
                    }
                    catch (BufferUnderflowException u){

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e){

                    }
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());

                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            //    if(hasConnection()) {
            try {
                ((MainActivity) getActivity()).restoreActionBar(name);

            TextView txtGenre = (TextView) rootView.findViewById(R.id.genreVal);
            txtGenre.setText(genre);
            TextView txtName = (TextView) rootView.findViewById(R.id.nameVal);
            txtName.setText(name);

            TextView txtAgent = (TextView) rootView.findViewById(R.id.agentVal);
            txtAgent.setText(agent);
            TextView txtBio = (TextView) rootView.findViewById(R.id.biographyVal);
            txtBio.setText(biography);

            TextView txtPhone = (TextView) rootView.findViewById(R.id.phoneVal);
            txtPhone.setText(phone);

            new DownloadImage().execute(profilePicUrl);
            }
            catch (Exception e){
            }
            //     }
        }
    }
    class FBFeedTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                    try{
                        //     System.out.println(responseString);
                        StringBuilder sb = new StringBuilder(responseString);
                        sb.delete(0,76);
                        sb.delete(sb.length()-9,sb.length());
                        fbUrl= sb.toString();
                        //     System.out.println(responseString);
                    }
                    catch (BufferUnderflowException u){

                    }

                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());

                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            catch (Exception e){

            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            //  if(hasConnection()) {
            try {
                setFB();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //   }
        }
    }
    class FBProfileTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                    try{
                        StringBuilder sb = new StringBuilder(responseString);
                        sb.delete(0,76);
                        sb.delete(sb.length()-9,sb.length());
                        fbProfileurl= sb.toString();
                    }
                    catch (BufferUnderflowException u){

                    }

                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {

                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            catch (Exception e){

            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            //if(hasConnection()) {
            String url = "http://mp.makhdumi.net/getdata.asmx/getFBPosts?user="+user;
            new FBFeedTask().execute(url);
            //  }
        }
    }
    class TwitterFeedTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                    try{
                        StringBuilder sb = new StringBuilder(responseString);
                        sb.delete(0,76);
                        sb.delete(sb.length()-9,sb.length());
                        twitterUrl= sb.toString();
                    }
                    catch (BufferUnderflowException u){

                    }

                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());

                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            catch (Exception e){

            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            //  if(hasConnection()) {
            try {
                setTwitter();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //   }
        }
    }
    class InstaFeedTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                    try{
                        StringBuilder sb = new StringBuilder(responseString);
                        sb.delete(0,76);
                        sb.delete(sb.length()-9,sb.length());
                        instaUrl= sb.toString();
                    }
                    catch (BufferUnderflowException u){

                    }

                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());

                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            catch (Exception e){

            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            //  if(hasConnection()) {
            try {
                setInsta();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //   }
        }
    }
    class SoundCloudTask extends AsyncTask<String, String, String>{
        boolean connection= true;
        @Override
        protected String doInBackground(String... uri) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                connection= true;
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                    try{
                        StringBuilder sb = new StringBuilder(responseString);
                        sb.delete(0,76);
                        sb.delete(sb.length()-9,sb.length());
                        soundCloudID= sb.toString();

                    }
                    catch (BufferUnderflowException u){

                    }

                }
                else{
                    //Closes the connection.

                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());

                }
            } catch (ClientProtocolException e) {
                connection= false;
                //TODO Handle probles..
            } catch (IOException e) {
                connection= false;
                //TODO Handle problems..
            }
            catch (Exception e){

            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            if(connection) {
                String  url = "https://w.soundcloud.com/player/?url=http://api.soundcloud.com/users/"+soundCloudID;
                wv1.loadUrl(url);
            }
        }
    }
    public static Date parse( String input ) throws java.text.ParseException {

        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );

        //this is zero time so we need to add that TZ indicator for
        if ( input.endsWith( "Z" ) ) {
            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = input.substring( 0, input.length() - inset );
            String s1 = input.substring( input.length() - inset, input.length() );

            input = s0 + "GMT" + s1;
        }

        return df.parse( input );

    }
    class LocalFeedTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                    try{
                        //   System.out.println(responseString);
                        StringBuilder sb = new StringBuilder(responseString);
                        sb.delete(0,76);
                        sb.delete(sb.length()-9,sb.length());
                        localUrl= sb.toString();
                        if(localUrl.length()==0)
                            musicPlayerLayout.setVisibility(View.INVISIBLE);

                        //   System.out.println(responseString);
                    }
                    catch (BufferUnderflowException u){

                    }

                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());

                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            catch (Exception e){

            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            //  if(hasConnection()) {
            try {
                setLocal();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //   }
        }
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        try {
            //mediaPlayer.pause();
            mediaPlayer.pause();
            wv1.onPause();
            facebookFeed.onPause();
            twitterFeed.onPause();
            instagramFeed.onPause();
        }
        catch (Exception e){

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
           // ((MainActivity) getActivity()).restoreActionBar(prevTitle);
            mediaPlayer.stop();
            mediaPlayer.release();
            stopThread= true;
            mainLayout.removeViewInLayout(wv1);
            wv1.removeAllViewsInLayout();
            wv1.removeAllViews();
            wv1.destroy();
            facebookMain.removeAllViewsInLayout();
            facebookFeed.removeAllViewsInLayout();
            facebookFeed.removeAllViews();
            facebookFeed.destroy();
            twitterMain.removeAllViewsInLayout();
            twitterFeed.removeAllViewsInLayout();
            twitterFeed.removeAllViews();
            twitterFeed.destroy();
            instagramMain.removeAllViewsInLayout();
            instagramFeed.removeAllViewsInLayout();
            instagramFeed.removeAllViews();
            instagramFeed.destroy();
            mainLayout.removeAllViewsInLayout();
            ((MainActivity) getActivity()).setEnableItem();
        } catch (Exception e) {

        }
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();

        System.gc();
        if(bMap!=null){
            bMap.recycle();
            bMap= null;
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("curUser", user);
    }

}
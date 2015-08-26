package net.makhdumi.mp.myapplicationexample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;

/**
 * Created by Fazlay on 7/25/2015.
 */
public class Search extends Fragment  {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private ProgressBar spinner;
    private ArrayList<Bitmap> bm;
    private String prevTitle="";
    private int current;
    private int previous;
    private ArrayList<String> uriList;
    public static String user = "";
    public static String genre= "";
    private ArrayList<String> userIdList;
    private ArrayList<ImageView> imageView;
    private String allBio;
    private String url;
    private Drawable picture;
    private LinearLayout mainLayout;
    private static final String ARG_SECTION_NUMBER = "section_number";
    View rootView;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Search newInstance(String u, String g) {
        Search fragment = new Search();
        Bundle args = new Bundle();
        user= u.replaceAll(" ","_");
        genre= g.replaceAll(" ","_");
        if(genre.contains("All")||genre.contains("all"))
            genre="";
        return fragment;
    }

    public Search() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).current= 1;
        ((MainActivity) getActivity()).setMarker(1);
        ((MainActivity) getActivity()).settings.setVisible(true);

        ((MainActivity) getActivity()).searchItem.setVisible(true);
        ((MainActivity) getActivity()).searchItem.setShowAsActionFlags( MenuItem.SHOW_AS_ACTION_ALWAYS);
        ((MainActivity) getActivity()).item.setVisible(false);
        ((MainActivity) getActivity()).searchItem.expandActionView();
       // if(((MainActivity) getActivity()).searchView)
       // ((MainActivity) getActivity()).searchView.onActionViewCollapsed();
        ((MainActivity) getActivity()).searchStarted=false ;

        if (savedInstanceState != null) {



            user = savedInstanceState.getString("curUser");
            genre = savedInstanceState.getString("curGenre");
        }
        ((MainActivity) getActivity()).restoreActionBar("Search by Artist Name");
        ((MainActivity) getActivity()).mTitle= "Search by Artist Name";

        //((MainActivity) getActivity()).initial=1;
        rootView = inflater.inflate(R.layout.musician_wall, container, false);
        prevTitle= ((MainActivity) getActivity()).getCurrentTitle();
        previous= ((MainActivity) getActivity()).previous;
        current= ((MainActivity) getActivity()).current;
        mainLayout= (LinearLayout) rootView.findViewById(R.id.mainActivityLayout);
        userIdList= new ArrayList<>();
        imageView= new ArrayList<>();
        bm= new ArrayList<>();
        uriList= new ArrayList<>();

        spinner=(ProgressBar) rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        url="http://mp.makhdumi.net/getdata.asmx/filter?name="+user+"&genre="+genre;
        new allBioTask().execute(url);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }


    class allBioTask extends AsyncTask<String, String, String> {
        boolean connection = true;

        @Override
        protected String doInBackground(String... uri) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                connection = true;
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                    try {
                        StringBuilder sb = new StringBuilder(responseString);
                        sb.delete(0, 76);
                        sb.delete(sb.length() - 9, sb.length());
                        allBio = sb.toString();

                    } catch (BufferUnderflowException u) {

                    }

                } else {
                    //Closes the connection.

                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());

                }
            } catch (ClientProtocolException e) {
                connection = false;
                //TODO Handle problems..
            } catch (IOException e) {
                connection = false;
                //TODO Handle problems..
            } catch (Exception e) {

            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            try {
                setLocal();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setLocal() throws JSONException {
        try {
            Point size = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            int halfScreenWidth = (int)(screenWidth *0.5);
            int quarterScreenWidth = (int)(halfScreenWidth * 0.5);
            JSONArray jsonArray = new JSONArray(allBio);

            String name="";
            String bio="";
            String genre="";
            String pic="";
            String id="";
            for (int i = 0; i < jsonArray.length(); i++) {
                final int index= i;
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                try {
                    name= jsonObject.getString("displayname");
                }
                catch (Exception e){

                }
                try {
                    genre= jsonObject.getString("genre");
                }
                catch (Exception e){

                }
                try {
                    bio= jsonObject.getString("biography");
                }
                catch (Exception e){

                }
                try {
                    pic= jsonObject.getString("profilepic");
                    uriList.add(pic);
                }
                catch (Exception e){

                }
                try {
                    id= jsonObject.getString("userid");
                    userIdList.add(id);
                }
                catch (Exception e){

                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 10,10, 5);

                LinearLayout info= new LinearLayout(getActivity());
                info.setLayoutParams(params);
                info.setOrientation(LinearLayout.VERTICAL);
                TextView textName= new TextView(getActivity());
                textName.setText("Name: "+name);
                textName.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
                textName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            startIntent(this, index);
                        }
                        catch (Exception e){}

                    }
                });
                info.addView(textName);
                TextView textGenre= new TextView(getActivity());
                //TextView textGenre= new TextView(this);
                textGenre.setText("Genre: "+genre);
                textGenre.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
                info.addView(textGenre);
                params= new LinearLayout.LayoutParams(quarterScreenWidth, quarterScreenWidth);
                params.setMargins(5, 10,10, 5);

                ImageView img= new ImageView(getActivity());
                img.setLayoutParams(params);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                BitmapFactory.Options bmOptions;
                bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 2;

                InputStream in = null;
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            startIntent(this, index);
                        }
                        catch (Exception e){}
                    }
                });
                imageView.add(img);
                try {
                    new setDrawable().execute(i);
                } catch (Exception e1) {
                }


                LinearLayout sub1=  new LinearLayout(getActivity());
                sub1.setOrientation(LinearLayout.HORIZONTAL);
                sub1.addView(imageView.get(i));
                sub1.addView(info);

                LinearLayout sub2=  new LinearLayout(getActivity());

                sub2.setOrientation(LinearLayout.VERTICAL);
                TextView biography=  new TextView(getActivity());
                biography.setText("Biography");
                biography.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
                sub2.addView(biography);

                TextView textBio=  new TextView(getActivity());
                if(bio.length()>150){
                    StringBuilder sb = new StringBuilder(bio);
                    sb.delete(149,sb.length());
                    bio= sb.toString() + "...";
                }
                textBio.setText(bio);
                //   textBio.setTextAppearance(this.getActivity(), android.R.style.TextAppearance_Medium);
                sub2.addView(textBio);
                params = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.MATCH_PARENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10,10, 10);
                LinearLayout subMain=  new LinearLayout(getActivity());
                subMain.setBackgroundColor(Color.WHITE);
                subMain.setOrientation(LinearLayout.VERTICAL);
                subMain.setLayoutParams(params);
                subMain.addView(sub1);
                subMain.addView(sub2);

                mainLayout.addView(subMain);

            }


        }
        catch (Exception e){
        }
        spinner.setVisibility(View.GONE);
    }

    class setDrawable extends AsyncTask<Integer, String, Drawable> {
        Integer index;
        @Override
        protected Drawable doInBackground(Integer... arg0) {
            // This is done in a background thread

            index= arg0[0];
            return downloadImage(arg0[0]);
        }


        protected Drawable downloadImage(Integer i) {
            BitmapFactory.Options bmOptions;
            bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 2;

            InputStream in = null;

            try {
                in = OpenHttpConnection(uriList.get(i));
                bm.add(BitmapFactory.decodeStream(in, null, bmOptions));
                in.close();
                //imageView.get(i[0]).setBackgroundDrawable(new BitmapDrawable(bm.get(i[0])));
                return new BitmapDrawable(bm.get(i));

            } catch (Exception e1) {
                return null;
            }
            //  return null;
        }



        protected void onPostExecute(Drawable result) {
            setImage(result, index);
            //Do anything with response..

        }
    }
    private void setImage(Drawable drawable, Integer i)
    {
        try {
            imageView.get(i).setBackgroundDrawable(drawable);
        }
        catch(IndexOutOfBoundsException e){

        }

    }

    public void startIntent(View.OnClickListener view, int id) {
        String temp= userIdList.get(id);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //if(((MainActivity) getActivity()).initial!=1)
        //    ((MainActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
        //((MainActivity) getActivity()).initial=1;
        ((MainActivity) getActivity()).mContent= MusicianActivity.newInstance(temp);
        ((MainActivity) getActivity()).list.push(0);
        ((MainActivity) getActivity()).list.push(0);
        fragmentManager.beginTransaction()
                .replace(R.id.container,  ((MainActivity) getActivity()).mContent).addToBackStack("as")
                .commit();
    }



    private static InputStream OpenHttpConnection(String strURL)
            throws IOException {
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
        }
        return inputStream;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }



    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ((MainActivity) getActivity()).restoreActionBar(prevTitle);
        if(((MainActivity) getActivity()).list.size()>1) {
            ((MainActivity) getActivity()).list.pop();
            ((MainActivity) getActivity()).current = (int) ((MainActivity) getActivity()).list.peek();
        }
        ((MainActivity) getActivity()).restoreActionBar(prevTitle);

        ((MainActivity) getActivity()).searchStarted= true ;
        ((MainActivity) getActivity()).searchView.clearFocus();
        //   ((MainActivity) getActivity()).invalidateOptionsMenu();
        unbindDrawables(rootView.findViewById(R.id.musicianWallLayout));
        System.gc();
        for(int i=0; i< bm.size(); i++){
            try {
                bm.get(i).recycle();
                bm.set(i,null);
            }
            catch (Exception e){

            }

        }
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("curUser", user);
        outState.putString("curGenre", genre);
        super.onSaveInstanceState(outState);
    }



}
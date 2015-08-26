package net.makhdumi.mp.myapplicationexample;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import java.util.List;
import java.util.Stack;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, AdapterView.OnItemSelectedListener,SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    private SimpleCursorAdapter mAdapter;
    public NavigationDrawerFragment mNavigationDrawerFragment;
    public CharSequence mTitle;
    public SearchView searchView;
    public static MenuItem item;
    public static MenuItem settings;
    public static MenuItem searchItem;
    public Menu mainMenu;
    public int current=0;
    public int previous=0;
    public int initial=1;
    public boolean searchStarted= true;
    public Stack list;
    private int currentSpinner=0;
    public boolean initialLoad= true;
    public boolean dropdownLoad= true;
    public boolean musicianView= false;
    public boolean firstLoad= true;
    public Fragment mContent=null;
    private Spinner spinner;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's instance
         //   mContent = getSupportFragmentManager().getFragment(
         //           savedInstanceState, "mContent");

        }

        list= new Stack();
        setContentView(R.layout.activity_main);
        Intent intent  = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

        }
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        list.push(position);
        if (position==0) {
            current=0;
            mContent= MusicianWall.newInstance(position + 1);
         //   invalidateOptionsMenu();
            if (initial != 1)
                getSupportFragmentManager().popBackStackImmediate();
            initial= 1;
            restoreActionBar("Most Viewed Artists");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mContent).addToBackStack(null)
                    .commit();
        }
        else if(position==2){

            dropdownLoad= true;
                current=2;
                initial= 1;
                restoreActionBar("Search By Genre");
                mTitle="Search By Genre";
        }

        else if(position==1){

          //  if (initial != 1)
           //     getSupportFragmentManager().popBackStackImmediate();
                initial=1;
                current=1;/*
            mContent= Search.newInstance("","");

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mContent).addToBackStack("first")
                    .commit();*/
                restoreActionBar("Search By Artist Name");
            mTitle="Search By Artist Name";

        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Most Viewed Artists";
                break;
            case 2:
                mTitle = "Search by Artist Name";
                break;
            case 3:
                mTitle = "Search by Genre";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void restoreActionBar(String temp) {
        mTitle= temp;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(temp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mainMenu= menu;
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();

            searchItem = menu.findItem(R.id.search_bar);
            searchView = (SearchView) searchItem.getActionView();
          //  searchView.setIconifiedByDefault(false);


            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            if (searchManager != null) {

                SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
                searchView.setSearchableInfo(info);
            }
            searchView.setOnQueryTextListener(this);
            searchView.setOnSuggestionListener(this);


            settings= menu.findItem(R.id.action_settings);
            ImageButton overflow= (ImageButton)MenuItemCompat.getActionView(settings);
            final ImageButton select= (ImageButton) MenuItemCompat.getActionView(settings);
            select.setBackgroundResource(R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha);
            select.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(MainActivity.this, select);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.genre_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            clearHistory();
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }
            });
            settings.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);



            item = menu.findItem(R.id.spinner);
            spinner = (Spinner) MenuItemCompat.getActionView(item);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.genre_array, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
            spinner.setPrompt("Genre");
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            spinner.setSelection(currentSpinner);
            item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
            //    item.expandActionView();
          //  searchView.clearFocus();
            if(current==0) {
              //  searchItem.setShowAsActionFlags( MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | (MenuItem.SHOW_AS_ACTION_IF_ROOM));
                item.setVisible(false);
                settings.setVisible(false);
                searchItem.setVisible(false);
            }
            else if(current==1) {
                settings.setVisible(true);
                item.setVisible(false);

                    searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

                        searchItem.expandActionView();

                if(searchStarted==true) {
                    searchView.onActionViewExpanded();
                    searchView.onActionViewCollapsed();
                }
                 //   searchView.clearFocus();
            }
            else if (current==2){
                item.setVisible(true);
                settings.setVisible(false);
                searchItem.setVisible(false);
            }
           // searchStarted= true;
            return true;
        }
        else {
          //
            if(initialLoad==false) {
              //  searchStarted = false;
                currentSpinner= spinner.getSelectedItemPosition();
                dropdownLoad= false;
                searchView.clearFocus();
            }
            else{
                initialLoad = false;
            }
            return super.onCreateOptionsMenu(menu);
        }
    }


    public void clearHistory(){
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
        suggestions.clearHistory();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length()>0) {
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(initial!=1)
            getSupportFragmentManager().popBackStackImmediate();
        initial=0;
        mContent= Search.newInstance(query,"");
        fragmentManager.beginTransaction()
                .replace(R.id.container, mContent).addToBackStack(null)
                .commit();
        searchView.clearFocus();

        return true;

    }
    @Override
    public boolean onSuggestionClick(int position) {
        String suggestion = getSuggestion(position);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(initial!=1)
            getSupportFragmentManager().popBackStackImmediate();
        initial=0;
        mContent= Search.newInstance(suggestion, "");
        fragmentManager.beginTransaction()
                .replace(R.id.container, mContent).addToBackStack("a")
                .commit();
        return true;
    }
    @Override
    public boolean onSuggestionSelect(int position) {
        String suggestion = getSuggestion(position);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(initial!=1)
            getSupportFragmentManager().popBackStackImmediate();
        initial=0;
        mContent= Search.newInstance(suggestion, "");
        fragmentManager.beginTransaction()
                .replace(R.id.container, mContent).addToBackStack(null)
                .commit();
        return true;
    }
    private String getSuggestion(int position) {
        Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(
                position);
        String suggest1 = cursor.getString(cursor
                .getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
        return suggest1;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        if(searchStarted== true) {
            FragmentManager fragmentManager = getSupportFragmentManager();

//            if (initial == 0)
  //              getSupportFragmentManager().popBackStackImmediate();
            initial = 0;
            mContent = Search.newInstance(newText, "");
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mContent).addToBackStack(null)
                    .commit();
       }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(dropdownLoad) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (initial != 1) {
                getSupportFragmentManager().popBackStackImmediate();
            }
            initial = 0;
            mContent = Dropdown.newInstance("", parent.getItemAtPosition(position).toString());
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mContent).addToBackStack(null)
                    .commit();
        }
        else {
            //spinner.setSelected(false);
            //spinner.setSelection(currentSpinner);
            dropdownLoad = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * A placeholder fragment containing a simple view.
     */


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStackImmediate();
        } else if(fragmentManager.getBackStackEntryCount() <= 1) {
             super.onBackPressed();
            super.onBackPressed();
//             onDestroy();
        }
    }
    public void setDisableItem(){
        mNavigationDrawerFragment.disableSelect();
    }
    public void setEnableItem(){
        mNavigationDrawerFragment.enableSelect();
    }
    public String getCurrentTitle(){
        return mTitle.toString();
    }
    public void setMarker(int i){
        mNavigationDrawerFragment.setMarker(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);


    }
}

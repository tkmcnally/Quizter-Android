package com.tkmcnally.quizter.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.Session;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.fragments.LeaderboardFragment;
import com.tkmcnally.quizter.fragments.ProfileFragment;
import com.tkmcnally.quizter.adapters.NavDrawerListAdapter;
import com.tkmcnally.quizter.fragments.QuizMeSelectionFragment;

/**
 * Created by Thomas on 1/10/14.
 */
public class NavDrawerActivity extends FragmentActivity {

    private String[] navigationOptionList;
    private int[] navigationOptionListIcons;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggler;

    public Bundle profileBundle;

    private CharSequence title, drawerTitle;

    private DisplayImageOptions options;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        Log.d("quizter", "NAVDRAWER RECREATED");

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)

                .build();


        ActionBar ab = getActionBar(); //needs  import android.app.ActionBar;
        ab.setTitle("Quizter");
        ab.setSubtitle("Dashboard");
        ab.setDisplayHomeAsUpEnabled(true);

        title = drawerTitle = getTitle();

        navigationOptionListIcons = new int[] { R.drawable.user, R.drawable.quizme, R.drawable.trophy, R.drawable.settings, R.drawable.signout};

        navigationOptionList = getResources().getStringArray(R.array.navigationOptions);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

      //  drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, navigationOptionList));
        drawerList.setAdapter(new NavDrawerListAdapter(this, navigationOptionList, navigationOptionListIcons));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggler = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggler);

        setDefaultFragment(0);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        switch(position) {
            case 0:
                Fragment fragment = new ProfileFragment();


                if(profileBundle != null) {
                    Log.d("quizter", "SET ARGUMENTS");
                    fragment.setArguments(profileBundle);
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                drawerList.setItemChecked(position, true);
                setTitle(navigationOptionList[position]);
                drawerLayout.closeDrawer(drawerList);
                drawerList.setItemChecked(position, false);
                break;
            case 1:
                Fragment fragment1 = new QuizMeSelectionFragment();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.content_frame, fragment1).commit();

                drawerList.setItemChecked(position, true);
                setTitle(navigationOptionList[position]);
                drawerLayout.closeDrawer(drawerList);
                drawerList.setItemChecked(position, false);
                break;
            case 2:
                Fragment fragment2 = new LeaderboardFragment();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.content_frame, fragment2).commit();

                drawerList.setItemChecked(position, true);
                setTitle(navigationOptionList[position]);
                drawerLayout.closeDrawer(drawerList);
                drawerList.setItemChecked(position, false);
                break;
            case 3:
                break;
            case 4:
                Session.getActiveSession().closeAndClearTokenInformation();
                Intent intent = new Intent(this, QuizterActivity.class);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

        }


    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggler.syncState();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggler.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public void setDefaultFragment(int position) {
        Fragment fragment = new ProfileFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "ProfileFragment").commit();

        setTitle(navigationOptionList[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public void onBackPressed() {
        ProfileFragment profileFragment = (ProfileFragment)getSupportFragmentManager().findFragmentByTag("ProfileFragment");

        if(profileFragment != null) {
            if (!profileFragment.isVisible()) {
                super.onBackPressed();
            }

        } else {
            super.onBackPressed();
        }

        //create a dialog to ask yes no question whether or not the user wants to exit
    }

    public void storeProfileBundle(Bundle profileBundle) {
        this.profileBundle = profileBundle;
    }

    public DisplayImageOptions getOptions() {
        return options;
    }
}

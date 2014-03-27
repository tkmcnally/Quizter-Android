package com.tkmcnally.quizter.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Session;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.fragments.LeaderboardFragment;
import com.tkmcnally.quizter.fragments.ProfileFragment;
import com.tkmcnally.quizter.adapters.NavDrawerListAdapter;
import com.tkmcnally.quizter.fragments.QuestionModifyFragment;
import com.tkmcnally.quizter.fragments.QuizMeSelectionFragment;
import com.tkmcnally.quizter.models.quizter.UserData;

import org.w3c.dom.Text;

/**
 * Created by Thomas on 1/10/14.
 */
public class NavDrawerActivity extends FragmentActivity {

    private String[] navigationOptionList;
    private int[] navigationOptionListIcons;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggler;

    private AlertDialog alertDialog;

    public Bundle profileBundle;

    private CharSequence title, drawerTitle;

    private DisplayImageOptions options;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private FrameLayout frame;
    private float lastTranslate = 0.0f;

    private UserData userData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        //Log.d("quizter", "NAVDRAWER RECREATED");

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(false)
                .cacheOnDisc(false)
                .considerExifParams(true)

                .build();


        ActionBar ab = getActionBar(); //needs  import android.app.ActionBar;
        ab.setTitle("Quizter");
        ab.setSubtitle("Dashboard");
        ab.setDisplayHomeAsUpEnabled(true);

        title = drawerTitle = getTitle();

        navigationOptionListIcons = new int[] { R.drawable.user, R.drawable.quizme, R.drawable.trophy, R.drawable.feedback, R.drawable.signout};

        navigationOptionList = getResources().getStringArray(R.array.navigationOptions);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        drawerList = (ListView) findViewById(R.id.left_drawer);

      //  drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, navigationOptionList));
        drawerList.setAdapter(new NavDrawerListAdapter(this, navigationOptionList, navigationOptionListIcons));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout.setDrawerShadow(getResources().getDrawable(R.drawable.drawer_shadow), GravityCompat.START);

        frame = (FrameLayout) findViewById(R.id.content_frame);

        MaskFilter filter = new EmbossMaskFilter(new float[]{0f, -1.0f, 0.5f}, 0.8f, 15f, 1f);

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

            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                float divisor = 4.5f - getResources().getDisplayMetrics().density;
                float moveFactor = (drawerLayout.getWidth() * slideOffset)/divisor;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    frame.setTranslationX(moveFactor);
                }
                else
                {
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    frame.startAnimation(anim);

                    lastTranslate = moveFactor;
                }
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
                FragmentManager fragmentManager = getSupportFragmentManager();

                Fragment myFragment = fragmentManager.findFragmentByTag("ProfileFragment");
                if (myFragment == null || !myFragment.isVisible()) {
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "ProfileFragment").commit();

                    if (profileBundle != null) {
                        //Log.d("quizter", "SET ARGUMENTS");
                        fragment.setArguments(profileBundle);
                    }
                }
                drawerList.setItemChecked(position, true);
                setTitle(navigationOptionList[position]);
                drawerLayout.closeDrawer(drawerList);
                drawerList.setItemChecked(position, false);

                break;
            case 1:

                Fragment fragment1 = new QuizMeSelectionFragment();
                FragmentManager fragmentManager1 = getSupportFragmentManager();

                ProfileFragment profileFragment = (ProfileFragment) fragmentManager1.findFragmentByTag("ProfileFragment");
                if("true".equals(userData.getSetup_profile())) {
                    Fragment myFragment1 = fragmentManager1.findFragmentByTag("QuizMeSelectionFragment");
                    if (myFragment1 == null || !myFragment1.isVisible()) {
                        fragmentManager1.beginTransaction().replace(R.id.content_frame, fragment1, "QuizMeSelectionFragment").commit();
                    }

                } else {
                    View inflatedView = this.getLayoutInflater().inflate(R.layout.quizter_profile_not_set, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setView(inflatedView);
                    builder.setInverseBackgroundForced(true);
                    builder.setCancelable(true);

                    Button okBtn =(Button)inflatedView.findViewById(R.id.profile_setup_ok_btn);
                    okBtn.setText("Ok");

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.hide();
                            alertDialog.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.show();

                }
                drawerList.setItemChecked(position, true);
                setTitle(navigationOptionList[position]);
                drawerLayout.closeDrawer(drawerList);
                drawerList.setItemChecked(position, false);
                break;
            case 2:
                Fragment fragment2 = new LeaderboardFragment();
                FragmentManager fragmentManager2 = getSupportFragmentManager();

                Fragment myFragment2 = fragmentManager2.findFragmentByTag("LeaderboardFragment");
                if (myFragment2 == null || !myFragment2.isVisible()) {
                    fragmentManager2.beginTransaction().replace(R.id.content_frame, fragment2, "LeaderboardFragment").commit();
                }
                drawerList.setItemChecked(position, true);
                setTitle(navigationOptionList[position]);
                drawerLayout.closeDrawer(drawerList);
                drawerList.setItemChecked(position, false);

                break;
            case 3:

                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("plain/text");
                intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{"wiredin.dev@gmail.com"});
                intent1.putExtra(Intent.EXTRA_SUBJECT, "Quizter - Feedback");
                intent1.putExtra(Intent.EXTRA_TEXT, "Developer information: " + Build.VERSION.RELEASE + " " + Build.VERSION.SDK_INT + " " + Build.VERSION.INCREMENTAL + "\n\n");

                drawerList.setItemChecked(position, true);
                setTitle(navigationOptionList[position]);
                drawerLayout.closeDrawer(drawerList);
                drawerList.setItemChecked(position, false);

                startActivity(Intent.createChooser(intent1, ""));
                break;

            case 4:
                Session.getActiveSession().closeAndClearTokenInformation();
                Intent intent2 = new Intent(this, QuizterActivity.class);
                finish();
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
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
                QuestionModifyFragment questionModifyFragment = (QuestionModifyFragment)getSupportFragmentManager().findFragmentByTag("QuestionModifyFragment");
                if(questionModifyFragment.isVisible()) {
                    Log.d("Quizter", "back pressed now");
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
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


    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public void handleUnauthorizedError() {
        View inflatedView = this.getLayoutInflater().inflate(R.layout.quizter_profile_not_set, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflatedView);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(true);

        TextView  message = (TextView) inflatedView.findViewById(R.id.profile_setup_dialog_message);
        message.setText("Your login has expired.");

        Button okBtn =(Button)inflatedView.findViewById(R.id.profile_setup_ok_btn);
        okBtn.setText("Ok");

        Session.getActiveSession().closeAndClearTokenInformation();
        final Intent intent = new Intent(this, QuizterActivity.class);
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.cancel();
                startActivity(intent);
            }
        });

        alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }
}

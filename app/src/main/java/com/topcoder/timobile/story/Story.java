package com.topcoder.timobile.story;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.topcoder.timobile.LoginActivity;
import com.topcoder.timobile.R;
import com.topcoder.timobile.Utils;
import com.topcoder.timobile.WelcomeViewFlipper;
import com.topcoder.timobile.browse_story.BrowseStory;
import com.topcoder.timobile.help.Help;
import com.topcoder.timobile.points.Points;
import com.topcoder.timobile.profile.Profile;
import com.topcoder.timobile.rewards.Rewards;
import com.topcoder.timobile.storyContent.StoryContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Story extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarStory);
        setSupportActionBar(myToolbar);

        // TODO Due to lack of time, couldn't add its interface
//        startActivity(new Intent(this, StoryContent.class));

        SharedPreferences settings = getSharedPreferences(Utils.myPrefs, 0);
        if ( settings.getBoolean(Utils.newUser, true)) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (settings.getBoolean(Utils.firstRun, true)) {
            startActivity(new Intent(this, WelcomeViewFlipper.class));
        }


        // comments and card adapters
        ListView cardslistView = (ListView) findViewById(R.id.card_list_view);
        CustomCardAdapter customCardAdapter = new CustomCardAdapter(this, R.layout.custom_card_adapter,
                new ArrayList<CardDetailsModel>());

        try {
            JSONObject obj = new JSONObject(Utils.loadJSONFromAsset(this, "stories.json"));
            for (int i = 1; i <= obj.length(); ++i) {
                JSONObject storySelected = obj.getJSONObject(i + "");
                String title = storySelected.getString("title");
                String subtitle = storySelected.getString("subtitle");
                int cards = storySelected.getInt("cards");
                int chapters = storySelected.getJSONArray("chapters").length();
                customCardAdapter.add(new CardDetailsModel(subtitle, title, cards, chapters, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce luctus congue mauris"));
            }
        } catch (JSONException j) {
            j.printStackTrace();
        }

        cardslistView.setAdapter(customCardAdapter);
        cardslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), BrowseStory.class);
                intent.putExtra(getResources().getString(R.string.story_position), position);
                startActivity(intent);
            }
        });


        // Nav part starts
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_story);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        headerview.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_story_selection:
                startActivity(new Intent(this, Story.class));
                break;
            case R.id.nav_bookmarks:
                break;
            case R.id.nav_help:
                startActivity(new Intent(this, Help.class));
                break;
            case R.id.nav_logout:Utils.logout(getBaseContext());
                break;
            case R.id.nav_rewards:
                startActivity(new Intent(this, Rewards.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, Profile.class));
                break;
            case R.id.nav_points:
                startActivity(new Intent(this, Points.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_story);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

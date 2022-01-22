package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lostandfound.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    TextView timeline,search,profile,add,lost;
    ViewPager viewPager;
    PagerViewAdapter pagerViewAdapter;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getIntent = getIntent();
        int p = getIntent.getIntExtra("position",0);


        mFirebaseAuth = FirebaseAuth.getInstance();
        timeline = findViewById(R.id.recent_tab);
        search =findViewById(R.id.search_tab);
        profile = findViewById(R.id.profile_tab);
        add = findViewById(R.id.add_lost_tab);
        lost=findViewById(R.id.lost_tab);
        viewPager =findViewById(R.id.fragment_container);


        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerViewAdapter);
        viewPager.setCurrentItem(p);
        onChangeTab(p);

        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        }

        private void onChangeTab(int position){
            if(position == 0){

                timeline.setTextSize(15);
                timeline.setTextColor(getColor(R.color.dark));

                lost.setTextSize(10);
                lost.setTextColor(getColor(R.color.bright));

                search.setTextSize(10);
                search.setTextColor(getColor(R.color.bright));

                add.setTextSize(10);
                add.setTextColor(getColor(R.color.bright));

                profile.setTextSize(10);
                profile.setTextColor(getColor(R.color.bright));
            }

            if(position == 1){

                timeline.setTextSize(10);
                timeline.setTextColor(getColor(R.color.bright));

                lost.setTextSize(15);
                lost.setTextColor(getColor(R.color.dark));

                search.setTextSize(10);
                search.setTextColor(getColor(R.color.bright));

                add.setTextSize(10);
                add.setTextColor(getColor(R.color.bright));

                profile.setTextSize(10);
                profile.setTextColor(getColor(R.color.bright));
            }

            if(position == 2){

                timeline.setTextSize(10);
                timeline.setTextColor(getColor(R.color.bright));

                lost.setTextSize(10);
                lost.setTextColor(getColor(R.color.bright));

                search.setTextSize(15);
                search.setTextColor(getColor(R.color.dark));

                add.setTextSize(10);
                add.setTextColor(getColor(R.color.bright));

                profile.setTextSize(10);
                profile.setTextColor(getColor(R.color.bright));
            }

            if(position == 3){

                timeline.setTextSize(10);
                timeline.setTextColor(getColor(R.color.bright));

                lost.setTextSize(10);
                lost.setTextColor(getColor(R.color.bright));

                search.setTextSize(10);
                search.setTextColor(getColor(R.color.bright));

                add.setTextSize(15);
                add.setTextColor(getColor(R.color.dark));

                profile.setTextSize(10);
                profile.setTextColor(getColor(R.color.bright));
            }

            if(position == 4){

                timeline.setTextSize(10);
                timeline.setTextColor(getColor(R.color.bright));

                lost.setTextSize(10);
                lost.setTextColor(getColor(R.color.bright));

                search.setTextSize(10);
                search.setTextColor(getColor(R.color.bright));

                add.setTextSize(10);
                add.setTextColor(getColor(R.color.bright));

                profile.setTextSize(15);
                profile.setTextColor(getColor(R.color.dark));
            }

        }
    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent i = new Intent(MainActivity.this,Login.class);
            startActivity(i);
        }
    }
}

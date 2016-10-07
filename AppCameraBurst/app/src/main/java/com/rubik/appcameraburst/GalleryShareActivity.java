package com.rubik.appcameraburst;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rubik.appcameraburst.main.Fragment_Recycler.RecyclerView_Fragment;
import com.rubik.appcameraburst.main.ViewPagerAdapter;

public class GalleryShareActivity extends AppCompatActivity {

    private static ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);//Set up View Pager

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager

       // Snackbar.make(toolbar, "Long press for actions", Snackbar.LENGTH_LONG).show();
    }

        //Setting View Pager
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RecyclerView_Fragment(), "Gallery of captured images");
        viewPager.setAdapter(adapter);
    }


        //Return current fragment
    public Fragment getFragment(int pos) {
        return adapter.getItem(pos);
    }


}

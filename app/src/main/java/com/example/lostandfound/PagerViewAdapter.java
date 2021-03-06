package com.example.lostandfound;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null ;

        switch (position){

            case 0:
                fragment = new Fragment_Timeline();
                break;
            case 1:
                fragment = new Fragment_Lost();
                break;
            case 2:
                fragment = new Fragment_Search();
                break;
            case 3:
                fragment = new Fragment_Add();
                break;
            case 4:
                fragment = new Fragment_Profile();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }
}

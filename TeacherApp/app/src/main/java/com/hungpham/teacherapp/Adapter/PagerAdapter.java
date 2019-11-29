package com.hungpham.teacherapp.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter {
  private int mNoOfTabs;
  private Context context;

  public PagerAdapter(FragmentManager fm, int NumberOfTabs, Context context)
  {
    super(fm);
    this.mNoOfTabs = NumberOfTabs;
    this.context=context;
  }


  @Override
  public Fragment getItem(int position) {

    Fragment fragment = null;
    switch(position)
    {

      case 0:
       // fragment  = new OrderFragment(context);

        break;
      //case 1:
        //fragment = new GeneratorQRFragment(context);
       // break;
      default:
        return null;
    }
    return fragment;
  }
  @Override
  public int getCount() {
    return mNoOfTabs;
  }

}

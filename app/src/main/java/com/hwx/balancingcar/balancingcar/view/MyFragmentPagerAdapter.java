package com.hwx.balancingcar.balancingcar.view;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{

	private List<String> mTitles;
	private List<Fragment> mFragments;

	public MyFragmentPagerAdapter(FragmentManager fm,  List<Fragment> mFragments) {
		super(fm);
		this.mFragments = mFragments;
	}


	public List<Fragment> getmFragments() {
		return mFragments;
	}

	public void setmFragments(List<Fragment> mFragments) {
		this.mFragments = mFragments;
	}

	public List<String> getmTitles() {
		return mTitles;
	}

	public void setmTitles(List<String> mTitles) {
		this.mTitles = mTitles;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles.get(position);
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

}

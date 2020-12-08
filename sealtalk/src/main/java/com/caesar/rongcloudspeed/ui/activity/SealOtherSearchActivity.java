package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.text.TextWatcher;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.ui.fragment.SearchOtherBaseFragment;

public class SealOtherSearchActivity extends SealSearchBaseActivity implements TextWatcher {
    private static final String TAG = "SealOtherSearchActivity";
    private SearchOtherBaseFragment currentFragment; //当前Fragment

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type=getIntent().getIntExtra("type",0);
        currentFragment = new SearchOtherBaseFragment();
        currentFragment.init(type);
        pushFragment(currentFragment);
    }

    public void search(String search) {
        currentFragment.search(search);
    }

    @Override
    public void clear() {
        currentFragment.clear();
    }

    private void pushFragment(SearchOtherBaseFragment fragment) {
        currentFragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_content_fragment, currentFragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
        search(search);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 1) {
            //只有searchAllFragment
            finish();
        } else {
            super.onBackPressed();
            getTitleBar().getEtSearch().setText(currentFragment.getInitSearch());
        }
    }
}

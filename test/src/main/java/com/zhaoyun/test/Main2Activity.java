package com.zhaoyun.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 Why did not writer complain to the people behind him?
 Last week I want to the theatre.
 I had a very good seat.
 The play was very interesting.
 I did not enjoy it.
 A young man and a young women were sitting behind me.
 They were talking loudly.
 I got very angry.
 I cloud not hear the actors.
 I turned round.
 I looked at the man and the women angrily.
 They did not pay any attention.
 In the end,I cloud not bear it.
 I turned round again.
 'I can't hear a word',I said angrily.
 'It's none of your business!',the young man said rudely.
 'This is a private conversation.'
 */
public class Main2Activity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            Fragment firstFragment = new HeadlinesFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, firstFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
    }
}

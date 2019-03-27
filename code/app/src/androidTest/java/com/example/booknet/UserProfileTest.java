package com.example.booknet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;



import org.junit.Test;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class UserProfileTest {

    @Before
    /**
     * Navigate to the UserProfile UI.
     */
    public void getToUserProfile(){

    }

    @Test
    /**
     *  Make sure that the profile logged in to is the one displayed.
     */
    public void verifyProfile(){

    }

    @Test
    /**
     *  Edit the profile, and make sure the edits take place properly.
     */
    public void editProfile(){

    }

}

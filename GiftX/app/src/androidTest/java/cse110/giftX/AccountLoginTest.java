package cse110.giftx;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;

import cse110.giftx.ui.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by AJ on 3/8/16.
 */
public class AccountLoginTest {

    private String email = "notarealemail@emailll.com";
    private String password = "password";

    private String goodEmail = "adejesus@ucsd.edu";
    private String goodPassword = "password";

    public ActivityTestRule<LoginActivity> mActivityTestRule =
            new ActivityTestRule<LoginActivity>(LoginActivity.class);
    private LoginActivity loginActivity;

    @Before
    public void setUp() {

        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);
        loginActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void test1BadLogin() throws Exception {
          Thread.sleep(2000);

        // Try to login with fake credentials
        onView(withId(R.id.edit_text_email))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login_with_password))
                .perform(click())
                // Make sure the login_with_password button is displayed
                // indicates user is still on login screen
                .check(matches(isDisplayed()));
    }

    @Test
    public void test2ValidLogin() throws Exception {
        Thread.sleep(2000);

        // Try to login with fake credentials
        onView(withId(R.id.edit_text_email))
                .perform(typeText(goodEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(goodPassword), closeSoftKeyboard());
        onView(withId(R.id.login_with_password))
                .perform(click());
        // Make sure pager is displayed
        // indicates user is on display groups page
        onView(withId(R.id.pager))
                .check(matches(isDisplayed()));
    }


}

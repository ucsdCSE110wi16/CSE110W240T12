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
public class CreateAccountTest {

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
    public void test1CreateMismatchAccount() {
        onView(withId(R.id.tv_sign_up)).perform(click());

        onView(withId(R.id.edit_text_first_name_create))
                .perform(typeText("John"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_user_last_name_create))
                .perform(typeText("Smith"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_email_create))
                .perform(typeText("JohnSmithattestEMAIL.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password_create))
                .perform(typeText("password111"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_confirm_password_create))
                .perform(typeText("password111"), closeSoftKeyboard());
        onView(withId(R.id.btn_create_account_final))
                .perform(click())
                        // Make sure we're still on the same page
                .check(matches(isDisplayed()));
    }

    @Test
    public void test2CreateShortPasswordAccount() {
        onView(withId(R.id.tv_sign_up)).perform(click());

        onView(withId(R.id.edit_text_first_name_create))
                .perform(typeText("John"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_user_last_name_create))
                .perform(typeText("Smith"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_email_create))
                .perform(typeText("JohnSmith@testEMAIL.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password_create))
                .perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_confirm_password_create))
                .perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.btn_create_account_final))
                .perform(click())
                        // Make sure we're still on the same page
                .check(matches(isDisplayed()));
    }

    @Test
    public void test3CreateGoodAccount() {
        onView(withId(R.id.tv_sign_up)).perform(click());

        onView(withId(R.id.edit_text_first_name_create))
                .perform(typeText("Espresso"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_user_last_name_create))
                .perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_email_create))
                .perform(typeText("EspressoTest@TestEmail.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password_create))
                .perform(typeText("password111"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_confirm_password_create))
                .perform(typeText("password111"), closeSoftKeyboard());
        onView(withId(R.id.btn_create_account_final))
                .perform(click())
                .check(matches(isDisplayed()));
    }

}

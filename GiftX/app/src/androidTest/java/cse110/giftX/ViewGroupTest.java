package cse110.giftX;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;

import cse110.giftX.ui.login.LoginActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by AJ on 3/8/16.
 */
public class ViewGroupTest {
    private String goodEmail = "arthur@test.com";
    private String goodPassword = "password";

    public ActivityTestRule<LoginActivity> mActivityTestRule =
            new ActivityTestRule<LoginActivity>(LoginActivity.class);
    private LoginActivity loginActivity;

    @Before
    public void setUp() {

        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);
        loginActivity = mActivityTestRule.getActivity();

        // Try to login with fake credentials
        onView(withId(R.id.edit_text_email))
                .perform(typeText(goodEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(goodPassword), closeSoftKeyboard());
        onView(withId(R.id.login_with_password))
                .perform(click());
    }

    @Test
    public void testViewMyGroup() throws InterruptedException {
        onView(withId(R.id.list_view_active_groups))
                .perform(click());
        onView(withId(R.id.title_days_until_sort))
                .check(matches(isDisplayed()));
        pressBack();
        Thread.sleep(2000);
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onData(hasToString(startsWith("Logout")))
                .perform(click());
    }
}

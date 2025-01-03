package com.example.teachingapp.user;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.R;
import com.example.teachingapp.User._RegistrationForm;
import com.example.teachingapp.User._RegistrationName;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class RegistrationNameTest {

    @Rule
    public ActivityScenarioRule<_RegistrationName> activityScenarioRule =
            new ActivityScenarioRule<>(_RegistrationName.class);

    @Test
    public void testNameValidation() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            EditText nameText = activity.findViewById(R.id.name);

            nameText.setText("12");
            assertFalse(activity.validateName(nameText, "Imię"));
            assertThat(nameText.getError(), is("Imię powinno zawierać tylko polskie znaki, spacje i -"));
            nameText.setError(null);

            nameText.setText("-Szymon");
            assertFalse(activity.validateName(nameText, "Imię"));
            assertThat(nameText.getError(), is("Imię powinno zawierać tylko polskie znaki, spacje i -"));
            nameText.setError(null);

            nameText.setText("Szymon@");
            assertFalse(activity.validateName(nameText, "Imię"));
            assertThat(nameText.getError(), is("Imię powinno zawierać tylko polskie znaki, spacje i -"));
            nameText.setError(null);

            nameText.setText("Szymon-Michał");
            assertTrue(activity.validateName(nameText, "Imię"));
            assertNull(nameText.getError());
        });
    }

    @Test
    public void testReturnButton() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        Intents.init();

        onView(withId(R.id.return_button)).check(matches(isDisplayed()));
        onView(withId(R.id.return_button)).perform(click());

        intended(hasComponent(_RegistrationForm.class.getName()));

        Intents.release();
    }
}

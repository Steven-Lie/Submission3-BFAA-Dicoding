package com.dicoding.githubusers

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.githubusers.ui.main.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val dummySearch = "user kosong"

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun noResultAppearance(){
        onView(withId(R.id.search_view)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_users_search)).check(matches(isDisplayed()))

        onView(withId(R.id.search_view)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText(dummySearch), closeSoftKeyboard())
        onView(withId(R.id.tv_no_result_search)).check(matches(isDisplayed()))
    }
}
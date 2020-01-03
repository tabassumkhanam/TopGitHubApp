package com.appstreet.topgithubapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.appstreet.topgithubapp.view.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecyclerViewTest {

    @get:Rule
    var activityActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)


    @Test
    fun recyclerViewScrollTest() {
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.rvTrendingDevelopers)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
    }


    @Test
    fun recyclerViewClickTest() {
        Thread.sleep(1500)

        Espresso.onView(ViewMatchers.withId(R.id.rvTrendingDevelopers)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )

        Thread.sleep(1500)
        Espresso.pressBack()

        Espresso.onView(ViewMatchers.withId(R.id.rvTrendingDevelopers)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                ViewActions.click()
            )
        )

        Thread.sleep(1500)
        Espresso.pressBack()

        Espresso.onView(ViewMatchers.withId(R.id.rvTrendingDevelopers)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                ViewActions.click()
            )
        )

        Thread.sleep(1500)
    }
}
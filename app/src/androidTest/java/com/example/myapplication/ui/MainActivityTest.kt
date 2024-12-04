package com.example.myapplication.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import com.example.myapplication.R
import com.example.myapplication.reg_auth.Authtorization
import org.junit.Rule
import org.junit.Test

class MainActivityTest{

    @get:Rule var activityScenarioRule = activityScenarioRule<Authtorization>()

    @Test
    fun checkNumberServicesBasket(){
        Thread.sleep(5000)
        onView(withId(R.id.size_basket))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkClickNavigationView(){
        Thread.sleep(5000)
        onView(withId(R.id.navigation_cars))
            .perform(click())

        onView(withId(R.id.title)).check(matches(withText("Машины")))
    }

    @Test
    fun openLeftMenu(){
        Thread.sleep(5000)
        onView(withId(R.id.user))
            .perform(click())
    }
}
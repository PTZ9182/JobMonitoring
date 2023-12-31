package org.d3ifcool.jobmonitoring.ui.loginRegister


import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.d3ifcool.jobmonitoring.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginPerusahaanFragmentTest : TestCase() {

    private lateinit var scenario: FragmentScenario<LoginPerusahaanFragment>

    @Before
    fun setup(){
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_JobMonitoring)
        scenario.moveToState(Lifecycle.State.STARTED)

    }

    @Test
    fun testAddDivisi(){
        val email = "vilboins@gmail.com"
        val password = "vilboins123"
        onView(withId(R.id.lp_isiform_email)).perform(typeText(email))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.lp_isiform_password)).perform(typeText(password))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.lp_button_login)).perform(click())
        
    }
}

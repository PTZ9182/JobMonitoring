package org.d3ifcool.jobmonitoring.ui.loginRegister


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.fragment.app.testing.launchFragmentInContainer
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.model.PerusahaanModel
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginPerusahaanFragmentTest  {

    companion object {
        private val MAHASISWA_DUMMY = PerusahaanModel(
            "1", "Goreng Soang", "suryana@gmail.com",
            "suryana123","Bandung, Jawa barat", "082128351092",
            "0"
        )
    }
    @Test
    fun testInsert() {

        val activityScenario = launchFragmentInContainer<LoginPerusahaanFragment>()
        onView(withId(R.id.lp_isiform_email)).perform(
            typeText(MAHASISWA_DUMMY.email))
        onView(withId(R.id.lp_isiform_password)).perform(
            typeText(MAHASISWA_DUMMY.password))
        onView(withText(R.string.lp_button_login)).perform(click())

        onView(withText(MAHASISWA_DUMMY.email)).check(matches(isDisplayed()))
        onView(withText(MAHASISWA_DUMMY.password)).check(matches(isDisplayed()))

        activityScenario.close()

    }
}

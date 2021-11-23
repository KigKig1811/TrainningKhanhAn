package com.vnpay.anlmk

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.vnpay.anlmk.ui.activities.splash.SplashActivity
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {

    @Rule
    var activityRule: ActivityTestRule<SplashActivity> =
        ActivityTestRule(SplashActivity::class.java, false, false)

    @Before
    fun init() {
    }

    @Test
    fun testAPI() {
        activityRule.activity.model.login("admin", "admin")
        assertEquals("com.example.testapp", activityRule.activity.packageName)

    }

    @Test
    fun useAppContext() {
        val scenario = launch(SplashActivity::class.java)

        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.moveToState(Lifecycle.State.RESUMED)
        assertEquals("com.example.testapp", activityRule.activity.packageName)

    }
}

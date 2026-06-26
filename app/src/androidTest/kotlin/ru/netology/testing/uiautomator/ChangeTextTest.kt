package ru.netology.testing.uiautomator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import android.content.Intent


const val SETTINGS_PACKAGE = "com.android.settings"
const val MODEL_PACKAGE = "ru.netology.testing.uiautomator"

const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class ChangeTextTest {
    private lateinit var device: UiDevice
    private val textToSet = "Netology"
    private fun waitForPackage(packageName: String) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(packageName)), TIMEOUT)
    }
    private fun findObjectById(id: String) =
        device.wait(Until.findObject(By.res(MODEL_PACKAGE, id)), TIMEOUT)

    @Before
    fun beforeEachTest() {
        // Press home
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()

        // Wait for launcher
        val launcherPackage = device.launcherPackageName
        device.wait(Until.hasObject(By.pkg(launcherPackage)), TIMEOUT)
    }

    @Test
    fun testInternetSettings() {
        waitForPackage(SETTINGS_PACKAGE)

        device.findObject(
            UiSelector().resourceId("android:id/title").instance(0)
        ).click()
    }

    @Test
    fun testChangeText() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)

        findObjectById("userInput").text = textToSet
        findObjectById("buttonChange").click()

        val result = findObjectById("textToBeChanged").text

        assertEquals(textToSet, result)
    }

    @Test
    fun testEmptyTextDoesNotChangeTextView() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)

        val textBefore = findObjectById("textToBeChanged").text

        findObjectById("userInput").text = "   "
        findObjectById("buttonChange").click()

        val textAfter = findObjectById("textToBeChanged").text

        assertEquals(textBefore, textAfter)
    }

    @Test
    fun testOpenTextInNewActivity() {
        val packageName = MODEL_PACKAGE
        val textToOpen = "Netology Activity"

        waitForPackage(packageName)

        findObjectById("userInput").text = textToOpen
        findObjectById("buttonActivity").click()

        val result = findObjectById("text").text

        assertEquals(textToOpen, result)
    }

}




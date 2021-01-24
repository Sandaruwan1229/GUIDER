package com.SEproject.guiderapp.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.SEproject.guiderapp.R
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage

/**
 * An introductory activity that explains the method of use of the app
 */
class IntroActivity : AppIntro() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this@IntroActivity)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            preferences.edit().putBoolean(getString(R.string.pref_overlay_permission_update), true).apply()
        }

        mCallingClass = ""
        if (intent.hasExtra(CALLING_CLASS_KEY)) {
            mCallingClass = intent.getStringExtra(CALLING_CLASS_KEY)
            if (mCallingClass == "SettingsFragment") {
                preferences.edit().putBoolean(getString(R.string.pref_display_tap_target_apps), true).apply()

                preferences.edit().putBoolean(getString(R.string.pref_display_tap_target_time_dialog), true).apply()
            }
        }

        val introMode = intent.getStringExtra(getString(R.string.intro_activity_mode))
        if (introMode == getString(R.string.intro_activity_mode_tutorial)) {
            launchTutorialMode()
        } else if (introMode == getString(R.string.intro_activity_mode_overlay_update)) {
            launchOverlayMode()
        }


    }

    fun launchOverlayMode() {

        val donePage = SliderPage()
        donePage.apply {

            title = getString(R.string.intro_page5_title)
            description = getString(R.string.intro_page5_desc)
            imageDrawable = R.drawable.ic_check_circle_big_green_128dp
            bgColor = resources.getColor(R.color.colorPrimaryDark)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            addSlide(OverlayPermissionSlide.newInstance(R.layout.fragment_overlay_permission_slide))
        }
        addSlide(AppIntroFragment.newInstance(donePage))

        isProgressButtonEnabled = true
        showSeparator(false)
        showStatusBar(false)
        setFadeAnimation()
    }

    fun launchTutorialMode() {
        val page1 = SliderPage()
        page1.title = getString(R.string.app_name)
        page1.description = getString(R.string.intro_page1_desc)
        page1.bgColor = resources.getColor(R.color.colorPrimaryDark)
        page1.imageDrawable = R.drawable.app_icon_large

        val page2 = SliderPage()
        page2.title = getString(R.string.intro_page5_title)
        page2.description = getString(R.string.intro_page5_desc)
        page2.imageDrawable = R.drawable.ic_check_circle_big_green_128dp
        page2.bgColor = resources.getColor(R.color.colorPrimaryDark)

        addSlide(AppIntroFragment.newInstance(page1))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addSlide(UsagePermissionSlide.newInstance(R.layout.fragment_usage_permission_slide))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            addSlide(OverlayPermissionSlide.newInstance(R.layout.fragment_overlay_permission_slide))
        }
        addSlide(AppIntroFragment.newInstance(page2))

        showSkipButton(false)
        isProgressButtonEnabled = true
        showSeparator(false)
        showStatusBar(false)
        setFadeAnimation()

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        getPager().currentItem = 2

    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putBoolean(getString(R.string.tutorial_seen_key), true).apply()

        val mainIntent = Intent(this, MainActivity::class.java)

        // Display tutorials preference has been selected from preference screen
        if (mCallingClass == "SettingsFragment") {
            // Finish all activities
            finishAffinity()
            startActivity(mainIntent)
        } else {
            startActivity(mainIntent)
            finish()
        }//Default behavior

    }

    companion object {
        private val CALLING_CLASS_KEY = "calling_class"
        private var mCallingClass: String? = null
    }

}
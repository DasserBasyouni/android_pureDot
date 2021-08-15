package com.g7.soft.pureDot.ui.screen

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ActivityMainBinding
import com.g7.soft.pureDot.ui.screen.home.HomeFragment
import com.g7.soft.pureDot.util.UiUtils
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity


class MainActivity : LocaleAwareCompatActivity() {

    internal lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration // with this or without menu+upButton bug still the same so we can remove the line?


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setTheme(R.style.Theme_PureDot)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.collapsingToolbarLayout.setupWithNavController(binding.toolbar, navController, appBarConfiguration)
        /*NavigationUI.setupActionBarWithNavController(
            this@MainActivity,
            navController,
            appBarConfiguration
        )*/

        // fix for insets, issue: https://github.com/material-components/material-components-android/issues/1310
        ViewCompat.setOnApplyWindowInsetsListener(binding.collapsingToolbarLayout, null)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        navController.addOnDestinationChangedListener { controller, destination, _ ->

            // an un-clean fix for screen title when having only toolbar without collapsingToolbar
            binding.toolbarTitle.text = destination.label

            UiUtils.setupToolNavBars(
                destination.id,
                this,
                scrollLockedDestinationIds = arrayListOf(R.id.homeFragment),
                fullScreenDestinationIds = arrayListOf(R.id.startFragment, R.id.homeFragment),
                loginDestinationIds = arrayListOf(R.id.loginFragment),
                transparentDestinationIds = arrayListOf(
                    R.id.forgetPasswordFragment,
                    R.id.phoneVerificationFragment,
                    R.id.contactUsFragment,
                    R.id.notificationFragment,
                    R.id.myOrdersFragment,
                    R.id.orderFragment,
                    R.id.settingsFragment,
                    R.id.aboutUsFragment,
                    R.id.termsFragment,
                    R.id.customerServiceFragment,
                    R.id.complainFragment,
                    R.id.workingHoursFragment,
                    R.id.myWalletFragment,
                    R.id.bankAccountFragment,
                    R.id.policyFragment,
                    R.id.changeLanguageFragment,
                ),
                collapsingToolBarTitleDestinationIds = arrayListOf(R.id.signUpFragment),
                collapsingHomeDestinationIds = arrayListOf(),
                bottomNavBarDestinationIds = arrayListOf(),
                partialGradientDestinationIds = arrayListOf(
                    R.id.profileFragment,
                    R.id.profileEditFragment,
                ),
            )
            // todo delete those?
            binding.root.requestLayout()
            binding.appBarLayout.requestLayout()
            binding.toolbar.requestLayout()
            binding.collapsingToolbarLayout.requestLayout()
            binding.collapsingToolbarLayout.requestLayout()
           // binding.nestedScrollView.requestLayout()
            binding.navHostFragment.requestLayout()
            binding.bottomNavigation.requestLayout()

/*
            // setup after destination change message
            //val previousDestinationId = controller.previousBackStackEntry?.destination?.id
            //val isNavigatingForward: Boolean = viewModel.lastBackStackSize.value ?: 0 < controller.backStack.size
            val msgResIdToShow = if (true/*isNavigatingForward*/) when {
                previousDestinationId == R.id.forgetPasswordFragment
                        && destination.id == R.id.verificationCodeFragment -> R.string.msg_code_is_sent
                previousDestinationId == R.id.resetPasswordFragment
                        && destination.id == R.id.loginFragment -> R.string.msg_password_changed
                previousDestinationId == R.id.verificationCodeFragment
                        && destination.id == R.id.loginFragment -> R.string.msg_account_created
                /*fromDestinationId == R.id.signUpFragment
                        && destination.id == R.id.homeFragment -> R.string.msg_account_created*/
                else -> null
            } else null
            msgResIdToShow?.let { ProjectDialogUtils.showSimpleMessage(this, it) }*/
            //viewModel.lastBackStackSize.value = controller.backStack.size
            //title = "test"
        }
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.navHostFragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        item.onNavDestinationSelected(findNavController(R.id.navHostFragment))
                || super.onOptionsItemSelected(item)

    override fun onBackPressed() {
        if (findNavController(R.id.navHostFragment).currentDestination?.id == R.id.homeFragment) {
            val currentFragment =
                supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.first()
            (currentFragment as HomeFragment).doBackPressed()
        } else
            super.onBackPressed()
    }

    fun superOnBackPressed() = super.onBackPressed()
}
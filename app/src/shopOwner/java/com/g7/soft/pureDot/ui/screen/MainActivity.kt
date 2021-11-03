package com.g7.soft.pureDot.ui.screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant.OrderStatus.Companion.isCancelable
import com.g7.soft.pureDot.databinding.ActivityMainBinding
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.ui.screen.filter.FilterViewModel
import com.g7.soft.pureDot.ui.screen.order.OrderFragment
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.g7.soft.pureDot.utils.UiUtils
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity


class MainActivity : LocaleAwareCompatActivity() {

    companion object {
        private const val NAV_ID_HOME = 1
        private const val NAV_ID_ORDERS = 2
        private const val NAV_ID_NOTIFICATION = 3
        private const val NAV_ID_ACCOUNT = 4
    }

    internal lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration // with this or without menu+upButton bug still the same so we can remove the line?
    internal val filterViewModel: FilterViewModel by viewModels()
    private var firstTimeExitPopup = true


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_PureDot)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(
            this@MainActivity,
            navController,
            appBarConfiguration
        )

        // fix for insets, issue: https://github.com/material-components/material-components-android/issues/1310
        ViewCompat.setOnApplyWindowInsetsListener(binding.collapsingToolbarLayout, null)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        navController.addOnDestinationChangedListener { _, destination, bundle ->

            // an un-clean fix for screen title when having only toolbar without collapsingToolbar
            binding.toolbarTitle.text = destination.label

            UiUtils.setupToolNavBars(
                destination.id,
                this,
                scrollLockedDestinationIds = arrayListOf(),
                fullScreenDestinationIds = arrayListOf(R.id.splashFragment),
                loginDestinationIds = arrayListOf(R.id.loginFragment),
                transparentDestinationIds = arrayListOf(
                    R.id.filterFragment,
                    R.id.homeFragment,
                    R.id.forgetPasswordFragment,
                    R.id.phoneVerificationFragment,
                    R.id.filterFragment,
                    R.id.myOrdersFragment,
                    R.id.orderFragment,
                    R.id.notificationFragment,
                    R.id.customerServiceFragment,
                    R.id.complainFragment,
                    R.id.myWalletFragment,
                    R.id.contactUsFragment,
                    R.id.aboutUsFragment,
                    R.id.policyFragment,
                    R.id.termsFragment,
                    R.id.submitComplainFragment,
                    R.id.changeLanguageFragment,
                    R.id.changePasswordFragment,
                ),
                collapsingToolBarTitleDestinationIds = arrayListOf(),
                collapsingHomeDestinationIds = arrayListOf(),
                bottomNavBarDestinationIds = arrayListOf(
                    R.id.homeFragment,
                    R.id.myOrdersFragment,
                    R.id.notificationFragment,
                    R.id.myAccountFragment,
                ),
                partialGradientDestinationIds = arrayListOf(
                    R.id.myAccountFragment
                )
            )
            setupToolbarCancelButton(destination, bundle)
            setupSelectedNabBarIcon(destination.id)

            // un clean fix
            if (destination.id == R.id.homeFragment) {
                supportActionBar?.setDisplayShowHomeEnabled(false)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }

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
        }

        // setup bottom nav bar
        binding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                NAV_ID_HOME, R.drawable.ic_home, getString(R.string.home)
            )
        )
        binding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                NAV_ID_ORDERS, R.drawable.ic_basket, getString(R.string.orders)
            )
        )
        binding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                NAV_ID_NOTIFICATION, R.drawable.ic_notification, getString(R.string.notification)
            )
        )
        binding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                NAV_ID_ACCOUNT, R.drawable.ic_account, getString(R.string.account)
            )
        )

        binding.bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                NAV_ID_HOME ->
                    findNavController(R.id.navHostFragment).navigate(R.id.homeFragment)
                NAV_ID_ORDERS ->
                    findNavController(R.id.navHostFragment).navigate(R.id.myOrdersFragment)
                NAV_ID_NOTIFICATION ->
                    findNavController(R.id.navHostFragment).navigate(R.id.notificationFragment)
                NAV_ID_ACCOUNT ->
                    findNavController(R.id.navHostFragment).navigate(R.id.myAccountFragment)
            }
        }
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.navHostFragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        item.onNavDestinationSelected(findNavController(R.id.navHostFragment))
                || super.onOptionsItemSelected(item)

    override fun onBackPressed() {
        /*if (findNavController(R.id.navHostFragment).currentDestination?.id == R.id.checkoutFragment) {
            val currentFragment =
                supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.first()
            (currentFragment as CheckoutFragment).doBackPressed()
        } else*/
        when {
            firstTimeExitPopup && findNavController(R.id.navHostFragment).currentDestination?.id == R.id.homeFragment -> {
                firstTimeExitPopup = false
                ProjectDialogUtils.onExitApp(this) { firstTimeExitPopup = true }
            }
            else -> super.onBackPressed()
        }
    }

    fun superOnBackPressed() = super.onBackPressed()


    private fun setupToolbarCancelButton(destination: NavDestination, bundle: Bundle?) {
        when (destination.id) {
            R.id.orderFragment -> {
                val order = bundle?.getParcelable<OrderModel>("order")
                if (isCancelable(order?.status)) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.cancelBtn.apply {
                            val fragment = supportFragmentManager.primaryNavigationFragment
                                ?.childFragmentManager?.fragments?.first() as OrderFragment
                            this.setOnClickListener { fragment.cancelOrder() }
                            this.visibility = View.VISIBLE
                        }
                    }, 1000)
                }
            }
            /*R.id.trackOrderFragment -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cancelBtn.apply {
                        val fragment = supportFragmentManager.primaryNavigationFragment
                            ?.childFragmentManager?.fragments?.first() as TrackOrderFragment
                        this.setOnClickListener { fragment.cancelOrder() }
                        this.visibility = View.VISIBLE
                    }
                }, 1000)
            }*/
            else -> binding.cancelBtn.visibility = View.GONE
        }
    }

    private fun setupSelectedNabBarIcon(destinationId: Int) {
        val menuItemId = when (destinationId) {
            R.id.homeFragment -> NAV_ID_HOME
            R.id.myOrdersFragment -> NAV_ID_ORDERS
            R.id.notificationFragment -> NAV_ID_NOTIFICATION
            R.id.myAccountFragment -> NAV_ID_ACCOUNT
            else -> null
        }
        menuItemId?.let { binding.bottomNavigation.show(it, true) }
    }
}
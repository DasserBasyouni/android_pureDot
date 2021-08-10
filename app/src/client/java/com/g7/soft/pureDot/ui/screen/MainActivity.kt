package com.g7.soft.pureDot.ui.screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
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
import com.g7.soft.pureDot.ui.screen.checkout.CheckoutFragment
import com.g7.soft.pureDot.ui.screen.order.OrderFragment
import com.g7.soft.pureDot.util.UiUtils
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity


class MainActivity : LocaleAwareCompatActivity() {

    companion object {
        private const val NAV_ID_HOME = 1
        private const val NAV_ID_SERVICES = 2
        private const val NAV_ID_BROWSE = 3
        private const val NAV_ID_CART = 4
        private const val NAV_ID_ACCOUNT = 5
    }

    internal lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration // with this or without menu+upButton bug still the same so we can remove the line?


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
                scrollLockedDestinationIds = arrayListOf(R.id.addressFragment),
                fullScreenDestinationIds = arrayListOf(R.id.startFragment, R.id.addressFragment),
                loginDestinationIds = arrayListOf(R.id.loginFragment),
                transparentDestinationIds = arrayListOf(
                    R.id.forgetPasswordFragment,
                    R.id.phoneVerificationFragment,
                    R.id.filterFragment,
                    R.id.allProductsFragment,
                    R.id.allCategoriesFragment,
                    R.id.browseFragment,
                    R.id.allStoresFragment,
                    R.id.storeFragment,
                    R.id.productFragment,
                    R.id.categoryFragment,
                    R.id.allReviewsFragment,
                    R.id.cartFragment,
                    R.id.checkoutFragment,
                    R.id.myOrdersFragment,
                    R.id.orderFragment,
                    R.id.servicesFragment,
                    R.id.trackOrderFragment,
                    R.id.notificationFragment,
                    R.id.favouritesFragment,
                    R.id.customerServiceFragment,
                    R.id.complainFragment,
                    R.id.myWalletFragment,
                    R.id.transferMoneyFragment,
                    R.id.contactUsFragment,
                    R.id.aboutUsFragment,
                    R.id.policyFragment,
                    R.id.termsFragment,
                    R.id.submitComplainFragment,
                    R.id.serviceFragment,
                    R.id.changeLanguageFragment,
                ),
                collapsingToolBarTitleDestinationIds = arrayListOf(R.id.signUpFragment),
                collapsingHomeDestinationIds = arrayListOf(R.id.homeFragment),
                bottomNavBarDestinationIds = arrayListOf(
                    R.id.homeFragment,
                    R.id.servicesFragment,
                    R.id.browseFragment,
                    R.id.cartFragment,
                    R.id.myAccountFragment,
                ),
                partialGradientDestinationIds = arrayListOf(
                    R.id.myAccountFragment
                )
            )
            setupToolbarCancelButton(destination, bundle)
            setupSelectedNabBarIcon(destination.id)

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
                NAV_ID_SERVICES, R.drawable.ic_services, getString(R.string.services)
            )
        )
        binding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                NAV_ID_BROWSE, R.drawable.ic_basket, getString(R.string.browse)
            )
        )
        binding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                NAV_ID_CART, R.drawable.ic_cart, getString(R.string.cart)
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
                NAV_ID_SERVICES ->
                    findNavController(R.id.navHostFragment).navigate(R.id.servicesFragment)
                NAV_ID_BROWSE ->
                    findNavController(R.id.navHostFragment).navigate(R.id.browseFragment)
                NAV_ID_CART ->
                    findNavController(R.id.navHostFragment).navigate(R.id.cartFragment)
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
        if (findNavController(R.id.navHostFragment).currentDestination?.id == R.id.checkoutFragment) {
            val currentFragment =
                supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.first()
            (currentFragment as CheckoutFragment).doBackPressed()
        } else
            super.onBackPressed()
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
            R.id.servicesFragment -> NAV_ID_SERVICES
            R.id.browseFragment -> NAV_ID_BROWSE
            R.id.cartFragment -> NAV_ID_CART
            R.id.myAccountFragment -> NAV_ID_ACCOUNT
            else -> null
        }
        menuItemId?.let { binding.bottomNavigation.show(it, true) }
    }
}
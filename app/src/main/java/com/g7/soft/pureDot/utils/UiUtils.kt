package com.g7.soft.pureDot.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.ext.dpToPx
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.launch


// keep eyes on upcoming fix: @link: https://stackoverflow.com/questions/62643517/immersive-fullscreen-on-android-11
class UiUtils(private val window: Window, private val view: View) {

    fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }

    fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, view)
            .show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
    }

    fun enableVideoFullScreen(activity: AppCompatActivity) {
        hideSystemUI()
        activity.supportActionBar?.hide()
    }

    fun disableVideoFullScreen(activity: AppCompatActivity) {
        showSystemUI()
        activity.supportActionBar?.show()
    }

    companion object {

        fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
            if (view.layoutParams is MarginLayoutParams) {
                val p = view.layoutParams as MarginLayoutParams
                p.setMargins(left, top, right, bottom)
                view.requestLayout()
            }
        }

        // loading
        fun createThenGetProgressDrawable(context: Context): CircularProgressDrawable {
            val progressDrawable = CircularProgressDrawable(context)
            progressDrawable.strokeWidth = 5f
            progressDrawable.centerRadius = 30f
            progressDrawable.colorFilter = BlendModeColorFilterCompat
                .createBlendModeColorFilterCompat(
                    getColor(context, R.color.warm_purple), // todo
                    BlendModeCompat.SRC_ATOP
                )
            progressDrawable.start()
            return progressDrawable
        }

        // customization
        fun setOrientationLock(
            destinationId: Int,
            activity: AppCompatActivity,
            unlockedOrientationDestinations: Array<Int>,
        ) {
            if (unlockedOrientationDestinations.contains(destinationId))
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
            else
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }

        fun setToolbarElevation(
            destinationId: Int,
            appBar: AppBarLayout,
            noToolbarElevationDestinationIds: Array<Int>,
        ) {
            if (noToolbarElevationDestinationIds.contains(destinationId))
                appBar.elevation = 0f
            else
                appBar.elevation = 4.dpToPx().toFloat()
        }

        /*fun setupNavigationBar(
                destinationId: Int,
                navHostFragment: FragmentContainerView,
                navigationBar: BottomNavigationView,
                deepOrangeDestinationIds: Array<Int>,
                orangeDestinationIds: Array<Int>,
                blueGreenDestinationIds: Array<Int>,
                greyDestinationIds: Array<Int>,
                healthyFoodDestinationIds: Array<Int>,
        ) {
            val colorRes = when {
                deepOrangeDestinationIds.contains(destinationId) -> R.color.deep_orange
                orangeDestinationIds.contains(destinationId) -> R.color.pumpkin_orange
                blueGreenDestinationIds.contains(destinationId) -> R.color.blue_green
                greyDestinationIds.contains(destinationId) -> R.color.charcoal_grey_two
                healthyFoodDestinationIds.contains(destinationId) -> R.color.sick_green

                else -> null
            }

            if (colorRes == null) {
                (navHostFragment.layoutParams as MarginLayoutParams).bottomMargin = 0
                navigationBar.visibility = View.GONE
            } else navigationBar.backgroundTintList = ColorStateList.valueOf(getColor(navigationBar.context, colorRes))
                    .also {
                        navigationBar.visibility = View.VISIBLE
                        (navHostFragment.layoutParams as MarginLayoutParams).bottomMargin = 56.dpToPx()
                    }
        }*/

        fun setToolBarStatusBarNavBarBackground(
            destinationId: Int,
            appBar: AppBarLayout,
            statusBarFl: FrameLayout,
            navHostFragment: FragmentContainerView,
            transparentDestinationIds: Array<Int>,
            blueDestinationIds: Array<Int>,
            loginDestinationIds: Array<Int>,
        ) {
            appBar.elevation = 0f

            val colorRes = when {
                transparentDestinationIds.contains(destinationId) -> android.R.color.transparent
                blueDestinationIds.contains(destinationId) -> R.color.bluish
                loginDestinationIds.contains(destinationId) -> R.color.bluish
                else -> null
            }

            if (colorRes != null) {
                // connect navHostFragment top appBar/parent
                val constraintSet = ConstraintSet()
                constraintSet.clone(navHostFragment.parent as ConstraintLayout)
                if (colorRes == android.R.color.transparent)
                    constraintSet.connect(
                        navHostFragment.id,
                        ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.TOP,
                        0
                    )
                else
                    constraintSet.connect(
                        navHostFragment.id,
                        ConstraintSet.TOP,
                        appBar.id,
                        ConstraintSet.BOTTOM,
                        0
                    )
                constraintSet.applyTo(navHostFragment.parent as ConstraintLayout)

                statusBarFl.setBackgroundResource(colorRes)
                appBar.setBackgroundResource(colorRes)
                statusBarFl.visibility = View.VISIBLE
            } else
                statusBarFl.visibility = View.GONE
        }

        fun setToolBarNavigationButton(
            destinationId: Int,
            toolbar: Toolbar,
            supportActionBar: ActionBar?,
            transparentDestinationIds: Array<Int>,
        ) {
            val drawableRes = when {
                transparentDestinationIds.contains(destinationId) -> R.drawable.ic_up_btn_blue
                else -> null
            }

            if (drawableRes != null) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                toolbar.setNavigationIcon(drawableRes)
                supportActionBar?.show()
            } else {
                supportActionBar?.hide()
            }
        }

        @SuppressLint("CutPasteId")
        fun setupToolNavBars(
            destinationId: Int,
            activity: MainActivity,
            scrollLockedDestinationIds: ArrayList<Int>,
            fullScreenDestinationIds: ArrayList<Int>,
            loginDestinationIds: ArrayList<Int>,
            transparentDestinationIds: ArrayList<Int>,
            partialGradientDestinationIds: ArrayList<Int>,
            collapsingToolBarTitleDestinationIds: ArrayList<Int>,
            collapsingHomeDestinationIds: ArrayList<Int>,
            bottomNavBarDestinationIds: ArrayList<Int>,
        ) {
            val binding = activity.binding
            val window = activity.window

            when {
                fullScreenDestinationIds.contains(destinationId) -> {
                    binding.toolbarTitle.visibility = View.GONE
                    binding.backgroundIv.setImageResource(R.drawable.app_background_with_scrim)
                    //binding.appBarLayout.setExpanded(false, false)

                    //val params = binding.appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
                    //params.behavior = null
                    /*val behaviour = params.behavior as AppBarLayout.Behavior
                    behaviour.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                        override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                            return false
                        }
                    })*/
                    binding.appBarLayout.visibility = View.GONE
                    binding.appBarLayout.requestLayout()

                    val params2: CoordinatorLayout.LayoutParams =
                        binding.nestedScrollView.layoutParams as CoordinatorLayout.LayoutParams
                    params2.behavior = null
                    binding.nestedScrollView.requestLayout()

                    window.statusBarColor =
                        getColor(binding.root.context, android.R.color.transparent)
                    window.navigationBarColor =
                        getColor(binding.root.context, android.R.color.transparent)

                    binding.toolbar.requestLayout()
                }
                loginDestinationIds.contains(destinationId) -> {
                    binding.collapsingToolbarLayout.isTitleEnabled = false
                    binding.toolbar.title = null

                    binding.toolbarTitle.visibility = View.GONE
                    binding.backgroundIv.setImageResource(R.color.semi_white)
                    binding.appBarLayout.setExpanded(true, true)

                    binding.toolbarContent.visibility = View.VISIBLE
                    binding.root.findViewById<View>(R.id.toolbarHomeContent)?.visibility = View.GONE
                    binding.collapsingToolbarLayout.layoutParams.height = 250.dpToPx()
                    binding.appBarLayout.setBackgroundResource(R.drawable.app_gradient_scrim)

                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.appBarLayout.requestLayout()

                    val params2: CoordinatorLayout.LayoutParams =
                        binding.nestedScrollView.layoutParams as CoordinatorLayout.LayoutParams
                    params2.behavior = AppBarLayout.ScrollingViewBehavior()
                    binding.nestedScrollView.requestLayout()
                    binding.toolbar.setNavigationIcon(R.drawable.ic_up_btn_white)

                    window.statusBarColor =
                        getColor(binding.root.context, android.R.color.transparent)
                    window.navigationBarColor = getColor(binding.root.context, R.color.bluish)
                }
                transparentDestinationIds.contains(destinationId) -> {
                    binding.collapsingToolbarLayout.isTitleEnabled = true

                    binding.toolbarTitle.visibility = View.VISIBLE
                    binding.backgroundIv.setImageResource(R.color.semi_white)
                    binding.appBarLayout.setExpanded(false, false)
                    // binding.appBarLayout.setExpanded(false, false)
                    //binding.appBarLayout.setBackgroundResource(android.R.color.transparent)
                    //binding.collapsingToolbarLayout.setContentScrimResource(android.R.color.transparent)
                    binding.toolbarContent.visibility = View.GONE
                    binding.root.findViewById<View>(R.id.toolbarHomeContent)?.visibility = View.GONE
                    binding.collapsingToolbarLayout.layoutParams.height = 56.dpToPx()
                    binding.appBarLayout.setBackgroundResource(android.R.color.transparent)

                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.appBarLayout.requestLayout()

                    val params2: CoordinatorLayout.LayoutParams =
                        binding.nestedScrollView.layoutParams as CoordinatorLayout.LayoutParams
                    params2.behavior = AppBarLayout.ScrollingViewBehavior()
                    binding.nestedScrollView.requestLayout()
                    binding.toolbar.setNavigationIcon(R.drawable.ic_up_btn_blue)

                    window.statusBarColor = getColor(binding.root.context, R.color.bluish)
                    window.navigationBarColor = getColor(binding.root.context, R.color.bluish)
                }
                collapsingToolBarTitleDestinationIds.contains(destinationId) -> {
                    binding.collapsingToolbarLayout.isTitleEnabled = true

                    binding.toolbarTitle.visibility = View.GONE
                    binding.backgroundIv.setImageResource(R.color.semi_white)
                    binding.appBarLayout.setExpanded(true, true)

                    binding.toolbarContent.visibility = View.GONE
                    binding.root.findViewById<View>(R.id.toolbarHomeContent)?.visibility = View.GONE
                    binding.collapsingToolbarLayout.layoutParams.height = 124.dpToPx()
                    binding.appBarLayout.setBackgroundResource(R.drawable.app_gradient_scrim)

                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.appBarLayout.requestLayout()

                    val params2: CoordinatorLayout.LayoutParams =
                        binding.nestedScrollView.layoutParams as CoordinatorLayout.LayoutParams
                    params2.behavior = AppBarLayout.ScrollingViewBehavior()
                    binding.nestedScrollView.requestLayout()
                    binding.toolbar.setNavigationIcon(R.drawable.ic_up_btn_white)

                    window.statusBarColor =
                        getColor(binding.root.context, android.R.color.transparent)
                    window.navigationBarColor = getColor(binding.root.context, R.color.bluish)
                }
                collapsingHomeDestinationIds.contains(destinationId) -> {
                    binding.collapsingToolbarLayout.isTitleEnabled = true

                    binding.collapsingToolbarLayout.title = ""
                    binding.toolbarTitle.visibility = View.GONE
                    binding.backgroundIv.setImageDrawable(null)
                    binding.appBarLayout.setExpanded(true, true)

                    binding.toolbarContent.visibility = View.GONE
                    binding.root.findViewById<View>(R.id.toolbarHomeContent)?.visibility =
                        View.VISIBLE
                    binding.collapsingToolbarLayout.layoutParams.height = 250.dpToPx()
                    binding.appBarLayout.setBackgroundResource(R.drawable.app_gradient_scrim_round)

                    activity.lifecycleScope.launch {
                        val firstName = UserRepository("").getFirstName(activity)

                        binding.root.findViewById<View>(R.id.toolbarHomeContent)?.rootView?.findViewById<TextView>(
                            R.id.welcomeTv
                        )?.text = activity.getString(R.string.conc_welcome_, firstName)
                    }

                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.appBarLayout.requestLayout()

                    val params2: CoordinatorLayout.LayoutParams =
                        binding.nestedScrollView.layoutParams as CoordinatorLayout.LayoutParams
                    params2.behavior = AppBarLayout.ScrollingViewBehavior()
                    binding.nestedScrollView.requestLayout()
                    binding.toolbar.navigationIcon = null

                    window.statusBarColor =
                        getColor(binding.root.context, android.R.color.transparent)
                    window.navigationBarColor = getColor(binding.root.context, R.color.bluish)
                }
                partialGradientDestinationIds.contains(destinationId) -> {
                    binding.collapsingToolbarLayout.isTitleEnabled = true

                    binding.toolbarTitle.visibility = View.GONE
                    binding.backgroundIv.setImageResource(R.color.semi_white)
                    // binding.appBarLayout.setExpanded(false, false)
                    //binding.appBarLayout.setBackgroundResource(android.R.color.transparent)
                    //binding.collapsingToolbarLayout.setContentScrimResource(android.R.color.transparent)
                    binding.toolbarContent.visibility = View.GONE
                    binding.root.findViewById<View>(R.id.toolbarHomeContent)?.visibility = View.GONE
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.collapsingToolbarLayout.layoutParams.height = 56.dpToPx()
                    binding.appBarLayout.setBackgroundResource(R.drawable.app_gradient_scrim_part_1)

                    //binding.appBarLayout.visibility = View.GONE
                    //binding.appBarLayout.requestLayout()

                    val params2: CoordinatorLayout.LayoutParams =
                        binding.nestedScrollView.layoutParams as CoordinatorLayout.LayoutParams
                    params2.behavior = AppBarLayout.ScrollingViewBehavior()
                    binding.nestedScrollView.requestLayout()
                    binding.toolbar.setNavigationIcon(R.drawable.ic_up_btn_white)

                    window.statusBarColor = getColor(binding.root.context, R.color.bluish)
                    window.navigationBarColor = getColor(binding.root.context, R.color.bluish)
                }
            }
            binding.nestedScrollView.setScrollingEnabled(
                !scrollLockedDestinationIds.contains(
                    destinationId
                )
            )

            binding.bottomNavigation.visibility =
                if (bottomNavBarDestinationIds.contains(destinationId)) View.VISIBLE else View.GONE
        }
    }
}
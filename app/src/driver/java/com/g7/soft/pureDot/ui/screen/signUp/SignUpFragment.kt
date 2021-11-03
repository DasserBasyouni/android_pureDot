package com.g7.soft.pureDot.ui.screen.signUp

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.constant.ProjectConstant.Companion.ValidationError
import com.g7.soft.pureDot.databinding.FragmentSignUpBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.messaging.FirebaseMessaging
import com.zeugmasolutions.localehelper.currentLocale
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_sign_up, container, false)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        viewModel.getCounties(requireActivity().currentLocale.toLanguageTag())
        viewModel.getCities(requireActivity().currentLocale.toLanguageTag())

        // observables
        viewModel.selectedCountryPosition.observe(viewLifecycleOwner, {
            viewModel.getCities(requireActivity().currentLocale.toLanguageTag())
        })
        viewModel.countiesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.countriesSpinner,
                viewModel.countiesResponse.value,
                initialText = getString(R.string.select_country)
            )
        })
        viewModel.citiesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.citiesSpinner,
                viewModel.citiesResponse.value,
                initialText = getString(R.string.select_city)
            )
        })

        // setup listeners
        binding.birthDateTil.editText?.setOnClickListener {
            val calendar = viewModel.dateOfBirthCalendar.value!!
            DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    viewModel.dateOfBirthCalendar.value?.set(Calendar.YEAR, year)
                    viewModel.dateOfBirthCalendar.value?.set(Calendar.MONTH, monthOfYear)
                    viewModel.dateOfBirthCalendar.value?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    viewModel.dateOfBirth.value = SimpleDateFormat(
                        getString(R.string.format_standard_date),
                        requireActivity().currentLocale
                    ).format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        setupUploaderBtn(binding.uploadLicenceBtn, CAR_LICENCE_REQUEST_CODE)
        setupUploaderBtn(binding.uploadCarFrontPlateBtn, CAR_FRONT_PLATE_REQUEST_CODE)
        setupUploaderBtn(binding.uploadCarBackPlateBtn, CAR_BACK_PLATE_REQUEST_CODE)
        setupUploaderBtn(binding.uploadNationalIdBtn, CAR_NATIONAL_ID_REQUEST_CODE)
        setupUploadImageLayoutRemoveButtons()

        binding.acceptTermsCb.makeLinks(
            Pair(
                getString(R.string.terms_and_conditions),
                View.OnClickListener {
                    findNavController().navigate(R.id.termsFragment)
                }),
        )

        binding.submitRequestBtn.setOnClickListener {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.w("Fetching FCM registration token failed")
                    Timber.w(task.exception)
                    return@OnCompleteListener
                }

                val fcmToken = task.result

                viewModel.register(requireActivity().currentLocale.toLanguageTag(), fcmToken)
                    .observeApiResponse(this, {
                        if (it != null) {
                            UserRepository("").saveUserData(
                                requireContext(), UserDataModel(
                                    tokenId = it,
                                    name = viewModel.name.value,
                                    phoneNumber = viewModel.phoneNumber.value,
                                    carBrand = viewModel.carBrand.value,
                                    dateOfBirth = viewModel.dateOfBirthCalendar.value?.timeInMillis
                                        ?.div(1000),
                                    email = viewModel.email.value,
                                    isMale = viewModel.isMale.value,
                                    language = requireActivity().currentLocale.toLanguageTag()
                                ), viewModel.password.value
                            )

                            findNavController().navigate(
                                SignUpFragmentDirections.actionSignUpFragmentToPhoneVerificationFragment(
                                    isPasswordReset = false,
                                    isWalletVerification = false,
                                    emailOrPhoneNumber = viewModel.phoneNumber.value
                                )
                            )
                        } else
                            ProjectDialogUtils.showSimpleMessage(
                                requireContext(),
                                R.string.something_went_wrong,
                                drawableResId = R.drawable.ic_cancel
                            )
                        //Hyperion.open(requireActivity()) todo remove with its dependency
                    }, validationObserve = {
                        binding.nameTil.error = if (it == ValidationError.EMPTY_NAME)
                            getString(R.string.error_empty_name) else null

                        binding.phoneNumberTil.error = if (it == ValidationError.EMPTY_PHONE_NUMBER)
                            getString(R.string.error_empty_phone_number) else null

                        binding.emailTil.error = if (it == ValidationError.EMPTY_EMAIL)
                            getString(R.string.error_empty_email) else null

                        binding.birthDateTil.error = if (it == ValidationError.EMPTY_DATE_OF_BIRTH)
                            getString(R.string.error_empty_date_of_birth) else null

                        binding.carBrandTil.error = if (it == ValidationError.EMPTY_CAR_BRAND)
                            getString(R.string.error_empty_car_brand) else null
                    })
            })
        }
    }

    private fun setupUploadImageLayoutRemoveButtons() {
        binding.licencePhotoLayout.apply {
            this.removeTv.setOnClickListener { viewModel.licenceImagePath.value = null }
        }
        binding.carFrontPlatePhotoLayout.apply {
            this.removeTv.setOnClickListener { viewModel.carFrontImagePath.value = null }
        }
        binding.carBackPlatePhotoLayout.apply {
            this.removeTv.setOnClickListener { viewModel.carBackImagePath.value = null }
        }
        binding.nationalIdPhotoLayout.apply {
            this.removeTv.setOnClickListener { viewModel.nationalIdImagePath.value = null }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAR_LICENCE_REQUEST_CODE -> {
                    val returnValue = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    viewModel.licenceImagePath.value = returnValue?.getOrNull(0)
                }
                CAR_FRONT_PLATE_REQUEST_CODE -> {
                    val returnValue = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    viewModel.carFrontImagePath.value = returnValue?.getOrNull(0)
                }
                CAR_BACK_PLATE_REQUEST_CODE -> {
                    val returnValue = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    viewModel.carBackImagePath.value = returnValue?.getOrNull(0)
                }
                CAR_NATIONAL_ID_REQUEST_CODE -> {
                    val returnValue = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    viewModel.nationalIdImagePath.value = returnValue?.getOrNull(0)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, Options.init().setRequestCode(100))
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Approve permissions to upload photos",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                return
            }
        }
    }


    private fun setupUploaderBtn(btn: MaterialButton, requestCode: Int) {
        btn.setOnClickListener {
            val options = Options.init()
                .setRequestCode(requestCode) //Request code for activity results
                .setCount(1) //Number of images to restict selection count
                .setFrontfacing(false) //Front Facing camera on start
                //.setPreSelectedUrls(returnValue) //Pre selected Image Urls
                .setSpanCount(4) //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Picture)
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion
            //.setPath("/pix/images") //Custom Path For media Storage
            Pix.start(this, options)
        }
    }

    private fun <T> setupSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<List<T>?>?,
        initialText: String
    ) {
        var selectedPosition = 0

        val spinnerData = when (networkResponse?.status) {
            ProjectConstant.Companion.Status.IDLE -> {
                spinner.isEnabled = false
                arrayListOf(initialText)
            }
            ProjectConstant.Companion.Status.LOADING -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.loading_))
            }
            ProjectConstant.Companion.Status.SUCCESS, ProjectConstant.Companion.Status.API_ERROR -> {
                val modelsList = networkResponse.data
                val dataList = when {
                    modelsList?.firstOrNull() is CountryModel -> {
                        selectedPosition = viewModel.selectedCountryPosition.value!!
                        modelsList.mapNotNull { (it as CountryModel).name }.toTypedArray()
                    }
                    modelsList?.firstOrNull() is CityModel -> {
                        selectedPosition = viewModel.selectedCityPosition.value!!
                        modelsList.mapNotNull { (it as CityModel).name }.toTypedArray()

                    }
                    /*modelsList?.firstOrNull() is ZipCodeModel -> {
                        selectedPosition = viewModel.selectedZipCodePosition.value!!
                        modelsList.mapNotNull { (it as ZipCodeModel).code }.toTypedArray()
                    }*/
                    else -> null
                }
                spinner.isEnabled = true
                arrayListOf(initialText).apply {
                    this.addAll((dataList ?: arrayOf()))
                }
            }
            else -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.something_went_wrong))
            }
        }

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerData
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(selectedPosition)
        }
    }


    companion object {
        const val CAR_LICENCE_REQUEST_CODE = 100
        const val CAR_FRONT_PLATE_REQUEST_CODE = 101
        const val CAR_BACK_PLATE_REQUEST_CODE = 102
        const val CAR_NATIONAL_ID_REQUEST_CODE = 103
    }

}
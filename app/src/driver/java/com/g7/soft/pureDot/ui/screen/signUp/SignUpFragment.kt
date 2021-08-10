package com.g7.soft.pureDot.ui.screen.signUp

import android.app.Activity
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
import com.g7.soft.pureDot.databinding.FragmentSignUpBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.model.ZipCodeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.google.android.material.button.MaterialButton
import com.zeugmasolutions.localehelper.currentLocale


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
        viewModel.getZipCodes(requireActivity().currentLocale.toLanguageTag())

        // observables
        viewModel.selectedCountryPosition.observe(viewLifecycleOwner, {
            viewModel.getCities(requireActivity().currentLocale.toLanguageTag())
        })
        viewModel.selectedCityPosition.observe(viewLifecycleOwner, {
            viewModel.getZipCodes(requireActivity().currentLocale.toLanguageTag())
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
        viewModel.zipCodesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.zipCodeSpinner,
                viewModel.zipCodesResponse.value,
                initialText = getString(R.string.select_zipcode)
            )
        })

        // setup listeners
        setupUploaderBtn(binding.uploadLicenceBtn, 2, CAR_LICENCE_REQUEST_CODE)
        setupUploaderBtn(binding.uploadCarFrontPlateBtn, 1, CAR_FRONT_PLATE_REQUEST_CODE)
        setupUploaderBtn(binding.uploadCarBackPlateBtn, 1, CAR_BACK_PLATE_REQUEST_CODE)
        setupUploaderBtn(binding.uploadNationalIdBtn, 2, CAR_NATIONAL_ID_REQUEST_CODE)
        setupUploadImageLayoutRemoveButtons()

        binding.acceptTermsCb.makeLinks(
            Pair(
                getString(R.string.terms_and_conditions),
                View.OnClickListener {
                    findNavController().navigate(R.id.termsFragment)
                }),
        )

        binding.submitRequestBtn.setOnClickListener {
            viewModel.register(requireActivity().currentLocale.toLanguageTag()).observeApiResponse(this, {
                // todo save user data
                if (it?.tokenId != null)
                    findNavController().navigate(
                        SignUpFragmentDirections.actionSignUpFragmentToPhoneVerificationFragment(
                            false,
                            viewModel.phoneNumber.value
                        )
                    )
                else
                    ProjectDialogUtils.showSimpleMessage(requireContext(), R.string.something_went_wrong, drawableResId = R.drawable.ic_cancel)
            })
        }
    }

    private fun setupUploadImageLayoutRemoveButtons() {
        binding.licencePhotoLayout1.apply {
            this.removeTv.setOnClickListener {
                viewModel.licenceImages.value = viewModel.licenceImages.value.apply {
                    this?.removeAt(0)
                }
            }
        }
        binding.licencePhotoLayout2.apply {
            this.removeTv.setOnClickListener {
                viewModel.licenceImages.value = viewModel.licenceImages.value.apply {
                    this?.removeAt(1)
                }
            }
        }
        binding.carFrontPlatePhotoLayout.apply {
            this.removeTv.setOnClickListener {
                viewModel.carFrontPlateImages.value = viewModel.carFrontPlateImages.value.apply {
                    this?.removeAt(0)
                }
            }
        }
        binding.carBackPlatePhotoLayout.apply {
            this.removeTv.setOnClickListener {
                viewModel.carBackPlateImages.value = viewModel.carBackPlateImages.value.apply {
                    this?.removeAt(0)
                }
            }
        }
        binding.nationalIdPhotoLayout1.apply {
            this.removeTv.setOnClickListener {
                viewModel.nationalIdImages.value = viewModel.nationalIdImages.value.apply {
                    this?.removeAt(0)
                }
            }
        }
        binding.nationalIdPhotoLayout2.apply {
            this.removeTv.setOnClickListener {
                viewModel.nationalIdImages.value = viewModel.nationalIdImages.value.apply {
                    this?.removeAt(1)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAR_LICENCE_REQUEST_CODE -> {
                    val returnValue = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    viewModel.licenceImages.value = returnValue?.toMutableList()
                }
                CAR_FRONT_PLATE_REQUEST_CODE -> {
                    val returnValue = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    viewModel.carFrontPlateImages.value = returnValue?.toMutableList()
                }
                CAR_BACK_PLATE_REQUEST_CODE -> {
                    val returnValue = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    viewModel.carBackPlateImages.value = returnValue?.toMutableList()
                }
                CAR_NATIONAL_ID_REQUEST_CODE -> {
                    val returnValue = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    viewModel.nationalIdImages.value = returnValue?.toMutableList()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, Options.init().setRequestCode(100))
                } else {
                    Toast.makeText(requireContext(), "Approve permissions to upload photos", Toast.LENGTH_LONG)
                        .show()
                }
                return
            }
        }
    }


    private fun setupUploaderBtn(btn: MaterialButton, maxImages: Int, requestCode: Int) {
        btn.setOnClickListener {
            val options = Options.init()
                .setRequestCode(requestCode) //Request code for activity results
                .setCount(maxImages) //Number of images to restict selection count
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
        val spinnerData = when (networkResponse?.status) {
            ProjectConstant.Companion.Status.IDLE -> {
                spinner.isEnabled = false
                arrayListOf(initialText)
            }
            ProjectConstant.Companion.Status.LOADING -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.loading_))
            }
            ProjectConstant.Companion.Status.SUCCESS -> {
                val modelsList = networkResponse.data
                val dataList = when {
                    modelsList?.firstOrNull() is CountryModel -> {
                        modelsList.mapNotNull { (it as CountryModel).name }.toTypedArray()
                    }
                    modelsList?.firstOrNull() is CityModel -> {
                        modelsList.mapNotNull { (it as CityModel).name }.toTypedArray()

                    }
                    modelsList?.firstOrNull() is ZipCodeModel -> {
                        modelsList.mapNotNull { (it as ZipCodeModel).code }.toTypedArray()
                    }
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
            spinner.setSelection(viewModel.selectedCountryPosition.value!!) // todo made before in ProfileEdit
        }
    }


    companion object {
        const val CAR_LICENCE_REQUEST_CODE = 100
        const val CAR_FRONT_PLATE_REQUEST_CODE = 101
        const val CAR_BACK_PLATE_REQUEST_CODE = 102
        const val CAR_NATIONAL_ID_REQUEST_CODE = 103
    }

}
package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.database.KoalaDetailViewModel
import java.util.*
import androidx.lifecycle.Observer
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.io.File

private const val TAG = "KoalaFragment"
private const val ARG_PHOTO_ID = "photo_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val REQUEST_PHOTO = 2
private const val DIALOG_PICTURE = "DialogPicture"


class KoalaFragment : Fragment(), DatePickerFragment.Callbacks {
    private lateinit var koala: Koala
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private lateinit var titleField: EditText
    private lateinit var photoPlace: EditText
    private lateinit var dateButton: Button


    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var mLocationField: TextView
    private lateinit var showMap: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var shareButton: Button



    private val koalaDetailViewModel: KoalaDetailViewModel by lazy {
        ViewModelProviders.of(this).get(KoalaDetailViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        koala = Koala()
        val koalaId: UUID = arguments?.getSerializable(ARG_PHOTO_ID) as UUID
        koalaDetailViewModel.loadKoala(koalaId)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_koala, container, false)
        titleField = view.findViewById(R.id.photo_title) as EditText
        photoPlace = view.findViewById(R.id.photo_place) as EditText
        dateButton = view.findViewById(R.id.photo_date) as Button

        photoButton = view.findViewById(R.id.koala_camera) as ImageButton
        photoView = view.findViewById(R.id.koala_photo) as ImageView
        mLocationField = view.findViewById(R.id.locationView)
        showMap= view.findViewById(R.id.map_button)
        shareButton = view.findViewById(R.id.share_button)


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Try to get a Location")

            if (GoogleApiAvailability.getInstance()
                    .isGooglePlayServicesAvailable(requireContext()) == ConnectionResult.SUCCESS
            ) {
                //get location
            }
        }
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext()) == ConnectionResult.SUCCESS) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token


                override fun isCancellationRequested() = false

            }).addOnSuccessListener { location: Location? ->
                location?.let {
                    Log.i("Location", "Got a Fix: " + location)
                }
            }
        }

        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext()) == ConnectionResult.SUCCESS){
            fusedLocationClient.getLastLocation()
                .addOnSuccessListener { location: Location? ->
                    location?.let{
                        koala.lat = location.getLatitude().toString()
                        koala.lon = location.getLongitude().toString()
                        mLocationField.setText(
                            String.format(
                                "Lat: %s,  lon:%s",
                                koala.lat,
                                koala.lon
                            )
                        )
                        Log.i("LOCATION", "Got a fix: " + location)
                    }
                }
        }




        return view
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        koalaDetailViewModel.koalaLiveData.observe(
            viewLifecycleOwner,
            Observer { koala ->
                koala?.let {
                    this.koala = koala
                    photoFile = koalaDetailViewModel.getPhotoFile(koala)
                    photoUri = FileProvider.getUriForFile(requireActivity(),
                        "com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.fileprovider",
                        photoFile)
                    updateUI()
                }
            })
    }



    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                koala.title = sequence.toString()

            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        val placeWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                koala.place = sequence.toString()

            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }


        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(koala.date).apply {
                setTargetFragment(this@KoalaFragment, REQUEST_DATE)
                show(this@KoalaFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        titleField.addTextChangedListener(titleWatcher)
        photoPlace.addTextChangedListener(placeWatcher)

        showMap.setOnClickListener {
                val intent = Intent(requireContext(), MapsActivity::class.java)
                // start Maps activity
                startActivity(intent)
        }

        shareButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            // return to ListViewModel activity
            startActivity(intent)
        }
        photoButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager

            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }

            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)

                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }

                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
        photoView.setOnClickListener {

            val zoomDialog= ZoomDialogFragment.newInstance(koala.photoFileName)

            fragmentManager?.let { it1 -> zoomDialog.show(it1,null) }

        }





    }


    override fun onStop() {
        super.onStop()
        koalaDetailViewModel.saveKoala(koala)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }


    override fun onDateSelected(date: Date) {
        koala.date = date
        updateUI()
    }

    private fun updateUI() {
        titleField.setText(koala.title)
        dateButton.text = koala.date.toString()
        updatePhotoView()
    }

    private fun updatePhotoView() {
        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        } else {
            photoView.setImageDrawable(null)
        }
    }

    companion object {

        fun newInstance(koalaId: UUID): KoalaFragment {
            val args = Bundle().apply {
                putSerializable(ARG_PHOTO_ID, koalaId)
            }
            return KoalaFragment().apply {
                arguments = args
            }
        }
    }
}
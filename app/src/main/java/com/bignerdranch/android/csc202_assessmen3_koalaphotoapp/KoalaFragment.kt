package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.database.KoalaDetailViewModel
import java.util.*
import androidx.lifecycle.Observer
import com.google.android.gms.location.LocationServices
import java.io.File

private const val TAG = "KoalaFragment"
private const val ARG_PHOTO_ID = "photo_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val REQUEST_PHOTO = 2


class KoalaFragment : Fragment(), DatePickerFragment.Callbacks {
    private lateinit var koala: Koala
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private lateinit var titleField: EditText
    //private lateinit var detailField: EditText
    private lateinit var dateButton: Button
    private lateinit var deletebutton: Button

    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var mClient: GoogleAppClient


    private val koalaDetailViewModel: KoalaDetailViewModel by lazy {
        ViewModelProviders.of(this).get(KoalaDetailViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        koala = Koala()
        val koalaId: UUID = arguments?.getSerializable(ARG_PHOTO_ID) as UUID
        mClient = new GoogleAppClient.Builder(activity)
            .addApi(LocationServices.API)
            .build;
        koalaDetailViewModel.loadKoala(koalaId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_koala, container, false)
        titleField = view.findViewById(R.id.photo_title) as EditText
        //detailField = view.findViewById(R.id.photo_details) as EditText
        dateButton = view.findViewById(R.id.photo_date) as Button
        //solvedCheckBox = view.findViewById(R.id.cri_solved) as CheckBox
        //deleteButton = view.findViewById(R.id.delete_button) as Button
        photoButton = view.findViewById(R.id.koala_camera) as ImageButton
        photoView = view.findViewById(R.id.koala_photo) as ImageView

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
        mClient.connect();


        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                koala.title = sequence.toString()
                //koala.details = sequence.toString()
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

    }

    override fun onStop() {
        super.onStop()
        mClient.disconnect()
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
package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.File
private const val PHOTO_URI = "image"

class ZoomDialogFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.zoom_layout, container, false)
        val imageView = view.findViewById(R.id.koalaPicture) as ImageView

        val photoFileName = arguments?.getSerializable("PHOTO_URI") as String

        imageView.setImageBitmap(BitmapFactory.decodeFile(requireContext().filesDir.path + "/" + photoFileName))

        return view
    }

    companion object {
        fun newInstance(photoFileName: String): ZoomDialogFragment {
            val frag = ZoomDialogFragment()
            val args = Bundle()
            args.putSerializable("PHOTO_URI", photoFileName)
            frag.arguments = args
            return frag
        }
    }


}
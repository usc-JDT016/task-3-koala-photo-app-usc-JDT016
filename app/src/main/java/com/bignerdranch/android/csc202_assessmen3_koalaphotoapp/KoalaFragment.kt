package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.database.KoalaDetailViewModel
import java.util.*
import androidx.lifecycle.Observer

private const val TAG = "KoalaFragment"
private const val ARG_PHOTO_ID = "photo_id"
class KoalaFragment : Fragment() {
    private lateinit var koala: Koala
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    private val koalaDetailViewModel: KoalaDetailViewModel by lazy {
        ViewModelProviders.of(this).get(KoalaDetailViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        koala = Koala()
        val koalaId: UUID = arguments?.getSerializable(ARG_PHOTO_ID) as UUID
        koalaDetailViewModel.loadKoala(koalaId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_koala, container, false)
        titleField = view.findViewById(R.id.photo_title) as EditText
        dateButton = view.findViewById(R.id.photo_date) as Button
        //solvedCheckBox = view.findViewById(R.id.cri_solved) as CheckBox
        dateButton.apply {
            text = koala.date.toString()
            isEnabled = false
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
                // This space intentionally left blank
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

        titleField.addTextChangedListener(titleWatcher)    }

    override fun onStop() {
        super.onStop()
        koalaDetailViewModel.saveKoala(koala)
    }


    private fun updateUI() {
        titleField.setText(koala.title)
        dateButton.text = koala.date.toString()

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
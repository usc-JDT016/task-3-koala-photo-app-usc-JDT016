package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "KoalaListFragment"

class KoalaListFragment : Fragment() {

    private lateinit var koalaRecyclerView: RecyclerView
    private var adapter: KoalaAdapter? = null

    private val koalaListViewModel: KoalaListViewModel by lazy {
        ViewModelProviders.of(this).get(KoalaListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total Photos: ${koalaListViewModel.koalas.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_koala_list, container, false)

        koalaRecyclerView =
            view.findViewById(R.id.koala_recycler_view) as RecyclerView
        koalaRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI() {
        val koalas = koalaListViewModel.koalas
        adapter = KoalaAdapter(koalas)
        koalaRecyclerView.adapter = adapter
    }



    private inner class KoalaHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var koala: Koala
        private val titleTextView: TextView = itemView.findViewById(R.id.photo_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.photo_date)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(koala: Koala) {
            this.koala = koala
            titleTextView.text = this.koala.title
            dateTextView.text = this.koala.date.toString()
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${koala.title} pressed!", Toast.LENGTH_SHORT)
                .show()
        }
    }
    private inner class KoalaAdapter(var koalas: List<Koala>)
        : RecyclerView.Adapter<KoalaHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : KoalaHolder {
            val view = layoutInflater.inflate(R.layout.list_item_koala, parent, false)
            return KoalaHolder(view)
        }

        override fun getItemCount() = koalas.size

        override fun onBindViewHolder(holder: KoalaHolder, position: Int) {
            val koala = koalas[position]

            holder.bind(koala)
        }
    }

    companion object {
        fun newInstance(): KoalaListFragment {
            return KoalaListFragment()
        }
    }
}
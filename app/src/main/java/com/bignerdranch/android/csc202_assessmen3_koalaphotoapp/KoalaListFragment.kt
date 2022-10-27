package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "KoalaListFragment"

class KoalaListFragment : Fragment() {
    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onKoalaSelected(koalaId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var koalaRecyclerView: RecyclerView
    private var adapter: KoalaAdapter? = KoalaAdapter(emptyList())

    private val koalaListViewModel: KoalaListViewModel by lazy {
        ViewModelProviders.of(this).get(KoalaListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        koalaRecyclerView.adapter = adapter


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        koalaListViewModel.koalaListLiveData.observe(
            viewLifecycleOwner,
            Observer { koalas ->
                koalas?.let {
                    Log.i(TAG, "Got photos ${koalas.size}")
                    updateUI(koalas)
                }
            })
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_koala_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_photo -> {
                val koala = Koala()
                koalaListViewModel.addKoala(koala)
                callbacks?.onKoalaSelected(koala.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private fun updateUI(koalas: List<Koala>) {
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
            callbacks?.onKoalaSelected(koala.id)
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
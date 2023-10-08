package com.emretanercetinkaya.testcase.ui.triplistfragment


import android.R
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.emretanercetinkaya.testcase.MainActivity
import com.emretanercetinkaya.testcase.databinding.FragmentTripListBinding
import com.emretanercetinkaya.testcase.model.CustomMarkerData
import com.emretanercetinkaya.testcase.utils.isInternetAvaiable
import kotlinx.coroutines.runBlocking


class TripListFragment : Fragment() {
    var internetProblemDialog : Dialog? = null
    private lateinit var bindingTripList: FragmentTripListBinding
    private lateinit var viewModel: TripListViewModel
    private var tripListModel: CustomMarkerData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TripListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        bindingTripList = FragmentTripListBinding.inflate(inflater, container, false)
        val bundle = arguments
        tripListModel = bundle?.getParcelable("tripListData")
        fetchRoutes()

        val networkStatusLiveData = isInternetAvaiable(requireContext())
        networkStatusLiveData.observe(viewLifecycleOwner, Observer { isConnected ->
            if (!isConnected) {
                if (internetProblemDialog != null) {
                    internetProblemDialog!!.show()
                } else {
                    internetProblemDialog = Dialog(requireContext(), R.style.Theme_Black_NoTitleBar_Fullscreen)
                    internetProblemDialog!!.setContentView(com.emretanercetinkaya.testcase.R.layout.internet_problem_view)
                    internetProblemDialog!!.show()
                }
            } else {
                if (internetProblemDialog != null) {
                    internetProblemDialog!!.dismiss()
                }
            }
        })

        return bindingTripList.root
    }

     fun fetchRoutes() {
        if (tripListModel != null) {
            val llm = LinearLayoutManager(context)
            val itemDecoration = DividerItemDecoration(requireContext(), llm.orientation)
            bindingTripList.tripsRV.layoutManager = llm
            bindingTripList.tripsRV.addItemDecoration(itemDecoration)
            bindingTripList.tripsRV.adapter = TripAdapter(tripListModel!!.trips) { it ->
                bindingTripList.progressBar.visibility = View.VISIBLE
                val stationId = tripListModel!!.stationId
                val tripId = it.id
                viewModel.tripBooking(stationId,tripId) {
                    if (it) {
                        bindingTripList.progressBar.visibility = View.GONE
                        runBlocking {
                            (requireActivity() as MainActivity).saveBookTrip(tripListModel!!)
                        }
                        findNavController().navigateUp()
                    } else {
                        runBlocking {
                            (requireActivity() as MainActivity).clearDataStore()
                        }
                        bindingTripList.progressBar.visibility = View.GONE
                        val dialogFragment = FullTripCustomDialog()
                        dialogFragment.show(requireActivity().supportFragmentManager,"FullTripDialog")
                    }
                }
            }
        }
    }


}


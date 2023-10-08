package com.emretanercetinkaya.testcase.ui.stationsmapfragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Criteria
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.emretanercetinkaya.testcase.MainActivity
import com.emretanercetinkaya.testcase.R
import com.emretanercetinkaya.testcase.databinding.FragmentStationsMapBinding
import com.emretanercetinkaya.testcase.model.CustomMarkerData
import com.emretanercetinkaya.testcase.utils.isInternetAvaiable
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.runBlocking


class StationsMapFragment : Fragment(), OnMapReadyCallback {
    val markerList = mutableListOf<Marker>()
    private lateinit var bindingStations: FragmentStationsMapBinding
    private lateinit var viewModel: StationsMapViewModel
    private val LOCATION_PERMISSION_REQUEST_CODE = 123
    private lateinit var selectedPointBitmap: Bitmap
    private lateinit var defaultPointBitmap: Bitmap
    private lateinit var bookedPointBitmap: Bitmap
    private lateinit var mMap: GoogleMap
    private var selectedMarker: Marker? = null
    private var isShowingFirstTimeScreen: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[StationsMapViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        bindingStations = FragmentStationsMapBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        selectedPointBitmap = generateSmallIcon(requireContext(), R.drawable.selectedpoint)
        defaultPointBitmap = generateSmallIcon(requireContext(), R.drawable.point)
        bookedPointBitmap = generateSmallIcon(requireContext(), R.drawable.completed)

        bindingStations.listTripsButton.setOnClickListener {
            for (item in markerList) {
                if (selectedMarker == item) {
                    val customData = item.tag as? CustomMarkerData
                    if (customData != null) {
                        val bundle = Bundle()
                        bundle.putParcelable("tripListData", customData)
                        view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_stationsMapFragment_to_tripListFragment, bundle) };
                    }
                }
            }
        }

        val networkStatusLiveData = isInternetAvaiable(requireContext())
        networkStatusLiveData.observe(viewLifecycleOwner, Observer { isConnected ->
            if (!isConnected) {
                TODO() // Internet Control
            }
        })

        return bindingStations.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        viewModel.markerList.observe(viewLifecycleOwner, Observer { stations ->
            addMarkers(stations, googleMap)
        })

        googleMap.setOnMapClickListener {
            if (selectedMarker != null) {
                selectedMarker!!.setIcon(BitmapDescriptorFactory.fromBitmap(defaultPointBitmap))
            }
            bindingStations.listTripsButton.visibility = View.GONE
        }


        googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(marker: Marker): View? {
                return null
            }

            override fun getInfoWindow(marker: Marker): View? {
                val view = layoutInflater.inflate(R.layout.custom_info_view, null)
                val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
                titleTextView.setText(marker.title)
                return view
            }
        })


        googleMap.setOnMarkerClickListener { marker ->
            marker.showInfoWindow()
            if (!bindingStations.listTripsButton.isVisible) bindingStations.listTripsButton.visibility = View.VISIBLE
            for (item in markerList) {
                val markerInfo = item.tag as CustomMarkerData
                if (marker == item) {
                    item.setIcon(BitmapDescriptorFactory.fromBitmap(selectedPointBitmap))
                    selectedMarker = item
                } else {
                    if (markerInfo.isBooked) {
                        item.setIcon(BitmapDescriptorFactory.fromBitmap(bookedPointBitmap))
                    } else {
                        item.setIcon(BitmapDescriptorFactory.fromBitmap(defaultPointBitmap))
                    }
                }
            }
            true
        }


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            val bestProvider = locationManager.getBestProvider(criteria, true)
            val location = bestProvider?.let { locationManager.getLastKnownLocation(it) }
            if (location != null) {
                if (isShowingFirstTimeScreen) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                    isShowingFirstTimeScreen = false
                }
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(41.039451535657044, 29.02118376413845), 12f))
            }
        } else {
            requestPermissions(
                arrayOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (selectedMarker != null) runBlocking {
            val bookedStation: CustomMarkerData? = (activity as MainActivity).getBookedTrip()
            if (bookedStation != null) {
                mMap.clear()
                markerList.clear()
                selectedMarker = null
                viewModel.changeTripState(bookedStation.stationId)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(mMap)
            }
        }
    }


    private fun addMarkers(stationList: ArrayList<CustomMarkerData>, googleMap: GoogleMap) {
        stationList.map { stations ->
            val markerOptions = MarkerOptions()
            markerOptions.position(stations.position)
            val customData = CustomMarkerData(stations.trips.size.toString() + " " + resources.getString(R.string.trips), stations.position, stations.stationId, stations.trips, false)
            val marker = googleMap.addMarker(markerOptions)
            if (marker != null) {
                if (stations.isBooked) {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(bookedPointBitmap))
                } else {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(defaultPointBitmap))
                }
                marker.title = customData.title
                marker.tag = customData
                markerList.add(marker)
            }
        }
    }

    private fun generateSmallIcon(context: Context, resourceId: Int): Bitmap {
        val height = 100
        val width = 100
        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }


}
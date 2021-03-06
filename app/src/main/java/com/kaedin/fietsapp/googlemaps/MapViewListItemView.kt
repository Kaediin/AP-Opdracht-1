package com.kaedin.fietsapp.googlemaps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kaedin.fietsapp.R
import com.kaedin.fietsapp.models.Vehicle
import com.kaedin.fietsapp.activity.VehicleDetailsActivity
import kotlinx.android.synthetic.main.list_item.view.*

class MapViewListItemView constructor(context: Context?, attrs: AttributeSet? = null) :
    CardView(context!!, attrs), OnMapReadyCallback {

    /**
     * Global scoped vars
     */
    private var mMapView: MapView? = null
    private var mVehicle: Vehicle? = null
    private var mView: View? = null

    /**
     * Inflate view with the list item layout
     * This view is now the context in which you can alter the text values
     * Set the mapview to the mapview from the context from the inflated view
     * @see LayoutInflater.inflate
     * @see MapView
     */
    private fun setupView() {
        mView = LayoutInflater.from(context).inflate(R.layout.list_item, this)
        mView!!.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        mMapView = mView!!.findViewById<View>(R.id.vehicle_list_item_map) as MapView
    }

    /** create map is activity is create
     * We dont handle any logic here we want to first create and then when resumed (which always follows next)
     * we can handle logic because we know for sure the mapView instance is created and wont cause a NullpointerException
     */
    fun mapViewOnCreate(savedInstanceState: Bundle?) {
        if (mMapView != null) {
            mMapView!!.onCreate(savedInstanceState)
        }
    }

    fun mapViewOnResume(vehicle: Vehicle) {
        if (mMapView != null) {
            mMapView!!.onResume()
            /**  */
            mVehicle = vehicle

            mView?.vehicle_id?.text = vehicle.type.toString()
            mView?.vehicle_name?.text = vehicle.name
            mView?.vehicle_dropoff_date?.text = vehicle.dropoffDate

            mMapView!!.getMapAsync(this)
            mView!!.list_item.setOnClickListener {
                launchIntentDetails()
            }
        }
    }

    /** Pasue map is activity is paused */
    fun mapViewOnPause() {
        if (mMapView != null) {
            mMapView!!.onPause()
        }
    }

    /** Destroy map is activity is destroyed */
    fun mapViewOnDestroy() {
        if (mMapView != null) {
            mMapView!!.onDestroy()
        }
    }

    /** Enable low memory if needed */
    fun mapViewOnLowMemory() {
        if (mMapView != null) {
            mMapView!!.onLowMemory()
        }
    }

    fun mapViewOnSaveInstanceState(outState: Bundle?) {
        if (mMapView != null) {
            mMapView!!.onSaveInstanceState(outState)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val location = LatLng(mVehicle!!.latitude, mVehicle!!.longitude)
        googleMap.apply {
            uiSettings.setAllGesturesEnabled(false)
            addMarker(MarkerOptions().position(location))
            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5f))
            setOnMapClickListener {
                launchIntentDetails()
            }
        }
    }

    private fun launchIntentDetails(){
        val intent = Intent(context, VehicleDetailsActivity::class.java)
        intent.putExtra("vehicle_id", mVehicle?.id)
        context.startActivity(intent)
    }

    init {
        setupView()
    }
}
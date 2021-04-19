package com.kaedin.spacex.googlemaps

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.fietsapp.Vehicle

class MapViewHolder(private val mMapViewListItemView: MapViewListItemView?) :
    RecyclerView.ViewHolder(
        mMapViewListItemView!!
    ) {
    fun mapViewListItemViewOnCreate(savedInstanceState: Bundle?) {
        mMapViewListItemView?.mapViewOnCreate(savedInstanceState)
    }

    fun mapViewListItemViewOnResume(vehicle: Vehicle) {
        mMapViewListItemView?.mapViewOnResume(vehicle)
    }

    fun mapViewListItemViewOnPause() {
        mMapViewListItemView?.mapViewOnPause()
    }

    fun mapViewListItemViewOnDestroy() {
        mMapViewListItemView?.mapViewOnDestroy()
    }

    fun mapViewListItemViewOnLowMemory() {
        mMapViewListItemView?.mapViewOnLowMemory()
    }

    fun mapViewListItemViewOnSaveInstanceState(outState: Bundle?) {
        mMapViewListItemView?.mapViewOnSaveInstanceState(outState)
    }
}
package com.ardy.ardysubmisfunda.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FavDat (
    var id: Int = 0,
    var username: String? = null,
    var photo:  String? = null
): Parcelable
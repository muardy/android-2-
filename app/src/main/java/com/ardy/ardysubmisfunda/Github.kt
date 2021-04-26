package com.ardy.ardysubmisfunda

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Github (
    var name: String = "",
    var username: String = "",
    var location: String = "",
    var repository: String = "",
    var company: String = "",
    var followers: String = "",
    var following: String = "",
    var photo:  String = ""
): Parcelable

package com.example.kamonwan_s.kotlinmessager.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String,val username : String ,val profileImageUrl:String) : Parcelable{
constructor():this("","","")
}

package com.rem113.chat

import android.os.Parcel
import android.os.Parcelable
import java.util.*


data class Message(val date : Date,
                   val text : String,
                   val username : String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readSerializable() as Date,
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(username)
    }

    override fun describeContents() : Int = 0

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message = Message(parcel)

        override fun newArray(size: Int): Array<Message?> = arrayOfNulls(size)
    }
}
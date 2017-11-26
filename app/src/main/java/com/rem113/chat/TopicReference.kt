package com.rem113.chat

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class TopicReference(val date: Date,
                 var title: String,
                 var last: String,
                 var messages : String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readSerializable() as Date,
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(date)
        parcel.writeString(title)
        parcel.writeString(last)
        parcel.writeString(messages)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<TopicReference> {
        override fun createFromParcel(parcel: Parcel): TopicReference = TopicReference(parcel)

        override fun newArray(size: Int): Array<TopicReference?> = arrayOfNulls(size)
    }


}
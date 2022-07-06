package com.susu.demoapplication.javabase.serial;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author : sudan
 * Time : 2022/1/13
 * Description: Parcelable持久化
 */
public class ParcelableEntity implements Parcelable {

    private int age;

    private String name;

    protected ParcelableEntity(Parcel in) {
        this.age = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<ParcelableEntity> CREATOR = new Creator<ParcelableEntity>() {
        @Override
        public ParcelableEntity createFromParcel(Parcel in) {
            return new ParcelableEntity(in);
        }

        @Override
        public ParcelableEntity[] newArray(int size) {
            return new ParcelableEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.age);
        dest.writeString(this.name);
    }
}

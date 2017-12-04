package io.github.xiewinson.easyrouter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by winson on 2017/11/30.
 */

public class U<T> implements Parcelable{
    private int s;
    private String sss;
    protected U(Parcel in) {
        s = in.readInt();
    }

    public static final Creator<U> CREATOR = new Creator<U>() {
        @Override
        public U createFromParcel(Parcel in) {
            return new U(in);
        }

        @Override
        public U[] newArray(int size) {
            return new U[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(s);
    }
}

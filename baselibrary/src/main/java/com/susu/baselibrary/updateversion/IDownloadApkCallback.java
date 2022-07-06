package com.susu.baselibrary.updateversion;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public interface IDownloadApkCallback extends IInterface {
    void onProgress(int var1, long var2, long var4) throws RemoteException;

    void onResult(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IDownloadApkCallback {
        private static final String DESCRIPTOR = "com.susu.updateversion.IDownloadApkCallback";
        static final int TRANSACTION_onProgress = 1;
        static final int TRANSACTION_onResult = 2;

        public Stub() {
            this.attachInterface(this, "com.susu.updateversion.IDownloadApkCallback");
        }

        public static IDownloadApkCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("com.susu.updateversion.IDownloadApkCallback");
                return (IDownloadApkCallback) (iin != null && iin instanceof IDownloadApkCallback ? (IDownloadApkCallback) iin : new IDownloadApkCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "com.susu.updateversion.IDownloadApkCallback";
            int _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readInt();
                    long _arg1 = data.readLong();
                    long _arg2 = data.readLong();
                    this.onProgress(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readInt();
                    this.onResult(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IDownloadApkCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.susu.updateversion.IDownloadApkCallback";
            }

            public void onProgress(int percent, long count, long total) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.susu.updateversion.IDownloadApkCallback");
                    _data.writeInt(percent);
                    _data.writeLong(count);
                    _data.writeLong(total);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onResult(int code) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.susu.updateversion.IDownloadApkCallback");
                    _data.writeInt(code);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }
}

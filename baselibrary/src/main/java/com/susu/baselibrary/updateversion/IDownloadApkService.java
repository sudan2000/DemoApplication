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
public interface IDownloadApkService extends IInterface {
    void registerCallback(IDownloadApkCallback var1) throws RemoteException;

    void unregisterCallback(IDownloadApkCallback var1) throws RemoteException;

    void injectAutoInstall(boolean var1) throws RemoteException;

    void injectShowProgress(boolean var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IDownloadApkService {
        private static final String DESCRIPTOR = "com.susu.updateversion.IDownloadApkService";
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_unregisterCallback = 2;
        static final int TRANSACTION_injectAutoInstall = 3;
        static final int TRANSACTION_injectShowProgress = 4;

        public Stub() {
            this.attachInterface(this, "com.susu.updateversion.IDownloadApkService");
        }

        public static IDownloadApkService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("com.susu.updateversion.IDownloadApkService");
                return (IDownloadApkService) (iin != null && iin instanceof IDownloadApkService ? (IDownloadApkService) iin : new IDownloadApkService.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "com.susu.updateversion.IDownloadApkService";
            boolean _arg0;
            IDownloadApkCallback _arg1;
            switch (code) {
                case 1:
                    data.enforceInterface(descriptor);
                    _arg1 = com.susu.baselibrary.updateversion.IDownloadApkCallback.Stub.asInterface(data.readStrongBinder());
                    this.registerCallback(_arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    _arg1 = com.susu.baselibrary.updateversion.IDownloadApkCallback.Stub.asInterface(data.readStrongBinder());
                    this.unregisterCallback(_arg1);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(descriptor);
                    _arg0 = 0 != data.readInt();
                    this.injectAutoInstall(_arg0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(descriptor);
                    _arg0 = 0 != data.readInt();
                    this.injectShowProgress(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IDownloadApkService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.susu.updateversion.IDownloadApkService";
            }

            public void registerCallback(IDownloadApkCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.susu.updateversion.IDownloadApkService");
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void unregisterCallback(IDownloadApkCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.susu.updateversion.IDownloadApkService");
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void injectAutoInstall(boolean autoInstall) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.susu.updateversion.IDownloadApkService");
                    _data.writeInt(autoInstall ? 1 : 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void injectShowProgress(boolean showProgress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.susu.updateversion.IDownloadApkService");
                    _data.writeInt(showProgress ? 1 : 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }
}

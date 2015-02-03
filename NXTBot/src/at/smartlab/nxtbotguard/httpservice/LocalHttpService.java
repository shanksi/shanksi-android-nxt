package at.smartlab.nxtbotguard.httpservice;

import java.io.IOException;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import at.smartlab.lego.NxtBrick;


public class LocalHttpService extends Service {
	private Camera camera;
	private GuardHTTPD httpd;
	
	private static YuvImage camImage;
	
	private static NxtBrick nxt = null;
	
	private static String pwd;
	
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	public LocalHttpService getService() {
            return LocalHttpService.this;
        }
    }

    @Override
    public void onCreate() {
    	Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
    	
    	AssetManager am = getAssets();
    	try {	
			httpd = new GuardHTTPD(8080, am);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("LocalService", "Received start id " + startId + ": " + intent);
        Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    	httpd.stop();
    	httpd = null;
   
        // Tell the user we stopped.
        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

	public static void setImage(YuvImage im) {
		synchronized (LocalHttpService.class) {
			camImage = im;
		}
	}

	public static YuvImage getImage() {
		synchronized (LocalHttpService.class) {
			return camImage;
		}
	}
	
	public static String getPwd() {
		synchronized (LocalHttpService.class) {
			return pwd;
		}
	}
	
	public static void setPwd(String p) {
		synchronized (LocalHttpService.class) {
			pwd = p;
		}
	}

	public static NxtBrick getNxt() {
		return nxt;
	}

	public static void setNxt(NxtBrick nxt) {
		LocalHttpService.nxt = nxt;
	}

	
	
}
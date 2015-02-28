

package at.smartlab.nxtbotguard;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.hardware.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.*;
import android.widget.TextView;

import at.smartlab.nxtbotguard.httpservice.LocalHttpService;

import uk.co.shanksi.nxt.*;

import android.net.wifi.*;
import android.widget.*;
import android.view.*;

public class NXTBotGuardActivity extends Activity implements PreviewCallback, Callback, SensorEventListener {
    private Thread.UncaughtExceptionHandler t;
	
	private NxtRobot robot;
	private TextView o;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
	
    final Camera camera = Camera.open();
    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context,
                              Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothClass btc = device.getBluetoothClass();
                Log.d("NXT", "Bluetooth: " + device.getName() + " " + btc.getDeviceClass() + " " + btc.getMajorDeviceClass());

                if (device.getName().equals("NXT")) {
                    Log.d("NXT", "Ok device found:" + device.getName());
					final ToggleButton bluetoothToggle = (ToggleButton) findViewById(R.id.bluetoothToggle);
					bluetoothToggle.setText(device.getAddress());
                    Method m;
                    try {
                        m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                        final BluetoothSocket socket = (BluetoothSocket) m.invoke(device, 1);
                        socket.connect();
						NxtBrick brick=new NxtBrick(socket.getOutputStream(), socket.getInputStream());
                        LocalHttpService.setNxt(brick);
						robot = new NxtRobot(brick);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
	
    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            // no-op -- wait until surfaceChanged()
        }

        public void surfaceChanged(SurfaceHolder holder,
                                   int format, int width,                            int height) {
            initPreview(width, height);
            startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // no-op
        }
    };
    private LocalHttpService mBoundService;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((LocalHttpService.LocalBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
        }
    };
    private boolean mIsBound;
    private SurfaceHolder previewHolder;
    private BluetoothAdapter mBluetoothAdapter = null;

    private void doConnect() {

        // find Bluetooth NXT
        Log.i("NXT", "About to start bluetooth");
        if (mBluetoothAdapter == null) {
            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter != null) {
                // Device does not support Bluetooth
                mBluetoothAdapter.startDiscovery();
                Log.e("BlueMarketing", "Start Discovery");
            }
        }

    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.i("NXT", "Starting");

		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		SharedPreferences settings = getSharedPreferences("CAMPREFS", 0);
        LocalHttpService.setPwd(settings.getString("pwd", ""));


        final Button storeButton = (Button) findViewById(R.id.saveButton);
        storeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("CAMPREFS", 0);
                final EditText et1 = (EditText) findViewById(R.id.password);
                final EditText et2 = (EditText) findViewById(R.id.retype);
                if (et1.getText().toString().equals(et2.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Stored", Toast.LENGTH_SHORT).show();
                    String pwd = et1.getText().toString();
                    LocalHttpService.setPwd(pwd);
                    Editor ed = settings.edit();
                    ed.putString("pwd", pwd);
                    ed.commit();
                }
            }
        });

        WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ipString = String.format(
                "%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));

        Log.i("NXT", ipString);


        final ToggleButton httpToggle = (ToggleButton) findViewById(R.id.httpToggle);
        httpToggle.setTextOn(ipString);
        httpToggle.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton t, boolean b) {
                if (t.isChecked()) doBindService();
                else doUnbindService();
            }
        });

        final ToggleButton bluetoothToggle = (ToggleButton) findViewById(R.id.bluetoothToggle);
        bluetoothToggle.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton t, boolean b) {
				Log.d("NXT", "Checked");
                if (t.isChecked()) doConnect();
            }
        });

        final Button forwardButton = (Button) findViewById(R.id.fwdButton);
		forwardButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
			    if(event.getAction() == event.ACTION_DOWN) {
					if (robot != null) robot.forward();
				}	
				if(event.getAction() == event.ACTION_UP) {
					if(robot != null) robot.stop();
				}	
			    return true;	
			}
		});
		
        final Button leftButton = (Button) findViewById(R.id.leftButton);
		leftButton.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == event.ACTION_DOWN) {
						if (robot != null) robot.leftspin();
					}	
					if(event.getAction() == event.ACTION_UP) {
						if(robot != null) robot.stop();
					}	
					return true;	
				}
			});
		
        final Button rightButton = (Button) findViewById(R.id.rightButton);
		rightButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == event.ACTION_DOWN) {
					if (robot != null) robot.rightspin();
				}	
				if(event.getAction() == event.ACTION_UP) {
					if(robot != null) robot.stop();
				}	
				return true;	
			}
		});
		
		o = (TextView)findViewById(R.id.orientationView);
		
		
        final SurfaceView preview = (SurfaceView) findViewById(R.id.preView);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        camera.setPreviewCallback(this);
    }

    private void initPreview(int width, int height) {
        try {
            camera.setPreviewDisplay(previewHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startPreview() {
        Parameters params = camera.getParameters();
        params.setPreviewFrameRate(1);
        camera.setDisplayOrientation(90);
        camera.setParameters(params);
        camera.startPreview();
    }

    public void onPreviewFrame(byte[] data, Camera camera) {

        Camera.Parameters parameters = camera.getParameters();

        Size size = parameters.getPreviewSize();

        //Log.e("NXTBotGuard", "" + size.width + " " + size.height);

        final YuvImage image = new YuvImage(data, parameters.getPreviewFormat(),
                size.width, size.height, null);

        mBoundService.setImage(image);
        /*
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/out.jpg");
        FileOutputStream filecon = new FileOutputStream(file);
        image.compressToJpeg(
                new Rect(0, 0, image.getWidth(), image.getHeight()), 90,
                filecon);
		*/
    }

    void doBindService() {
        Intent mServiceIntent = new Intent(this, LocalHttpService.class);
        bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        camera.setPreviewCallback(null);
        camera.release();
        doUnbindService();

        unregisterReceiver(mReceiver);
        if (mBluetoothAdapter != null) {
            // Device does not support Bluetooth
            mBluetoothAdapter.cancelDiscovery();
        }

        super.onDestroy();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    protected void onResume() {
        super.onResume();
        mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

	private boolean lock = true;
    public void onSensorChanged(SensorEvent event) {
		try{
		if(lock) {
			lock=false;
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
           SensorManager.getOrientation(mR, mOrientation);
			o.setText(String.format("Orientation: %f, %f, %f",
									 mOrientation[0], mOrientation[1], mOrientation[2]));
            Log.i("OrientationTestActivity", String.format("Orientation: %f, %f, %f",
                                                           mOrientation[0], mOrientation[1], mOrientation[2]));
        }
		}}catch(Exception ex) {}
		lock = true;
    }
}

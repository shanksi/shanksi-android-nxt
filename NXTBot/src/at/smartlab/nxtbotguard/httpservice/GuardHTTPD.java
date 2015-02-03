package at.smartlab.nxtbotguard.httpservice;

import java.io.IOException;


import android.content.res.AssetManager;


import http.NanoHTTPD;


public class GuardHTTPD extends NanoHTTPD  {
	
	
	public GuardHTTPD(int port, AssetManager am) throws IOException {
		super(port, am);
		
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		
		super.finalize();
	}

}

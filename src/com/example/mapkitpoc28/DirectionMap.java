package com.example.mapkitpoc28;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DirectionMap extends Activity implements LocationListener{
	protected LocationManager locationManager;
	protected Context context;
	//protected boolean gps_enabled,network_enabled;
	double curlat,curlong;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		Button mButton = (Button)findViewById(R.id.button1);
//	    mButton.setOnClickListener(new View.OnClickListener() 
//	    {
//	    
//	          public void onClick(View view) 
//	          {
	        	  
	        	  /**String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", destinationLatitude, destinationLongitude, "Where the party is at");
	        	  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
	        	  intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
	        	  startActivity(intent);**/
	        	   
	        	  
	        	  String uri = "http://maps.google.com/maps?saddr=" +  13.006406 +","+80.25101099+"&daddr="+12.98072+","+80.24588;
	        	  Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
	        	 // String url = "http://maps.google.com/maps?saddr="+currentLattitude+","+currentLongitude+"&daddr="+targetLat+","+targetLang+"&mode=driving";
	        	 //String geoUriString = getResources().getString(R.string.map_location);  
	        	  //Uri geoUri = Uri.parse(geoUriString);  
	        	//  Intent mapCall = new Intent(Intent.ACTION_VIEW, geoUri);  
	        	  intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
	        	  try
	        	  {
	        	  startActivity(intent);  
	        	  }
	        	  catch(ActivityNotFoundException Ex)
	        	  {
	        		  try
	                  {
	                      Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW,null);
	                      startActivity(unrestrictedIntent);
	                  }
	                  catch(ActivityNotFoundException innerEx)
	                  {
	                      Toast.makeText(DirectionMap.this, "Please install a maps application", Toast.LENGTH_LONG).show();
	                  }
	        	  }
	          }
	    //});
	//}
	          
	@Override
	public void onLocationChanged(Location location) 
	{
		curlat=location.getLatitude();
		curlong=location.getLongitude();
	}
	@Override
	public void onProviderDisabled(String provider)
	{
		//Log.d("Latitude","disable");
	}
	@Override
	public void onProviderEnabled(String provider) 
	{
		//Log.d("Latitude","enable");
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		//Log.d("Latitude","status");
	}

}

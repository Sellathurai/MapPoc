package com.example.mapkitpoc28;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.spec.MGF1ParameterSpec;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.SAXException;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract.Document;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.LineOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;
import com.mapquest.android.maps.Overlay.OverlayTapListener;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RouteResponse.Collections;
import com.mapquest.android.maps.ServiceResponse.Info;

 




public class MyLocationMap extends MapActivity {
	protected ScrollView scroller;
	String smallastDistance;
	private static final String Tag = null;
	public MapView myMap;
	protected MyLocationOverlay myLocationOverlay;
	protected Button followMeButton;
	GeoPoint currentLocation;
	double latitude,longitude;
	AnnotationView annotation;
	ArrayList<Double> arrayTofindMinDistance = new ArrayList<Double>();
	ArrayList<String> findArea = new ArrayList<String>();
	/** Called when the activity is first created. */
	
	 WebView itinerary;
	final RouteManager routeManager=new RouteManager(this);
	
	
	
	 
	protected void init() {
         
		 
    	annotation = new AnnotationView(myMap);
    	// add an onclick listener to the annotationView
    	annotation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(myMap.getContext(), 
						"Clicked the "+annotation.getOverlayItem().getTitle()+" annotation", 
						Toast.LENGTH_SHORT).show();
			}
		});
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        
        Button mButton = (Button)findViewById(R.id.button1);
       
 
        setupMapView();
        setupMyLocation();
       
        addOverlay();
      
        route();
        http();
        
        
    }

   

     

    
	private void setupMapView() {
		this.myMap = (MapView) findViewById(R.id.map);
		
		// enable the zoom controls
		myMap.setBuiltInZoomControls(true);
	}
	
	protected void setupMyLocation() {
		this.myLocationOverlay = new MyLocationOverlay(this, myMap);
		
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				 currentLocation = myLocationOverlay.getMyLocation(); 
				latitude=currentLocation.getLatitude();
				longitude=currentLocation.getLongitude();
				System.out.println("My location coordinate "+currentLocation.toString());
				myMap.getController().animateTo(currentLocation);
				myMap.getController().setZoom(14);
				myMap.getOverlays().add(myLocationOverlay);
				myLocationOverlay.setFollowing(true);
				
				

				
			}
		});
	}
	
	 public boolean onTap(GeoPoint p, MapView map) 
	 {
		 
		 System.out.println("on taped");
		return false;
		 
	 }
	
	public void alertMessge(String msg){
	
	AlertDialog alertDialog = new AlertDialog.Builder(MyLocationMap.this)
	.create();
 
    alertDialog.setTitle("My current location Address");

  
           alertDialog.setMessage(""+msg);

           alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {

}
});

// Showing Alert Message
         alertDialog.show();
	}
	public String findAddress(double LATITUDE, double LONGITUDE)
	{
		
		
		List<Address> addresses = null;
		try {
			// Get Address by using Latitude and Longitude value
 
			addresses = new Geocoder(MyLocationMap.this, Locale.getDefault())
			.getFromLocation(LATITUDE,
					LONGITUDE, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String address = "";
		if (addresses != null && addresses.size() >= 0) {
			address = addresses.get(0).getAddressLine(0);

			if (addresses != null && addresses.size() >= 1) {
				address += ", " + addresses.get(0).getAddressLine(1);
			}

			if (addresses != null && addresses.size() >= 2) {
				address += ", " + addresses.get(0).getAddressLine(2);
			}
			if (addresses != null && addresses.size() >= 3) {
				address += ", " + addresses.get(0).getPostalCode();
				address += ", " + addresses.get(0).getAddressLine(3);

			}
			if (addresses != null && addresses.size() >= 4) {
				address += ", " + addresses.get(0).getAddressLine(4);
				
			}
		}

		// Set the address in TextView
		return address;
	//	callSMS.setText(address);
	}
	
	public String getaddress(double LATITUDE, double LONGITUDE) throws IOException{
		List<Address> addresses;
		 Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

		String address1 = addresses.get(0).getAddressLine(0);
		String city = addresses.get(0).getAddressLine(1);
		return address1;
		
		
	}
	
	private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
	
	@Override
	protected void onResume() {
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
		
		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableCompass();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	public boolean isRouteDisplayed() {
		return true;
	}
	///To find route between two location
	private void displayRoute(double fromLat,double fromLong,double toLat,double toLong) {
	      RouteManager routeManager = new RouteManager(this);
	      routeManager.setMapView(myMap);
	      String fromLattitude=""+fromLat;
	      String fromLongitidue=""+fromLong;
	      String toLattitude=""+toLat;
	      String toLongitude=""+toLong;
	      
	      routeManager.createRoute(fromLattitude+","+ fromLongitidue,toLattitude+","+toLongitude);
	    }

	
	 

	
	         private void addOverlay() {
		 
		         try {
			 
			    Drawable icon = getResources().getDrawable(R.drawable.location_marker);
		    	DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(icon);
		    	CustomItemizedOverlay customoverlay=new CustomItemizedOverlay(icon, this);
		    	
		    	 List<Overlay> mapOverlays = myMap.getOverlays();
		    	Drawable tracktor = this.getResources().getDrawable(R.drawable.redtractorcard);
		    	Drawable timber = this.getResources().getDrawable(R.drawable.timper);
		    	Drawable livehood = this.getResources().getDrawable(R.drawable.agri);
		    	Drawable truck = this.getResources().getDrawable(R.drawable.truck);
		    	Drawable smalTractor = this.getResources().getDrawable(R.drawable.tracktor1);


		    	//Set the bounding for the drawable
		    	tracktor.setBounds(
		    	    0 - icon.getIntrinsicWidth() / 1, 0 - icon.getIntrinsicHeight(), 
		    	    icon.getIntrinsicWidth() / 1, 0);
		    	timber.setBounds(
			    	    0 - icon.getIntrinsicWidth() / 1, 0 - icon.getIntrinsicHeight(), 
			    	    icon.getIntrinsicWidth() / 1, 0);
		    	livehood.setBounds(
			    	    0 - icon.getIntrinsicWidth() / 1, 0 - icon.getIntrinsicHeight(), 
			    	    icon.getIntrinsicWidth() / 1, 0);

		    	truck.setBounds(
			    	    0 - icon.getIntrinsicWidth() / 1, 0 - icon.getIntrinsicHeight(), 
			    	    icon.getIntrinsicWidth() / 1, 0);
		    	smalTractor.setBounds(
			    	    0 - icon.getIntrinsicWidth() / 1, 0 - icon.getIntrinsicHeight(), 
			    	    icon.getIntrinsicWidth() / 1, 0);
		    	OverlayItem adyar = new OverlayItem(new GeoPoint(13.001177,80.256495), "Vehicle:Tractor", "Adyar Mob:98982323423");
		    	overlay.addItem(adyar);
		    	customoverlay.addOverlay(adyar);
		    	adyar.setMarker(smalTractor);
		    	//mapOverlays.add(customoverlay);
		    	//Set the new marker to the overlay
		    	OverlayItem velachery = new OverlayItem(new GeoPoint(12.98072,80.22376), "Vehicle:HarvestMachine", "Velachery ,Mob:98348349589");
		    	overlay.addItem(velachery);
		    	customoverlay.addOverlay(velachery);
		    	velachery.setMarker(truck);
		    	//mapOverlays.add(customoverlay);
		    	
		    	OverlayItem thiruvamiyur = new OverlayItem(new GeoPoint(12.982672,80.263380), "Vehicle:bullock cart", "Thiruvamiyur, Mob:9586948955");
		    	overlay.addItem(thiruvamiyur);
		    	customoverlay.addOverlay(thiruvamiyur);
		    	//mapOverlays.add(customoverlay);
		    	thiruvamiyur.setMarker(livehood);
		    	
		    	OverlayItem guinty = new OverlayItem(new GeoPoint(13.010133,80.211216), "Vehicle:Tractor ", "Guindy,Mob:98483348448");
		    	overlay.addItem(guinty);
		    	customoverlay.addOverlay(guinty);
		    	guinty.setMarker(timber);
		    	//mapOverlays.add(customoverlay);
		    	
		    	OverlayItem kasthuriBai = new OverlayItem(new GeoPoint(13.0170908,80.244677), "Vehicle:Farm Truck", "Kotturpuram, Mob:9898256743");
		    	overlay.addItem(kasthuriBai);
		    	customoverlay.addOverlay(kasthuriBai);
		    	kasthuriBai.setMarker(tracktor);
		    	//mapOverlays.add(customoverlay);
		    	
		    	//To find distance between two point
		    	double adyardistance= findDistance(13.006406, 80.25101099, 13.001177, 80.256495,"Adyar");
		    	double guintydistance= findDistance(13.006406, 80.25101099, 13.010133, 80.211216,"Guindy");
		    	double thiruvamiyurdistance= findDistance(13.006406, 80.25101099, 13.03246, 80.24588,"Thiruvanmiyur");
		    	double velacherydistance= findDistance(13.006406, 80.25101099, 12.98072, 80.24588,"Velachery");
		    	
		    	displayRoute(13.006406, 80.25101099, 13.001177, 80.256495);
		    	displayRoute(13.006406, 80.25101099, 13.010133, 80.211216);
		    	displayRoute(13.006406, 80.25101099, 12.982672, 80.2633809);
		    	displayRoute(13.006406, 80.25101099, 12.98072, 80.223766);
		    	displayRoute(13.006406, 80.25101099, 13.017090, 80.2446778);
		    	
		    	 
		    	 //to find smallest distance
		    	arrayTofindMinDistance.add(adyardistance);
		    	arrayTofindMinDistance.add(guintydistance);
		    	arrayTofindMinDistance.add(thiruvamiyurdistance);
		    	
		    	arrayTofindMinDistance.add(velacherydistance);
		    	
		    	
		    	double smallValue=getDifference();
		    	
		    	//alertMessge("Nearest location from my area is "+smallastDistance+"which is around"+smallValue+"KM");
		    	System.out.println("min value out"+smallValue);
		    	 
		    	Toast.makeText(getApplicationContext(), "Nearest location from my area is  "+ smallastDistance +"Around"+smallValue +"KM", Toast.LENGTH_SHORT).show();
 
	           overlay.setOnFocusChangeListener(new ItemizedOverlay.OnFocusChangeListener() {
					@Override
					public void onFocusChanged(ItemizedOverlay overlay, OverlayItem newFocus) {
						// when focused item changes
						myMap.getController().animateTo(newFocus.getPoint());
						Toast.makeText(myMap.getContext().getApplicationContext(), newFocus.getTitle() + ": " + 
								newFocus.getSnippet(), Toast.LENGTH_SHORT).show();
						
					}		
		    	});
	           
	         
	            myMap.getOverlays().add(overlay);
		    	myMap.invalidate();
		    	// test setting the focus on a specific item
		    	overlay.setFocus(guinty);
		   
		} catch (Exception e) {
			e.printStackTrace();
		}
	   
	    }
	 
	 
	          public void route() {
 
			//create a route manager
			    routeManager.setMapView(myMap);
			   
			   routeManager.setRouteCallback(new RouteManager.RouteCallback() {
				@Override
				public void onError(RouteResponse routeResponse) {
					Info info=routeResponse.info;
					int statusCode=info.statusCode;
					
					StringBuilder message =new StringBuilder();
					message.append("Unable to create route.\n")
						.append("Error: ").append(statusCode).append("\n")
						.append("Message: ").append(info.messages);
					Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
					
				}

				@Override
				public void onSuccess(RouteResponse arg0) {
					// TODO Auto-generated method stub
				}
 	          
	          });
	          }
	 
	        public void findMultipleRout() {
		 
				//build a list of the locations
				List<String> locations=new ArrayList<String>();
				locations.add("Chennai, Tamil Nadu");
				locations.add("Salem, Tamil Nadu");
				locations.add("Trichy, Tamil Nadu");

				//run the route
				routeManager.createRoute(locations);
			}
	 
	 
	   public double getDifference()  
	    {  
	        //  
	        double smallest = arrayTofindMinDistance.get(0);  
	        double largest = arrayTofindMinDistance.get(0);  
	        for (int i=0; i<arrayTofindMinDistance.size(); i++)  
	        {  
	            if(arrayTofindMinDistance.get(i)>largest) 
	            {
	                largest = arrayTofindMinDistance.get(i);  
	            }
	            else if(arrayTofindMinDistance.get(i)<smallest) 
	            {
	                smallest = arrayTofindMinDistance.get(i); 
	            smallastDistance=findArea.get(i);
	            }
	            System.out.println("inner loop "+smallastDistance);
	        }  
	        System.out.println("outer loop "+smallastDistance);
	       // double difference = largest - smallest;  
	        return smallest;  
	    }  
	
	   

	   private void addEventText(String text){
		   Toast.makeText(getApplicationContext(), " "+ text, Toast.LENGTH_SHORT).show();	
			 
		
		}
	 //private void showLineOverlay(double fromLatitude, double fromlongitude,double toLatitude, double tolongitude) {
		 private void showLineOverlay() {
		 try {
			 
			 Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		        paint.setColor(Color.BLUE);
		        paint.setAlpha(100);
		        paint.setStyle(Paint.Style.STROKE);
		        paint.setStrokeJoin(Paint.Join.ROUND);
		        paint.setStrokeCap(Paint.Cap.ROUND);
		        paint.setStrokeWidth(5);

				// sample line data
				List<GeoPoint> routeData = new ArrayList<GeoPoint>();
				routeData.add(new GeoPoint(13.006406, 80.25101099));
				routeData.add(new GeoPoint(13.001177, 80.256495));
				routeData.add(new GeoPoint(13.006406, 80.25101099));
				routeData.add(new GeoPoint(13.010133, 80.211216));
				routeData.add(new GeoPoint(13.006406, 80.25101099));
				routeData.add(new GeoPoint(13.0170908,80.244677));
				routeData.add(new GeoPoint(13.006406, 80.25101099));
				routeData.add(new GeoPoint(12.982672,80.263380));
				routeData.add(new GeoPoint(13.006406, 80.25101099));
				routeData.add(new GeoPoint(12.98072,80.22376));
				
 
				
		        LineOverlay line = new LineOverlay(paint);
		        line.setData(routeData);
		        line.setKey("Line #1");

				line.setTapListener(new LineOverlay.OverlayTapListener() {			
					@Override
					public void onTap(GeoPoint gp, MapView mapView) {
						System.out.println("line click event lat / long "+gp.getLatitude()+gp.getLongitude());
						
						double fromLat=gp.getLatitude();
						double fromng=gp.getLongitude();
						
						double distance=findDistance(latitude, longitude, fromLat, fromng,"");
						DecimalFormat form = new DecimalFormat("0.00");
						
						double exactDistInkm=distance/1000;
						
						
						
						Toast.makeText(getApplicationContext(), "Distance from my location in KM  "+ form.format(exactDistInkm) +"KM", Toast.LENGTH_SHORT).show();			
					}
				});
				myMap.getOverlays().add(line);
		        
				myMap.invalidate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	        
		}
		
	

		public double findDistance(double fromLatitude, double fromlongitude,double toLatitude, double tolongitude,String Area  ){
			
			double distance =0.0;
			try {
				Location from = new Location(""); 
				from.setLatitude(fromLatitude); 
				from.setLongitude(fromlongitude);  
				Location to = new Location(""); 
				to.setLatitude(toLatitude); 
				to.setLongitude(tolongitude);  
				 distance = from.distanceTo(to);
				 distance=distance/1000;
				 DecimalFormat form = new DecimalFormat("0.00");
				 form.format(distance);
				 System.out.println("the distance value "+distance);
				 findArea.add(Area);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return distance;
		}


		public static String getUrl(double fromLat, double fromLon,
				   double toLat, double toLon) {// connect to map web service
				  StringBuffer urlString = new StringBuffer();
				  urlString.append("http://maps.google.com/maps?f=d&hl=en");
				  urlString.append("&saddr=");// from
				  urlString.append(Double.toString(fromLat));
				  urlString.append(",");
				  urlString.append(Double.toString(fromLon));
				  urlString.append("&daddr=");// to
				  urlString.append(Double.toString(toLat));
				  urlString.append(",");
				  urlString.append(Double.toString(toLon));
				  urlString.append("&ie=UTF8&0&om=0&output=kml");
				  return urlString.toString();
				 }
		
		
		

		public void http()
		{ 
		
			
			HttpClient Client = new DefaultHttpClient();
        
              String URL = "http://www.mapquestapi.com/directions/v2/route?key=Kmjtd%7Cluu22gurnq%2Can%3Do5-gzrl&from=Lancaster,PA&to=York,PA&callback=renderNarrative";

                 try
                       {
                      String SetServerString = "";
            
                    // Create Request to server and get response
            
                      HttpGet httpget = new HttpGet(URL);
                     ResponseHandler<String> responseHandler = new BasicResponseHandler();
                     SetServerString = Client.execute(httpget, responseHandler);
                     
                     System.out.println("server response "+SetServerString);
            
         }
       catch(Exception ex)
          {
                System.out.println("Fail!");
           }
    }
 

	    
//	    public void startRoute()
//	    {
//	    	
//	    	
//	    	double src_lat = 25.04202; // the testing source 
//	    	double src_long = 121.534761; 
//	    	double dest_lat = 25.05202; // the testing destination 
//	    	double dest_long = 121.554761; 
//	    	GeoPoint srcGeoPoint = new GeoPoint((int) (src_lat * 1E6), 
//	    	(int) (src_long * 1E6)); 
//	    	GeoPoint destGeoPoint = new GeoPoint((int) (dest_lat * 1E6), 
//	    	(int) (dest_long * 1E6)); 
	//
//	    	DrawPath(srcGeoPoint, destGeoPoint, Color.GREEN, myMap); 
	//
//	    	myMap.getController().animateTo(srcGeoPoint); 
//	    	myMap.getController().setZoom(15); 
	//
//	    	//Read more: http://tw.tonytuan.org/2009/06/android-driving-direction-route-path.html#ixzz2vBOZQmsz
//	    }
		 
		
		 private void DrawPath(GeoPoint src, GeoPoint dest, MapView mMapView) {

		        // connect to map web service
		        StringBuilder urlString = new StringBuilder();
		        urlString.append("http://maps.google.com/maps?f=d&hl=en");
		        urlString.append("&saddr=");// from
		        urlString.append(Double.toString((double) src.getLatitudeE6() / 1.0E6));
		        urlString.append(",");
		        urlString.append(Double.toString((double) src.getLongitudeE6() / 1.0E6));
		        urlString.append("&daddr=");// to
		        urlString.append(Double.toString((double) dest.getLatitudeE6() / 1.0E6));
		        urlString.append(",");
		        urlString.append(Double.toString((double) dest.getLongitudeE6() / 1.0E6));
		        urlString.append("&ie=UTF8&om=1&output=kml");
		        Log.d("Map directions", "URL= " + urlString.toString());

		        // get the kml (XML) doc. And parse it to get the coordinates(direction route).
		        org.w3c.dom.Document doc = null;
		        HttpURLConnection urlConnection = null;
		        URL url = null;
		        try {
		            url = new URL(urlString.toString());
		            urlConnection = (HttpURLConnection) url.openConnection();
		            urlConnection.setRequestMethod("GET");
		            urlConnection.setDoOutput(true);
		            urlConnection.setDoInput(true);
		            urlConnection.connect();

		            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		            DocumentBuilder db = dbf.newDocumentBuilder();
		            doc = db.parse(urlConnection.getInputStream());

		            if (doc.getElementsByTagName("GeometryCollection").getLength() > 0) {
		                String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getFirstChild().getNodeValue();
		                Log.d("Map directions", "path= " + path);
		                String[] pairs = path.split(" ");
		                String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude
		                                                        // lngLat[1]=latitude
		                                                        // lngLat[2]=height
		                GeoPoint startGP = new GeoPoint(
		                        (int) (Double.parseDouble(lngLat[1]) * 1E6),
		                        (int) (Double.parseDouble(lngLat[0]) * 1E6));

		                mMapView.getOverlays().add(new MyOverLay(startGP, startGP, 1));//starting point
		                GeoPoint gp1 = null;
		                GeoPoint gp2 = startGP;
		                for (int i = 1; i <pairs.length; i++)
		                {
		                    lngLat = pairs[i].split(",");
		                    gp1 = gp2;
		                    // watch out! For GeoPoint, first:latitude, second:longitude
		                    gp2 = new GeoPoint(
		                            (int) (Double.parseDouble(lngLat[1]) * 1E6),
		                            (int) (Double.parseDouble(lngLat[0]) * 1E6));

		                    mMapView.getOverlays().add(new MyOverLay(gp1, gp2, 2));//route
		                }

		                mMapView.getOverlays().add(new MyOverLay(dest, dest, 3));   //last point
		            }
		        } catch (MalformedURLException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (ParserConfigurationException e) {
		            e.printStackTrace();
		        } catch (SAXException e) {
		            e.printStackTrace();
		        }
		    }
		
		
}
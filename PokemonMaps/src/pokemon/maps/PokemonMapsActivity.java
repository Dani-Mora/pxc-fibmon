package pokemon.maps;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapquest.android.maps.BoundingBox;
import com.mapquest.android.maps.DrawableOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.Overlay;

public class PokemonMapsActivity extends MapActivity {
   
	public static int _indexPokemon = -1;
	
	protected MapView map;
    private MyLocationOverlay myLocationOverlay;
	
    private double longitude =2.113055 ;
    private double latitude = 41.389379;
    
    private GeoPoint point1 = new GeoPoint(41.390239, 2.10823);
    private GeoPoint point2 = new GeoPoint(41.385201, 2.121813);
        
    private DrawableOverlay drawableOverlay = null;
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //setContentView(R.layout.galery);
      setContentView(R.layout.main);
      
      setupMapView();
      setupMyLocation();
      showDrawableOverlay();      
      
     setupButtons();
    }

	public void setupButtons() {
		
		final Double dif = 0.0001;
		
		/*** EDIT POINT1 ***/
		final Button button = (Button) findViewById(R.id._left1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                point1 = new GeoPoint(point1.getLatitude(), point1.getLongitude() - dif);               
                showDrawableOverlay();
            }
        });
        
        final Button button1 = (Button) findViewById(R.id._right1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                point1 = new GeoPoint(point1.getLatitude(), point1.getLongitude()  + dif);
                showDrawableOverlay();
            }
        });
        
        final Button button2 = (Button) findViewById(R.id._up1);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                point1 = new GeoPoint(point1.getLatitude()  + dif, point1.getLongitude());
                showDrawableOverlay();
            }
        });
        
        final Button button3 = (Button) findViewById(R.id._down1);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                point1 = new GeoPoint(point1.getLatitude()  - dif, point1.getLongitude());
                showDrawableOverlay();
            }
        });     
        /*** EDIT POINT2 ***/
        final Button button4 = (Button) findViewById(R.id._left2);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                point2 = new GeoPoint(point2.getLatitude(), point2.getLongitude() - dif);
                showDrawableOverlay();
            }
        });  
        
        final Button button5 = (Button) findViewById(R.id._right2);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                point2 = new GeoPoint(point2.getLatitude(), point2.getLongitude() + dif);
                showDrawableOverlay();
            }
        });
        
        final Button button6 = (Button) findViewById(R.id._up2);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                point2 = new GeoPoint(point2.getLatitude() + dif, point2.getLongitude());
                showDrawableOverlay();
            }
        });
        
        final Button button7 = (Button) findViewById(R.id._down2);
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                point2 = new GeoPoint(point2.getLatitude() - dif, point2.getLongitude());
                showDrawableOverlay();
            }
        });    
	}	
	
    // return false since no route is being displayed 
    @Override
    public boolean isRouteDisplayed() {
      return false;
    }
    
    /**
	 * Overlays the mapquest logo with an assumed bounding box.
	 */
	private void showDrawableOverlay() {	
		System.out.println("Elements: " + map.getOverlays().size());
		System.out.println(String.format("Coordenades [%1$,.5f - %2$,.5f] - [%3$,.5f - %4$,.5f]", point1.getLatitude(), point1.getLongitude(), point2.getLatitude(), point2.getLongitude()));
		
		BoundingBox bbox = new BoundingBox(point1, point2);
		Drawable drawable = getResources().getDrawable(R.drawable.mapa);    
		DrawableOverlay drawableOverlay = new DrawableOverlay(drawable, bbox);
			
		if(this.map.getOverlays().size() > 0) {
			map.getOverlays().set(0, drawableOverlay);
		}
		else this.map.getOverlays().add(0,drawableOverlay);
		
		this.map.invalidate();
	}
    
 // set your map and enable default zoom controls 
    private void setupMapView() {
    	// set the zoom level, center point and enable the default zoom controls 
        this.map = (MapView) findViewById(R.id.map);
        map.getController().setZoom(15);
        map.getController().setCenter(new GeoPoint(latitude, longitude));
        map.setBuiltInZoomControls(true);
    }
    
    // set up a MyLocationOverlay and execute the runnable once we have a location fix 
    private void setupMyLocation() {
      this.myLocationOverlay = new MyLocationOverlay(this, map);
      try {
    	  if(_indexPokemon >= 0) myLocationOverlay.setMarker(getResources().getDrawable(_indexPokemon), MyLocationOverlay.CENTER_HORIZONTAL);
      }
      catch(Exception e) {
    	  System.out.println("Error: " + e.getMessage());
      }
      
      myLocationOverlay.enableMyLocation();
      myLocationOverlay.runOnFirstFix(new Runnable() {
        public void run() {
          GeoPoint currentLocation = myLocationOverlay.getMyLocation();
          //map.getController().animateTo(currentLocation);
          map.getController().setCenter(currentLocation);
          map.getController().setZoom(16);
          map.getOverlays().add(myLocationOverlay);
          myLocationOverlay.setFollowing(true);
        }
      });
    }
    
 // enable features of the overlay 
    @Override
    protected void onResume() {
      myLocationOverlay.enableMyLocation();
      myLocationOverlay.enableCompass();
      super.onResume();
    }

    // disable features of the overlay when in the background 
    @Override
    protected void onPause() {
      super.onPause();
      myLocationOverlay.disableCompass();
      myLocationOverlay.disableMyLocation();
    }

  }


/*
drawableOverlay.setTapListener(new Overlay.OverlayTapListener() {			
	@Override
	public void onTap(GeoPoint p, MapView mapView) {
		Toast.makeText(getApplicationContext(), "Drawable Tapped!", Toast.LENGTH_SHORT).show();				
	}
});*/
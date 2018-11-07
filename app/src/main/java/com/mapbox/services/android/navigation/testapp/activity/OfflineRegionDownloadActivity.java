package com.mapbox.services.android.navigation.testapp.activity;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.testapp.R;

import butterknife.OnClick;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;

public class OfflineRegionDownloadActivity extends AppCompatActivity {

  private MapView mapView;
  private TextView downloadButton;
  private View selectionBox;
  private MapboxMap mapboxMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
//    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_offline_region_download);

    // Define our views, ones the center box and the others our view container used for the
    // snackbar.
    selectionBox = findViewById(R.id.selection_box);
//    final View viewContainer = findViewById(R.id.query_feature_count_map_container);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(mapboxMap -> {

      this.mapboxMap = mapboxMap;
      GeoJsonSource geoJsonSource = new GeoJsonSource("highlighted-shapes-source");

      mapboxMap.addSource(geoJsonSource);
//      geoJsonSource.


      // add a layer to the map that highlights the maps buildings inside the bounding box.
      FillLayer fillLayer =
        new FillLayer("highlighted-shapes-layer", "highlighted-shapes-source")
          .withProperties(fillColor(Color.parseColor("#50667F")));

      mapboxMap.addLayer(fillLayer);



      // Toast instructing user to tap on the box
//        Toast.makeText(
//          FeatureCountActivity.this,
//          getString(R.string.tap_on_feature_box_instruction),
//          Toast.LENGTH_LONG
//        ).show();

//        selectionBox.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View view) {
      // Perform feature query within the selectionBox. The bounding box is
      // calculated using the view but you can also use a map bounding box made up
      // of latitudes and longitudes.
//            int top = selectionBox.getTop() - mapView.getTop();
//            int left = selectionBox.getLeft() - mapView.getLeft();
//            RectF box = new RectF(left, top, left + selectionBox.getWidth(), top + selectionBox.getHeight());
//            List<Feature> features = mapboxMap.queryRenderedFeatures(box, "building");

      // Show the features count
//            Snackbar.make(
//              viewContainer,
//              String.format(getString(R.string.feature_count_snackbar_feature_size), features.size()),
//              Snackbar.LENGTH_LONG).show();

//            mapboxMap.getProjection().getVisibleRegion()

//            GeoJsonSource source = mapboxMap.getSourceAs("highlighted-shapes-source");
//            if (source != null) {
//              source.setGeoJson(FeatureCollection.fromFeatures(features));
//            }
//          }
//        });
//      }
    });
  }

  @OnClick(R.id.download_button)
  public void onDownloadClick() {
    int top = selectionBox.getTop() - mapView.getTop();
    int left = selectionBox.getLeft() - mapView.getLeft();
    RectF box = new RectF(left, top, left + selectionBox.getWidth(), top + selectionBox.getHeight());
    LatLng topLeft = mapboxMap.getProjection().fromScreenLocation(new PointF(left, top));
    LatLng bottomRight = mapboxMap.getProjection().fromScreenLocation(new PointF(left + selectionBox.getWidth(), top + selectionBox.getHeight()));
//    BoundingBox.
//      projec
  }



  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}
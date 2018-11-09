package com.mapbox.services.android.navigation.v5.navigation.offline;

import com.mapbox.api.routetiles.v1.MapboxRouteTiles;
import com.mapbox.geojson.BoundingBox;

import retrofit2.Callback;

/**
 * This is a wrapper class for the {@link MapboxRouteTiles} class. This class takes care of
 * interfacing with {@link MapboxRouteTiles}, but also starts a download task to download the TAR
 * file which is returned.
 */
public class OfflineTiles {

  private final MapboxRouteTiles mapboxRouteTiles;

  OfflineTiles(MapboxRouteTiles mapboxRouteTiles) {
    this.mapboxRouteTiles = mapboxRouteTiles;
  }

  /**
   * @return
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   *
   */
  public void getRouteTiles(Callback callback) {
    mapboxRouteTiles.enqueueCall(callback);
  }

  /**
   *
   */
  public static class Builder {
    private final MapboxRouteTiles.Builder mapboxRouteTilesBuilder;
    private String version;

    Builder() {
      mapboxRouteTilesBuilder = MapboxRouteTiles.builder();
    }

    /**
     * @param version
     * @return
     */
    public Builder version(String version) {
      this.version = version;
      mapboxRouteTilesBuilder.version(version);
      return this;
    }

    /**
     *
     * @param accessToken
     * @return
     */
    public Builder accessToken(String accessToken) {
      mapboxRouteTilesBuilder.accessToken(accessToken);
      return this;
    }

    /**
     *
     * @param boundingBox
     * @return
     */
    public Builder boundingBox(BoundingBox boundingBox) {
      mapboxRouteTilesBuilder.boundingBox(boundingBox);
      return this;
    }

    public String version() {
      return version;
    }

    /**
     *
     * @return
     */
    public OfflineTiles build () {
      return new OfflineTiles(mapboxRouteTilesBuilder.build());
    }
  }


}

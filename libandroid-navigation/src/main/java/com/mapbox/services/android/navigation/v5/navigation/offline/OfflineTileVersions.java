package com.mapbox.services.android.navigation.v5.navigation.offline;

import com.mapbox.api.routetiles.v1.versions.MapboxRouteTileVersions;
import com.mapbox.api.routetiles.v1.versions.models.RouteTileVersionsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is a wrapper class for the {@link MapboxRouteTileVersions} class. It returns a list of
 * all available versions of Routing Tiles available via {@link OfflineTiles}. This class
 * encapsulates the unwrapping of the list from the response.
 */
public class OfflineTileVersions {

  private final MapboxRouteTileVersions mapboxRouteTileVersions;

  /**
   * Creates a new OfflineTileVersions object with the given access token.
   *
   * @param accessToken to use
   */
  public OfflineTileVersions(String accessToken) {
    this.mapboxRouteTileVersions =
      MapboxRouteTileVersions.builder()
        .accessToken(accessToken)
        .build();
  }

  /**
   * Call to receive all the available versions of Offline Tiles available.
   *
   * @param versionsListener to be updated with the versions
   */
  public void getRouteTileVersions(final VersionsListener versionsListener) {
    mapboxRouteTileVersions.enqueueCall(new Callback<RouteTileVersionsResponse>() {
      @Override
      public void onResponse(Call<RouteTileVersionsResponse> call, Response<RouteTileVersionsResponse> response) {
        versionsListener.updateVersions(response.body().availableVersions());
      }

      @Override
      public void onFailure(Call<RouteTileVersionsResponse> call, Throwable throwable) {

      }
    });
  }

  /**
   * Interface to listen to be updated with the versions when they are recieved.
   */
  public interface VersionsListener {
    void updateVersions(List<String> versions);
  }
}

package com.mapbox.services.android.navigation.v5.navigation.offline;

import com.mapbox.api.routetiles.v1.versions.MapboxRouteTileVersions;
import com.mapbox.api.routetiles.v1.versions.models.RouteTileVersionsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflineTileVersions {

  private final MapboxRouteTileVersions mapboxRouteTileVersions;

  public OfflineTileVersions(String accessToken) {
    this.mapboxRouteTileVersions =
      MapboxRouteTileVersions.builder()
        .accessToken(accessToken)
        .build();
  }

  /**
   *
   */
  public void getRouteTileVersions(final VersionsListener versionsListener) {
    mapboxRouteTileVersions.enqueueCall(new Callback<RouteTileVersionsResponse>() {
      @Override
      public void onResponse(Call<RouteTileVersionsResponse> call, Response<RouteTileVersionsResponse> response) {
        versionsListener.updateVersions(response.body().availableVersions());
      }

      @Override
      public void onFailure(Call<RouteTileVersionsResponse> call, Throwable t) {

      }
    });
  }

  public interface VersionsListener {
    void updateVersions(List<String> versions);
  }
}

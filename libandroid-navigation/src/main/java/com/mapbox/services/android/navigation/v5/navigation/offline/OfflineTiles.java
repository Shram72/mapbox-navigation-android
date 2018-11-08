package com.mapbox.services.android.navigation.v5.navigation.offline;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.api.routetiles.v1.MapboxRouteTiles;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.services.android.navigation.v5.utils.DownloadTask;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
@AutoValue
public abstract class OfflineTiles {
//  Navigator navigator

  /**
   *
   * @return
   */
  @NonNull
  public abstract String destPath();

  /**
   *
   * @return
   */
  @NonNull
  public abstract MapboxRouteTiles mapboxRouteTiles();

  /**
   *
   * @return
   */
  @Nullable
  public abstract DownloadTask.DownloadListener downloadListener();

  /**
   *
   * @return
   */
  public static Builder builder() {
    return new AutoValue_OfflineTiles.Builder();
  }

  /**
   *
   */
  public void getRouteTiles() {
    mapboxRouteTiles().enqueueCall(new DownloadCallback());
  }

  /**
   *
   */
  @AutoValue.Builder
  public abstract static class Builder {
    private final MapboxRouteTiles.Builder mapboxRouteTilesBuilder;

    Builder() {
      mapboxRouteTilesBuilder = MapboxRouteTiles.builder();
    }

    /**
     *
     * @param version
     * @return
     */
    public Builder version(String version) {
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

    /**
     *
     * @param destPath
     * @return
     */
    public abstract Builder destPath(String destPath);

    /**
     *
     * @param downloadListener
     * @return
     */
    public abstract Builder downloadListener(DownloadTask.DownloadListener downloadListener);

    abstract Builder mapboxRouteTiles(MapboxRouteTiles mapboxRouteTiles);

    abstract OfflineTiles autoBuild();

    /**
     *
     * @return
     */
    public OfflineTiles build() {
      mapboxRouteTiles(mapboxRouteTilesBuilder.build());

      return autoBuild();
    }
  }

  private class DownloadCallback implements Callback<ResponseBody> {

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
      new DownloadTask(destPath(), "tar",
        new DownloadTask.DownloadListener() {
          @Override
          public void onFinishedDownloading(@NonNull File file) {
            DownloadTask.DownloadListener downloadListener = OfflineTiles.this.downloadListener();
            if (downloadListener != null) {
              downloadListener.onFinishedDownloading(file);
            }

//            navigator.unpackTiles(destPath(), destPath() + "/tiles");
          }

          @Override
          public void onErrorDownloading() {
            DownloadTask.DownloadListener downloadListener = OfflineTiles.this.downloadListener();
            if (downloadListener != null) {
              downloadListener.onErrorDownloading();
            }
          }

          // todo change to THREAD_POOL_EXECUTOR
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, response.body());


    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }
  }
}

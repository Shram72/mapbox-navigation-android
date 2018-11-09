package com.mapbox.services.android.navigation.v5.navigation.offline;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.mapbox.services.android.navigation.v5.utils.DownloadTask;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class serves to contain the complicated chain of events that must happen to download
 * offline routing tiles. It creates and maintains a directory structure with the root in the
 * Offline directory, or wherever someone specifies.
 */
public class RoutingTileDownloadManager {

  enum State {
    NOT_STARTED, WAITING_FOR_RESPONSE, DOWNLOADING_TAR, UNPACKING_TAR, COMPLETED
  }

  private State state = State.NOT_STARTED; // todo manage errors sent to listener with state info
  private final File destDirectory;
  private final File tileDirectory;
  private String version;

  private RoutingTileDownloadListener listener;

  public RoutingTileDownloadManager() {
    this(Environment.getExternalStoragePublicDirectory("Offline"));
  }

  public RoutingTileDownloadManager(File destDirectory) {
    this.destDirectory = destDirectory;
    tileDirectory = new File(destDirectory, "tiles");
  }

  public void setListener(RoutingTileDownloadListener listener) {
    this.listener = listener;
  }

  public void startDownloadChain(final OfflineTiles.Builder builder) {

    state = State.WAITING_FOR_RESPONSE;
    version = builder.version();
    OfflineTiles offlineTiles = builder.build();

    offlineTiles.getRouteTiles(new Callback<ResponseBody>() {

      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        state = State.DOWNLOADING_TAR;

        new DownloadTask(
          tileDirectory.getAbsolutePath(),
          version,
          "tar",
          getDownloadListener())
          .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response.body());
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable throwable) {
        onError();
      }
    });
  }

  private DownloadTask.DownloadListener getDownloadListener() {

    return new DownloadTask.DownloadListener() {

      @Override
      public void onFinishedDownloading(@NonNull File file) {

        state = State.UNPACKING_TAR;

        String destPath = new File(tileDirectory, version).getAbsolutePath();
        new TileUnpacker().unpack(file, destPath, new UnpackUpdateTask.UpdateListener() {
          @Override
          public void onProgressUpdate(Long progress) {
            if (progress == 100) {
              listener.onComplete();
              state = State.COMPLETED;
            } else {
              listener.onProgressUpdate(progress.intValue());
            }
          }
        });
      }

      @Override
      public void onErrorDownloading() {
        onError();
      }
    };
  }

  private void onError() {
    state = State.NOT_STARTED;

    if (listener != null) {
      listener.onError();
    }
  }

  public interface RoutingTileDownloadListener {
    void onError();

    void onProgressUpdate(int percent);

    void onComplete();
  }
}

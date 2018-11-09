package com.mapbox.services.android.navigation.v5.navigation.offline;

import android.os.AsyncTask;

import java.io.File;

public class UnpackUpdateTask extends AsyncTask<File, Long, File> {
  private UpdateListener updateListener;

  public UnpackUpdateTask(UpdateListener updateListener) {
    this.updateListener = updateListener;
  }

  @Override
  protected File doInBackground(File... files) {
    File tar = files[0];
    long size = tar.length();

    while (tar.length() > 0) {
      publishProgress((((tar.length()/size)) * 100));
    }

    return tar;
  }

  @Override
  protected void onPostExecute(File file) {
    super.onPostExecute(file);
    updateListener.onProgressUpdate(100L);
  }

  @Override
  protected void onProgressUpdate(Long... values) {
    if (updateListener != null) {
      updateListener.onProgressUpdate(values[0]);
    }
  }

  public interface UpdateListener {
    void onProgressUpdate(Long progress);
  }
}

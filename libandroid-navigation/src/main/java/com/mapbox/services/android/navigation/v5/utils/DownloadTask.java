package com.mapbox.services.android.navigation.v5.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class DownloadTask extends AsyncTask<ResponseBody, Void, File> {

  private static final int END_OF_FILE_DENOTER = -1;
  private static int instructionNamingInt = 0;
  private final String destDirectory;
  private final DownloadListener downloadListener;
  private final String extension;
  private final String fileName;

  public DownloadTask(String destDirectory, String extension, @Nullable
    DownloadListener downloadListener) {
    this(destDirectory, "", extension, downloadListener);
  }

  public DownloadTask(String destDirectory, String fileName, String extension, @Nullable
    DownloadListener downloadListener) {
    this.fileName = fileName;
    this.destDirectory = destDirectory;
    this.downloadListener = downloadListener;
    this.extension = extension;
  }

  @Override
  protected File doInBackground(ResponseBody... responseBodies) {
    return saveAsFile(responseBodies[0]);
  }

  /**
   * Saves the file returned in the response body as a file in the cache directory
   *
   * @param responseBody containing file
   * @return resulting file, or null if there were any IO exceptions
   */
  private File saveAsFile(ResponseBody responseBody) {
    try {
      File file = new File(destDirectory + File.separator + fileName + getDistinguisher() + "." +
        extension);
      InputStream inputStream = null;
      OutputStream outputStream = null;

      try {
        inputStream = responseBody.byteStream();
        outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int numOfBufferedBytes;

        while ((numOfBufferedBytes = inputStream.read(buffer)) != END_OF_FILE_DENOTER) {
          outputStream.write(buffer, 0, numOfBufferedBytes);
        }

        outputStream.flush();
        return file;

      } catch (IOException exception) {
        if (downloadListener != null) {
          downloadListener.onErrorDownloading();
        }
        return null;

      } finally {
        if (inputStream != null) {
          inputStream.close();
        }

        if (outputStream != null) {
          outputStream.close();
        }
      }

    } catch (IOException exception) {
      return null;
    }
  }

  private String getDistinguisher() {
    return instructionNamingInt++ > 0 ? "" + instructionNamingInt : "";
  }

  @Override
  protected void onPostExecute(File instructionFile) {
    if (downloadListener == null) {
      return;
    }

    if (instructionFile == null) {
      downloadListener.onErrorDownloading();
    } else {
      downloadListener.onFinishedDownloading(instructionFile);
    }
  }

  public interface DownloadListener {
    void onFinishedDownloading(@NonNull File file);

    void onErrorDownloading();
  }
}

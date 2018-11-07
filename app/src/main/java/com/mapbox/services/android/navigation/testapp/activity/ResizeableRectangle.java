package com.mapbox.services.android.navigation.testapp.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ResizeableRectangle extends FrameLayout {

  public ResizeableRectangle(@NonNull Context context) {
    this(context, null);
  }

  public ResizeableRectangle(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public ResizeableRectangle(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  private void initialize() {

  }


}

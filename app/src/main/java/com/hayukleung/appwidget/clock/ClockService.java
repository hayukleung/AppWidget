package com.hayukleung.appwidget.clock;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.IBinder;
import android.util.SparseIntArray;
import android.widget.RemoteViews;
import com.hayukleung.appwidget.R;
import com.hayukleung.appwidget.ResourceManager;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * chargerlink_v2
 * com.hayukleung.bequiet.ui.widget
 * ClockService.java
 *
 * by hayukleung
 * at 2016-08-30 16:44
 */
public class ClockService extends Service {

  private Timer mTimer;

  // private Bitmap mHandS;
  private Bitmap mHandM;
  private Bitmap mHandH;

  private final SparseIntArray mSecondMap = new SparseIntArray(60);

  @Override public void onCreate() {
    super.onCreate();
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if (null == mTimer) {
      mTimer = new Timer();
    }
    mTimer.schedule(new MyTimerTask(), 0, 500);
    return START_STICKY;
  }

  @Override public void onDestroy() {
    // if (null != mHandS && !mHandS.isRecycled()) {
    // mHandS.recycle();
    // mHandS = null;
    // }
    if (null != mHandM && !mHandM.isRecycled()) {
      mHandM.recycle();
      mHandM = null;
    }
    if (null != mHandH && !mHandH.isRecycled()) {
      mHandH.recycle();
      mHandH = null;
    }
    mTimer.cancel();
    mTimer = null;
    super.onDestroy();
  }

  @Override public IBinder onBind(Intent intent) {
    return null;
  }

  private final class MyTimerTask extends TimerTask {

    @Override public void run() {

      // schema 0 使用drawable旋转
      // Date date = new Date();
      // String h = new SimpleDateFormat("hh", Locale.CHINA).format(date);
      // String m = new SimpleDateFormat("mm", Locale.CHINA).format(date);
      // String s = new SimpleDateFormat("ss", Locale.CHINA).format(date);
      // 获取Widgets管理器
      AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());
      // widgetManager所操作的Widget对应的远程视图即当前Widget的layout文件
      RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.app_widget_clock);
      // remoteView.setImageViewResource(R.id.time_s, ResourceManager.getDrawableResId(getApplicationContext().getPackageName(), String.format("time_sec_%s", s)));
      // remoteView.setImageViewResource(R.id.time_m, ResourceManager.getDrawableResId(getApplicationContext().getPackageName(), String.format("time_min_%s", m)));
      // remoteView.setImageViewResource(R.id.time_h, ResourceManager.getDrawableResId(getApplicationContext().getPackageName(), String.format("time_hour_%s", h)));

      // schema 1 使用bitmap旋转
      Calendar calendar = Calendar.getInstance();
      int rawS = calendar.get(Calendar.SECOND);
      int rawM = calendar.get(Calendar.MINUTE);
      int rawH = calendar.get(Calendar.HOUR);

      float realS = rawS;
      float realM = rawM + realS / 60.0f;
      float realH = rawH + realM / 60.0f;

      float rotateS = 360f / 60f * realS;
      float rotateM = 360f / 60f * realM;
      float rotateH = 360f / 12f * realH;

      // if (null == mHandS || mHandS.isRecycled()) {
      // mHandS = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.widget_sec_00);
      // }
      if (null == mHandM || mHandM.isRecycled()) {
        mHandM = BitmapFactory.decodeResource(getApplicationContext().getResources(),
            R.drawable.widget_min_00);
      }
      if (null == mHandH || mHandH.isRecycled()) {
        mHandH = BitmapFactory.decodeResource(getApplicationContext().getResources(),
            R.drawable.widget_hour_00);
      }


      Integer secResId = mSecondMap.get(rawS, -1);
      if (-1 == secResId) {
        String s = rawS >= 10 ? String.valueOf(rawS) : String.format(Locale.CHINA, "0%d", rawS);
        secResId = ResourceManager.getDrawableResId(getApplicationContext().getPackageName(),
            String.format("time_sec_%s", s));
        mSecondMap.put(rawS, secResId);
      }
      remoteView.setImageViewResource(R.id.time_s, secResId);

      // remoteView.setImageViewBitmap(R.id.time_s, rotateBitmap(mHandS, rotateS));
      remoteView.setImageViewBitmap(R.id.time_m, rotateBitmap(mHandM, rotateM));
      remoteView.setImageViewBitmap(R.id.time_h, rotateBitmap(mHandH, rotateH));

      // 当点击Widgets时触发的世界
      // remoteView.setOnClickPendingIntent(viewId, pendingIntent)
      ComponentName componentName =
          new ComponentName(getApplicationContext(), ClockAppWidgetProvider.class);
      widgetManager.updateAppWidget(componentName, remoteView);
    }
  }

  /**
   * 旋转
   *
   * @param source
   * @param degree
   * @return
   */
  private Bitmap rotateBitmap(Bitmap source, float degree) {
    if (null == source) {
      return null;
    }
    int size = source.getWidth();
    Matrix matrix = new Matrix();
    matrix.reset();
    matrix.setRotate(degree, size / 2, size / 2);
    return Bitmap.createBitmap(source, 0, 0, size, size, matrix, true);
  }
}
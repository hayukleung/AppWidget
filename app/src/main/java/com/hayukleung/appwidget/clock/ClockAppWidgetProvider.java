package com.hayukleung.appwidget.clock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hayukleung.appwidget.R;
import com.hayukleung.appwidget.yylx.Callback;
import com.hayukleung.appwidget.yylx.Sound;

/**
 * 写轮眼小部件
 *
 * chargerlink_v2
 * com.hayukleung.bequiet.ui.widget.clock
 * ClockAppWidgetProvider.java
 *
 * by hayukleung
 * at 2016-08-26 14:58
 */
public class ClockAppWidgetProvider extends AppWidgetProvider {

  public ClockAppWidgetProvider() {
    super();
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    super.onUpdate(context, appWidgetManager, appWidgetIds);

    final int N = appWidgetIds.length;
    for (int i = 0; i < N; i++) {
      int appWidgetId = appWidgetIds[i];
      RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_clock);


      // for oneplus
      // goNFC(context, remoteViews, "com.finshell.wallet", "com.nearme.wallet.nfc.ui.NfcConsumeActivity");


      // for android
      goClockSetting(context, remoteViews, "com.android.deskclock", "com.android.deskclock.DeskClock");
      // for nexus
      goClockSetting(context, remoteViews, "com.google.android.deskclock", "com.android.deskclock.DeskClock");
      // for sony xperia
      goClockSetting(context, remoteViews, "com.sonyericsson.organizer", "com.sonyericsson.organizer.Organizer");
      // for xiaomi
      goClockSetting(context, remoteViews, "com.android.deskclock", "com.android.deskclock.DeskClockTabActivity");
      // for 奇酷360
      goClockSetting(context, remoteViews, "com.yulong.android.xtime", "yulong.xtime.ui.main.XTimeActivity");
      // TODO for other android phone



      Intent intent = new Intent(context, ClockAppWidgetProvider.class);
      intent.setAction("yylx");
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
      PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
      remoteViews.setOnClickPendingIntent(R.id.background, pendingIntent);

      // remoteViews.setFloat(R.id.shyaringan, "setRotation", 0);
      appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


    // context.startService(new Intent(context, ClockService.class));
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    if ("yylx".equals(intent.getAction())) {
      // TODO



      Sound.INSTANCE.playSoundScanFinish(context, new Callback() {
        @Override
        public void onPlayFinish() {


          Intent nfcIntent = new Intent().setComponent(new ComponentName("com.finshell.wallet", "com.nearme.wallet.nfc.ui.NfcConsumeActivity"));
          nfcIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          PendingIntent intent = PendingIntent.getActivity(context, 2, nfcIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
          context.startActivity(nfcIntent);
        }
      });
    }
  }

  @Override public void onDeleted(Context context, int[] appWidgetIds) {
    super.onDeleted(context, appWidgetIds);
  }

  @Override public void onEnabled(Context context) {
  }

  @Override public void onDisabled(Context context) {
    context.stopService(new Intent(context, ClockService.class));
  }

  private void goNFC(Context context, RemoteViews remoteViews, final String pkgName, final String clzName) {

    Intent nfcIntent;

    nfcIntent = new Intent().setComponent(new ComponentName(pkgName, clzName));
    PendingIntent intent = PendingIntent.getActivity(context, 2, nfcIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setOnClickPendingIntent(R.id.clock, intent);
  }

  private void goClockSetting(Context context, RemoteViews remoteViews, final String pkgName, final String clzName) {
    if (isIntentCorrect(context, pkgName, clzName)) {
      remoteViews.setOnClickPendingIntent(R.id.clock, getClockPendingIntent(context, pkgName, clzName));
    } else {
    }
  }

  private boolean isIntentCorrect(Context context, final String pkgName, final String clzName) {
    Intent clockIntent;
    ResolveInfo resolved;

    clockIntent = new Intent().setComponent(new ComponentName(pkgName, clzName));
    resolved = context.getPackageManager().resolveActivity(clockIntent, PackageManager.MATCH_DEFAULT_ONLY);
    return null != resolved;
  }

  private PendingIntent getClockPendingIntent(Context context, final String pkgName, final String clzName) {
    Intent clockIntent;

    clockIntent = new Intent().setComponent(new ComponentName(pkgName, clzName));
    return PendingIntent.getActivity(context, 2, clockIntent, PendingIntent.FLAG_UPDATE_CURRENT);
  }
}

package dev.glyphgram.companion;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.nothing.ketchum.Glyph;
import com.nothing.ketchum.GlyphFrame;
import com.nothing.ketchum.GlyphManager;

public final class GlyphService extends Service implements GlyphManager.Callback {
    public static final String ACTION_ON = "dev.glyphgram.companion.GLYPH_ON";
    public static final String ACTION_OFF = "dev.glyphgram.companion.GLYPH_OFF";

    private static final String TAG = "GlyphGram";
    private static final String CHANNEL_ID = "glyph_recording";
    private static final int NOTIFICATION_ID = 22111;

    private GlyphManager manager;
    private boolean connected;
    private boolean desiredOn;

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager nm = getSystemService(NotificationManager.class);
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Glyph video light",
                NotificationManager.IMPORTANCE_LOW);
        channel.setDescription("Active while Glyph is used as lighting for a video message");
        nm.createNotificationChannel(channel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, buildNotification());
        desiredOn = intent != null && ACTION_ON.equals(intent.getAction());

        if (!desiredOn) {
            shutdownGlyph();
            stopForeground(STOP_FOREGROUND_REMOVE);
            stopSelf();
            return START_NOT_STICKY;
        }

        try {
            if (manager == null) {
                manager = GlyphManager.getInstance(getApplicationContext());
                manager.init(this);
            } else if (connected) {
                showGlyph();
            }
        } catch (Throwable t) {
            Log.e(TAG, "Unable to initialize GlyphManager", t);
            report("Glyph init failed: " + errorText(t));
            stopForeground(STOP_FOREGROUND_REMOVE);
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onServiceConnected(ComponentName name) {
        connected = true;
        try {
            boolean authorized = manager.register(Glyph.DEVICE_22111);
            if (!authorized) {
                report("Glyph registration rejected by Nothing OS");
                return;
            }
            manager.openSession();
            report("Glyph service connected");
            if (desiredOn) {
                showGlyph();
            }
        } catch (Throwable t) {
            Log.e(TAG, "Unable to open Glyph session", t);
            report("Glyph session failed: " + errorText(t));
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        connected = false;
    }

    private void showGlyph() {
        if (!connected || manager == null || !desiredOn) {
            return;
        }
        try {
            manager.turnOff();
            GlyphFrame.Builder builder = manager.getGlyphFrameBuilder();
            GlyphFrame frame = builder
                    .buildChannelA()
                    .buildChannelB()
                    .buildChannelC()
                    .buildChannelD()
                    .buildChannelE()
                    .build();
            manager.toggle(frame);
            report("Glyph is on");
        } catch (Throwable t) {
            Log.e(TAG, "Unable to turn Glyph on", t);
            report("Glyph command failed: " + errorText(t));
        }
    }

    private void report(String message) {
        Log.i(TAG, message);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private String errorText(Throwable error) {
        String message = error.getMessage();
        return error.getClass().getSimpleName() + (message == null ? "" : ": " + message);
    }

    private void shutdownGlyph() {
        desiredOn = false;
        if (manager == null) {
            return;
        }
        try {
            manager.turnOff();
        } catch (Throwable t) {
            Log.w(TAG, "turnOff failed", t);
        }
        if (connected) {
            try {
                manager.closeSession();
            } catch (Throwable t) {
                Log.w(TAG, "closeSession failed", t);
            }
        }
        try {
            manager.unInit();
        } catch (Throwable t) {
            Log.w(TAG, "unInit failed", t);
        }
        manager = null;
        connected = false;
    }

    private Notification buildNotification() {
        return new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setContentTitle("Glyph lighting is active")
                .setContentText("Recording a Telegram video message")
                .setOngoing(true)
                .build();
    }

    @Override
    public void onDestroy() {
        shutdownGlyph();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

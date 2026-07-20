package dev.glyphgram.companion;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(24), dp(48), dp(24), dp(24));

        TextView title = new TextView(this);
        title.setText("GlyphGram");
        title.setTextSize(26);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView help = new TextView(this);
        help.setText("Nothing Phone (2) video-message lighting\n\nCreated by @M0zy1\n\nUse these buttons to test the Glyph connection before enabling the AyuGram plugin.");
        help.setTextSize(16);
        help.setTextColor(Color.DKGRAY);
        help.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams helpLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        helpLp.setMargins(0, dp(24), 0, dp(24));
        root.addView(help, helpLp);

        Button on = new Button(this);
        on.setText("Turn Glyph on");
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command(GlyphService.ACTION_ON);
            }
        });
        root.addView(on, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button off = new Button(this);
        off.setText("Turn Glyph off");
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command(GlyphService.ACTION_OFF);
            }
        });
        root.addView(off, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(root);
    }

    private void command(String action) {
        Intent intent = new Intent(this, GlyphService.class).setAction(action);
        startForegroundService(intent);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}

package bttv.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserPreferences {

    private static SharedPreferences prefs = null;
    private static SharedPreferences.Editor editor = null;

    public static void ensureLoaded(Context ctx) {
        if(ctx == null && (prefs == null || editor == null)) {
            Log.e("LBTTVUserPReferences", "ensureLoaded: ctx is null, can't set editor field, this will cause problems!", new Exception());
            return;
        }
        if (prefs == null) {
            prefs = ctx.getSharedPreferences("BTTV", 0);
        }
        if (editor == null) {
            editor = prefs.edit();
        }
    }

    public static boolean getBTTVEmotesEnabled(Context ctx) {
        ensureLoaded(ctx);
        return prefs.getBoolean("enable_bttv_emotes", true);
    }

    public static void setBTTVEmotesEnabled(Context ctx, boolean value) {
        ensureLoaded(ctx);
        editor.putBoolean("enable_bttv_emotes", value);
        editor.apply();
    }

    public static boolean getBTTVGifEmotesEnabled(Context ctx) {
        ensureLoaded(ctx);
        return prefs.getBoolean("enable_bttv_gif_emotes", true);
    }

    public static void setBTTVGifEmotesEnabled(Context ctx, boolean value) {
        ensureLoaded(ctx);
        editor.putBoolean("enable_bttv_gif_emotes", value);
        editor.apply();
    }

    public static boolean getFFZEmotesEnabled(Context ctx) {
        ensureLoaded(ctx);
        return prefs.getBoolean("enable_ffz_emotes", true);
    }

    public static void setFFZEmotesEnabled(Context ctx, boolean value) {
        ensureLoaded(ctx);
        editor.putBoolean("enable_ffz_emotes", value);
        editor.apply();
    }

    public static boolean get7TVEmotesEnabled(Context ctx) {
        ensureLoaded(ctx);
        return prefs.getBoolean("enable_7tv_emotes", true);
    }

    public static void set7TVEmotesEnabled(Context ctx, boolean value) {
        ensureLoaded(ctx);
        editor.putBoolean("enable_7tv_emotes", value);
        editor.apply();
    }

    public static boolean getAutoRedeemChannelPointsEnabled(Context ctx) {
        ensureLoaded(ctx);
        return prefs.getBoolean("enable_auto_redeem_channel_points", true);
    }

    public static void setAutoRedeemChannelPointsEnabled(Context ctx, boolean value) {
        ensureLoaded(ctx);
        editor.putBoolean("enable_auto_redeem_channel_points", value);
        editor.apply();
    }

    public static boolean getShouldShowSleepTimer(Context ctx) {
        ensureLoaded(ctx);
        return prefs.getBoolean("enable_sleep_timer", true);
    }

    public static void setShouldShowSleepTimer(Context ctx, boolean value) {
        ensureLoaded(ctx);
        editor.putBoolean("enable_sleep_timer", value);
        editor.apply();
    }

}

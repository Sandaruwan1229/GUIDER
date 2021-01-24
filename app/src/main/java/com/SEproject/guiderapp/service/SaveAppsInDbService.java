package com.SEproject.guiderapp.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import androidx.palette.graphics.Palette;

import com.SEproject.guiderapp.R;
import com.SEproject.guiderapp.data.AppColumns;
import com.SEproject.guiderapp.model.App;
import com.SEproject.guiderapp.utils.Utils;

import java.util.Collections;
import java.util.List;

import timber.log.Timber;

import static com.SEproject.guiderapp.data.AppProvider.Apps.URI_APPS;

/**
 * Fetches installed apps using PackageManager and saves them in the database for faster loading
 * Launched on first app launch from splash screen
 */
public class SaveAppsInDbService extends IntentService {

    public SaveAppsInDbService() {
        super("SaveAppsInDbService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
           saveAppsInDb();
        }
    }

    /**
     * Loads a list of installed apps on the device using PackageManager
     * Saves the apps in a database
     */
    private void saveAppsInDb() {

        PackageManager mPackageManager = getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> activities = mPackageManager.queryIntentActivities(intent, 0);
        Collections.sort(activities, new ResolveInfo.DisplayNameComparator(mPackageManager));
        for (ResolveInfo ri : activities) {
            ContentValues values = new ContentValues();
            App app = new App();
            app.setmPackage(ri.activityInfo.packageName);
            app.setmTitle((String) ri.loadLabel(mPackageManager));
            app.setmIcon(ri.activityInfo.loadIcon(mPackageManager));

            //Fetches vibrant colour of app icon using Palette library for better design
            Palette p = Palette.from(Utils.getBitmapFromDrawable(app.getmIcon())).generate();
            app.setmAppColor(p.getVibrantColor(getResources().getColor(R.color.black)));
            app.setmTextColor(Utils.getTextColor(app.getmAppColor()));

            values.put(AppColumns.APP_TITLE, app.getmTitle());
            values.put(AppColumns.PACKAGE_NAME, app.getmPackage());
            values.put(AppColumns.PALETTE_COLOR, app.getmAppColor());
            values.put(AppColumns.TEXT_COLOR, app.getmTextColor());
            try{

            getContentResolver().insert(URI_APPS, values);
            }catch (Exception e){
                Timber.d("COULD NOT INSERT FROM SERVICE");
            }
        }


    }

}

package com.SEproject.guiderapp.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.SEproject.guiderapp.R;

public class OverlayPermissionSlide extends Fragment {

    private static final String ARG_LAYOUT_RES_ID = "layoutResIDD";
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 123;
    private int layoutResId;
    private SharedPreferences preferences;
    private Context mContext;

    public static OverlayPermissionSlide newInstance(int layoutResId) {
        OverlayPermissionSlide overlayPermissionSlide = new OverlayPermissionSlide();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        overlayPermissionSlide.setArguments(args);

        return overlayPermissionSlide;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();

        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        Button mPermissionButton = (Button) getView().findViewById(R.id.btn_popup_permission);

        mPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grantPermissionClicked();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        ImageView mCheckImageView = (ImageView) getView().findViewById(R.id.iv_overlay_permission_slide_check_state);
        if (hasOverlayPermission(mContext)) {
            mCheckImageView.setImageResource(R.drawable.ic_check_green_24dp);
        } else {
            mCheckImageView.setImageResource(R.drawable.ic_clear_red_24dp);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {

            ImageView mCheckImageView = (ImageView) getView().findViewById(R.id.iv_overlay_permission_slide_check_state);
            if (hasOverlayPermission(mContext)) {
                mCheckImageView.setImageResource(R.drawable.ic_check_green_24dp);
            } else {
                Toast.makeText(mContext, "App will not function properly. Permission is necessary", Toast.LENGTH_LONG).show();
                mCheckImageView.setImageResource(R.drawable.ic_clear_red_24dp);
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(layoutResId, container, false);
    }


    /**
     * Handles the button click to launch activity or not
     */
    private void grantPermissionClicked() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && !hasOverlayPermission(mContext)) {
            requestOverlayPermission();
        } else {
            Toast.makeText(mContext, "Permission already granted!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Launches activity in settings to grant permission
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestOverlayPermission() {
        Intent overlayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        Toast.makeText(mContext, R.string.overlay_permission_instruction, Toast.LENGTH_LONG).show();
        startActivityForResult(overlayIntent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
    }

    /**
     * Checks if permission is granted or not
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    boolean hasOverlayPermission(Context context) {
        boolean granted;
        if (!Settings.canDrawOverlays(context)) {
            //Permission not granted
            granted = false;
//            Toast.makeText(context, "App will not function properly. Permission is necessary", Toast.LENGTH_LONG).show();
        } else {
            granted = true;
        }

        preferences.edit().putBoolean(getString(R.string.overlay_permission_pref), granted).apply();
        return granted;
    }

}

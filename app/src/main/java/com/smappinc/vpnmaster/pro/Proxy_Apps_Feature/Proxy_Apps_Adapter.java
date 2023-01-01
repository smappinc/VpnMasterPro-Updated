package com.smappinc.vpnmaster.pro.Proxy_Apps_Feature;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smappinc.vpnmaster.pro.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


class InstalledAppsAdapter extends RecyclerView.Adapter<InstalledAppsAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<PackageInfo> mDataSet;

    public InstalledAppsAdapter(Context context, ArrayList<PackageInfo> list) {
        mContext = context;
        mDataSet = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewLabel;
        public ImageView mImageViewIcon;
        public RelativeLayout mItem;

        public ViewHolder(View v) {
            super(v);
            mTextViewLabel = v.findViewById(R.id.Apk_Name);
            mImageViewIcon = v.findViewById(R.id.packageImage);
            mItem = v.findViewById(R.id.item);
        }
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.allowed_apps_adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        getAppIconByPackageName(mDataSet.get(position).packageName, holder.mImageViewIcon);
        getApplicationLabelByPackageName(mDataSet.get(position).packageName, holder.mTextViewLabel);

        holder.mItem.setOnClickListener(v -> {
            if (mContext != null) {
                Log.d("package_name", "" + mDataSet.get(position).packageName);

                PackageManager manager = mContext.getPackageManager();
                try {
                    Intent i = manager.getLaunchIntentForPackage(mDataSet.get(position).packageName);
                    if (i == null)
                        throw new PackageManager.NameNotFoundException();
                    mContext.startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
        });
    }

    private void getApplicationLabelByPackageName(String packageName, TextView textView) {
        PackageManager packageManager = mContext.getPackageManager();
        ApplicationInfo applicationInfo;
        String label = "Unknown";
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                label = (String) packageManager.getApplicationLabel(applicationInfo);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        textView.setText(label);
    }

    private void getAppIconByPackageName(String packageName, ImageView imageView) {
        Drawable icon;
        try {
            icon = mContext.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // Get a default icon
            icon = ContextCompat.getDrawable(mContext, R.drawable.ic_launcher_background);
        }
        imageView.setImageDrawable(icon);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

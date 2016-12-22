package com.example.intentinstaller;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.app.AlertDialog.Builder;

public class PackageSelectionDialog implements OnItemClickListener{
	
	private Context m_parent; // call from
	private OnPackageSelectListener m_listener; // recieve result
	private AlertDialog m_dlg; //dialog
	private PackageInfoArrayAdapter m_packageinfoarrayadapter; //failinfoadapter

	// constructer
	public PackageSelectionDialog(Context context, OnPackageSelectListener listener){
		m_parent = context;
		m_listener = (OnPackageSelectListener)listener;
	}
	
	public void show(Context context){
		// title
		String strTitle = "package List";
		
		// list view
		ListView listview = new ListView(m_parent);
		listview.setScrollingCacheEnabled(false);
		listview.setOnItemClickListener(this);
		List<PackageInfo> listPackageInfo = new ArrayList<PackageInfo>();
		
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> package_list = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(PackageInfo info : package_list){
			if(info.applicationInfo != null){
				if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0){
					// system image application
					//Log.d("systemApp", info.packageName);
				}else{
					//Log.d("installApp", info.packageName);
					listPackageInfo.add(info);
				}
			}
		}
		m_packageinfoarrayadapter = new PackageInfoArrayAdapter(m_parent,listPackageInfo);
		listview.setAdapter(m_packageinfoarrayadapter);
		
		Builder builder = new AlertDialog.Builder(m_parent);
		builder.setTitle(strTitle);
		builder.setPositiveButton("Cancel", null);
		builder.setView(listview);
		m_dlg = builder.show();
		
	}
	
	public void onItemClick(AdapterView<?> l, View v, int position, long id){
		if(null != m_dlg){
			m_dlg.dismiss();
			m_dlg = null;
		}
		PackageInfo packageinfo = m_packageinfoarrayadapter.getItem(position);
		m_listener.onPackageSelect(packageinfo.packageName);
		
	}
	
	public interface OnPackageSelectListener{
		public void onPackageSelect(String packageName);
	}
}

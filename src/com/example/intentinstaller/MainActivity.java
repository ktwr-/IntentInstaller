package com.example.intentinstaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements PackageSelectionDialog.OnPackageSelectListener,FileSelectionDialog.OnFileSelectListener {
	// option menu id
	private static final int MENUID_PACKAGE= 0;

	private static final int MENUID_APK = 1;
	
	// initial folda
	private String m_strInitialDir = Environment.getExternalStorageDirectory().toString();
	
	
	AlertDialog mAlertDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		menu.add(0,MENUID_PACKAGE,0,"Select Package...");
		menu.add(0,MENUID_APK,0,"Select APK...");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
		case MENUID_PACKAGE:
			// dialog object
			PackageSelectionDialog dlg = new PackageSelectionDialog(this, this);
			dlg.show(this);
			return true;
		case MENUID_APK:
			FileSelectionDialog apkdlg = new FileSelectionDialog(this,this);
			apkdlg.show(new File(m_strInitialDir));
			return true;
		}
		return false;
	}
	
	public void onPackageSelect(String packageName) {
		Toast.makeText(this, "Package Uninstall", Toast.LENGTH_SHORT).show();
		apkUninstall(packageName);
		
		
	}
	
	public void onFileSelect(File file){
		Toast.makeText(this, "Apk Install"+ file.getPath(), Toast.LENGTH_SHORT).show();
		m_strInitialDir = file.getParent();
		apkInstall(file.getPath());
		
	}
	
	/**
	 * 
	 * @param apkName which contains source path of apk that you want to install
	 */
	public void apkInstall(String apkName){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(apkName)),"application/vnd.android.package-archive");
		startActivity(intent);
	}
	
	/**
	 * 
	 * @param packageName which you want to uninstall.
	 */
	public void apkUninstall(String packageName){
		Intent uninstall = new Intent(Intent.ACTION_DELETE);
		// Example
		//uninstall.setData(Uri.parse("package:it.reyboz.minesweeper"));
		uninstall.setData(Uri.parse("package:"+packageName));
		startActivity(uninstall);
	}

}

/*
 * Copyright (c) 2014, Kevin Cernekee
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * In addition, as a special exception, the copyright holders give
 * permission to link the code of portions of this program with the
 * OpenSSL library.
 */

package com.smappinc.vpnmaster.pro.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

import com.smappinc.vpnmaster.pro.R;

public class ErrorDialog extends UserDialog
	implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

	public String mTitle;
	public String mMessage;
   Context mContext;
   OpenVpnService openserv;
	private AlertDialog mAlert;

	public ErrorDialog(OpenVpnService vserv,Context m,SharedPreferences prefs, String title, String message) {
		super(prefs);
		mTitle = title;
		mMessage = message;
		mContext=m;
		openserv=vserv;
	}

	@Override
	public void onStart(Context context) {
		super.onStart(context);
		mAlert = new AlertDialog.Builder(context)
			.setTitle(mTitle)
			.setMessage(mMessage)
			.setPositiveButton("ok", this)
			.create();
		mAlert.setOnDismissListener(this);
		mAlert.show();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// catches Pos/Neg/Neutral and Back button presses
		finish(true);

      //Home_fragment_Class.Sa
		mAlert = null;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		//Log.e("tryClick==>> ",OpenConnectManagementThread.iptoConnect);
        /*autoServer=true;
		Intent intent =new Intent(mContext.getApplicationContext(),Home_Activity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//openserv.getReconnectName();
		openserv.stopSelf();
		//mContext.stopService(mContext.getApplicationContext());
       mContext.startActivity(intent);
*/

	//	Log.e("click==>> ",OpenConnectManagementThread.iptoConnect);
	}

	@Override
	public void onStop(Context context) {
		super.onStop(context);
		Log.e("stop==>> ",OpenConnectManagementThread.iptoConnect);
		finish(null);
		if (mAlert != null) {
			mAlert.dismiss();
		}
	}
}

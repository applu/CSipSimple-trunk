/**
 * Copyright (C) 2010 Regis Montoya (aka r3gis - www.r3gis.fr)
 * This file is part of CSipSimple.
 *
 *  CSipSimple is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CSipSimple is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CSipSimple.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.csipsimple.utils;

import java.lang.reflect.Field;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.Uri;

public class Compatibility {
	
	private static final String THIS_FILE = "Compat";
	private static int currentApi = 0;

	public static int getApiLevel() {
		
		if(currentApi>0) {
			return currentApi;
		}
		
		if(android.os.Build.VERSION.SDK.equalsIgnoreCase("3")) {
			currentApi = 3;
		}else {
			try {
				Field f = android.os.Build.VERSION.class.getDeclaredField("SDK_INT");
				currentApi = (Integer) f.get(null);
			} catch (Exception e) {
				return 0;
			} 
		}
		
		return currentApi;
	}
	
	
	public static boolean isCompatible(int apiLevel) {
		return getApiLevel() >= apiLevel;
	}


	/**
	 * Get the stream id for in call track. Can differ on some devices.
	 * Current device for which it's different :
	 * Archos 5IT
	 * @return
	 */
	public static int getInCallStream() {
		if(android.os.Build.BRAND.equalsIgnoreCase("archos")) {
			//Since archos has no voice call capabilities, voice call stream is not implemented
			//So we have to choose the good stream tag, which is by default falled back to music
			return AudioManager.STREAM_MUSIC;
		}
		return AudioManager.STREAM_VOICE_CALL;
	}
	
	public static boolean useRoutingApi() {
		Log.d(THIS_FILE, "Current device "+android.os.Build.BRAND+" - "+android.os.Build.DEVICE);
		if( (android.os.Build.BRAND.equalsIgnoreCase("htc") && !isCompatible(5)) ||
				android.os.Build.BRAND.equalsIgnoreCase("google") ||
				android.os.Build.DEVICE.equals("GT-I9000")) {
			return false;
		}
		return true;
	}


	public static int getInCallMode() {
		if(android.os.Build.BRAND.equalsIgnoreCase("sdg")) {
			return 3;
		}
		
		if(android.os.Build.BRAND.equalsIgnoreCase("htc") && 
				!isCompatible(4)) {
			return AudioManager.MODE_IN_CALL;
		}
		
		return AudioManager.MODE_NORMAL;
	}
	
	
	public static void setFirstRunParameters(PreferencesWrapper preferencesWrapper) {
		//TODO : make it compatible with 1.5 deviceso
		//preferencesWrapper.setCodecPriority("iLBC/8000/1", 
		//		android.os.Build.CPU_ABI.equalsIgnoreCase("armeabi-v7a")?"125":"0");
	}
	
	public static boolean useFlipAnimation() {
		if(android.os.Build.BRAND.equalsIgnoreCase("archos")) {
			return false;
		}
		return true;
	}
	
	
	public static boolean canResolveIntent(Context context, final Intent intent) {
		 final PackageManager packageManager = context.getPackageManager();
		 //final Intent intent = new Intent(action);
		 List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		 return list.size() > 0;
	}
	
	private static Boolean canMakeGSMCall = null;
	
	public static boolean canMakeGSMCall(Context context) {
		if(canMakeGSMCall == null) {
			Intent intentMakePstnCall = new Intent(Intent.ACTION_CALL);
			intentMakePstnCall.setData(Uri.fromParts("tel", "1", null));
			canMakeGSMCall = canResolveIntent(context, intentMakePstnCall);
		}
		return canMakeGSMCall;
	}
}


/*
 * Copyright (C) The Ambient Dynamix Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ambientdynamix.update.contextplugin;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.ambientdynamix.api.application.ContextPluginInformation;
import org.ambientdynamix.api.application.VersionInfo;
import org.ambientdynamix.api.contextplugin.PluginConstants.PLATFORM;
import org.ambientdynamix.core.BaseActivity;
import org.ambientdynamix.core.DynamixService;
import org.ambientdynamix.util.RepositoryInfo;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.google.gson.Gson;

import eu.smartsantander.androidExperimentation.Constants;
import eu.smartsantander.androidExperimentation.jsonEntities.Plugin;
import eu.smartsantander.androidExperimentation.jsonEntities.PluginList;
import eu.smartsantander.androidExperimentation.operations.Communication;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

public class SimpleSourceBase {
	private final String TAG = this.getClass().getSimpleName();
	private final String URLS = Constants.URL;

	protected List<DiscoveredContextPlugin> createDiscoveredPlugins(
			RepositoryInfo repo, InputStream input, PLATFORM platform,
			VersionInfo platformVersion, VersionInfo frameworkVersion,
			boolean processSingle) throws Exception {

		// SmartSantander Modifications
		Log.i("AndroidExperimentation", "Start Plugin Discovery");
		String jsonPluginList = "";
		List<DiscoveredContextPlugin> plugs = new ArrayList<DiscoveredContextPlugin>();
		try {
			PluginList pluginList = getPluginList();
			Log.i(TAG, "Plugin List setted");
			List<Plugin> plugList = pluginList.getPluginList();
			for (Plugin plugInfo : plugList) {
				if (isEnabled(plugInfo) == true)
					continue;
				ContextPluginBinder plugBinder = new ContextPluginBinder();
				DiscoveredContextPlugin plug = plugBinder
						.createDiscoveredPlugin(repo, plugInfo);
				plugs.add(plug);
			}
			return plugs;
		} catch (Exception e) {
			Log.w(TAG, "Exception Installin plugins: " + e.getMessage());
			BaseActivity.toast("uException Installin plugins:"+e.getMessage(), Toast.LENGTH_LONG);
			return plugs;
		}
	}

	Boolean isEnabled(Plugin plugInfo) {
		for (ContextPluginInformation plugin : DynamixService
				.getAllContextPluginInfo()) {
			if (plugin.getPluginName().equals(plugInfo.getName()) == true) {
				return true;
			}
		}
		return false;
	}

	public PluginList getPluginList() throws Exception {
		File root = android.os.Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/dynamix");
		if (dir.exists() == false) {
			dir.mkdirs();
		}
		Communication communication = new Communication();
		List<Plugin> pluginList = communication.sendGetPluginList();
		Plugin pluginXML = null;
		for (Plugin plug : pluginList) {
			Constants.checkFile(plug.getFilename(), plug.getInstallUrl());
			if (plug.getName().equals("plugs.xml")) {
				pluginXML = plug;
			}
		}
		pluginList.remove(pluginXML);
		PluginList plist = new PluginList();
		plist.setPluginList(pluginList);

		SharedPreferences pref = DynamixService.getAndroidContext()
				.getApplicationContext().getSharedPreferences("runningJob", 0); // 0
																				// -
																				// for
																				// private
																				// mode
		Editor editor = pref.edit();
		editor = (DynamixService.getAndroidContext().getSharedPreferences(
				"pluginObjects", 0)).edit();
		String plistString = (new Gson()).toJson(plist, PluginList.class);
		editor.putString("pluginObjects", plistString);
		editor.commit();

		return plist;

	}
}

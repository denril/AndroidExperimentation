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
package org.ambientdynamix.security;

import java.io.Serializable;

import org.ambientdynamix.api.contextplugin.ContextPlugin;
import org.ambientdynamix.api.contextplugin.security.PrivacyRiskLevel;

/**
 * Example PrivacyPolicy that returns 'PrivacyRiskLevel.MEDIUM' for all ContextPlugins.
 * 
 * @see PrivacyPolicy
 * @author Darren Carlson
 */
public class MediumTrustPrivacyPolicy extends PrivacyPolicy implements Serializable {
	private static final long serialVersionUID = -3738914194713901081L;

	public MediumTrustPrivacyPolicy() {
		super("Medium Trust",
				"The application is allowed to receive context information with a medium privacy risk level.");
	}

	@Override
	public PrivacyRiskLevel getMaxPrivacyRisk(ContextPlugin plug) {
		return PrivacyRiskLevel.MEDIUM;
	}
}

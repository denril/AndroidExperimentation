package org.ambientdynamix.contextplugins.ExperimentPlugin;

import java.util.Set;
public interface IPluginInfo {
	public abstract String getStringRepresentation(String format);

	public abstract String getImplementingClassname();

	public abstract String getContextType();

	public abstract Set<String> getStringRepresentationFormats();
	
	public abstract String getState();

	public abstract void setState(String state);
	
	public abstract String getPayload();
	
	public abstract void setPayload(String payload);
}
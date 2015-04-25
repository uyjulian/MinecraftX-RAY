package com.uyjulian.minecraft.XrayMod;

public class XrayModVersion {
	public static final String getVersion() {
		return "${version}";
	}
	public static final String getName() {
		return "${fullName}";
	}
	public static final String getShortName() {
		return "${shortName}";
	}
	public static final String getGitCommit() {
		return "${git.revision}";
	}
	public static final boolean getGitModified() {
		return false; //TODO: actually check
	}
	public static final boolean getDebug() {
		return false;
	}
}

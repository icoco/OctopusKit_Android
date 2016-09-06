package com.ixkit.app;


public class Resource {
	

	public static String packageActivity(String shortActivityName){
		return "com.ixkit.app.ui." + shortActivityName;
	}


	public static String getGitHubLink(){
		return "https://github.com/icoco/OctopusKit_Android";
	}
}

package test;

import java.net.InetSocketAddress;
import java.net.Proxy;

import external.wrappers.NPMUtilWrapper;
import util.ConfigManager;
import util.PackageManager;

    

public class TestNPMReadmeWrapper {
	

	private static final String download_path = ConfigManager.getInstance().getProperty("download_path");
	public static final String npm_filename = ConfigManager.getInstance().getProperty("npm_filename");

	public static void main(String[] args) {
		
		Proxy proxy = new Proxy(                                      //
			    Proxy.Type.HTTP,                                      //
			    InetSocketAddress.createUnresolved("192.168.2.12", 3128) //
			);
		
		//NPMUtilWrapper rw = new NPMUtilWrapper();
		NPMUtilWrapper.createDescFile(download_path+"/"+npm_filename);
		//String result = rw.downloadReadmeContent("lodash", proxy);
		//System.out.println(result);
		System.out.println(PackageManager.getInstance().getDesc("quagga"));
		
		

	}

}

package util;

public class Tester {

	public static void main(String[] args) {
        PackageManager pkgm = PackageManager.getInstance();
        StopWordManager swm = StopWordManager.getInstance();
        for(String pkg:pkgm.getInstance().getPkgNames()){
        	if(swm.isStopWord(pkg)){
        		System.out.println(pkg);
        	}
        }

	}

}

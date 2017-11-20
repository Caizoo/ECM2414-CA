package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
	
	public static void main(String[] args) { // used to run test suite from command line
		Result result = JUnitCore.runClasses(AllTests.class);
		for(Failure fail: result.getFailures()) {
			System.out.println(fail.toString());
		}
		String outString = "";
		if(result.wasSuccessful()) {
			outString += "Test was run successfully\nruntime: "+(result.getRunTime()/1000f)+"s\nrun count of: "+result.getRunCount();
		} else {
			outString += "Test failed, failure count: "+result.getFailureCount();
		}
		System.out.println(outString);
		System.out.println("IMPORTANT: test files will still be in current directory");
	}

}

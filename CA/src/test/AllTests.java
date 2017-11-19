package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BagTest.class, BlackBagTest.class, BlackBagTypeTest.class, PebbleGameTest.class, PebbleTest.class,
		WhiteBagTest.class, WhiteBagTypeTest.class, ReadWriteFileTest.class }) 

/*
 * IMPORTANT: When running test, possible coverage may change as bags may not be refilled in some games
 */
public class AllTests {   
	// nothing
}

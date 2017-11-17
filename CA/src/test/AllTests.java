package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BagTest.class, BlackBagTest.class, BlackBagTypeTest.class, PebbleGameTest.class, PebbleTest.class,
		WhiteBagTest.class, WhiteBagTypeTest.class })
public class AllTests {
	// nothing
}

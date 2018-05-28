package OOP.Tests;

import org.junit.Test;

import static OOP.Tests.TestFunctions.*;

/**
 * Created by elran on 08/01/17.
 */
public class TestRunAll {
	@Test
	public void test() {

		launchTest(TestOrdered.class,10,3,2, 1);
		launchTest(TestOrderedInher.class,12,3,2, 1);

		launchTest(UnorderedTest.class,2,0,1, 0);
		launchTest(UnorderedInheriting.class,4,1,2, 0);
	}
}

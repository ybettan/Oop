package OOP.Tests;

import OOP.Solution.*;

import static OOP.Tests.TestFunctions.*;
/**
 * Created by elran on 05/01/17.
 */
@OOPTestClass(OOPTestClass.OOPTestClassType.UNORDERED)
public class UnorderedInheriting extends UnorderedTest {
	@OOPSetup()
	private void setupSon(){
		shouldPass(1,testSetup);
		if(testSetup==1)
			testSetup++;
	}
	@OOPTest
	private void testSetupCorrect()
	{
		OOPUnitCore.assertEquals(2,testSetup);
	}
	@OOPBefore({"overRideThis"})
	private void beforeSon(){
		shouldPass(1,a);
		a++;
	}
	@OOPTest()
	@Override
	protected void overRideThis() {
		shouldPass(2,a);
		a++;
	}
	@OOPAfter("overRideThis")
	private void afterSon(){
		shouldPass(3,a);
		a++;
	}
	@OOPBefore({"testAfterBeforeOverRide"})
	protected void beforeFather2(){
		shouldPass(0,c);
		c++;
	}
	@OOPAfter({"testAfterBeforeOverRide"})
	protected void afterFather2(){
		shouldPass(2,c);
	}
	@OOPTest()
	protected void failTest(){
		fail();
	}
	@OOPTest()
	private void errorTestAfterFail(){
		//nothing needed
	}
	@OOPAfter({"errorTestAfterFail"})
	private void afterFail(){
		fail();
	}
	@OOPTest()
	private void errorTestBeforeFail(){
		//nothing needed
	}
	@OOPBefore({"errorTestBeforeFail"})
	private void beforeFail(){
		fail();
	}
}
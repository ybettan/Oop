package OOP.Tests;

import OOP.Provided.OOPExpectedException;
import OOP.Solution.*;

import static OOP.Tests.TestFunctions.*;
/**
 * Created by elran on 08/01/17.
 */
@OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
public class TestOrderedInher extends TestOrdered {

    @OOPExceptionRule
    private OOPExpectedException expected = OOPExpectedExceptionImpl.none();

    @OOPBefore({"test10"})
    public void beforeTest10_1()
    {
        expected.expect(ExceptionDummy.class);
    }

    @OOPBefore({"test12"})
    public void beforeTest12_1()
    {
        expected = OOPExpectedExceptionImpl.none();
    }

	//SUCCESS
	@Override
	@OOPTest(order = 16)
	protected void test16() //we override
	{
		c=2;
	}

	//SUCCESS
	@OOPTest(order = 18)
	private void test18()
	{
		shouldPass(2,c);//make sure this ran insted of father's function
	}

	@OOPBefore({"test17"})
	private void SbeforeTest17_1(){
		shouldPass(1,d);
		d++;
	}
	//SUCCESS
	@OOPTest(order = 17)
	public void test17()
	{
		shouldPass(2,d);
		d++;
	}
	@OOPAfter({"test17"})
	public void SafterTest17_1()
	{
		shouldPass(3,d);
		d++;
	}
}

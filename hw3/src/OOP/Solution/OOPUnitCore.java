package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPResult;
import static OOP.Provided.OOPResult.OOPTestResult.*;
import static OOP.Solution.OOPTestClass.OOPTestClassType.*;

//FIXME: remove some imports?
import OOP.Solution.OOPResultImpl;
import OOP.Provided.OOPExpectedException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.*;


public class OOPUnitCore {


    private static void runSetupMethods(Class<?> testClass, Object testClassInst)
            throws IllegalAccessException, InvocationTargetException {

        /* stoping condition - Object.getClass().getSuperClass() return null */
        if (testClass == null)
            return;

        /* activate first the base class methods */
        runSetupMethods(testClass.getSuperclass(), testClassInst);

        /* get all the Class methods annotated with @OOPSetup */
        ArrayList<Method> setupMethods = Arrays
              .stream(testClass.getMethods())
              .filter(m -> m.isAnnotationPresent(OOPSetup.class))
              .collect(Collectors.toCollection(ArrayList::new));

        for (Method m : setupMethods)
            m.invoke(testClassInst);
    }

    private static void runBeforeMethods(Class<?> testClass, String methodName,
            Object testClassInst)
            throws IllegalAccessException, InvocationTargetException {

        /* stoping condition - Object.getClass().getSuperClass() return null */
        if (testClass == null)
            return;

        /* activate first the base class methods */
        runBeforeMethods(testClass.getSuperclass(), methodName, testClassInst);

        /* get all the Class methods annotated with @OOPBefore */
        ArrayList<Method> setupMethods = Arrays
              .stream(testClass.getMethods())
              .filter(m -> m.isAnnotationPresent(OOPBefore.class))
              .collect(Collectors.toCollection(ArrayList::new));

        for (Method m : setupMethods)
            m.invoke(testClassInst);
    }

    private static void runAfterMethods(Class<?> testClass, String methodName,
            Object testClassInst)
            throws IllegalAccessException, InvocationTargetException {

        /* stoping condition - Object.getClass().getSuperClass() return null */
        if (testClass == null)
            return;

        /* get all the Class methods annotated with @OOPAfter */
        ArrayList<Method> setupMethods = Arrays
              .stream(testClass.getMethods())
              .filter(m -> m.isAnnotationPresent(OOPAfter.class))
              .collect(Collectors.toCollection(ArrayList::new));

        for (Method m : setupMethods)
            m.invoke(testClassInst);

        /* secondley, activate the base class methods */
        runAfterMethods(testClass.getSuperclass(), methodName, testClassInst);
    }

    private static OOPResult runSingleTestMethod(Method method,
            Class<?> testClass, Object testClassInst) throws IllegalAccessException {

        //FIXME: can we assume there is only a single OOPExpectedException
        //field in the test class ?

        /* get OOPExpectedException field.
         * it may be private so we can't just access it - we need to locate it
         * according to @OOPExceptionRule annotation */
        OOPExpectedException expectedException = (OOPExpectedException)
            Arrays.stream(testClass.getDeclaredFields())
                  .filter(f -> f.isAnnotationPresent(OOPExceptionRule.class))
                  .collect(Collectors.toCollection(ArrayList::new))
                  .get(0)
                  .get(testClassInst);


        try {
            method.invoke(testClassInst);
        }

        /* Exception --> Throwable --> Object */
        catch (Exception e) {

            /* SUCCESS */
            if (expectedException.assertExpected(e))
                return new OOPResultImpl(SUCCESS, null);

            /* EXPECTED_EXCEPTION_MISMATCH */
            else
                return new OOPResultImpl(EXPECTED_EXCEPTION_MISMATCH, e.getMessage());
        }

        /* OOPAssertionFailure --> AssertionError --> Error --> Throwable --> Object */
        catch (OOPAssertionFailure oaf) {

            /* FAILURE */
            return new OOPResultImpl(FAILURE, oaf.getMessage());
        }

        catch (Throwable t) {

            /* ERROR */
            return new OOPResultImpl(ERROR, t.getClass().getName());
        }

        /* SUCCESS */
        return new OOPResultImpl(SUCCESS, null);
}





    public static void assertEquals(Object expected, Object actual)
            throws OOPAssertionFailure {

        /* self check */
        if (expected == actual)
            return;
        
        /* null check, if both are null previous condition would applied */
        if (expected == null || actual == null)
            throw new OOPAssertionFailure();

        /* check type equality */
        if (expected.getClass() != actual.getClass())
            throw new OOPAssertionFailure();

        /* they have the same structure, recursively check all the fields */
        Field[] expectedFields = expected.getClass().getFields();
        Field[] actualFields = actual.getClass().getFields();
        for (int i=0 ; i<expectedFields.length ; i++) {
            OOPUnitCore.assertEquals(expectedFields[i], actualFields[i]);
        }
    }

    public static void fail() throws OOPAssertionFailure {
        throw new OOPAssertionFailure();
    }

    public static OOPTestSummary runClass(Class<?> testClass)
            throws InstantiationException, IllegalAccessException,
                              InvocationTargetException, IllegalArgumentException,
                              NoSuchMethodException {

        return runClass(testClass, "");
    }

    public static OOPTestSummary runClass(Class<?> testClass, String tag)
            throws InstantiationException, IllegalAccessException,
                   InvocationTargetException, IllegalArgumentException,
                   NoSuchMethodException {

        /* check that the class isn't null */
        if (testClass == null)
            throw new IllegalArgumentException();

        /* check if OOPTestClass annotation is present */
        if (!testClass.isAnnotationPresent(OOPTestClass.class))
            throw new IllegalArgumentException();
        
        /* create an instance of input class.
         * according to the PDF we can assume a default c'tor exist.
         * we will use  Constructor.newInstance() since the c'tor may be privae */
        Object testClassInst = testClass.getConstructor().newInstance();

        /* run all the methods annotated with @OOPSetup */
        runSetupMethods(testClass, testClassInst);

        /* get all the @OOPTest annotated methods with relevant tag */
        ArrayList<Method> methods = Arrays
              .stream(testClass.getMethods())
              .filter(m -> m.isAnnotationPresent(OOPTest.class))
              .filter(m -> m.getAnnotation(OOPTest.class).tag() == tag)
              .collect(Collectors.toCollection(ArrayList::new));

        /* if @OOPTestClass.value=ORDERED then we sort methods by @OOPTest.order */
        if (testClass.getAnnotation(OOPTestClass.class).value() == ORDERED) {
            methods.stream()
                   .sorted((m1, m2) -> (m2.getAnnotation(OOPTest.class)).order()
                                     - (m1.getAnnotation(OOPTest.class).order()))
                   .collect(Collectors.toCollection(ArrayList::new));
        }

        /* create a Map<String, OOPResult> to store test methods results */
        Map<String, OOPResult> testMap = new HashMap<String, OOPResult>();

        for (Method m : methods) {

            /* activate all its @OOPBefore methods */
            runBeforeMethods(testClass, m.getName(), testClassInst);

            /* activate the method itself.
             * according to the PDF we can assume that all the test methods
             * receive no arguments and have void return type */
            OOPResult methodRes = runSingleTestMethod(m, testClass, testClassInst);
            testMap.put(m.getName(), methodRes);

            /* activate all its @OOPAfter methods */
            runAfterMethods(testClass, m.getName(), testClassInst);
        }

        return new OOPTestSummary(testMap);
    }

}

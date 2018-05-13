package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.*;


public class OOPUnitCore {


    private static OOPTestSummary invokeInternal(Class<?> testClass, 
            Object testClassInstance, Method method)
            throws IllegalAccessException, InvocationTargetException {

        /* run all the @OOPBefore methods */
        ArrayList<Method> beforeMethods = Arrays
              .stream(testClass.getMethods())
              .filter(m -> m.isAnnotationPresent(OOPBefore.class))
              .filter(m -> Arrays.asList(m.getAnnotation(OOPBefore.class)
                                          .value())
                                          .contains(method.getName()))
              .collect(Collectors.toCollection(ArrayList::new));
        for (Method m : beforeMethods)
            m.invoke(testClassInstance);

        /* run the current method */
        method.invoke(testClassInstance);

        /* run all the @OOPAfter methods */
        ArrayList<Method> afterMethods = Arrays
              .stream(testClass.getMethods())
              .filter(m -> m.isAnnotationPresent(OOPAfter.class))
              .filter(m -> Arrays.asList(m.getAnnotation(OOPAfter.class)
                                          .value())
                                          .contains(method.getName()))
              .collect(Collectors.toCollection(ArrayList::new));
        for (Method m : afterMethods)
            m.invoke(testClassInstance);
    }

    public static void assertEquals(Object expected, Object actual)
            throws OOPAssertionFailure {

        /* self check */
        if (expected == actual)
            return;
        
        /* null check, if both are null previous condition would applied */
        if (expected == null)
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
                              InvocationTargetException, IllegalArgumentException {

        return runClass(testClass, "");
    }

    public static OOPTestSummary runClass(Class<?> testClass, String tag)
            throws InstantiationException, IllegalAccessException,
                              InvocationTargetException, IllegalArgumentException {

        /* check that the class isn't null */
        if (testClass == null)
            throw new IllegalArgumentException();

        /* check if OOPTestClass annotation is present */
        if (testClass.isAnnotationPresent(OOPTestClass.class))
            throw new IllegalArgumentException();
        
        //FIXME: the c'tor may be private - should we use:
        //                  getMethod(OOPTestClass.name) -> activate ?
        
        /* create an instance of input class.
         * according to the PDF we can assume a default c'tor exist */
        Object testClassInst = testClass.newInstance();

        //FIXME: getMethods() get only the pulics, can we assume all the methods
        //       are public ?
        //FIXME: can we assume all the test methods receive 0 arguments?
        
        /* run all the methods with OOPSetup annotation including inherited */
        ArrayList<Method> setupMethods = Arrays
              .stream(testClass.getMethods())
              .filter(m -> m.isAnnotationPresent(OOPSetup.class))
              .collect(Collectors.toCollection(ArrayList::new));
        for (Method m : setupMethods)
            m.invoke(testClassInst);

        //FIXME: should it be ran in the @Test order (maybe use forEachOrdered method ?)

        /* run all the test methods */
        ArrayList<Method> methods = Arrays
              .stream(testClass.getMethods())
              .filter(m -> m.isAnnotationPresent(OOPTest.class))
              .filter(m -> m.getAnnotation(OOPTest.class).tag() == tag)
              .collect(Collectors.toCollection(ArrayList::new));
        for (Method m : methods)
            invokeInternal(testClass, testClassInst, m);
    }
}

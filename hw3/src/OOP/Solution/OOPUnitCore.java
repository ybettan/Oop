package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPResult;
import static OOP.Provided.OOPResult.OOPTestResult.*;
import static OOP.Solution.OOPTestClass.OOPTestClassType.*;

import OOP.Provided.OOPExpectedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.*;


public class OOPUnitCore {


    private static Object backupInst(Object obj) {

        /* create the backup */
        Object objBackup = null;

        try {
            /* STAFF ASSUMPTION: we can assume a default c'tor exist. */
            objBackup = obj.getClass().getConstructor().newInstance();

            /* create a backup for each field */
            for (Field f : obj.getClass().getDeclaredFields()) {

                /* make the field accessible in case it isn't
                 * MY ASSUMPTION: the field must exist since it was instanciated
                 * from obj itself */
                f.setAccessible(true);

                /* MY ASSUMPTION: IllegalAccessException won't be thrown because
                 * we used setAccessible(true) */
                Object objFieldVal = f.get(obj);

                /* the field support clone() method */
                if (objFieldVal instanceof Cloneable) {

                    /* MY ASSUMPTION: NoSuchMethodException won't be thrown since
                     * we used setAccessible(true) */
                    Method cloneMethod = obj.getClass().getMethod("clone");

                    /* MY ASSUMPTION: IllegalAccessException won't be thrown
                     * because we used setAccessible(true) */
                    f.set(objBackup, cloneMethod.invoke(objFieldVal));
                }

                /* the object has a copy c'tor */
                try {
                    Constructor<?> copyCtor = obj.getClass()
                        .getConstructor(obj.getClass());

                    /* MY ASSUMPTION: InstantiationException won't be thrown
                     * since obj class isn't abstract */
                    f.set(objBackup, copyCtor.newInstance(objFieldVal));
                }

                /* the object doesn't have a copy c'tor */
                catch (NoSuchMethodException e) {/* do nothing */}

                /* the object has neither a clone() method nor a copy c'tor.
                 * MY ASSUMPTION: IllegalAccessException won't be thrown because
                 * we used setAccessible(true) */
                f.set(objBackup, objFieldVal);
            }
        }
        catch (Exception e) {
            System.err.println("unpossible exception was thrown at line " +
                    new Exception().getStackTrace()[0].getLineNumber());
        }

        return objBackup;
    }

    private static void recoverInst(Object obj, Object objBackup) {

        /* recover field by field */
        for (Field f : objBackup.getClass().getDeclaredFields()) {


            /* make the fields accessible in case they aren't */
            f.setAccessible(true);

            /* MY ASSUMPTION: IllegalAccessException won't be thrown because
             * we used setAccessible(true) */
            try {
                f.set(obj, f.get(objBackup));
            }
            catch (IllegalAccessException e) {
                System.err.println("unpossible exception was thrown at line " +
                        new Exception().getStackTrace()[0].getLineNumber());
            }
        }
    }

    private static void runSetupMethods(Class<?> testClass, Object testClassInst) {

        /* stopping condition - Object.getClass().getSuperClass() return null */
        if (testClass == null)
            return;

        /* activate first the base class methods */
        runSetupMethods(testClass.getSuperclass(), testClassInst);

        /* get all the Class methods annotated with @OOPSetup */
        ArrayList<Method> setupMethods = Arrays
              .stream(testClass.getMethods())
              .filter(m -> m.isAnnotationPresent(OOPSetup.class))
              .collect(Collectors.toCollection(ArrayList::new));

        for (Method m : setupMethods) {

            /* STAFF ASSUMPTION: we can assume @OOPSetup methods won't throw
             * any exception */
            try {
                m.invoke(testClassInst);
            }
            catch (Exception e) {
                System.err.println("unpossible exception was thrown at line " +
                        new Exception().getStackTrace()[0].getLineNumber());
            }
        }
    }

    private static boolean runBeforeAfterMethods(Class<?> testClass,
            String methodName, Object testClassInst, String annotationName) {

        /* stopping condition - Object.getClass().getSuperClass() return null */
        if (testClass == null)
            return true;

        /* activate first the base class methods */
        if (!runBeforeAfterMethods(testClass.getSuperclass(), methodName,
                    testClassInst, annotationName))
            return false;

        /* get all the Class methods annotated with @OOPBefore and relevant
         * to our current method */
        ArrayList<Method> beforeAfterMethods =
                    Arrays
                    .stream(testClass.getMethods())
                    .filter(m -> m.isAnnotationPresent(OOPBefore.class))
                    .filter(annotationName.equals("OOPBefore") ?
                               m -> Arrays.asList(m.getAnnotation(OOPBefore.class)
                                   .value()).contains(methodName) :
                               m -> Arrays.asList(m.getAnnotation(OOPAfter.class)
                                   .value()).contains(methodName)
                            )
                    .collect(Collectors.toCollection(ArrayList::new));

        for (Method m : beforeAfterMethods) {

            /* backup the class instance object in case @OOPBefore method
             * throws an exception */
            Object testClassInstBackup = backupInst(testClassInst);

            try {
                m.invoke(testClassInst);
            }

            catch (Exception e) {
                recoverInst(testClassInst, testClassInstBackup);
                return false;
            }
        }

        return true;
    }

    private static OOPResult runSingleTestMethod(Method method, Object testClassInst) {

        /* get OOPExpectedException field.
         * it may be private so we can't just access it - we need to locate it
         * according to @OOPExceptionRule annotation
         * if no field marked with @OOPExceptionRule exist then we don't expect
         * any exception */
        OOPExpectedException expectedException = null;
        ArrayList<Field> expectedExceptionsArr =
                Arrays.stream(testClassInst.getClass().getDeclaredFields())
                      .filter(f -> f.isAnnotationPresent(OOPExceptionRule.class))
                      .collect(Collectors.toCollection(ArrayList::new));
        if (expectedExceptionsArr.size() == 0)
            expectedException = OOPExpectedException.none();
        else {
            Field expectedExceptionField = expectedExceptionsArr.get(0);
            expectedExceptionField.setAccessible(true);

            /* MY ASSUMPTION: IllegalAccessException won't be thrown because
             * we used setAccessible(true) */
            try {
                expectedException = (OOPExpectedException)
                    expectedExceptionField.get(testClassInst);
            }
            catch (IllegalAccessException e) {
                System.err.println("unpossible exception was thrown at line " +
                        new Exception().getStackTrace()[0].getLineNumber());
            }
        }

        /* return the result.
         * STAFF ASSUMPTION we can assume that all the test methods receive no
         * arguments and have void return type */
        try {
            method.invoke(testClassInst);
        }

        /* InvocationTargetException --> ReflectiveOperationException -->
         * Exception --> Throwable --> Object.
         * this exception mean the target method has thrown an exception */
        catch (InvocationTargetException ie) {

            /* get the original exception thrown by the target method */
            Exception e = (Exception) ie.getTargetException();

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

        /* ERROR - according to FAQ */
        if (expectedException.getExpectedException() != null)
            return new OOPResultImpl(ERROR, expectedException
                    .getExpectedException().getName());

        /* SUCCESS */
        else
            return new OOPResultImpl(SUCCESS, null);
    }

    private static boolean overridedsMethod(Object o, Method m) {

        if (m.getDeclaringClass() == o.getClass())
            return true;

        return false;
    }

    private static void assertEqualsAux(Object o1, Object o2, Class<?> c)
            throws OOPAssertionFailure {

        /* if we reached Object superClass then return */
        if (c == null)
            return;

        /* if both object are null then they are equal */
        if (o1 == null && o2 == null)
            return;

        /* else if only one of the is null they are different */
        if (o1 == null || o2 == null)
            throw new OOPAssertionFailure();

        /* get the equal method.
         * MY ASSUMPTION: NoSuchMethodException can't be thrown since
         * Object implements it */
        Method equalsMethod = null;
        try {
            equalsMethod = o1.getClass().getMethod("equals", Object.class);
        }
        catch (Exception e) {}

        /* if the objects override equals() then use it */
        if (overridedsMethod(o1, equalsMethod)) {
            if (!o1.equals(o2))
                throw new OOPAssertionFailure();
            return;
        }

        /* check field by field */
        for (Field f : c.getDeclaredFields()) {

            f.setAccessible(true);

            /* get the fields values */
            Object fieldValObj1 = null;
            Object fieldValObj2 = null;
            try {

                /* MY ASSUMPTION: IllegalAccessException can't be thrown since we
                 * used setAccessible(true) */
                fieldValObj1 = f.get(o1);
                fieldValObj2 = f.get(o2);
            }
            catch (Exception e) {}

            /* check fields recursivelly */
            assertEqualsAux(fieldValObj1, fieldValObj2, fieldValObj1.getClass());
        }

        assertEqualsAux(o1, o2, c.getSuperclass());
    }










    public static void assertEquals(Object o1, Object o2) throws OOPAssertionFailure {

        assertEqualsAux(o1, o2, o1.getClass());
    }

    public static void fail() throws OOPAssertionFailure {
        throw new OOPAssertionFailure();
    }

    public static OOPTestSummary runClass(Class<?> testClass)
            throws IllegalArgumentException {

        return runClass(testClass, "");
    }

    public static OOPTestSummary runClass(Class<?> testClass, String tag)
            throws IllegalArgumentException {

        /* check that the class isn't null */
        if (testClass == null)
            throw new IllegalArgumentException();

        /* check if OOPTestClass annotation is present */
        if (!testClass.isAnnotationPresent(OOPTestClass.class))
            throw new IllegalArgumentException();
        
        /* create an instance of input class.
         * we will use  Constructor.newInstance() since the c'tor may be privae 
         * STAFF ASSUMPTION: we can assume a default c'tor exist. */
        Object testClassInst = null;
        try {
            testClassInst = testClass.getConstructor().newInstance();
        }
        catch (Exception e) {
            System.err.println("unpossible exception was thrown at line " +
                    new Exception().getStackTrace()[0].getLineNumber());
        }

        /* run all the methods annotated with @OOPSetup */
        runSetupMethods(testClass, testClassInst);

        /* get all the @OOPTest annotated methods with relevant tag */
        ArrayList<Method> methods = Arrays
              .stream(testClass.getMethods())
              .filter(m -> m.isAnnotationPresent(OOPTest.class))
              .filter(m -> m.getAnnotation(OOPTest.class).tag().equals(tag))
              .collect(Collectors.toCollection(ArrayList::new));

        /* if @OOPTestClass.value=ORDERED then we sort methods by @OOPTest.order */
        if (testClass.getAnnotation(OOPTestClass.class).value() == ORDERED) {
            methods = methods
                     .stream()
                     .sorted((m1, m2) -> (m2.getAnnotation(OOPTest.class)).order()
                            -(m1.getAnnotation(OOPTest.class).order()))
                     .collect(Collectors.toCollection(ArrayList::new));
        }

        /* create a Map<String, OOPResult> to store test methods results */
        Map<String, OOPResult> testMap = new HashMap<String, OOPResult>();

        for (Method m : methods) {

            /* activate all its @OOPBefore methods.
             * if a @OOPBefore method throws an exception then continue to
             * the next test */
            if (!runBeforeAfterMethods(testClass, m.getName(), testClassInst, "OOPBefore"))
                continue;

            /* activate the method itself */
            OOPResult methodRes = runSingleTestMethod(m, testClassInst);
            testMap.put(m.getName(), methodRes);

            /* activate all its @OOPAfter methods */
            runBeforeAfterMethods(testClass, m.getName(), testClassInst, "OOPAfter");
        }

        return new OOPTestSummary(testMap);
    }
}




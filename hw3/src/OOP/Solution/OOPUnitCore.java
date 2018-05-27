package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPResult;
import static OOP.Provided.OOPResult.OOPTestResult.*;
import static OOP.Solution.OOPTestClass.OOPTestClassType.*;

import OOP.Provided.OOPExpectedException;

import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;


public class OOPUnitCore {

//-----------------------------------------------------------------------------
//                          PRIVATE - Object Backup
//-----------------------------------------------------------------------------

    public static Object backupInst(Object obj) {

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

                /* the field object support clone() method */
                if (objFieldVal instanceof Cloneable) {

                    /* MY ASSUMPTION: NoSuchMethodException won't be thrown since
                     * we used setAccessible(true) */
                    Method cloneMethod = obj.getClass().getMethod("clone");

                    /* MY ASSUMPTION: IllegalAccessException won't be thrown
                     * because we used setAccessible(true) */
                    f.set(objBackup, cloneMethod.invoke(objFieldVal));

                    /* continue to next field */
                    continue;
                }

                /* the field object has a copy c'tor */
                try {
                    Constructor<?> copyCtor = objFieldVal.getClass()
                        .getConstructor(objFieldVal.getClass());

                    /* MY ASSUMPTION: InstantiationException won't be thrown
                     * since obj class isn't abstract */
                    f.set(objBackup, copyCtor.newInstance(objFieldVal));

                    /* continue to next field */
                    continue;
                }

                /* the field object doesn't have a copy c'tor */
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

    public static void recoverInst(Object obj, Object objBackup) {

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

//-----------------------------------------------------------------------------
//                   PRIVATE - Get OOPExpecteException
//-----------------------------------------------------------------------------

    //FIXME: make all private methods private again
    /* first class need to be done with an empty Set */
    public static void getAllFields(Class<?> c, Set<Field> res) {

        if (c == null)
            return;

        res.addAll(Arrays.asList(c.getDeclaredFields()));
        getAllFields(c.getSuperclass(), res);
    }

    /* return null if the field doesn't exist */
    public static OOPExpectedException getExpectedException(Object testClassInst) {

        Set<Field> allFields = new HashSet<Field>();
        getAllFields(testClassInst.getClass(), allFields);

        Vector<Field> tmp =
               allFields
              .stream()
              .filter(m -> m.isAnnotationPresent(OOPExceptionRule.class))
              .collect(Collectors.toCollection(Vector::new));

        OOPExpectedException res = null;

        if (tmp.size() != 0) {
            Field resField = tmp.get(0); 
            resField.setAccessible(true);

            /* MY ASSUMPTION: IllegalAccessException won't be thrown because
             * we used setAccessible(true) */
            try {
                res = (OOPExpectedException) resField.get(testClassInst);
            }
            catch (IllegalAccessException e) {
                System.err.println("unpossible exception was thrown at line " +
                        new Exception().getStackTrace()[0].getLineNumber());
            }
        }

        return res;
    }

//-----------------------------------------------------------------------------
//                      PRIVATE - Get Ordered Methods
//-----------------------------------------------------------------------------

    /* used by getAllMethods() */
    public static Object methodKey(Method m) {

        return Arrays.asList(m.getName(),
            MethodType.methodType(m.getReturnType(), m.getParameterTypes()));
    }

    public static Set<Method> getAllMethods(Class<?> c) {

        Set<Method> methods = new LinkedHashSet<>();

        /* get all the public methods including inherited */
        Collections.addAll(methods, c.getMethods());

        Map<Object,Set<Package>> types = new HashMap<>();
        final Set<Package> pkgIndependent = Collections.emptySet();
        for(Method m: methods)
            types.put(methodKey(m), pkgIndependent);

        for(Class<?> current=c; current!=null; current=current.getSuperclass()) {
            for(Method m: current.getDeclaredMethods()) {
                final int mod = m.getModifiers(),
                    access= Modifier.PUBLIC|Modifier.PROTECTED|Modifier.PRIVATE;
                if(!Modifier.isStatic(mod))
                    switch(mod&access) {
                    case Modifier.PUBLIC: continue;
                    default:
                        Set<Package> pkg = types.computeIfAbsent(methodKey(m),
                                key -> new HashSet<>());
                        if(pkg!=pkgIndependent && pkg.add(current.getPackage()))
                            break;
                        else
                            continue;
                    case Modifier.PROTECTED:
                        if(types.putIfAbsent(methodKey(m), pkgIndependent)!=null)
                            continue;
                        /* otherwise fall-through */
                    case Modifier.PRIVATE:
                }
                methods.add(m);
            }
        }
        return methods;
    }

    /* by order: ... -> derived2 -> derived1 -> base
     * first iteration shult get an empty Vector */
    public static void getInheritanceCahin(Class<?> c, Vector<Class<?>> res) {

        if (c == null)
            return;

        res.add(c);
        getInheritanceCahin(c.getSuperclass(), res);
    }

    /* by order: base -> derived1 -> derived2 -> ... */
    public static Vector<Method> getOrderedSetupMethods(Class<?> c) {

        Vector<Class<?>> inheritanceChain = new Vector<Class<?>>();
        getInheritanceCahin(c, inheritanceChain);

        Vector<Method> orderedMethods =
               getAllMethods(c)
              .stream()
              .filter(m -> m.isAnnotationPresent(OOPSetup.class))
              .sorted((m1, m2) -> inheritanceChain.indexOf(m2.getDeclaringClass())
                                - inheritanceChain.indexOf(m1.getDeclaringClass()))
              .collect(Collectors.toCollection(Vector::new));

        return orderedMethods;
    }

    /* by order: base -> derived1 -> derived2 -> ... */
    public static Vector<Method> getOrderedBeforeMethods(Class<?> c,
            String methodName) {

        Vector<Class<?>> inheritanceChain = new Vector<Class<?>>();
        getInheritanceCahin(c, inheritanceChain);

        Vector<Method> orderedMethods =
               getAllMethods(c)
              .stream()
              .filter(m -> m.isAnnotationPresent(OOPBefore.class))
              .filter(m -> Arrays.asList(m.getAnnotation(OOPBefore.class)
                          .value()).contains(methodName))
              .sorted((m1, m2) -> inheritanceChain.indexOf(m2.getDeclaringClass())
                                - inheritanceChain.indexOf(m1.getDeclaringClass()))
              .collect(Collectors.toCollection(Vector::new));

        return orderedMethods;
    }

    /* by order: ... -> derived2 -> derived1 -> base */
    public static Vector<Method> getOrderedAfterMethods(Class<?> c,
            String methodName) {

        Vector<Class<?>> inheritanceChain = new Vector<Class<?>>();
        getInheritanceCahin(c, inheritanceChain);

        Vector<Method> orderedMethods =
               getAllMethods(c)
              .stream()
              .filter(m -> m.isAnnotationPresent(OOPAfter.class))
              .filter(m -> Arrays.asList(m.getAnnotation(OOPAfter.class)
                          .value()).contains(methodName))
              .sorted((m1, m2) -> inheritanceChain.indexOf(m1.getDeclaringClass())
                                - inheritanceChain.indexOf(m2.getDeclaringClass()))
              .collect(Collectors.toCollection(Vector::new));

        return orderedMethods;
    }

    /* by order of @OOPTest(order=n) */
    public static Vector<Method> getOrderedTestMethods(Class<?> c,
            String tagName, boolean useTag) {

        Vector<Method> orderedMethods = null;

        if (useTag) {

            orderedMethods =
                   getAllMethods(c)
                  .stream()
                  .filter(m -> m.isAnnotationPresent(OOPTest.class))
                  .filter(m -> m.getAnnotation(OOPTest.class).tag().equals(tagName))
                  .sorted((m1, m2) -> m1.getAnnotation(OOPTest.class).order()
                                    - m2.getAnnotation(OOPTest.class).order())
                  .collect(Collectors.toCollection(Vector::new));
        }
        else {

            orderedMethods =
                   getAllMethods(c)
                  .stream()
                  .filter(m -> m.isAnnotationPresent(OOPTest.class))
                  .sorted((m1, m2) -> m1.getAnnotation(OOPTest.class).order()
                                    - m2.getAnnotation(OOPTest.class).order())
                  .collect(Collectors.toCollection(Vector::new));
        }

        return orderedMethods;
    }

//-----------------------------------------------------------------------------
//                   PRIVATE - Activate Ordered Methods
//-----------------------------------------------------------------------------

    public static void runSetupMethods(Object testClassInst) {

        Vector<Method> orderedSetupMethods =
            getOrderedSetupMethods(testClassInst.getClass());

        for (Method m : orderedSetupMethods) {

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

    public static boolean runBeforeAfterMethods(Object testClassInst,
            String methodName, String afterOrBefore, String thrownClassName) {

        Vector<Method> orderedMethods;
        
        if (afterOrBefore == "before")
           orderedMethods =
               getOrderedBeforeMethods(testClassInst.getClass(), methodName);
        else
           orderedMethods =
               getOrderedAfterMethods(testClassInst.getClass(), methodName);

        for (Method m : orderedMethods) {

            /* backup the class instance object in case @OOPBefore method
             * throws an exception */
            Object testClassInstBackup = backupInst(testClassInst);

            /* make m accessible in case it is private */
            m.setAccessible(true);

            try {
                m.invoke(testClassInst);
            }

            /* MY ASSUMPTION: IllegalAccessException can't be thrown since we used
             * setAccessibel(true) */
            catch (IllegalAccessException iae) {
                System.err.println("unpossible exception was thrown at line " +
                        new Exception().getStackTrace()[0].getLineNumber());
            }

            catch (InvocationTargetException ie) {

                /* get the original exception thrown by the target method */
                Throwable t = ie.getTargetException();

                /* recover the object */
                recoverInst(testClassInst, testClassInstBackup);

                /* get the trowable's class name */
                thrownClassName = thrownClassName.concat(t.getClass().getName());

                return false;
            }
        }

        return true;
    }

    public static boolean runBeforeMethods(Object testClassInst,
            String methodName, String thrownClassName) {
        
        return runBeforeAfterMethods(testClassInst, methodName, "before",
                thrownClassName);
    }

    public static boolean runAfterMethods(Object testClassInst,
            String methodName, String thrownClassName) {
        
        return runBeforeAfterMethods(testClassInst, methodName, "after",
                thrownClassName);
    }

    public static OOPResult runSingleTestMethod(Method method,
            Object testClassInst) {

        /* if OOPExpectedException field exist - reset it */
        OOPExpectedException expectedException =
            getExpectedException(testClassInst);
        if (expectedException != null)
            expectedException = OOPExpectedException.none();

        /* make it accessibel in case it is private */
        method.setAccessible(true);

        /* return the result.
         * STAFF ASSUMPTION: we can assume that all the test methods receive no
         * arguments and have void return type */
        try {
            method.invoke(testClassInst);
        }

        /* MY ASSUMPTION: IllegalAccessException can't be thrown since we used
         * setAccessibel(true) */
        catch (IllegalAccessException iae) {
            System.err.println("unpossible exception was thrown at line " +
                    new Exception().getStackTrace()[0].getLineNumber());
        }

        /* this exception mean the target method has thrown an exception */
        catch (InvocationTargetException ie) {

            /* get the original exception thrown by the target method */
            Throwable t = ie.getTargetException();

            /* get expectedException after method ran */
            expectedException = getExpectedException(testClassInst);

            /* SUCCESS */
            if (Exception.class.isAssignableFrom(t.getClass()))
                if (expectedException.assertExpected((Exception)t))
                    return new OOPResultImpl(SUCCESS, null);

            /* FAILURE */
            if (t.getClass().equals(OOPAssertionFailure.class))
                return new OOPResultImpl(FAILURE, t.getMessage());

            /* EXPECTED_EXCEPTION_MISMATCH */
            if (expectedException.getExpectedException() != null)
                return new OOPResultImpl(EXPECTED_EXCEPTION_MISMATCH, t.getMessage());

            /* ERROR */
            else
                return new OOPResultImpl(ERROR, t.getClass().getName());
        }

        /* get expectedException after method ran */
        expectedException = getExpectedException(testClassInst);

        /* ERROR - according to FAQ */
        if (expectedException != null &&
                expectedException.getExpectedException() != null)
            return new OOPResultImpl(ERROR, expectedException
                    .getExpectedException().getName());

        /* SUCCESS */
        else
            return new OOPResultImpl(SUCCESS, null);
    }


//-----------------------------------------------------------------------------
//                      PRIVATE - Objects Equality
//-----------------------------------------------------------------------------

    public static boolean overridedsMethod(Object o, Method m) {

        if (m.getDeclaringClass() == o.getClass())
            return true;

        return false;
    }

    public static void assertEqualsAux(Object o1, Object o2, Class<?> c)
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

//-----------------------------------------------------------------------------
//                          PRIVATE - runClassAux 
//-----------------------------------------------------------------------------

    public static OOPTestSummary runClassAux(Class<?> testClass, String tag,
            boolean useTag) throws IllegalArgumentException {

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

        //FIXME: update according to ophir's answer
        /* run all the methods annotated with @OOPSetup */
        runSetupMethods(testClassInst);

        /* we will always run it in ORDER since we chose the order for UNORDERED */
        Vector<Method> testMethods = getOrderedTestMethods(testClass, tag, useTag);

        /* create a Map<String, OOPResult> to store test methods results */
        Map<String, OOPResult> testMap = new HashMap<String, OOPResult>();

        for (Method m : testMethods) {

            String throwableName = new String();
            OOPResult methodRes = null;

            /* activate all its @OOPBefore methods.
             * if a @OOPBefore method throws an exception then continue to
             * the next test */
            if (!runBeforeMethods(testClassInst, m.getName(), throwableName)) {

                /* in this case the result is ERROR with the throwable class name */
                methodRes = new OOPResultImpl(ERROR, throwableName);
                testMap.put(m.getName(), methodRes);

                /* continue to next test method */
                continue;
            }

            /* activate the method itself */
            methodRes = runSingleTestMethod(m, testClassInst);
            testMap.put(m.getName(), methodRes);

            /* activate all its @OOPAfter methods */
            if (!runAfterMethods(testClassInst, m.getName(), throwableName)) {

                /* in this case the result is ERROR with the throwable class name */
                methodRes = new OOPResultImpl(ERROR, throwableName);
                testMap.put(m.getName(), methodRes);
            }
        }

        return new OOPTestSummary(testMap);
    }









//-----------------------------------------------------------------------------
//                                 PUBLIC
//-----------------------------------------------------------------------------

    public static void assertEquals(Object o1, Object o2)
            throws OOPAssertionFailure {

        assertEqualsAux(o1, o2, o1.getClass());
    }

    public static void fail() throws OOPAssertionFailure {
        throw new OOPAssertionFailure();
    }

    public static OOPTestSummary runClass(Class<?> testClass)
            throws IllegalArgumentException {

        return runClassAux(testClass, null, false);
    }

    //FIXME: remove all the catch printing errors
    public static OOPTestSummary runClass(Class<?> testClass, String tag)
            throws IllegalArgumentException {

        return runClassAux(testClass, tag, true);
    }
}




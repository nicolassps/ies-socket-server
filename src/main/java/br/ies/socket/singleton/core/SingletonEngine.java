package br.ies.socket.singleton.core;

import br.ies.socket.singleton.annotation.Inject;
import br.ies.socket.singleton.annotation.Singleton;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

@Log
public class SingletonEngine {
    private static final HashMap<Class<?>, Object> INSTANCES = new HashMap<>();
    private static final String[] PACKAGES_SCAN = {"br.ies.socket.service", "br.ies.socket.repository"};

    public static <T> T getInstance(Class<T> c){
        return c.cast(INSTANCES.get(c));
    }

    public static void init() {
        log.log(INFO, "Starting singleton instances");

        var classes = findAllSingletonClasses();
        log.log(INFO, "Loaded {0} classes", classes.size());

        classes
                .stream()
                .filter(c -> nonNull(c.getAnnotation(Singleton.class)))
                .iterator()
                .forEachRemaining(SingletonEngine::createInstance);

        log.log(INFO, "Injecting dependencies of {0} Singleton components", INSTANCES.size());

        INSTANCES.values()
                .stream()
                .forEach(SingletonEngine::injectDependencies);

        log.log(INFO, "All injections occurred with success");
    }

    private static void injectDependencies(Object o){
        var fields = Arrays.stream(o
                .getClass()
                .getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class));

        fields
                .filter(Field::trySetAccessible)
                .forEach(field -> {
                    try {
                        field.set(o, getCandidateOfClass(field.getType()));
                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static Object getCandidateOfClass(Class<?> c) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if(INSTANCES.containsKey(c))
            return INSTANCES.get(c);

        var constructor = Arrays
                .stream(c.getConstructors())
                .filter(cons -> cons.getParameters().length == 0)
                .findFirst();

        if(constructor.isEmpty())
            throw new RuntimeException("No candidates for class: " + c.getName());

        return constructor.get().newInstance();
    }

    private static void createInstance(Class<?> c) {
        var constructorDefault = Arrays.stream(c.getConstructors())
                .filter(constructor -> constructor.getParameters().length == 0)
                .findFirst();

        constructorDefault
                .ifPresentOrElse(
                        constructor -> {
                            try {
                                INSTANCES.put(c, constructor.newInstance());
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> log.log(WARNING, "SingletonEngine for class {0}", c.getName())
                );
    }

    private static Set<Class<?>> findAllSingletonClasses(){
        var set = new HashSet<Class<?>>();

        Arrays.stream(PACKAGES_SCAN)
                .map(SingletonEngine::findAllClassesUsingClassLoader)
                .forEach(set::addAll);

        return set;
    }

    private static Set<Class<?>> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader
                .getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));

        assert stream != null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(l -> packageName
                        + "."
                        + l.substring(0, l.lastIndexOf('.')))
                .map(SingletonEngine::getClass)
                .collect(Collectors.toSet());
    }

    private static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.log(WARNING, "SingletonEngine failed to getClass, message: {0}", e.getMessage());
        }
        return null;
    }
}

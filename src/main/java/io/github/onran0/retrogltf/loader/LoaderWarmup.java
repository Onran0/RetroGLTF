package io.github.onran0.retrogltf.loader;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.io.PrintStream;
import java.io.OutputStream;
import java.util.Set;

public class LoaderWarmup {

    public static void initializeClasses() {
        PrintStream srcErr = System.err;

        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) { }
        }));

        Reflections reflections = new Reflections(
                LoaderWarmup.class.getPackage().getName(),
                Scanners.SubTypes.filterResultsBy(s -> true)
        );

        System.setErr(srcErr);

        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        for (Class<?> clazz : classes) {
            try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            } catch (ClassNotFoundException e) {
                srcErr.printf("failed to warmup loader: %s", e);
            }
        }
    }
}
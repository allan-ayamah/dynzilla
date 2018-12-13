package com.atena.dynzilla;

import java.io.IOException;
import java.io.InputStream;

import com.atena.dynzilla.ResourceLocator;


public class ClassLoaderResourceLocator implements ResourceLocator {

    private final ClassLoader classLoader;

    public ClassLoaderResourceLocator(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public InputStream getInputStream(String relativePath) throws IOException {
        System.out.println(classLoader.getResource(""));
        InputStream input = classLoader.getResourceAsStream(relativePath);
        if (input != null) {
            return input;
        }
    System.out.println(classLoader.getResource(relativePath));
        return getClass().getClassLoader().getResourceAsStream(relativePath);
    }

}

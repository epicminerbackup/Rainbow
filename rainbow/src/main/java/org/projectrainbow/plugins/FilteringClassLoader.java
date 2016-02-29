package org.projectrainbow.plugins;

public class FilteringClassLoader extends ClassLoader {
    public FilteringClassLoader(ClassLoader delegate) {
        super(delegate);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (isBadClass(name)) {
            throw new ClassNotFoundException(name);
        }
        return super.findClass(name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (isBadClass(name)) {
            throw new ClassNotFoundException(name);
        }
        return super.loadClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean b) throws ClassNotFoundException {
        if (isBadClass(name)) {
            throw new ClassNotFoundException(name);
        }
        return super.loadClass(name, b);
    }

    private boolean isBadClass(String name) {
        return name.startsWith("org.projectrainbow.") || name.startsWith("net.minecraft.") || name.startsWith("joebkt.") || !name.contains(".");
    }
}

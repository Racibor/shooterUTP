package shooter.reflection_utils;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

    public static List<Class<?>> getClasses(String pack) {
        List<Class<?>> classes = new ArrayList<>();
        URL root = Thread.currentThread().getContextClassLoader().getResource(pack.replace(".", "/"));

        File[] files = new File(root.getFile()).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });

        try {
            for(File file : files) {
                String className = file.getName().replaceAll(".class$", "");
                Class<?> clazz = Class.forName(pack + "." + className);
                classes.add(clazz);
            }
            return classes;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }
}

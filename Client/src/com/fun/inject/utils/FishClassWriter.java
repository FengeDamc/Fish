package com.fun.inject.utils;

import com.fun.inject.Agent;

import org.objectweb.asm.ClassReader;

public class FishClassWriter extends org.objectweb.asm.ClassWriter {
    public FishClassWriter(int i) {
        super(i);
    }

    public FishClassWriter(ClassReader classReader, int i) {
        super(classReader, i);

    }

    @Override
    protected String getCommonSuperClass(String var1, String var2) {

        Class var3;
        Class var4;
        try {
            var3 = Agent.findClass(var1.replace('/', '.'));
            var4 = Agent.findClass(var2.replace('/', '.'));
        } catch (Exception var7) {
            throw new RuntimeException(var7.toString());
        }

        if (var3.isAssignableFrom(var4)) {
            return var1;
        } else if (var4.isAssignableFrom(var3)) {
            return var2;
        } else if (!var3.isInterface() && !var4.isInterface()) {
            do {
                var3 = var3.getSuperclass();
            } while(!var3.isAssignableFrom(var4));

            return var3.getName().replace('.', '/');
        } else {
            return "java/lang/Object";
        }
    }
}

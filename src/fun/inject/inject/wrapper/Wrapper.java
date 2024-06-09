package fun.inject.inject.wrapper;


import fun.inject.Agent;
import fun.inject.inject.Mappings;
import fun.utils.Classes;

public class Wrapper {

    private final String name, obfName;
    public Class<?> clazz;
    public Object obj;

    public Wrapper(String name) {
        this.name = name;

        obfName = Mappings.getObfClass(name);
        if (obfName != null) {
            try {
                clazz = Agent.findClass(obfName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Wrapper(Classes c){
        name=c.getVClass().friendly_name;
        obfName=c.getName();
        clazz=c.getClazz();
    }

    public String getName() {
        return name;
    }

    public String getObfName() {
        return obfName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

}

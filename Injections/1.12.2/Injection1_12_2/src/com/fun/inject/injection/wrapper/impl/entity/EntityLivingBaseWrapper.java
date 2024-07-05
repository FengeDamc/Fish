package com.fun.inject.injection.wrapper.impl.entity;

import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.utils.version.clazz.Classes;

public class EntityLivingBaseWrapper extends EntityWrapper {
    public EntityLivingBaseWrapper(Object entity) {
        super(Classes.EntityLivingBase);
        this.obj = entity;
    }

    public EntityLivingBaseWrapper(int id) {
        super(id);
    }

    public EntityLivingBaseWrapper(Classes c) {
        super(c);
    }
    public int getHurtTime() {
        return (Integer) ReflectionUtils.getFieldValue(obj, Mappings.getObfField("field_70737_aN"));
    }
}

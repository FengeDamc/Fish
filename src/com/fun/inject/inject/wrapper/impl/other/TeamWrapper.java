package com.fun.inject.inject.wrapper.impl.other;

import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.inject.wrapper.Wrapper;

public class TeamWrapper extends Wrapper {
    public TeamWrapper(Object o) {
        super(Classes.Team);
        this.obj=o;
    }

    public boolean isSameTeam(TeamWrapper team) {
        return (boolean) Methods.isSameTeam.invoke(obj,team.obj);
    }
}

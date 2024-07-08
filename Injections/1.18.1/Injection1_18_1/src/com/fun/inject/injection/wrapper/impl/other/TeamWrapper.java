package com.fun.inject.injection.wrapper.impl.other;

import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.injection.wrapper.Wrapper;
import net.minecraft.world.scores.Team;


public class TeamWrapper extends Wrapper {
    public TeamWrapper(Object o) {
        super(Classes.Team);
        this.obj=o;
    }

    public boolean isSameTeam(TeamWrapper team) {
        return ((Team)obj).isAlliedTo((Team)team.obj);
    }
}

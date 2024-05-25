package com.darkmagician6.eventapi.event.events;

import com.darkmagician6.eventapi.EventManager;

public class EventMotion extends Event{
    public double x,y,z;
    public float yaw,pitch;
    public EventMotion(double x,double y,double z,float yaw,float pitch){
        this.x=x;
        this.y=y;
        this.z=z;
        this.yaw=yaw;
        this.pitch=pitch;

    }
}

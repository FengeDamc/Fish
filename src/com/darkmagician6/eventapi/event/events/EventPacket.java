package com.darkmagician6.eventapi.event.events;


public class EventPacket extends Event {
    public Object packet;
    public EventPacket(Object packetIn){
        packet=packetIn;
    }
}

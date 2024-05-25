package fun.inject.inject.wrapper.impl.network.packets;

import fun.inject.inject.wrapper.Wrapper;
import fun.utils.Classes;

public class PacketWrapper extends Wrapper {
    public Object packetObj;
    public PacketWrapper(String name) {
        super(name);
    }
    public PacketWrapper(String name,Object obj) {
        super(name);
        packetObj=obj;
    }
    public PacketWrapper(Classes name, Object obj) {
        super(name);
        packetObj=obj;
    }
}

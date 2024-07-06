package com.fun.network;

import java.io.Serializable;

public interface IPacket extends Serializable {
    void process();
}

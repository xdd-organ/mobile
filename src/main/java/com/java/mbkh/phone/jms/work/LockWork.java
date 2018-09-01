package com.java.mbkh.phone.jms.work;

import java.util.Map;

public interface LockWork {

    void doService(String channelId, Map<String, String> msg);

}

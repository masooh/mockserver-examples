package com.github.masooh.logback;

import java.io.File;

import ch.qos.logback.core.rolling.TriggeringPolicyBase;

public class TriggerAlwaysPolicy<E> extends TriggeringPolicyBase<E> {
    public boolean isTriggeringEvent(File activeFile, E event) {
        return true;
    }
}

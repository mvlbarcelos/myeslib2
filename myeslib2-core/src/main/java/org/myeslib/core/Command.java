package org.myeslib.core;

import java.io.Serializable;
import java.util.UUID;

public interface Command extends Serializable {

    UUID commandId();

}

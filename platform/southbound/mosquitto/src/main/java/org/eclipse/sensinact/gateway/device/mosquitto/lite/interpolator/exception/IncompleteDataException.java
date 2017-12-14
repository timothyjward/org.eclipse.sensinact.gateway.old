/*
 * Copyright (c) 2017 CEA.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    CEA - initial API and implementation
 */
package org.eclipse.sensinact.gateway.device.mosquitto.lite.interpolator.exception;

public class IncompleteDataException extends InterpolationException {
    public IncompleteDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompleteDataException(Throwable cause) {
        super(cause);
    }

    public IncompleteDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IncompleteDataException(String message) {
        super(message);
    }

    public IncompleteDataException() {
        super();
    }
}
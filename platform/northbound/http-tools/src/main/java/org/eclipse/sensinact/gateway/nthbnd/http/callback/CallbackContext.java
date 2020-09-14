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
package org.eclipse.sensinact.gateway.nthbnd.http.callback;

/**
 * {@link RequestWrapper} and {@link ResponseWrapper} holder
 * 
 * @author <a href="mailto:cmunilla@kentyou.com">Christophe Munilla</a>
 */
public interface CallbackContext {

    /**
     * Returns an {@link RequestWrapper} wrapping the underlying 
     * request whatever it is an Http or a websocket one.
     *
     * @return an {@link RequestWrapper} wrapping the underlying 
     * request
     */
    RequestWrapper getRequest();
    
    /**
     * Returns an {@link ResponseWrapper} wrapping the underlying 
     * response to be sent back to the requirer.
     *
     * @return an {@link ResponseWrapper} wrapping the underlying 
     * response
     */
    ResponseWrapper getResponse();
}

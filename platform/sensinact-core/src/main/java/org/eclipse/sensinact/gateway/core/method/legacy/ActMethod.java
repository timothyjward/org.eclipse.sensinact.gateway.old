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
package org.eclipse.sensinact.gateway.core.method.legacy;


import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.core.method.AbstractAccessMethod;
import org.eclipse.sensinact.gateway.core.method.AccessMethod;
import org.eclipse.sensinact.gateway.core.method.AccessMethodExecutor;

/**
 * Extended {@link AccessMethod} dedicated to an Actuation
 * 
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public class ActMethod extends AbstractAccessMethod
{
	/**
	 * Constructor
	 * 
     * @param mediator
     * @param uri
     * @param preProcessingExecutor
     */
    public ActMethod(Mediator mediator, String uri, 
			AccessMethodExecutor preProcessingExecutor)
    {
	    this(mediator, uri, preProcessingExecutor,null);
    }

	/**
	 * Constructor
	 * 
     * @param mediator
     * @param uri
     * @param preProcessingExecutor
     * @param postProcessingExecutor
     */
    public ActMethod(Mediator mediator, String uri, 
			AccessMethodExecutor preProcessingExecutor, 
			AccessMethodExecutor postProcessingExecutor)
    {
	    super(mediator, uri, AccessMethod.ACT, preProcessingExecutor,
	    		postProcessingExecutor, null);
    }

    /**
     * @inheritDoc
     *
     * @see org.eclipse.sensinact.gateway.core.method.AbstractAccessMethod#
     * createAccessMethodResult(java.lang.Object[])
     */
    @Override
    protected ActResult createAccessMethodResult(Object[] parameters)
    {
	    return new ActResult(super.mediator, uri, parameters);
    }
}

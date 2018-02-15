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
package org.eclipse.sensinact.gateway.core;

/**
 * Filter to be applied on the response of an sensiNact's 
 * access method call
 * 
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public interface Filtering
{	
	/**
	 * Returns true if this ResponseFilter is able
	 * to handle the String type of filter passed as 
	 * parameter; returns false otherwise
	 * 
	 * @param type the String type of filter
	 * 
	 * @return 
	 * <ul>
	 * 		<li>true if the specified type of filter is 
	 * 			handled by this ResponseFilter</li>
	 * 		<li> false otherwise</li>
	 * </ul> 
	 */
	boolean handle(String type);
	
	/**
	 * Applies the String filter passed as parameter on the 
	 * &lt;F&gt; typed result Object also passed as parameter 
	 * and  returned the &lt;F&gt; typed filtered result, in 
	 * the same format 
	 * 
	 * @param filter the String filter to apply on the 
	 * specified Object
	 * @param result &lt;F&gt; typed result Object to be
	 * filtered 
	 * 
	 * @return the &lt;F&gt; typed result
	 */
	<F> F apply(String filter, F result);
}
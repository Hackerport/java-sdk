/*
 * @(#)OutboundConnectionCache.java	1.4 10/03/23
 * 
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.corba.se.pept.transport;

/**
 * @author Harold Carr
 */
public interface OutboundConnectionCache 
    extends ConnectionCache
{
    public Connection get(ContactInfo contactInfo);

    public void put(ContactInfo contactInfo, Connection connection);

    public void remove(ContactInfo contactInfo);
}

// End of file.

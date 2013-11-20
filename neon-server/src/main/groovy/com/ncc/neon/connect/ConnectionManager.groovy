package com.ncc.neon.connect

import com.ncc.neon.metadata.MetadataConnection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
/*
 * ************************************************************************
 * Copyright (c), 2013 Next Century Corporation. All Rights Reserved.
 *
 * This software code is the exclusive property of Next Century Corporation and is
 * protected by United States and International laws relating to the protection
 * of intellectual property.  Distribution of this software code by or to an
 * unauthorized party, or removal of any of these notices, is strictly
 * prohibited and punishable by law.
 *
 * UNLESS PROVIDED OTHERWISE IN A LICENSE AGREEMENT GOVERNING THE USE OF THIS
 * SOFTWARE, TO WHICH YOU ARE AN AUTHORIZED PARTY, THIS SOFTWARE CODE HAS BEEN
 * ACQUIRED BY YOU "AS IS" AND WITHOUT WARRANTY OF ANY KIND.  ANY USE BY YOU OF
 * THIS SOFTWARE CODE IS AT YOUR OWN RISK.  ALL WARRANTIES OF ANY KIND, EITHER
 * EXPRESSED OR IMPLIED, INCLUDING, WITHOUT LIMITATION, IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE HEREBY EXPRESSLY
 * DISCLAIMED.
 *
 * PROPRIETARY AND CONFIDENTIAL TRADE SECRET MATERIAL NOT FOR DISCLOSURE OUTSIDE
 * OF NEXT CENTURY CORPORATION EXCEPT BY PRIOR WRITTEN PERMISSION AND WHEN
 * RECIPIENT IS UNDER OBLIGATION TO MAINTAIN SECRECY.
 *
 * 
 * @author tbrooks
 */

/**
 * This holds the connection information for the application.
 *
 * We have a connection to mongo per JVM for metadata. Users may also use that connection.
 * We have a session connection per user for other connections. This allows one user to connect
 * to hive and another to connect to mongo.
 */


@Component
class ConnectionManager {

    private final String defaultMongoUrl

    @Autowired
    MetadataConnection metadataConnection

    @Autowired
    SessionConnection sessionConnection

    ConnectionManager() {
        this.defaultMongoUrl = System.getProperty("mongo.hosts", "localhost")
    }

    void connect(ConnectionInfo info) {
        if(info.dataSource == DataSources.mongo && info.connectionUrl == defaultMongoUrl){
            sessionConnection.connectionInfo = null
            return
        }
        sessionConnection.connectionInfo = info
    }

    def getClient() {
        if(!sessionConnection.connectionInfo){
            return metadataConnection.client
        }
        return sessionConnection.getClient()
    }

    boolean isConnectedToHive() {
        sessionConnection.connectionInfo?.dataSource == DataSources.hive
    }

}

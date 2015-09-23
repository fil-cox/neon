/*
 * Copyright 2013 Next Century Corporation
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ncc.neon.elasticsearch

import com.ncc.neon.AbstractQueryExecutorIntegrationTest
import com.ncc.neon.IntegrationTestContext
import com.ncc.neon.connect.ConnectionInfo
import com.ncc.neon.connect.DataSources
import com.ncc.neon.query.elasticsearch.ElasticSearchQueryExecutor
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assume
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * Integration test that verifies the neon server properly translates elasticsearch queries.
 * These tests parallel the acceptance tests in the javascript client query acceptance tests
 */
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = IntegrationTestContext)
class ElasticSearchQueryExecutorIntegrationTest extends AbstractQueryExecutorIntegrationTest{
    private static final String HOST_STRING = System.getProperty("elasticsearch.host")

    private ElasticSearchQueryExecutor elasticSearchQueryExecutor

    protected String getResultsJsonFolder() {
        return "elasticsearch-json/"
    }

    @SuppressWarnings('JUnitPublicNonTestMethod')
    @Autowired
    public void setElasticSearchQueryExecutor(ElasticSearchQueryExecutor elasticSearchQueryExecutor) {
        this.elasticSearchQueryExecutor = elasticSearchQueryExecutor
    }

    @Before
    void before() {
        // Establish the connection, or skip the tests if no host was specified
        Assume.assumeTrue(HOST_STRING != null && HOST_STRING != "")
        this.elasticSearchQueryExecutor.connectionManager.currentRequest = new ConnectionInfo(host: HOST_STRING, dataSource: DataSources.elasticsearch)
    }


    protected ElasticSearchQueryExecutor getQueryExecutor(){
        elasticSearchQueryExecutor
    }

    @Override
    protected def convertRowValueToBasicJavaType(def val) {
        return super.convertRowValueToBasicJavaType(val)
    }

    @Override
    protected def jsonObjectToMap(jsonObject) {
        def map = [:]
        jsonObject.keys().each { key ->
            def value = jsonObject.get(key)
            if (key =~ AbstractQueryExecutorIntegrationTest.DATE_FIELD_REGEX) {
                DateTimeFormatter formatIn = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                map[key] = formatIn.withZoneUTC().parseDateTime(value).toString()
            } else if (value instanceof JSONArray) {
                map[key] = jsonArrayToList(value)
            } else if (value instanceof JSONObject) {
                map[key] = jsonObjectToMap(value)
            } else {
                map[key] = value
            }
        }
        return map
    }
}

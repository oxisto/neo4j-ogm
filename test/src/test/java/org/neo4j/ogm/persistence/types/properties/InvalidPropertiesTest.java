/*
 * Copyright (c) 2002-2020 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
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
 */

package org.neo4j.ogm.persistence.types.properties;

import org.junit.Test;
import org.neo4j.ogm.domain.properties.UserWithInvalidPropertiesType;
import org.neo4j.ogm.exception.core.MappingException;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.testutil.MultiDriverTestClass;

/**
 * Test for {@link org.neo4j.ogm.annotation.Properties} annotation that tests that invalid cases throw
 * {@link MappingException}
 *
 * @author Frantisek Hartman
 */
public class InvalidPropertiesTest extends MultiDriverTestClass {

    private static Session session;

    @Test(expected = MappingException.class)
    public void shouldThrowInvalidMappingException() throws Exception {
        session = new SessionFactory(driver,
            UserWithInvalidPropertiesType.class.getName())
            .openSession();
    }

}

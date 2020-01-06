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

package org.neo4j.ogm.cypher.compiler;

import java.util.Collection;
import java.util.Map;

import org.neo4j.ogm.model.Node;

/**
 * Builds a node to be persisted in the database.
 *
 * @author Luanne Misquitta
 * @author Mark Angrish
 */
public interface NodeBuilder {

    Long reference();

    NodeBuilder addProperty(String key, Object value);

    NodeBuilder addProperties(Map<String, ?> properties);

    NodeBuilder addLabels(Collection<String> labels);

    String[] addedLabels();

    NodeBuilder removeLabels(Collection<String> labels);

    Node node();

    NodeBuilder setPrimaryIndex(String primaryIndexField);
}

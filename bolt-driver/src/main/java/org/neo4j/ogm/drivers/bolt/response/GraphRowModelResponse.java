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

package org.neo4j.ogm.drivers.bolt.response;

import java.util.Arrays;

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.ogm.model.GraphRowListModel;
import org.neo4j.ogm.response.model.DefaultGraphRowListModel;
import org.neo4j.ogm.transaction.TransactionManager;

/**
 * @author Luanne Misquitta
 */
public class GraphRowModelResponse extends BoltResponse<GraphRowListModel> {

    private BoltGraphRowModelAdapter adapter = new BoltGraphRowModelAdapter(new BoltGraphModelAdapter());

    public GraphRowModelResponse(StatementResult result, TransactionManager transactionManager) {
        super(result, transactionManager);
        adapter.setColumns(Arrays.asList(columns()));
    }

    @Override
    public GraphRowListModel fetchNext() {
        if (result.hasNext()) {
            DefaultGraphRowListModel model = new DefaultGraphRowListModel();
            model.add(adapter.adapt(result.next().asMap()));

            while (result.hasNext()) {
                model.add(adapter.adapt(result.next().asMap()));
            }
            return model;
        }
        return null;
    }
}

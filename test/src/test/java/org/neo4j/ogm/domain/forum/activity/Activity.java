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

package org.neo4j.ogm.domain.forum.activity;

import java.util.Date;

import org.neo4j.ogm.annotation.GraphId;

/**
 * @author Vince Bickers
 */
public abstract class Activity {

    private Date date;

    @GraphId  // not strictly necessary, can always default to field id, but required to explicitly use this getter
    private Long id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getActivityId() {
        return id;
    }

    public void setActivityId(Long id) {
        this.id = id;
    }
}

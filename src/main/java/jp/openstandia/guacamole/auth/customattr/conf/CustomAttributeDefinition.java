/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package jp.openstandia.guacamole.auth.customattr.conf;

import java.util.Collection;

public class CustomAttributeDefinition {
    public final String method;
    public final String form;
    public final String name;
    public final String type;
    public final Collection<String> options;

    CustomAttributeDefinition(String method, String form, String name, String type, Collection<String> options) {
        this.method = method;
        this.form = form;
        this.name = name;
        this.type = type;
        this.options = options;
    }

    public boolean canRead() {
        return method.toLowerCase().contains("r");
    }

    public boolean canWrite() {
        return method.toLowerCase().contains("w");
    }
}
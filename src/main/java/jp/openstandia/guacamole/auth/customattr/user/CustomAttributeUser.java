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

package jp.openstandia.guacamole.auth.customattr.user;

import jp.openstandia.guacamole.auth.customattr.conf.ConfigurationService;
import jp.openstandia.guacamole.auth.customattr.conf.CustomAttributeDefinition;
import org.apache.guacamole.net.auth.DelegatingUser;
import org.apache.guacamole.net.auth.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CustomAttribute-specific User implementation which wraps a User from another extension.
 */
public class CustomAttributeUser extends DelegatingUser {

    /**
     * Requested method.
     */
    private final Method method;

    /**
     * Configuration service.
     */
    private final ConfigurationService confService;

    enum Method {
        READ,
        WRITE,
    }

    /**
     * Wraps the given User object.
     *
     * @param confService
     * @param user        The User object to wrap.
     */
    public CustomAttributeUser(ConfigurationService confService, Method method, User user) {
        super(user);
        this.confService = confService;
        this.method = method;
    }

    /**
     * Returns the User object wrapped by this CustomAttributeUser.
     *
     * @return The wrapped User object.
     */
    public User getUndecorated() {
        return getDelegateUser();
    }

    @Override
    public Map<String, String> getAttributes() {

        // Create independent, mutable copy of attributes
        Map<String, String> attributes =
                new HashMap<String, String>(super.getAttributes());

        if (this.method == Method.READ) {
            List<CustomAttributeDefinition> customAttributes = confService.getCustomAttributes();
            for (CustomAttributeDefinition def : customAttributes) {
                if (!def.canRead()) {
                    attributes.remove(def.name);
                }
            }
        } else if (this.method == Method.WRITE) {
            List<CustomAttributeDefinition> customAttributes = confService.getCustomAttributes();
            for (CustomAttributeDefinition def : customAttributes) {
                if (!def.canWrite()) {
                    attributes.remove(def.name);
                }
            }
        }

        return attributes;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {

        // Create independent, mutable copy of attributes
        attributes = new HashMap<String, String>(attributes);

        List<CustomAttributeDefinition> customAttributes = confService.getCustomAttributes();
        for (CustomAttributeDefinition def : customAttributes) {
            if (!def.canWrite()) {
                attributes.remove(def.name);
            }
        }

        super.setAttributes(attributes);
    }
}

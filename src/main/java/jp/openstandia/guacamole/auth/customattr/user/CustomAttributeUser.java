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

import org.apache.guacamole.net.auth.DelegatingUser;
import org.apache.guacamole.net.auth.User;

import java.util.HashMap;
import java.util.Map;

/**
 * CustomAttribute-specific User implementation which wraps a User from another extension.
 */
public class CustomAttributeUser extends DelegatingUser {

    /**
     * Wraps the given User object.
     *
     * @param user The User object to wrap.
     */
    public CustomAttributeUser(User user) {
        super(user);
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

        // TODO control exposing attributes?

        return attributes;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {

        // Create independent, mutable copy of attributes
        attributes = new HashMap<String, String>(attributes);

        // TODO control exposing attributes?

        super.setAttributes(attributes);
    }
}

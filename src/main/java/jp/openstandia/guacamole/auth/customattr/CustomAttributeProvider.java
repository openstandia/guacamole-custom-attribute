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

package jp.openstandia.guacamole.auth.customattr;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jp.openstandia.guacamole.auth.customattr.conf.ConfigurationService;
import jp.openstandia.guacamole.auth.customattr.user.CustomAttributeUserContext;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.AbstractAuthenticationProvider;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.UserContext;

/**
 * AuthenticationProvider implementation which defines custom attribute.
 */
public class CustomAttributeProvider extends AbstractAuthenticationProvider {

    /**
     * Injector which will manage the object graph of this authentication
     * provider.
     */
    private final Injector injector;

    /**
     * Creates a new CustomAttrProvider.
     *
     * @throws GuacamoleException If a required property is missing, or an error occurs while parsing
     *                            a property.
     */
    public CustomAttributeProvider() throws GuacamoleException {

        // Set up Guice injector.
        injector = Guice.createInjector(
                new CustomAttributeProviderModule(this)
        );

    }

    @Override
    public String getIdentifier() {
        return "customattr";
    }

    @Override
    public UserContext decorate(UserContext context,
                                AuthenticatedUser authenticatedUser, Credentials credentials)
            throws GuacamoleException {

        ConfigurationService confService =
                injector.getInstance(ConfigurationService.class);

        return new CustomAttributeUserContext(context, confService);

    }

    @Override
    public UserContext redecorate(UserContext decorated, UserContext context,
                                  AuthenticatedUser authenticatedUser, Credentials credentials)
            throws GuacamoleException {

        ConfigurationService confService =
                injector.getInstance(ConfigurationService.class);

        return new CustomAttributeUserContext(context, confService);
    }
}

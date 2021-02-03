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

import com.google.inject.Inject;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.properties.StringGuacamoleProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for retrieving configuration information regarding the CustomAttribute
 * extension.
 */
public class ConfigurationService {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    /**
     * The Guacamole server environment.
     */
    @Inject
    private Environment environment;

    /**
     * The list of the custom attribute definition.
     * The format is [form-name][method][attr1-name]:[attr1-type]:([attr1-option]),[form-name][method][attr2-name]:[attr2-type]:([attr2-option]),...
     */
    private static final StringGuacamoleProperty CUSTOM_ATTRIBUTES =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "custom-attributes";
                }

            };

    /**
     * Returns the list of the custom attribute definition.
     *
     * @return The list of the custom attribute definition.
     */
    public List<CustomAttributeDefinition> getCustomAttributes() {
        String s;
        try {
            s = environment.getProperty(CUSTOM_ATTRIBUTES);
        } catch (GuacamoleException e) {
            logger.error("Failed to parse custom-attributes", e);
            return Collections.emptyList();
        }

        logger.debug("custom-attributes: {}", s);

        if (s == null) {
            return Collections.emptyList();
        }

        String[] list = s.split(",");
        List<CustomAttributeDefinition> attrs = Arrays.stream(list)
                .map(x -> x.split(":"))
                .filter(x -> x.length == 4 || x.length == 5)
                .map(x -> x.length == 4 ?
                        new CustomAttributeDefinition(x[0], x[1], x[2], x[3], null)
                        : new CustomAttributeDefinition(x[0], x[1], x[2], x[3], options(x[4])))
                .collect(Collectors.toList());

        return attrs;
    }

    private Collection<String> options(String s) {
        return Arrays.asList(s.split(";"));
    }
}
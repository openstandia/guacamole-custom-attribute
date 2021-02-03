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
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.form.*;
import org.apache.guacamole.net.auth.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CustomAttribute-specific UserContext implementation which wraps the UserContext of
 * some other extension, providing (or hiding) additional data.
 */
public class CustomAttributeUserContext extends DelegatingUserContext {

    private final ConfigurationService confService;

    /**
     * Creates a new CustomAttributeUserContext which wraps the given UserContext,
     * providing additional CustomAttribute-specific data.
     *
     * @param userContext The UserContext to wrap.
     * @param confService Configuration service.
     */
    public CustomAttributeUserContext(UserContext userContext, ConfigurationService confService) {
        super(userContext);
        this.confService = confService;
    }

    @Override
    public Directory<User> getUserDirectory() throws GuacamoleException {
        return new DecoratingDirectory<User>(super.getUserDirectory()) {

            @Override
            protected User decorate(User object) {
                if (object instanceof CustomAttributeUser) {
                    return object;
                }
                return new CustomAttributeUser(confService, CustomAttributeUser.Method.READ, object);
            }

            @Override
            public void add(User object) throws GuacamoleException {
                super.add(new CustomAttributeUser(confService, CustomAttributeUser.Method.WRITE, object));
            }

            @Override
            protected User undecorate(User object) {
                assert (object instanceof CustomAttributeUser);
                return ((CustomAttributeUser) object).getUndecorated();
            }
        };
    }

    @Override
    public Collection<Form> getUserAttributes() {
        Map<String, Form> map = new LinkedHashMap<>();

        for (CustomAttributeDefinition def : confService.getCustomAttributes()) {
            Field f = toField(def);

            Form form = map.get(def.form);
            if (form == null) {
                ArrayList<Field> fields = new ArrayList<>();
                fields.add(f);
                map.put(def.form, new Form(def.form, fields));
            } else {
                form.getFields().add(f);
            }
        }

        ArrayList base = new ArrayList(super.getUserAttributes());
        base.addAll(map.entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList()));

        return base;
    }

    private Field toField(CustomAttributeDefinition x) {
        switch (x.type) {
            case Field.Type.BOOLEAN:
                return new BooleanField(x.name, x.options != null && x.options.size() > 0 ? x.options.iterator().next() : "true");
            case Field.Type.DATE:
                return new DateField(x.name);
            case Field.Type.EMAIL:
                return new EmailField(x.name);
            case Field.Type.ENUM:
                // TODO implement options
                return new EnumField(x.name, x.options);
            case Field.Type.LANGUAGE:
                return new LanguageField(x.name);
            case Field.Type.MULTILINE:
                return new MultilineField(x.name);
            case Field.Type.PASSWORD:
                return new PasswordField(x.name);
            case Field.Type.TERMINAL_COLOR_SCHEME:
                return new TerminalColorSchemeField(x.name);
            case Field.Type.TIME:
                return new TimeField(x.name);
            case Field.Type.TIMEZONE:
                return new TimeZoneField(x.name);
            case Field.Type.USERNAME:
                return new UsernameField(x.name);
            case Field.Type.TEXT:
            default:
                return new TextField(x.name, x.options);
        }
    }
}

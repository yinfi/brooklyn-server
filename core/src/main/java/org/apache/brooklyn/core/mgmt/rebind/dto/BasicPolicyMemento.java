/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.brooklyn.core.mgmt.rebind.dto;

import java.io.Serializable;
import java.util.Map;

import org.apache.brooklyn.api.mgmt.rebind.mementos.PolicyMemento;
import org.apache.brooklyn.api.objs.HighlightTuple;
import org.apache.brooklyn.core.config.Sanitizer;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

/**
 * The persisted state of a policy.
 * 
 * @author aled
 */
public class BasicPolicyMemento extends AbstractMemento implements PolicyMemento, Serializable {

    private static final long serialVersionUID = -4025337943126838761L;
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractMemento.Builder<Builder> {
        protected Map<String,Object> config = Maps.newLinkedHashMap();
        protected Map<String,HighlightTuple> highlights = Maps.newLinkedHashMap();
        
        public Builder from(PolicyMemento other) {
            super.from(other);
            config.putAll(other.getConfig());
            highlights.putAll(other.getHighlights());
            return this;
        }
        public Builder config(Map<String,?> vals) {
            config.putAll(vals); return this;
        }
        public Builder highlights(Map<String,HighlightTuple> vals) {
            highlights.putAll(vals); return this;
        }
        public PolicyMemento build() {
            return new BasicPolicyMemento(this);
        }
    }
    
    private Map<String,Object> config;
    private Map<String, Object> fields;
    private Map<String, HighlightTuple> highlights;

    @SuppressWarnings("unused") // For deserialisation
    private BasicPolicyMemento() {}

    // Trusts the builder to not mess around with mutability after calling build()
    protected BasicPolicyMemento(Builder builder) {
        super(builder);
        config = toPersistedMap(builder.config);
        highlights = toPersistedMap(builder.highlights);
    }
    
    @Deprecated
    @Override
    protected void setCustomFields(Map<String, Object> fields) {
        this.fields = toPersistedMap(fields);
    }
    
    @Deprecated
    @Override
    public Map<String, Object> getCustomFields() {
        return fromPersistedMap(fields);
    }

    @Override
    public Map<String, Object> getConfig() {
        return fromPersistedMap(config);
    }

    @Override
    public Map<String, HighlightTuple> getHighlights() {
        return highlights;
    }

    @Override
    protected MoreObjects.ToStringHelper newVerboseStringHelper() {
        return super.newVerboseStringHelper().add("config", Sanitizer.sanitize(getConfig())).add("highlights", highlights);
    }
}

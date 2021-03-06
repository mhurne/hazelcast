/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.map.client;

import com.hazelcast.client.AllPartitionsClientRequest;
import com.hazelcast.client.SecureRequest;
import com.hazelcast.map.operation.MapFlushOperationFactory;
import com.hazelcast.map.MapPortableHook;
import com.hazelcast.map.MapService;
import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.security.permission.ActionConstants;
import com.hazelcast.security.permission.MapPermission;
import com.hazelcast.spi.OperationFactory;

import java.io.IOException;
import java.security.Permission;
import java.util.Map;

public class MapFlushRequest extends AllPartitionsClientRequest implements Portable, SecureRequest {

    protected String name;

    public MapFlushRequest() {
    }

    public MapFlushRequest(String name) {
        this.name = name;
    }

    public int getFactoryId() {
        return MapPortableHook.F_ID;
    }

    public int getClassId() {
        return MapPortableHook.FLUSH;
    }

    @Override
    protected OperationFactory createOperationFactory() {
        return new MapFlushOperationFactory(name);
    }

    @Override
    protected Object reduce(Map<Integer, Object> map) {
        return true;
    }

    public String getServiceName() {
        return MapService.SERVICE_NAME;
    }

    public void write(PortableWriter writer) throws IOException {
        writer.writeUTF("n", name);
    }

    public void read(PortableReader reader) throws IOException {
        name = reader.readUTF("n");
    }

    public Permission getRequiredPermission() {
        return new MapPermission(name, ActionConstants.ACTION_PUT);
    }

    @Override
    public String getMethodName() {
        return "flush";
    }
}

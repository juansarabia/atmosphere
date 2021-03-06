/*
 * Copyright 2013 Jeanfrancois Arcand
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.atmosphere.container.version;

import org.apache.catalina.websocket.WsOutbound;
import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.AtmosphereResponse;
import org.atmosphere.websocket.WebSocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Tomcat WebSocket Support
 *
 * @author Jeanfrancois Arcand
 */
public class TomcatWebSocket extends WebSocket {

    private final WsOutbound outbound;

    public TomcatWebSocket(WsOutbound outbound, AtmosphereConfig config) {
        super(config);
        this.outbound = outbound;
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public WebSocket write(String s) throws IOException {
        logger.trace("WebSocket.write() for {}", resource() != null ? resource().uuid() : "");
        outbound.writeTextMessage(CharBuffer.wrap(s));
        return this;
    }

    @Override
    public WebSocket write(byte[] b, int offset, int length) throws IOException {
        logger.trace("WebSocket.write() for {}", resource() != null ? resource().uuid() : "");
        outbound.writeBinaryMessage(ByteBuffer.wrap(b, offset, length));
        return this;
    }

    @Override
    public void close() {
        try {
            logger.trace("WebSocket.close() for AtmosphereResource {}", resource() != null ? resource().uuid() : "null");
            outbound.close(1005, ByteBuffer.wrap(new byte[0]));
        } catch (IOException e) {
            logger.trace("", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close(AtmosphereResponse r) throws IOException {
        logger.trace("WebSocket.close()");
        outbound.close(1005, ByteBuffer.wrap(new byte[0]));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSocket flush(AtmosphereResponse r) throws IOException {
        outbound.flush();
        return this;
    }

    @Override
    public String toString() {
        return outbound.toString();
    }
}

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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.sidecar;

import com.google.inject.Guice;
import com.google.inject.Inject;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.apache.cassandra.sidecar.routes.HealthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CassandraSidecarDaemon
{
    private static final Logger logger = LoggerFactory.getLogger(CassandraSidecarDaemon.class);
    private final HealthService healthService;
    private final HttpServer server;
    private final Router router;
    private final Configuration config;

    @Inject
    public CassandraSidecarDaemon(HealthService healthService, HttpServer server, Router router,
                                  Configuration config)
    {
        this.healthService = healthService;
        this.server = server;
        this.router = router;
        this.config = config;
    }

    public void start()
    {
        banner();
        logger.info("Starting Cassandra Sidecar on port {}", config.getPort());

        router.route().path("/__health").handler(healthService::handleHealth);

        healthService.start();
        server.listen();
    }

    public void stop()
    {
        logger.info("Stopping Cassandra Sidecar");
        healthService.stop();
        server.close();
    }

    private void banner()
    {
        System.out.println(" _____                               _              _____ _     _                     \n" +
                "/  __ \\                             | |            /  ___(_)   | |                    \n" +
                "| /  \\/ __ _ ___ ___  __ _ _ __   __| |_ __ __ _   \\ `--. _  __| | ___  ___ __ _ _ __ \n" +
                "| |    / _` / __/ __|/ _` | '_ \\ / _` | '__/ _` |   `--. \\ |/ _` |/ _ \\/ __/ _` | '__|\n" +
                "| \\__/\\ (_| \\__ \\__ \\ (_| | | | | (_| | | | (_| |  /\\__/ / | (_| |  __/ (_| (_| | |   \n" +
                " \\____/\\__,_|___/___/\\__,_|_| |_|\\__,_|_|  \\__,_|  \\____/|_|\\__,_|\\___|\\___\\__,_|_|   \n" +
                "                                                                                      \n" +
                "                                                                                      ");
    }

    public static void main(String[] args)
    {
        CassandraSidecarDaemon app = Guice.createInjector(new MainModule())
                .getInstance(CassandraSidecarDaemon.class);

        app.start();
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
    }
}


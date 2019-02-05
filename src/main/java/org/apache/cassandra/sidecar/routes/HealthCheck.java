package org.apache.cassandra.sidecar.routes;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

public class HealthCheck implements Supplier<Boolean>
{
    private Logger logger = LoggerFactory.getLogger(HealthService.class);
    private final String CASSANDRA_HOST;
    private final int CASSANDRA_PORT;

    public HealthCheck(String cassandraHost, int cassandraPort)
    {
        this.CASSANDRA_HOST = cassandraHost;
        this.CASSANDRA_PORT = cassandraPort;
    }

    private boolean check()
    {
        Cluster cluster = null;
        try
        {
            cluster = Cluster.builder()
                    .addContactPointsWithPorts(InetSocketAddress.createUnresolved(CASSANDRA_HOST, CASSANDRA_PORT))
                    .build();
            Session session = cluster.connect();
            ResultSet rs = session.execute("SELECT release_version FROM system.local");
            return (rs.one() != null);
        }
        catch(Exception e)
        {
            logger.warn("Failed to reach Cassandra");
            return false;
        }
        finally
        {
            if (cluster != null)
                cluster.close();
        }
    }

    @Override
    public Boolean get()
    {
        return check();
    }
}

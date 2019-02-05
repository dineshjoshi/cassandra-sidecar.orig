package org.apache.cassandra.sidecar.mocks;

import java.util.function.Supplier;

public class MockHealthCheck implements Supplier<Boolean>
{
    private volatile boolean status;

    @Override
    public Boolean get()
    {
        return status;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }
}

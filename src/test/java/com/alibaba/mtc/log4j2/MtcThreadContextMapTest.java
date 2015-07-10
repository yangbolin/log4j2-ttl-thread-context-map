package com.alibaba.mtc.log4j2;

import com.alibaba.mtc.MtContextRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class MtcThreadContextMapTest {
    static Logger logger = LogManager.getLogger(MtcThreadContextMapTest.class);

    @Test
    public void testName() throws Exception {
        // Log in Main Thread
        logger.info("Log in main!");

        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Run task in thread pool
        executorService.submit(createTask()).get();

        // Init Log Context, set MTC
        // More KV if needed
        final String TRACE_ID = "trace-id";
        final String TRACE_ID_VALUE = "XXX-YYY-ZZZ";
        ThreadContext.put(TRACE_ID, TRACE_ID_VALUE);

        // Log in Main Thread
        logger.info("Log in main!");
        executorService.submit(createTask()).get();

        executorService.shutdown();
    }

    Runnable createTask() {
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                // Log in thread pool
                logger.info("Log in Runnable!");
            }
        };
        return MtContextRunnable.get(task);
    }
}
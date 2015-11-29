package com.jhipster.application.context;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.StrictAssertions.assertThat;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public class ThreadLocalContextTest {

    private ContextHolder contextHolder = new BasicContextHolder();
    private Set<Context> contextSet = new HashSet<>();
    private List<Thread> threadToTest = new ArrayList<>();

    private final int threadCount = 100;

    private boolean result = true;
    private int finishedThreadCount;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(contextHolder, "strategy", new ThreadLocalContextStrategy());
        contextSet.add(contextHolder.getCurrentContext());
        for(int i = 0; i < threadCount; i++) {
            threadToTest.add(new Thread(new RunnableImpl()));
        }
    }

    @Test
    public void doThreadTest() {
        threadToTest.forEach(thread -> {
            thread.start();
        });
        while(finishedThreadCount < threadCount - 1) {
            try {
                Thread.sleep((long)(100));
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertThat(result).isTrue();
    }

    private class RunnableImpl implements Runnable {

        @Override
        public void run() {
            try {
                for(int i = 0; i < 100; i++) {
                    int size = contextSet.size();
                    Context newContext = contextHolder.getCurrentContext();
                    if(contextSet.contains(newContext)) {
                        if(i == 0) {
                            assertThat(false).isFalse();
                        }
                    } else {
                        contextSet.add(contextHolder.getCurrentContext());
                        if(i != 0) {
                            assertThat(false).isFalse();
                        }
                    }
                    try {
                        Thread.sleep((long)(Math.random() * 10));
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch(AssertionError e) {
                result = false;
                e.printStackTrace();
            } finally {
                finishedThreadCount++;
            }
        }

    }

}

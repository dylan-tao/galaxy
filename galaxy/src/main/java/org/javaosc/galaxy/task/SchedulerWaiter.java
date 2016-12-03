package org.javaosc.galaxy.task;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
class SchedulerWaiter implements Runnable {
	
    protected Scheduler mgr;
    protected Thread thread;
    private long sleepUntil = -1;
    private boolean debug = false;
    private boolean shutdown = false;
    
    private void debug(String s) {
        if (debug)
            System.out.println("[" + Thread.currentThread().getName() + "] AlarmWaiter: " + s);
    }
    
    public SchedulerWaiter(Scheduler mgr, boolean isDaemon, String waiterName) {
        this.mgr = mgr;
        
        // start the thread
        thread = new Thread(this, waiterName);
        thread.setPriority( 1 );
        thread.setDaemon(isDaemon);
        thread.start();
    }
    
    public synchronized void update(long _sleep_until) {
        this.sleepUntil = _sleep_until;
        debug("Update for " + _sleep_until); // timeToSleep);
        debug("calling notify() to update thread wait timeout");
        notify();
    }
    
    public synchronized void restart(long _sleep_until) {
        this.sleepUntil = _sleep_until;
        notify();
    }
    
    public synchronized void stop() {
        shutdown = true;
        notify();
    }  
    
    public synchronized void run() {
        debug("running");
        while(!shutdown) { 
            try {
                // check if there's an scheduled
                if (sleepUntil <= 0) {
                    // no alarm. Wait for a new alarm to come along.
                    wait();
                } // if
                else {
                    // Found schedule, set timeout based on schedule time
                    long timeout = sleepUntil - System.currentTimeMillis();
                    if (timeout > 0) {
                        wait(timeout);
                    }
                }
                
                // now that we've awakened again, check if an alarm is due (within
                // 1 second or already past)
                if (sleepUntil >= 0 && (sleepUntil - System.currentTimeMillis() < 1000)) {
                    // yes, an alarm is ready (or already past). Notify the manager to ring it.
                    sleepUntil = -1;
                    debug("notifying manager to ring next alarm");
                    mgr.ringNextAlarm();
                }
                
            }
            catch(InterruptedException e) {
                debug("interrupted");
            }
        }
        debug("stopping");
    }
    
}



package org.javaosc.ratel.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class Scheduler {
    
    protected SchedulerWaiter waiter;
    protected SortedSet<SchedulerEntry> queue;
    private boolean debug = false;
    
    private void debug(String s) {
        if (debug)
            System.out.println("[" + Thread.currentThread().getName() + "] AlarmManager: " + s);
    }
    
    public Scheduler(boolean isDaemon, String threadName) {
        queue = new TreeSet<SchedulerEntry>();
        waiter = new SchedulerWaiter(this, isDaemon, threadName);
    }
    
    public Scheduler() {
        this(false, "AlarmManager");
    }
    
    public synchronized SchedulerEntry add(String _name, Date _date,
            SchedulerListener _listener) throws PastDateException {
        SchedulerEntry entry = new SchedulerEntry(_name, _date, _listener);
        add(entry);
        return entry;
    }
    
    public synchronized SchedulerEntry add(String _name, int _delay, boolean _isRepeating,
            SchedulerListener _listener) throws PastDateException {
        SchedulerEntry entry = new SchedulerEntry(_name, _delay, _isRepeating, _listener);
        add(entry);
        return entry;
    }
    
    public synchronized SchedulerEntry add(String _name, int _minute, int _hour,
            int _dayOfMonth, int _month,
            int _dayOfWeek,
            int _year,
            SchedulerListener _listener)
    throws PastDateException {
        
        SchedulerEntry entry = new SchedulerEntry(_name, _minute, _hour,
                _dayOfMonth, _month,
                _dayOfWeek,
                _year,
                _listener);
        add(entry);
        return entry;
    }
    
    public synchronized SchedulerEntry add(String _name, int[] _minutes, int[] _hours,
            int[] _daysOfMonth, int[] _months,
            int[] _daysOfWeek,
            int _year,
            SchedulerListener _listener)
    throws PastDateException {
        
        SchedulerEntry entry = new SchedulerEntry(_name, _minutes, _hours,
                _daysOfMonth, _months,
                _daysOfWeek,
                _year,
                _listener);
        add(entry);
        return entry;
    }
    
    public synchronized void add(SchedulerEntry _entry) throws PastDateException {
        debug("Add a new alarm entry : " + _entry);
        
        queue.add(_entry);
        if (queue.first().equals(_entry)) {
            debug("This new alarm is the top one, update the waiter thread");
            waiter.update(_entry.alarmTime);
        }
    }
    
    public synchronized boolean removeAlarm(SchedulerEntry _entry) {
        
        boolean found = false;
        
        if( ! queue.isEmpty() ) {
            SchedulerEntry was_first = (SchedulerEntry)queue.first();
            found = queue.remove(_entry);
            
            // update the queue if it's not now empty, and the first alarm has changed
            if ( !queue.isEmpty() && _entry.equals(was_first) )
            {
                waiter.update( ((SchedulerEntry) queue.first()).alarmTime );
            }
        }
        
        return found;
    } // removeAlarm()
    
    public synchronized void removeAllAlarms() {
        queue.clear();
    }
    
    public synchronized void removeAllAlarmsAndStop() {
        waiter.stop();
        waiter = null;
        queue.clear();
    }
    
    public boolean isStopped() {
        return (waiter == null);
    }
    
    public synchronized boolean containsAlarm(SchedulerEntry _alarmEntry) {
        return queue.contains(_alarmEntry);
    }
    
    public synchronized List<SchedulerEntry> getAllAlarms() {
        List<SchedulerEntry> result = new ArrayList<SchedulerEntry>();
        
        Iterator<SchedulerEntry> iterator = queue.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        
        return result;
    }
    
    protected synchronized void ringNextAlarm() {
        debug("ringing next alarm");
        
        // if the queue is empty, there's nothing to do
        if (queue.isEmpty()) {
            return;
        }
        
        // Removes this alarm and notifies the listener
        SchedulerEntry entry = (SchedulerEntry) queue.first();
        queue.remove(entry);
        
        // NOTE: if the entry is still running when its next alarm time comes up,
        // that execution of the entry will be skipped.
        if( entry.isRingInNewThread() ) {
            new Thread( new RunnableRinger(entry) ).start();
        }
        else {
            // ring in same thread, sequentially.. can delay other alarms
            try {
                entry.ringAlarm();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        // Reactivates the alarm if it is repetitive
        if (entry.isRepeating) {
            entry.updateAlarmTime();
            queue.add(entry);
        }
        
        // Notifies the AlarmWaiter thread for the next alarm
        if (queue.isEmpty()) {
            debug("no more alarms to handle; queue is empty");
        }
        else {
            long alarmTime = ((SchedulerEntry)queue.first()).alarmTime;
            if (alarmTime - System.currentTimeMillis() < 1000) {
                debug("next alarm is within 1 sec or already past - ring it without waiting");
                ringNextAlarm();
            }
            else {
                debug("updating the waiter for next alarm: " + queue.first());
                waiter.restart(alarmTime);
            }
        }
    } // notifyListeners()
    
    public void finalize() {
        if (waiter != null)
            waiter.stop();
    }
    
    private class RunnableRinger implements Runnable {
        SchedulerEntry entry = null;
        
        RunnableRinger(SchedulerEntry _entry) {
            entry = _entry;
        }
        
        public void run() {
            try {
                entry.ringAlarm();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

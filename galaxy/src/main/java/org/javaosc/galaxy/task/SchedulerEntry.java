package org.javaosc.galaxy.task;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class SchedulerEntry implements Comparable<Object>, java.io.Serializable {
	
	private static final long serialVersionUID = -5936264684809854801L;
	
	private int[] minutes = {-1};
    private static int minMinute = 0;
    private static int maxMinute = 59;
    
    private int[] hours = {-1};
    private static int minHour = 0;
    private static int maxHour = 23;
    
    private int[] daysOfMonth = {-1};
    private static int minDayOfMonth = 1;
    // maxDayOfMonth varies by month
    
    private int[] months = {-1};
    private static int minMonth = 0;
    private static int maxMonth = 11;
    
    private int[] daysOfWeek = {-1};
    private static int minDayOfWeek = 1;
    private static int maxDayOfWeek = 7;
    
    private int year = -1; // no support for a list of years -- must be * or specified
    
    private String name;
    private static int UNIQUE = 0; // used to generate names if they are null
    
    private boolean ringInNewThread = false;  // default: false
    
    private boolean isRelative;
    public boolean isRepeating;
    public long alarmTime;
    private long lastUpdateTime;
    private transient SchedulerListener listener;
    private transient boolean debug = false;
    
    private void debug(String s) {
        if (debug)
            System.out.println("[" + Thread.currentThread().getName() + "] AlarmEntry "+name+": " + s);
    }
    
    public SchedulerEntry(String _name, Date _date, SchedulerListener _listener)
    throws PastDateException {
        
        setName(_name);
        listener = _listener;
        Calendar alarm = Calendar.getInstance();
        alarm.setTime(_date);
        minutes = new int[] { alarm.get(Calendar.MINUTE) };
        hours = new int[] { alarm.get(Calendar.HOUR_OF_DAY) };
        daysOfMonth = new int[] { alarm.get(Calendar.DAY_OF_MONTH) };
        months = new int[] { alarm.get(Calendar.MONTH) };
        year = alarm.get( Calendar.YEAR );
        
        isRepeating = false;
        isRelative = false;
        alarmTime = _date.getTime();
        checkAlarmTime();
    }
    
    public SchedulerEntry(String _name, int _delayMinutes, boolean _isRepeating, SchedulerListener _listener)
    throws PastDateException {
        if (_delayMinutes < 1) {
            throw new PastDateException();
        }
        
        setName(_name);
        minutes = new int[] { _delayMinutes };
        listener = _listener;
        isRepeating = _isRepeating;
        
        isRelative = true;
        updateAlarmTime();
    }
    
    public SchedulerEntry(String _name, int _minute, int _hour, int _dayOfMonth, int _month,
            int _dayOfWeek, int _year, SchedulerListener _listener)
    throws PastDateException {
        this(_name, new int[]{_minute}, new int[]{_hour}, new int[]{_dayOfMonth}, new int[]{_month},
                new int[]{_dayOfWeek}, _year, _listener);
    }
    
    public SchedulerEntry(String _name, int[] _minutes, int[] _hours, int[] _daysOfMonth, int[] _months,
            int[] _daysOfWeek, int _year, SchedulerListener _listener)
    throws PastDateException {
        
        setName(_name);
        minutes = _minutes;
        hours = _hours;
        daysOfMonth = _daysOfMonth;
        months = _months;
        daysOfWeek = _daysOfWeek;
        year = _year;
        listener = _listener;
        isRepeating = (_year == -1);
        isRelative = false;
        
        updateAlarmTime();
        checkAlarmTime();
    }
    
    private void setName(String _name)
    {
        name = _name;
        if( name == null )
            name = "alarm" + (UNIQUE++);
    }
    
    public String getName() {
        return name;
    }
    
    public void setRingInNewThead()
    {
        ringInNewThread = true;
    }
    public boolean isRingInNewThread()
    {
        return ringInNewThread;
    }
    
    void checkAlarmTime() throws PastDateException {
        long delay = alarmTime - System.currentTimeMillis();
        
        if (delay <= 1000) {
            throw new PastDateException();
        }
    }
    
    public void ringAlarm()
    {
        listener.execute(this);
    }
    
    public void updateAlarmTime() {
        Calendar now = Calendar.getInstance();
        
        if (isRelative) {
            // relative only uses minutes field, with only a single value (NOT -1)
            alarmTime = now.getTime().getTime() + (minutes[0] * 60000);
            return;
        }
        
        Calendar alarm = (Calendar)now.clone();
        alarm.set( Calendar.SECOND, 0 );
        
        debug("now: " + now.getTime());
        
        // increase alarm minutes
        int current = alarm.get( Calendar.MINUTE );
        int offset = 0;
        // force increment at least to next minute
        offset = getOffsetToNext( current, minMinute, maxMinute, minutes );
        alarm.add( Calendar.MINUTE, offset );
        debug( "after min: " + alarm.getTime() );
        
        // update alarm hours if necessary
        current = alarm.get( Calendar.HOUR_OF_DAY );  // (as updated by minute shift)
        offset = getOffsetToNextOrEqual( current, minHour, maxHour, hours );
        alarm.add( Calendar.HOUR_OF_DAY, offset );
        debug( "after hour (current:"+current+"): " + alarm.getTime() );
        
        if( daysOfMonth[0] != -1 && daysOfWeek[0] != -1 )
        {
            // BOTH are restricted - take earlier match
            Calendar dayOfWeekAlarm = (Calendar)alarm.clone();
            updateDayOfWeekAndMonth( dayOfWeekAlarm );
            
            Calendar dayOfMonthAlarm = (Calendar)alarm.clone();
            updateDayOfMonthAndMonth( dayOfMonthAlarm );
            
            // take the earlier one
            if( dayOfMonthAlarm.getTime().getTime() < dayOfWeekAlarm.getTime().getTime() )
            {
                alarm = dayOfMonthAlarm;
                debug( "after dayOfMonth CLOSER: " + alarm.getTime() );
            }
            else
            {
                alarm = dayOfWeekAlarm;
                debug( "after dayOfWeek CLOSER: " + alarm.getTime() );
            }
        }
        else if( daysOfWeek[0] != -1 ) // only dayOfWeek is restricted
        {
            // update dayInWeek and month if necessary
            updateDayOfWeekAndMonth( alarm );
            debug( "after dayOfWeek: " + alarm.getTime() );
        }
        else if( daysOfMonth[0] != -1 ) // only dayOfMonth is restricted
        {
            // update dayInMonth and month if necessary
            updateDayOfMonthAndMonth( alarm );
            debug( "after dayOfMonth: " + alarm.getTime() );
        }
        // else if neither is restricted (both[0] == -1), we don't need to do anything.
        
        
        debug("alarm: " + alarm.getTime());
        
        alarmTime = alarm.getTime().getTime();
        lastUpdateTime = System.currentTimeMillis();
    }
    
    void updateDayOfMonthAndMonth( Calendar alarm )
    {
        int currentMonth = alarm.get( Calendar.MONTH );
        int currentDayOfMonth = alarm.get( Calendar.DAY_OF_MONTH );
        int offset = 0;
        
        // loop until we have a valid day AND month (if current is invalid)
        while( !isIn(currentMonth, months) || !isIn(currentDayOfMonth, daysOfMonth) )
        {
            // if current month is invalid, advance to 1st day of next valid month
            if( !isIn(currentMonth, months) )
            {
                offset = getOffsetToNextOrEqual( currentMonth, minMonth, maxMonth, months );
                alarm.add( Calendar.MONTH, offset );
                alarm.set( Calendar.DAY_OF_MONTH, 1 );
                currentDayOfMonth = 1;
            }
            
            // advance to the next valid day of month, if necessary
            if( !isIn(currentDayOfMonth, daysOfMonth) )
            {
                int maxDayOfMonth = alarm.getActualMaximum( Calendar.DAY_OF_MONTH );
                offset = getOffsetToNextOrEqual( currentDayOfMonth, minDayOfMonth, maxDayOfMonth, daysOfMonth );
                alarm.add( Calendar.DAY_OF_MONTH, offset );
            }
            
            currentMonth = alarm.get( Calendar.MONTH );
            currentDayOfMonth = alarm.get( Calendar.DAY_OF_MONTH );
        }
    }
    
    
    void updateDayOfWeekAndMonth( Calendar alarm )
    {
        int currentMonth = alarm.get( Calendar.MONTH );
        int currentDayOfWeek = alarm.get( Calendar.DAY_OF_WEEK );
        int offset = 0;
        
        // loop until we have a valid day AND month (if current is invalid)
        while( !isIn(currentMonth, months) || !isIn(currentDayOfWeek, daysOfWeek) )
        {
            // if current month is invalid, advance to 1st day of next valid month
            if( !isIn(currentMonth, months) )
            {
                offset = getOffsetToNextOrEqual( currentMonth, minMonth, maxMonth, months );
                alarm.add( Calendar.MONTH, offset );
                alarm.set( Calendar.DAY_OF_MONTH, 1 );
                currentDayOfWeek = alarm.get( Calendar.DAY_OF_WEEK );
            }
            
            // advance to the next valid day of week, if necessary
            if( !isIn(currentDayOfWeek, daysOfWeek) )
            {
                offset = getOffsetToNextOrEqual( currentDayOfWeek, minDayOfWeek, maxDayOfWeek, daysOfWeek );
                alarm.add( Calendar.DAY_OF_YEAR, offset );
            }
            
            currentDayOfWeek = alarm.get( Calendar.DAY_OF_WEEK );
            currentMonth = alarm.get( Calendar.MONTH );
        }
    }
    
    static int getOffsetToNext( int current, int min, int max, int[] values )
    {
        int offset = 0;
        
        // find the distance to the closest valid value > current (wrapping if neccessary)
        
        // {-1} means *  -- offset is 1 because current++ is valid value
        if (values[0] == -1 )
        {
            offset = 1;
        }
        else
        {
            // need to wrap
            if( current >= last(values) )
            {
                int next = values[0];
                offset = (max-current+1) + (next-min);
            }
            else // current < max(values) -- find next valid value after current
            {
                findvalue:
                for( int i=0; i<values.length; i++ )
                {
                    if( current < values[i] )
                    {
                        offset = values[i] - current;
                        break findvalue;
                    }
                }
            } // end current < max(values)
        }
        
        return offset;
    }
    
    static int getOffsetToNextOrEqual( int current, int min, int max, int[] values )
    {
        int offset = 0;
        int[] safeValues = null;
        
        // find the distance to the closest valid value >= current (wrapping if necessary)
        
        // {-1} means *  -- offset is 0 if current is valid value
        if (values[0] == -1 || isIn(current, values) )
        {
            offset = 0;
        }
        else
        {
            safeValues = discardValuesOverMax( values, max );
            
            // need to wrap
            if( current > last(safeValues) )
            {
                int next = safeValues[0];
                offset = (max-current+1) + (next-min);
            }
            else // current <= max(values) -- find next valid value
            {
                findvalue:
                for( int i=0; i<values.length; i++ )
                {
                    if( current < safeValues[i] )
                    {
                        offset = safeValues[i] - current;
                        break findvalue;
                    }
                }
            } // end current <= max(values)
        }
        
        return offset;
    }
    
    static boolean isIn( int find, int[] values )
    {
        if( values[0] == -1 )
        {
            return true;
        }
        else
        {
            for( int i=0; i<values.length; i++ )
            {
                if( find == values[i] )
                    return true;
            }
            return false;
        }
    }
    
    static int last( int[] intArray )
    {
        return intArray[ intArray.length - 1 ];
    }
    
    static int[] discardValuesOverMax( int[] values, int max )
    {
        int[] safeValues = null;
        for( int i=0; i<values.length; i++ )
        {
            if( values[i] > max )
            {
                safeValues = new int[i];
                System.arraycopy( values, 0, safeValues, 0, i );
                return safeValues;
            }
        }
        return values;
    }

    
    private static String arrToString( int[] intArray )
    {
        if( intArray == null )
            return "null";
        if( intArray.length == 0 )
            return "{}";
        
        String s = "{";
        for( int i=0; i<intArray.length-1; i++ )
        {
            s += intArray[i] + ", ";
        }
        s += intArray[intArray.length-1] + "}";
        
        return s;
    }
    
    public int compareTo(Object obj) {
        SchedulerEntry other = (SchedulerEntry)obj;
        if (alarmTime < other.alarmTime)
            return -1;
        else if (alarmTime > other.alarmTime)
            return 1;
        else // alarmTime == other.alarmTime
        {
            if( lastUpdateTime < other.lastUpdateTime )
                return -1;
            else if( lastUpdateTime > other.lastUpdateTime)
                return 1;
            else
                return 0;    
        }
    }
    
    public boolean equals(Object obj) {
        
    	if(obj == null || !(obj instanceof SchedulerEntry)){
    		return false;
    	}
        
        SchedulerEntry entry = (SchedulerEntry)obj;
        return (name.equals(entry.name)
                && alarmTime == entry.alarmTime
                && isRelative == entry.isRelative
                && isRepeating == entry.isRepeating
                && Arrays.equals(minutes, entry.minutes)
                && Arrays.equals(hours, entry.hours)
                && Arrays.equals(daysOfMonth, entry.daysOfMonth)
                && Arrays.equals(months, entry.months)
                && Arrays.equals(daysOfWeek, entry.daysOfWeek) );
    }
    
    public String toString() {
        if (year != -1) {
            return "Alarm ("+name+") at " + new Date(alarmTime);
        }
        StringBuffer sb = new StringBuffer("Alarm ("+name+") params");
        sb.append(" minute="); sb.append( arrToString(minutes) );
        sb.append(" hour="); sb.append( arrToString(hours) );
        sb.append(" dayOfMonth="); sb.append( arrToString(daysOfMonth) );
        sb.append(" month="); sb.append( arrToString(months) );
        sb.append(" dayOfWeek="); sb.append( arrToString(daysOfWeek) );
        sb.append(" (next alarm date=" + new Date(alarmTime) + ")");
        return sb.toString();
    }
    
    public static void main( String[] args ) {
        
        System.out.println( "GETTING OFFSETS" );
        
        System.out.println( "getOffsetToNext(3, 0, 11, new int[]{3,5,7,9}) = " +
                getOffsetToNext(3, 0, 11, new int[]{3,5,7,9}) );
        System.out.println( "getOffsetToNextOrEqual(3, 0, 11, new int[]{3,5,7,9}) = " +
                getOffsetToNextOrEqual(3, 0, 11, new int[]{3,5,7,9}) );
        
        System.out.println();
        System.out.println( "getOffsetToNext(9, 0, 11, new int[]{3,5,7,9}) = " +
                getOffsetToNext(9, 0, 11, new int[]{3,5,7,9}) );
        System.out.println( "getOffsetToNextOrEqual(9, 0, 11, new int[]{3,5,7,9}) = " +
                getOffsetToNextOrEqual(9, 0, 11, new int[]{3,5,7,9}) );
        
        System.out.println();
        System.out.println( "getOffsetToNext(0, 0, 11, new int[]{0}) = " +
                getOffsetToNext(0, 0, 11, new int[]{0}) );
        System.out.println( "getOffsetToNextOrEqual(0, 0, 11, new int[]{0}) = " +
                getOffsetToNextOrEqual(0, 0, 11, new int[]{0}) );
        
        System.out.println();
        System.out.println( "getOffsetToNext(5, 0, 11, new int[]{5}) = " +
                getOffsetToNext(5, 0, 11, new int[]{5}) );
        System.out.println( "getOffsetToNextOrEqual(5, 0, 11, new int[]{5}) = " +
                getOffsetToNextOrEqual(5, 0, 11, new int[]{5}) );
        
        System.out.println();
        System.out.println( "getOffsetToNext(0, 0, 11, new int[]{-1}) = " +
                getOffsetToNext(0, 0, 11, new int[]{-1}) );
        System.out.println( "getOffsetToNextOrEqual(0, 0, 11, new int[]{-1}) = " +
                getOffsetToNextOrEqual(0, 0, 11, new int[]{-1}) );
        
        System.out.println();
        
        System.out.println();
        System.out.println( "discardValuesOverMax(new int[]{0,1,2,3,4,5,6}, 4)) = " + 
                arrToString(discardValuesOverMax(new int[]{0,1,2,3,4,5,6}, 4)) );
        System.out.println( "discardValuesOverMax(new int[]{0,1,2,3,4,5,6}, 6)) = " + 
                arrToString(discardValuesOverMax(new int[]{0,1,2,3,4,5,6}, 6)) );
        System.out.println( "discardValuesOverMax(new int[]{0,1,2,3,4,5,6}, 0)) = " + 
                arrToString(discardValuesOverMax(new int[]{0,1,2,3,4,5,6}, 0)) );
        System.out.println( "discardValuesOverMax(new int[]{0,1,2,3,4,5,6}, 7)) = " + 
                arrToString(discardValuesOverMax(new int[]{0,1,2,3,4,5,6}, 7)) );
    }
}










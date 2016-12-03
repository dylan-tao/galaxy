package org.javaosc.galaxy.task;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public interface SchedulerListener {
  
  public abstract void execute(SchedulerEntry entry);
  
}


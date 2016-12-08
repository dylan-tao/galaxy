package org.javaosc.galaxy.assist;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09 Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 8566978699237041746L;
	
	private long code;

    public ServiceException(long code, String message) {
        this(code, message, null);
    }

    public ServiceException(long code, Throwable cause) {
        this(code, null, cause);
    }

    public ServiceException(long code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public long getErrorCode() {
        return this.code;
    }
}

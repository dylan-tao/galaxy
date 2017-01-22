// Copyright (C) 2007 by Jason Hunter <jhunter_AT_acm_DOT_org>.
// All rights reserved.  Use of this class is limited.
// Please see the LICENSE for more information.

package org.javaosc.galaxy.web.multipart;

import java.io.IOException;

/** 
 * Thrown to indicate an upload exceeded the maximum size.
 *
 * @see org.javaosc.galaxy.web.multipart.MultipartParser
 *
 * @author <b>Jason Hunter</b>, Copyright &#169; 2007
 * @version 1.0, 2007/04/11
 */
public class ExceededSizeException extends IOException {
	
	private static final long serialVersionUID = -2461042130016255431L;

/**
   * Constructs a new ExceededSizeException with no detail message.
   */
  public ExceededSizeException() {
    super();
  }

  /**
   * Constructs a new ExceededSizeException with the specified
   * detail message.
   *
   * @param s the detail message
   */
  public ExceededSizeException(String s) {
    super(s);
  }
}

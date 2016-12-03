package org.javaosc.framework.convert;

import org.javaosc.galaxy.assist.ClassHandler;
import org.javaosc.galaxy.convert.Convert;

public class ClsImplTest {

	public static void main(String[] args) {
		
		System.out.println(ClassHandler.isImplClass(User.class,Convert.class));;

	}

}

package org.javaosc.framework.convert;

import org.javaosc.ratel.assist.ClassHandler;
import org.javaosc.ratel.convert.Convert;

public class ClsImplTest {

	public static void main(String[] args) {
		
		System.out.println(ClassHandler.isImplClass(User.class,Convert.class));;

	}

}

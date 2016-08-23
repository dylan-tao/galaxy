package org.javaosc.framework.context;

import org.javaosc.framework.annotation.Mapping;
import org.javaosc.framework.annotation.Service;
import org.javaosc.framework.constant.Constant.ProxyMode;

public class ScanAnnotationTest {
	
	public static void main(String[] args) {
		boolean isService = UserServiceImpl.class.isAnnotationPresent(Service.class);
		System.out.println(isService);
		
		boolean isAction = UserAction.class.isAnnotationPresent(Mapping.class);
		System.out.println(isAction);
		
		UserServiceImpl userService = BeanFactory.getService(ProxyMode.DEFAULT, UserServiceImpl.class);
		System.out.println(userService.getUserName());
//		Class<?> cls = UserServiceImpl.class;
//		  ScanAnnotation.setClassGlobalParam(UserServiceImpl.class);
//		  UserService service = null;
//			try {
//				service = (UserService) cls.newInstance();
//			}  catch (Exception e) {
//				e.printStackTrace();
//			}
//		     System.out.println(service.getUserName());
		     
//		 	Class<?> cls = UserAction.class;
//			  ScanAnnotation.setServiceField(UserAction.class);
		
	}
}

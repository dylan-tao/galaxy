package org.javaosc.framework.web.assist;

import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09 Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class MethodParamName {

	public static String[] getParamNames(Class<?> cls, Method method) {
		ClassPool pool = new ClassPool(true);
		LocalVariableAttribute attr = null;
		int pos = 0;
		String[] paramNames = null;
		try {
			CtClass clz = pool.get(cls.getName());
			CtClass[] params = new CtClass[method.getParameterTypes().length];
			for (int i = 0; i < method.getParameterTypes().length; i++) {
				params[i] = pool.getCtClass(method.getParameterTypes()[i].getName());
			}
			CtMethod cm = clz.getDeclaredMethod(method.getName(), params);
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			attr = (LocalVariableAttribute)codeAttribute.getAttribute(LocalVariableAttribute.tag);
			pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			paramNames = new String[cm.getParameterTypes().length];
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < paramNames.length; i++) {
			paramNames[i] = attr.variableName(i + pos);
		}
		return paramNames;
	}

	// public static List<String> getParamNames(Method method) {
	// try {
	// int size = method.getParameterTypes().length;
	// if (size == 0)
	// return new ArrayList<String>(0);
	// List<String> list =
	// getParamNames(method.getDeclaringClass()).get(getKey(method));
	// if (list != null && list.size() != size)
	// return list.subList(0, size);
	// return list;
	// } catch (Throwable e) {
	// throw new RuntimeException(e);
	// }
	// }
	//
	// protected static List<String> getParamNames(Constructor<?> constructor) {
	// try {
	// int size = constructor.getParameterTypes().length;
	// if (size == 0)
	// return new ArrayList<String>(0);
	// List<String> list =
	// getParamNames(constructor.getDeclaringClass()).get(getKey(constructor));
	// if (list != null && list.size() != size)
	// return list.subList(0, size);
	// return list;
	// } catch (Throwable e) {
	// throw new RuntimeException(e);
	// }
	// }
	//
	//
	// private static Map<String, List<String>> getParamNames(Class<?> klass)
	// throws IOException {
	// InputStream in = klass.getResourceAsStream(Constant.LINE +
	// klass.getName().replace(Constant.DOT, Constant.LINE) +
	// Constant.SUFFIX_CLASS);
	// return getParamNames(in);
	// }
	//
	// private static Map<String, List<String>> getParamNames(InputStream in)
	// throws IOException {
	// DataInputStream dis = new DataInputStream(new BufferedInputStream(in));
	// Map<String, List<String>> names = new HashMap<String, List<String>>();
	// Map<Integer, String> strs = new HashMap<Integer, String>();
	// dis.skipBytes(4);
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	//
	// int constant_pool_count = dis.readUnsignedShort();
	// for (int i = 0; i < (constant_pool_count - 1); i++) {
	// byte flag = dis.readByte();
	// switch (flag) {
	// case 7:
	// dis.skipBytes(2);
	// break;
	// case 9:
	// case 10:
	// case 11:
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	// break;
	// case 8:
	// dis.skipBytes(2);
	// break;
	// case 3:
	// case 4:
	// dis.skipBytes(4);
	// break;
	// case 5:
	// case 6:
	// dis.skipBytes(8);
	// i++;
	// break;
	// case 12:
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	// break;
	// case 1:
	// int len = dis.readUnsignedShort();
	// byte[] data = new byte[len];
	// dis.read(data);
	// strs.put(i + 1, new String(data, ProperConstant.DEFAULT_ENCODING_VALUE));
	// break;
	// case 15:
	// dis.skipBytes(1);
	// dis.skipBytes(2);
	// break;
	// case 16:
	// dis.skipBytes(2);
	// break;
	// case 18:
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	// break;
	// default:
	// throw new RuntimeException("Impossible!! flag="+flag);
	// }
	// }
	//
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	//
	// int interfaces_count = dis.readUnsignedShort();
	// dis.skipBytes(2 * interfaces_count);
	//
	// int fields_count = dis.readUnsignedShort();
	// for (int i = 0; i < fields_count; i++) {
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	// int attributes_count = dis.readUnsignedShort();
	// for (int j = 0; j < attributes_count; j++) {
	// dis.skipBytes(2);
	// int attribute_length = dis.readInt();
	// dis.skipBytes(attribute_length);
	// }
	// }
	//
	// int methods_count = dis.readUnsignedShort();
	// for (int i = 0; i < methods_count; i++) {
	// dis.skipBytes(2);
	// String methodName = strs.get(dis.readUnsignedShort());
	// String descriptor = strs.get(dis.readUnsignedShort());
	// short attributes_count = dis.readShort();
	// for (int j = 0; j < attributes_count; j++) {
	// String attrName = strs.get(dis.readUnsignedShort());
	// int attribute_length = dis.readInt();
	// if ("Code".equals(attrName)) {
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	// int code_len = dis.readInt();
	// dis.skipBytes(code_len);
	// int exception_table_length = dis.readUnsignedShort();
	// dis.skipBytes(8 * exception_table_length);
	//
	// int code_attributes_count = dis.readUnsignedShort();
	// for (int k = 0; k < code_attributes_count; k++) {
	// int str_index = dis.readUnsignedShort();
	// String codeAttrName = strs.get(str_index);
	// int code_attribute_length = dis.readInt();
	// if ("LocalVariableTable".equals(codeAttrName)) {
	// int local_variable_table_length = dis.readUnsignedShort();
	// List<String> varNames = new
	// ArrayList<String>(local_variable_table_length);
	// for (int l = 0; l < local_variable_table_length; l++) {
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	// String varName = strs.get(dis.readUnsignedShort());
	// dis.skipBytes(2);
	// dis.skipBytes(2);
	// if (!"this".equals(varName))
	// varNames.add(varName);
	// }
	// names.put(methodName + "," + descriptor, varNames);
	// } else
	// dis.skipBytes(code_attribute_length);
	// }
	// } else
	// dis.skipBytes(attribute_length);
	// }
	// }
	// dis.close();
	// return names;
	// }
	//
	// private static String getKey(Object obj) {
	// StringBuilder sb = new StringBuilder();
	// if (obj instanceof Method) {
	// sb.append(((Method)obj).getName()).append(',');
	// getDescriptor(sb, (Method)obj);
	// } else if (obj instanceof Constructor<?>) {
	// sb.append("<init>,");
	// getDescriptor(sb, (Constructor<?>)obj);
	// } else
	// throw new RuntimeException("Not Method or Constructor!");
	// return sb.toString();
	// }
	//
	// private static void getDescriptor(StringBuilder sb ,Method method){
	// sb.append('(');
	// for (Class<?> klass : method.getParameterTypes())
	// getDescriptor(sb, klass);
	// sb.append(')');
	// getDescriptor(sb, method.getReturnType());
	// }
	//
	// private static void getDescriptor(StringBuilder sb , Constructor<?>
	// constructor){
	// sb.append('(');
	// for (Class<?> klass : constructor.getParameterTypes())
	// getDescriptor(sb, klass);
	// sb.append(')');
	// sb.append('V');
	// }
	//
	// private static void getDescriptor(final StringBuilder buf, final Class<?>
	// c) {
	// Class<?> d = c;
	// while (true) {
	// if (d.isPrimitive()) {
	// char car;
	// if (d == Integer.TYPE) {
	// car = 'I';
	// } else if (d == Void.TYPE) {
	// car = 'V';
	// } else if (d == Boolean.TYPE) {
	// car = 'Z';
	// } else if (d == Byte.TYPE) {
	// car = 'B';
	// } else if (d == Character.TYPE) {
	// car = 'C';
	// } else if (d == Short.TYPE) {
	// car = 'S';
	// } else if (d == Double.TYPE) {
	// car = 'D';
	// } else if (d == Float.TYPE) {
	// car = 'F';
	// } else{
	// car = 'J';
	// }
	// buf.append(car);
	// return;
	// } else if (d.isArray()) {
	// buf.append('[');
	// d = d.getComponentType();
	// } else {
	// buf.append('L');
	// String name = d.getName();
	// int len = name.length();
	// for (int i = 0; i < len; ++i) {
	// char car = name.charAt(i);
	// buf.append(car == '.' ? '/' : car);
	// }
	// buf.append(';');
	// return;
	// }
	// }
	// }
}

package org.javaosc.framework.param;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javaosc.galaxy.constant.Configuration;
import org.javaosc.galaxy.constant.Constant;

import net.sf.cglib.asm.$ClassReader;
import net.sf.cglib.asm.$ClassVisitor;
import net.sf.cglib.asm.$Label;
import net.sf.cglib.asm.$MethodVisitor;
import net.sf.cglib.asm.$Opcodes;
import net.sf.cglib.asm.$Type;
  

public class GetMethodParamName {

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		
		String[] s = jdk8(); 
//		String[] s = cust();
//		String[] s = asm();
		
		System.out.println(System.currentTimeMillis() - t1);
		for(int i=0;i<s.length;i++){
			System.out.println(s[i]);
		}
	  
	}
	
	public static String[] asm(){
		Method method = null;
		try {
			method = GetMethodParamName.class.getDeclaredMethod("hha", String.class, int.class, String[].class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return getParameterNamesByAsm(GetMethodParamName.class, method); 
	}
	
	//too low ,must complate class -> store in class some info
	public static String[] jdk8(){
		Method method = null;
		try {
			method = GetMethodParamName.class.getDeclaredMethod("hha", String.class, int.class, String[].class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		Parameter[] params = method.getParameters();
		String[] methodNames = new String[params.length];
		for (int i=0;i<params.length;i++) {  
			System.out.println(params[i].getName());
			methodNames[i] = params[i].getName();
        }
		return methodNames;
	}
	
	public static String[] cust(){
		Method method = null;
		try {
			method = GetMethodParamName.class.getDeclaredMethod("hha", String.class, int.class, String[].class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return getParamName(method);
	}
	
	public static String hha(String username,int age,String[] hobbily){
		System.out.println("dg");
		return null;
	}
	
	public static String[] getParameterNamesByAsm(Class<?> clazz, final Method method) {  
        final Class<?>[] parameterTypes = method.getParameterTypes();  
        if (parameterTypes == null || parameterTypes.length == 0) {  
            return null;  
        }  
        final $Type[] types = new $Type[parameterTypes.length];  
        for (int i = 0; i < parameterTypes.length; i++) {  
            types[i] = $Type.getType(parameterTypes[i]);  
        }  
        final String[] parameterNames = new String[parameterTypes.length];  
  
        String className = clazz.getName();  
        int lastDotIndex = className.lastIndexOf(".");  
        className = className.substring(lastDotIndex + 1) + ".class";  
        InputStream is = clazz.getResourceAsStream(className);  
        try {  
        	$ClassReader classReader = new $ClassReader(is);  
            classReader.accept(new $ClassVisitor($Opcodes.ASM5) {  
                @Override  
                public $MethodVisitor visitMethod(int access, String name,  
                        String desc, String signature, String[] exceptions) {  
                    // 只处理指定的方法  
                    $Type[] argumentTypes = $Type.getArgumentTypes(desc);  
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {  
                        return super.visitMethod(access, name, desc, signature,  
                                exceptions);  
                    }  
                    return new $MethodVisitor($Opcodes.ASM5) {  
                        @Override  
                        public void visitLocalVariable(String name, String desc,  
                                String signature, $Label start,  
                                $Label end, int index) {  
                            // 非静态成员方法的第一个参数是this  
                            if (Modifier.isStatic(method.getModifiers())) {  
                                parameterNames[index] = name;  
                            } else if (index > 0) {  
                                parameterNames[index - 1] = name;  
                            }  
                        }  
                    };  
                }  
            }, 0);  
        } catch (IOException e) {  
        } finally {  
            try {  
                if (is != null) {  
                    is.close();  
                }  
            } catch (Exception e2) {  
            }  
        }  
        return parameterNames;  
    }  
	
	
	public static String[] getParamName(Method method) {
		try {
			int size = method.getParameterTypes().length;
			if (size == 0) {
				return new String[0];
			}else{
				List<String> list = getParamNames(method.getDeclaringClass()).get(getKey(method));
				if (list != null){
					if(list.size() == size){
						return list.toArray(new String[size]);
					}else{
						return list.subList(0, size).toArray(new String[size]);
					}
				}else{
					return new String[0];
				}
			}
		} catch (Throwable e) {
			return null;
		}
	}

	protected static List<String> getParamNames(Constructor<?> constructor) {
		try {
			int size = constructor.getParameterTypes().length;
			if (size == 0){
				return new ArrayList<String>(0);
			}
			List<String> list = getParamNames(constructor.getDeclaringClass()).get(getKey(constructor));
			if (list != null){
				if(list.size() == size){
					return list;
				}else{
					return list.subList(0, size);
				}
			}else{
				return new ArrayList<String>(0);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static Map<String, List<String>> getParamNames(Class<?> klass) throws IOException {
		InputStream in = klass.getResourceAsStream(Constant.URL_LINE + klass.getName().replace(Constant.DOT, Constant.URL_LINE) + Constant.SUFFIX_CLASS);
		return getParamNames(in);
	}

	private static Map<String, List<String>> getParamNames(InputStream in) throws IOException {
		DataInputStream dis = new DataInputStream(new BufferedInputStream(in));
		Map<String, List<String>> names = new HashMap<String, List<String>>();
		Map<Integer, String> strs = new HashMap<Integer, String>();
		dis.skipBytes(4);
		dis.skipBytes(2);
		dis.skipBytes(2);

		int constant_pool_count = dis.readUnsignedShort();
		for (int i = 0; i < (constant_pool_count - 1); i++) {
			byte flag = dis.readByte();
			switch (flag) {
			case 7:
				dis.skipBytes(2);
				break;
			case 9:
			case 10:
			case 11:
				dis.skipBytes(2);
				dis.skipBytes(2);
				break;
			case 8:
				dis.skipBytes(2);
				break;
			case 3:
			case 4:
				dis.skipBytes(4);
				break;
			case 5:
			case 6:
				dis.skipBytes(8);
				i++;
				break;
			case 12:
				dis.skipBytes(2);
				dis.skipBytes(2);
				break;
			case 1:
				int len = dis.readUnsignedShort();
				byte[] data = new byte[len];
				dis.read(data);
				strs.put(i + 1, new String(data,
						Configuration.DEFAULT_ENCODING_VALUE));
				break;
			case 15:
				dis.skipBytes(1);
				dis.skipBytes(2);
				break;
			case 16:
				dis.skipBytes(2);
				break;
			case 18:
				dis.skipBytes(2);
				dis.skipBytes(2);
				break;
			default:
				throw new RuntimeException("Impossible!! flag=" + flag);
			}
		}

		dis.skipBytes(2);
		dis.skipBytes(2);
		dis.skipBytes(2);

		int interfaces_count = dis.readUnsignedShort();
		dis.skipBytes(2 * interfaces_count);

		int fields_count = dis.readUnsignedShort();
		for (int i = 0; i < fields_count; i++) {
			dis.skipBytes(2);
			dis.skipBytes(2);
			dis.skipBytes(2);
			int attributes_count = dis.readUnsignedShort();
			for (int j = 0; j < attributes_count; j++) {
				dis.skipBytes(2);
				int attribute_length = dis.readInt();
				dis.skipBytes(attribute_length);
			}
		}

		int methods_count = dis.readUnsignedShort();
		for (int i = 0; i < methods_count; i++) {
			dis.skipBytes(2);
			String methodName = strs.get(dis.readUnsignedShort());
			String descriptor = strs.get(dis.readUnsignedShort());
			short attributes_count = dis.readShort();
			for (int j = 0; j < attributes_count; j++) {
				String attrName = strs.get(dis.readUnsignedShort());
				int attribute_length = dis.readInt();
				if ("Code".equals(attrName)) {
					dis.skipBytes(2);
					dis.skipBytes(2);
					int code_len = dis.readInt();
					dis.skipBytes(code_len);
					int exception_table_length = dis.readUnsignedShort();
					dis.skipBytes(8 * exception_table_length);

					int code_attributes_count = dis.readUnsignedShort();
					for (int k = 0; k < code_attributes_count; k++) {
						int str_index = dis.readUnsignedShort();
						String codeAttrName = strs.get(str_index);
						int code_attribute_length = dis.readInt();
						if ("LocalVariableTable".equals(codeAttrName)) {
							int local_variable_table_length = dis
									.readUnsignedShort();
							List<String> varNames = new ArrayList<String>(
									local_variable_table_length);
							for (int l = 0; l < local_variable_table_length; l++) {
								dis.skipBytes(2);
								dis.skipBytes(2);
								String varName = strs.get(dis
										.readUnsignedShort());
								dis.skipBytes(2);
								dis.skipBytes(2);
								if (!"this".equals(varName))
									varNames.add(varName);
							}
							names.put(methodName + "," + descriptor, varNames);
						} else
							dis.skipBytes(code_attribute_length);
					}
				} else
					dis.skipBytes(attribute_length);
			}
		}
		dis.close();
		return names;
	}

	private static String getKey(Object obj) {
		StringBuilder sb = new StringBuilder();
		if (obj instanceof Method) {
			sb.append(((Method) obj).getName()).append(',');
			getDescriptor(sb, (Method) obj);
		} else if (obj instanceof Constructor<?>) {
			sb.append("<init>,");
			getDescriptor(sb, (Constructor<?>) obj);
		} else
			throw new RuntimeException("Not Method or Constructor!");
		return sb.toString();
	}

	private static void getDescriptor(StringBuilder sb, Method method) {
		sb.append('(');
		for (Class<?> klass : method.getParameterTypes())
			getDescriptor(sb, klass);
		sb.append(')');
		getDescriptor(sb, method.getReturnType());
	}

	private static void getDescriptor(StringBuilder sb,
			Constructor<?> constructor) {
		sb.append('(');
		for (Class<?> klass : constructor.getParameterTypes())
			getDescriptor(sb, klass);
		sb.append(')');
		sb.append('V');
	}

	private static void getDescriptor(final StringBuilder buf, final Class<?> c) {
		Class<?> d = c;
		while (true) {
			if (d.isPrimitive()) {
				char car;
				if (d == Integer.TYPE) {
					car = 'I';
				} else if (d == Void.TYPE) {
					car = 'V';
				} else if (d == Boolean.TYPE) {
					car = 'Z';
				} else if (d == Byte.TYPE) {
					car = 'B';
				} else if (d == Character.TYPE) {
					car = 'C';
				} else if (d == Short.TYPE) {
					car = 'S';
				} else if (d == Double.TYPE) {
					car = 'D';
				} else if (d == Float.TYPE) {
					car = 'F';
				} else {
					car = 'J';
				}
				buf.append(car);
				return;
			} else if (d.isArray()) {
				buf.append('[');
				d = d.getComponentType();
			} else {
				buf.append('L');
				String name = d.getName();
				int len = name.length();
				for (int i = 0; i < len; ++i) {
					char car = name.charAt(i);
					buf.append(car == '.' ? '/' : car);
				}
				buf.append(';');
				return;
			}
		}
	}

}

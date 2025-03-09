package plz.lizi.sillyyouare.sillymc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NonpublicObj {
	private static Unsafe unsafe;

	static{
		try{
			final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
			unsafeField.setAccessible(true);
			unsafe = (Unsafe) unsafeField.get(null);
		}catch(Exception ignored){}
	}

	private final Class<?> clazz;
	private Object self = null;

	public NonpublicObj(Class<?> inc){
		clazz = inc;
	}

	public NonpublicObj(Object o){
		clazz = o.getClass();
		self = o;
	}

	private void fieldSFS(Field field, Object value) {
		try {
			Object fieldBase = unsafe.staticFieldBase(field);
			long fieldOffset = unsafe.staticFieldOffset(field);
			unsafe.putObject(fieldBase, fieldOffset, value);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public Object fieldV(String[] names){
		try{
			Field f = null;
			List<String> namesL = Arrays.stream(names).toList();
			for (String name : namesL){
				try{
					f = clazz.getDeclaredField(name);
					break;
				}catch (Exception ignored){}
			}
			if (f==null)throw new RuntimeException("null");
			f.setAccessible(true);
			return f.get(self);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public void fieldM(String[] names, Object set){
		try{
			Field f = null;
			List<String> namesL = Arrays.stream(names).toList();
			for (String name : namesL){
				try{
					f = clazz.getDeclaredField(name);
					break;
				}catch (Exception ignored){}
			}
			if (f==null)throw new RuntimeException("null");
			f.setAccessible(true);
			if (Modifier.isStatic(f.getModifiers())){
				fieldSFS(f, set);
			}else {
				f.set(self, set);
			}
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public Object methodI(String[] names, Object[] args){
		try {
			Method m = null;
			List<String> namesL = Arrays.stream(names).toList();
			List<Class<?>> argCs = new ArrayList<>();
			for (Object o : args){
				argCs.add(o.getClass());
			}
			for (String name : namesL){
				try{
					m = clazz.getDeclaredMethod(name, argCs.toArray(new Class<?>[]{}));
					break;
				}catch (Exception ignored){}
			}
			if (m==null)throw new RuntimeException("null");
			m.setAccessible(true);
			return m.invoke(self, args);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}

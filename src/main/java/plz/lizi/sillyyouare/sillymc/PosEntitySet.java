package plz.lizi.sillyyouare.sillymc;

import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class PosEntitySet {
	public Map<Class<?>, Set<Point3D>> map = new HashMap<>();

	public void put(Entity e){
		map.computeIfAbsent(e.getClass(), k -> new HashSet<>()).add(new Point3D(e.getX(), e.getY(), e.getZ()));
	}

	public boolean contains(Entity e){
		Class<?> ce = e.getClass();
		AtomicBoolean rv = new AtomicBoolean(false);
		if (map.get(ce)!=null){
			Set<Point3D> ps = map.get(ce);;
			ps.forEach(p->{
				Point3D z = new Point3D(e.getX(), e.getY(), e.getZ());
				if (z.distance(p)<=1) rv.set(true);
			});
		}
		return rv.get();
	}
}

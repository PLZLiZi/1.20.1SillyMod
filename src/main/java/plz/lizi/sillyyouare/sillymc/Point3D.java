package plz.lizi.sillyyouare.sillymc;

public class Point3D {
	public double x, y, z;

	public Point3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = x;
	}

	public double getX(){return x;}
	public double getY(){return y;}
	public double getZ(){return z;}

	public double distance(Point3D to) {
		double dx = this.x - to.x;
		double dy = this.y - to.y;
		double dz = this.z - to.z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
}

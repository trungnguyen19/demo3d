package nehe.lesson35;

public class Test {
	public static void main(String[] args) {
		AVI_Reader ar = new AVI_Reader();
		//ar.run("src/main/java/nehe/lesson35/data/Face2.avi"); // cvid
		//ar.run("/home/public/Desktop/fp.avi"); // CRAM
		//ar.run("/home/public/Desktop/S3DS - Demo 20 (Merge Balanced).avi"); // cvid
		//ar.run("/home/public/Desktop/dfs.avi"); // CRAM
		ar.run("/home/public/Desktop/video1.avi");
		System.out.println("Ok!");
	}
}

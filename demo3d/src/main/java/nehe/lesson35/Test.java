package nehe.lesson35;

import com.trungnd10.utils.MyConstants;

public class Test {
	public static void main(String[] args) {
		AVI_Reader ar = new AVI_Reader();
		// ar.run("src/main/java/nehe/lesson35/data/Face2.avi"); // cvid
		// ar.run("data/videos/avi/fp.avi"); // CRAM
		// ar.run("data/videos/avi/S3DS - Demo 20 (Merge Balanced).avi"); //
		// cvid
		// ar.run("data/videos/avi/dfs.avi"); // CRAM
		// ar.run("data/videos/avi/SlowMotion.20x.Race.avi"); // IV50
		ar.run(MyConstants.dataPath + "videos/avi/video1.avi");
		System.out.println("Ok!");
	}
}

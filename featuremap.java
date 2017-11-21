import java.io.*;
import java.util.*;

public class featuremap {

	public static int[][] calculateFeatureMap(int[][] map, int[][] filter, int stride) {

		int flenN = filter[0].length; //filter length n
		int flenM = filter.length; // m
		
		int resSizeN = (map[0].length - flenN)/stride +1 ;//o= w-k/s +1 ; w = inputsize ; k = filter size; s = stride
		int resSizeM = (map.length - flenM)/stride +1;
		
		int[][] res = new int[resSizeM][resSizeN]; //change stride here
		int resIndexN = 0;
		int resIndexM = 0;

		for (int mP = flenM - 1; mP < map.length; mP+=stride) {//change stride here
			resIndexN = 0;
			for (int nP = flenN - 1; nP < map[mP].length; nP+=stride) { // and here

				//System.out.println(nP);	

				res[resIndexM][resIndexN] = 0;

				int tNMindex = 0;//temp n map index
				int tMMindex = 0;//temp m map index

				for (int i = flenM - 1; i > -1; i--) {//filter m
					tNMindex = 0;
					for (int j = flenN - 1; j > -1; j--) { //filter n
						//System.out.println("m:" + (mP - tNMindex) + " n:" + (nP - tMMindex) + " i: " + i + " j:" + j);
						//System.out.println(filter[i][j] + " * " + map[mP - tNMindex][nP - tMMindex]);

						res[resIndexM][resIndexN] += filter[i][j] * map[mP - tMMindex][nP - tNMindex];

						tNMindex++;

					}
					tMMindex++;

				} 

				resIndexN++;

			}
			resIndexM++;

		}
		return res;
	}

	public static int[][] readMap(File file) throws FileNotFoundException {

		Scanner sc = new Scanner(file);
		int n = sc.nextInt();
		int m = sc.nextInt();

		int map[][] = new int[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = sc.nextInt();
			}

		}
		return map;

	}

	public static void main(String[] args) throws FileNotFoundException {

		int[][] map = readMap(new File("input.txt"));

		//--------------------------filter 1-------------------------
		int[][] filter = readMap(new File("filter1.txt"));
		System.out.println(printMap(map));
		System.out.println(printMap(filter));
		
		int[][] res  = calculateFeatureMap(map, filter, 1);
		
		exportAsCSV(res, new File("filter1.csv"));
		System.out.println(printMap(res));

		
		//--------------------------filter 2-------------------------
		filter = readMap(new File("filter2.txt"));
		System.out.println(printMap(map));
		System.out.println(printMap(filter));
		
		res  = calculateFeatureMap(map, filter, 1);
		
		exportAsCSV(res, new File("filter2.csv"));
		System.out.println(printMap(res));

		//--------------------------filter 3-------------------------
		filter = readMap(new File("filter3.txt"));
		System.out.println(printMap(map));
		System.out.println(printMap(filter));
		
		res  = calculateFeatureMap(map, filter, 1);
		
		exportAsCSV(res, new File("filter3.csv"));
		System.out.println(printMap(res));

		//--------------------------filter 4-------------------------
		filter = readMap(new File("filter4.txt"));
		System.out.println(printMap(map));
		System.out.println(printMap(filter));
		
		res  = calculateFeatureMap(map, filter, 1);
		
		exportAsCSV(res, new File("filter4.csv"));
		System.out.println(printMap(res));
	}

	public static String printMap(int[][] map) {

		int m = map.length;
		String res = "";
		for (int i = 0; i < m; i++) {
			int n = map[i].length;
			for (int j = 0; j < n; j++) {
				res += " " + map[i][j];
			}
			res += "\n";

		}

		return res;
	}

	public static void exportAsCSV(int[][] map, File file) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(file);
		StringBuilder sb = new StringBuilder();
			
		int m = map.length;
		
		for (int i = 0; i < m; i++) {
			int n = map[i].length;
			for (int j = 0; j < n; j++) {
				sb.append(map[i][j]);
				sb.append(",");
			}
			sb.append("\n");
		}

		pw.write(sb.toString());
		pw.close();


	}

}

import java.io.*;
import java.util.*;

public class featuremap {

	public static double[][] calculateFeatureMap(double[][] map, double[][] filter, int stride, int decimalPlaces) {

		int flenN = filter[0].length; //filter length n
		int flenM = filter.length; // m
		
		int resSizeN = (map[0].length - flenN)/stride +1 ;//o= w-k/s +1 ; w = inputsize ; k = filter size; s = stride
		int resSizeM = (map.length - flenM)/stride +1;
		
		double[][] res = new double[resSizeM][resSizeN]; //change stride here
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

						res[resIndexM][resIndexN] += (double) (filter[i][j] * map[mP - tMMindex][nP - tNMindex]);

						tNMindex++;

					}
					tMMindex++;

				}
				double decimalMult = Math.pow(10.0, decimalPlaces);
				res[resIndexM][resIndexN] = Math.round(res[resIndexM][resIndexN]/(flenN*flenM) * decimalMult)/decimalMult ;

				resIndexN++;

			}
			resIndexM++;

		}
		return res;
	}

	
	public static double[][] maxPooling(double[][] map, int stride, int n, int m) {

		int flenN = n; //filter length n
		int flenM = m; // m
		
		int resSizeN = (map[0].length - flenN)/stride +2 ;//needs to be changed
		int resSizeM = (map.length - flenM)/stride +2;
		
		double[][] res = new double[resSizeM][resSizeN];
		int resIndexN = 0;
		int resIndexM = 0;

		for (int mP = 0; mP < map.length; mP+=stride) {
			resIndexN = 0;
			for (int nP = 0; nP < map[mP].length; nP+=stride) {

				//System.out.println(nP);

				res[resIndexM][resIndexN] = 0;

				int tNMindex = 0;//temp n map index
				int tMMindex = 0;//temp m map index
				
				double max = 0;
				for (int i = 0; i < flenM; i++) {//filter m
					tNMindex = 0;
					for (int j = 0; j < flenN; j++) { //filter n
						//System.out.println("m:" + (mP - tNMindex) + " n:" + (nP - tMMindex) + " i: " + i + " j:" + j);
						//System.out.println(filter[i][j] + " * " + map[mP - tNMindex][nP - tMMindex]);
						if(mP+tMMindex >= map.length || nP+tNMindex >= map[mP].length)
							continue;

						double current = map[mP + tMMindex][nP + tNMindex];
						max = current > max ? current: max;

						tNMindex++;

					}
					tMMindex++;

				}
				
				res[resIndexM][resIndexN] = max;

				resIndexN++;

			}
			resIndexM++;

		}
		return res;
	}

	public static void relu(double[][] map){
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j< map[0].length; j++){
				if(map[i][j] < 0){
					map[i][j] = 0;
				}

			}
		}

	}

	public static double[][] readMap(File file) throws FileNotFoundException {

		Scanner sc = new Scanner(file);
		int n = sc.nextInt();
		int m = sc.nextInt();

		double map[][] = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = (double) sc.nextInt();
			}

		}
		return map;

	}

	public static void main(String[] args) throws FileNotFoundException {

		double[][] map = readMap(new File("input.txt"));

		int decimalPlaces = 1;
		
		
		double[][] filter1 = readMap(new File("filter1.txt"));
		double[][] filter2 = readMap(new File("filter2.txt"));
		double[][] filter3 = readMap(new File("filter3.txt"));
		double[][] filter4 = readMap(new File("filter4.txt"));
		
		
		double[][] filter21 = readMap(new File("filter21.txt"));//stride 3
		double[][] filter22 = readMap(new File("filter22.txt"));


		//--------------------------filter 1-------------------------
		System.out.println(printMap(map));
		System.out.println(printMap(filter1));
		
		double[][] res  = calculateFeatureMap(map, filter1, 1, decimalPlaces);

		relu(res);	

		double[][] maxPool = maxPooling(res, 1, 2, 2);

		
		exportAsCSV(maxPool, new File("maxPool/filter1.csv"));

		double[][] filter1Res = calculateFeatureMap(maxPool, filter21, 3, decimalPlaces);
		double[][] filter2Res = calculateFeatureMap(maxPool, filter22, 3, decimalPlaces);

		
		exportAsCSV(filter1Res, new File("conv2/filter11.csv"));
		exportAsCSV(filter2Res, new File("conv2/filter12.csv"));

		relu(filter1Res);
		relu(filter2Res);

		exportAsCSV(filter1Res, new File("relu2/filter11.csv"));
		exportAsCSV(filter2Res, new File("relu2/filter12.csv"));

		System.out.println(printMap(filter1Res));
		System.out.println(printMap(filter2Res));

		
		//--------------------------filter 2-------------------------
		System.out.println(printMap(map));
		System.out.println(printMap(filter2));
		
		res  = calculateFeatureMap(map, filter2, 1, decimalPlaces);
		relu(res);
		
		maxPool = maxPooling(res, 1, 2, 2);


		exportAsCSV(maxPool, new File("maxPool/filter2.csv"));
		
		filter1Res = calculateFeatureMap(maxPool, filter21, 3, decimalPlaces);
		filter2Res = calculateFeatureMap(maxPool, filter22, 3, decimalPlaces);

		exportAsCSV(filter1Res, new File("conv2/filter21.csv"));
		exportAsCSV(filter2Res, new File("conv2/filter22.csv"));

		relu(filter1Res);
		relu(filter2Res);

		exportAsCSV(filter1Res, new File("relu2/filter21.csv"));
		exportAsCSV(filter2Res, new File("relu2/filter22.csv"));
		
		System.out.println(printMap(filter1Res));
		System.out.println(printMap(filter2Res));

		//--------------------------filter 3-------------------------
		System.out.println(printMap(map));
		System.out.println(printMap(filter3));
		
		res  = calculateFeatureMap(map, filter3, 1, decimalPlaces);
		relu(res);

		maxPool = maxPooling(res, 1, 2, 2);
		
		exportAsCSV(maxPool, new File("maxPool/filter3.csv"));
		
		filter1Res = calculateFeatureMap(maxPool, filter21, 3, decimalPlaces);
		filter2Res = calculateFeatureMap(maxPool, filter22, 3, decimalPlaces);

		exportAsCSV(filter1Res, new File("conv2/filter31.csv"));
		exportAsCSV(filter2Res, new File("conv2/filter32.csv"));

		relu(filter1Res);
		relu(filter2Res);

		exportAsCSV(filter1Res, new File("relu2/filter31.csv"));
		exportAsCSV(filter2Res, new File("relu2/filter32.csv"));
		
		System.out.println(printMap(filter1Res));
		System.out.println(printMap(filter2Res));

		//--------------------------filter 4-------------------------
		System.out.println(printMap(map));
		System.out.println(printMap(filter4));
		
		res  = calculateFeatureMap(map, filter4, 1, decimalPlaces);
		relu(res);
		
		maxPool = maxPooling(res, 1, 2, 2);
		
		exportAsCSV(maxPool, new File("maxPool/filter4.csv"));

		filter1Res = calculateFeatureMap(maxPool, filter21, 3, decimalPlaces);
		filter2Res = calculateFeatureMap(maxPool, filter22, 3, decimalPlaces);	

		exportAsCSV(filter1Res, new File("conv2/filter41.csv"));
		exportAsCSV(filter2Res, new File("conv2/filter42.csv"));
		
		relu(filter1Res);
		relu(filter2Res);

		exportAsCSV(filter1Res, new File("relu2/filter41.csv"));
		exportAsCSV(filter2Res, new File("relu2/filter42.csv"));
		
		System.out.println(printMap(filter1Res));
		System.out.println(printMap(filter2Res));
	}

	public static String printMap(double[][] map) {

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

	public static void exportAsCSV(double[][] map, File file) throws FileNotFoundException{
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

package algorithm;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class First_Class {

	static int ss = 1000, sc = 1000, ls = 1000, lc = 1000; //change back to 1000 later
	static int t = 4, T = 12; //change T back to 1000 later
	static final long  small_size = 4, large_size = 40;
	static final int R_small = 4, R_large = 50;
	public static Genome genome; 
	
	public enum Type{
		COOPERATOR,
		SELFISH
	}
	
	public enum Size{
		SMALL,
		LARGE
	}
	
	public First_Class() throws IOException{
		process();
	}
	
	public static void main(String[] args) throws IOException{
		new First_Class();
	}
	
	private void process() throws IOException{
		//init
		genome = new Genome(ss, sc, ls, lc, small_size, large_size, t, T);
        String csvFile = "C:/Users/NICHOLAS/workspace3/algorithm/src/algorithm/Data/this.csv";

        
    	File fout = new File(csvFile);
    	FileOutputStream fos = new FileOutputStream(fout);
    	
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    	String str;

		bw.write("T, "); bw.write("sc, "); bw.write("ss, "); bw.write("lc, "); bw.write("ls, ");
		bw.newLine();
		bw.write("0, "); bw.write("0.25, "); bw.write("0.25, "); bw.write("0.25, "); bw.write("0.25, ");
		bw.newLine();
        
		for(int i = 1; i <= T; i++){
			System.out.println("Generation: " + i);
			//dispersion
			genome.group_aggregation();
			//reproduction
			genome.reproduce();	
			//dispersal
			genome.group_dispersal();
			//rescale
			genome.normalise();
			//return population proportions
			str = String.valueOf(i); bw.write(str); bw.write(", ");
			str = String.valueOf(genome.population_frequency(Type.COOPERATOR, Size.SMALL));	bw.write(str); bw.write(","); 
			str = String.valueOf(genome.population_frequency(Type.SELFISH, Size.SMALL)); bw.write(" "); bw.write(str); bw.write(",");
			str = String.valueOf(genome.population_frequency(Type.COOPERATOR, Size.LARGE)); bw.write(" "); bw.write(str); bw.write(",");
			str = String.valueOf(genome.population_frequency(Type.SELFISH, Size.LARGE)); bw.write(" "); bw.write(str); bw.newLine();
		}
		bw.close();
	}
	
}

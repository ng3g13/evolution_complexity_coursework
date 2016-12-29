package algorithm;

import java.util.Random;

public class First_Class {

	static int ss = 200, sc = 200, ls = 200, lc = 200; //change back to 1000 later
	static int small_size = 4, large_size = 40, t = 4, T = 1; //change T back to 1000 later
	public static Genome genome; 
	Random r = new Random();
	
	public First_Class(){
		process();
	}
	
	public static void main(String[] args){
		new First_Class();
	}
	
	private void process(){
		//init
		genome = new Genome(ss, sc, ls, lc, small_size, large_size, t, T);
		
		for(int i = 0; i < T; i++){
		//dispersion
		genome.group_aggregation();
		//reproduction
		genome.reproduce();	
		//dispersal
		genome.group_dispersal();
		//rescale
		genome.rescale();
		
		//System.out.println(i);
		}
	}
	
}

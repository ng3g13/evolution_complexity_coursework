package algorithm;

import java.util.ArrayList;
import java.util.Random;

public class Genome {
	
	int ss, sc, ls, lc, small_size, large_size, t, T, N;
	static final float K = 0.1f, R_small = 4, R_large = 50, G_selfish = 0.02f, G_cooperative = 0.018f;
	static final float C_selfish = 0.2f, C_cooperative = 0.1f;
	//group index | element of group | int: 1-selfish, 0-cooperative.
	ArrayList<ArrayList<Integer>> group_small = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> group_small_inner;
	
	ArrayList<ArrayList<Integer>> group_large = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> group_large_inner;
	
	Random r;
	
	private enum Type{
		COOPERATOR,
		SELFISH
	}
	
	private enum Size{
		SMALL,
		LARGE
	}
	
	public Genome(int ss, int sc, int ls, int lc, int small_size, int large_size, int t, int T){
		this.ss = ss;
		this.sc = sc;
		this.ls = ls;
		this.lc = lc;
		this.small_size = small_size;
		this.large_size = large_size;
		this.t = t;
		this.T= T;
		N = ss + sc + ls + lc;
	}
	
	public void group_dispersion(){
		int counter_small = ss + sc;
		int counter_large = ls + lc;
		int sc_count = sc;
		int ss_count = ss;
		int lc_count = lc;
		int ls_count = ls;

		//4 times
		while(counter_small >= small_size){
			group_small_inner = new ArrayList<Integer>();
			for(int i = 0; i<small_size; i++){	
				r = new Random();
				//randomly pick individuals of 'small' size
				if(r.nextInt(2) == 0 && sc_count > 0){ //cooperator
					group_small_inner.add(0);
					sc_count--;
				}else{ //selfish
					if(ss_count > 0){
						group_small_inner.add(1);
						ss_count--;	
					}else if(sc_count > 0){
						group_small_inner.add(0);
						sc_count--;						
					}

				}	
			}
			group_small.add(group_small_inner);	
			counter_small = sc_count + ss_count;
		}
			
	
		//40 times
		while(counter_large >= large_size){
			group_large_inner = new ArrayList<Integer>();			
			for(int i = 0; i<large_size; i++){	
				r = new Random();
				//randomly pick individuals of 'small' size
				if(r.nextInt(2) == 0 && lc_count > 0){ //cooperator
					group_large_inner.add(0);
					lc_count--;
				}else{ //selfish
					if(ls_count > 0){
						group_large_inner.add(1);
						ls_count--;						
					}else if(lc_count > 0){
						group_large_inner.add(0);
						lc_count--;						
					}

				}
			}
			group_large.add(group_large_inner);
			counter_large = lc_count + ls_count;
		}	
	
		
		/*
		//testing purposes
		for(int i = 0; i<group_large.size(); i++){
			System.out.println(group_large.get(i));
		}
		*/


	}
	
	public void reproduce(){

		int temp_c = 0, temp_s = 0;
		int temp_c_1, temp_s_1;

		for(int o = 0; o < t; o++){
			//reproduce small groups
			for(int i = 0; i < group_small.size(); i++){
			
				for(int j = 0; j < group_small.get(i).size(); j++){
					if(group_small.get(i).get(j) == 0){ //cooperative
						temp_c++;
					}else{
						temp_s++;
					}
				}
				temp_c_1 = calculation(temp_c, temp_s, Type.COOPERATOR, Size.SMALL);
				temp_s_1 = calculation(temp_c, temp_s, Type.SELFISH, Size.SMALL);
			

				change_pop(temp_c_1, temp_c, Type.COOPERATOR, Size.SMALL, i);
				change_pop(temp_s_1, temp_c, Type.SELFISH, Size.SMALL, i);			
		
				temp_c = 0;
				temp_s = 0;
			}
			
			//reproduce large groups
			for(int i = 0; i < group_large.size(); i++){
			
				for(int j = 0; j < group_large.get(i).size(); j++){
					if(group_large.get(i).get(j) == 0){ //cooperative
						temp_c++;
					}else{
						temp_s++;
					}
				}
				temp_c_1 = calculation(temp_c, temp_s, Type.COOPERATOR, Size.LARGE);
				temp_s_1 = calculation(temp_c, temp_s, Type.SELFISH, Size.LARGE);
			

				change_pop(temp_c_1, temp_c, Type.COOPERATOR, Size.LARGE, i);
				change_pop(temp_s_1, temp_c, Type.SELFISH, Size.LARGE, i);			
		
				temp_c = 0;
				temp_s = 0;
			}
		}


	
	}
	
	private int calculation(int ni_c, int ni_s, Type type, Size size){
		float temp;
		float R;
		int result = 0;
		if(size == Size.SMALL){
			R = R_small;
		}else{
			R = R_large;
		}
		
		if(type == Type.COOPERATOR){
			temp = ((ni_c*G_cooperative*C_cooperative)/((ni_c*G_cooperative*C_cooperative)+(ni_s*G_selfish*C_selfish)))*R;
			result = Math.round(ni_c*(1-K) + (temp/C_cooperative));
			return result;
		}else if(type == Type.SELFISH){
			temp = ((ni_s*G_selfish*C_selfish)/((ni_c*G_cooperative*C_cooperative)+(ni_s*G_selfish*C_selfish)))*R;
			result = Math.round(ni_s*(1-K) + (temp/C_selfish));
			return result;
		}
		return result;
	}
	
	private void change_pop(int temp_x_1, int temp_x, Type type, Size size, int index){
		int g;
		if(type == Type.COOPERATOR){
			g = 0;
		}else{
			g = 1;
		}
		
		if(size == Size.SMALL){
			//change population of coordinators and selfish for each small group.
			if(temp_x_1 - temp_x > 0){				
				for(int i = 0; i < (temp_x_1 - temp_x); i++){
					group_small.get(index).add(g); }
			}else if(temp_x_1 - temp_x < 0){
				for(int j = 0; j < (temp_x - temp_x_1); j++){
					group_small.get(index).remove(g); }
			} //then don't change for if difference is 0.
		}else if(size == Size.LARGE){
			//change population of coordinators and selfish for each small group.
			if(temp_x_1 - temp_x > 0){
				for(int i = 0; i < (temp_x_1 - temp_x); i++){
					group_large.get(index).add(g); }
			}else if(temp_x_1 - temp_x < 0){
				for(int j = 0; j < (temp_x - temp_x_1); j++){
					group_large.get(index).remove(g); }
			} //then don't change for if difference is 0.			
		}
	}
	
}

package algorithm;

import java.util.ArrayList;
import java.util.Random;

public class Genome{
	
	long ss, sc, ls, lc, t, T, N, small_size, large_size;
	static final float K = 0.1f, R_small = 4, R_large = 50, G_selfish = 0.02f, G_cooperative = 0.018f;
	static final float C_selfish = 0.2f, C_cooperative = 0.1f;
	//group index | element of group | int: 1-selfish, 0-cooperative.
	ArrayList<Group> group_list_small =  new ArrayList<Group>();
	
	ArrayList<Group> group_list_large =  new ArrayList<Group>();
	Random r;

	

	
	public Genome(int ss, int sc, int ls, int lc, long small_size, long large_size, int t, int T){
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
	
	public void group_aggregation(){
		long counter_small = ss + sc;
		long counter_large = ls + lc;
		long sc_count = sc;
		long ss_count = ss;
		long lc_count = lc;
		long ls_count = ls;
		Group temp_group;

		
		group_list_small.clear();
		group_list_small.trimToSize();
		group_list_large.clear();
		group_list_large.trimToSize();
		System.gc();

		
		//4 times
		while(counter_small >= small_size){
			temp_group = new Group(R_small);
			for(int i = 0; i<small_size; i++){	
				r = new Random();
				//randomly pick individuals of 'small' size
				if(r.nextInt(2) == 0 && sc_count >= 0){ //cooperator
					temp_group.increment(First_Class.Type.COOPERATOR);
					sc_count--;
				}else{ //selfish
					if(ss_count >= 0){
						temp_group.increment(First_Class.Type.SELFISH);
						ss_count--;	
					}else if(sc_count >= 0){
						temp_group.increment(First_Class.Type.COOPERATOR);
						sc_count--;						
					}
				}	
			}
			group_list_small.add(temp_group);	
			counter_small = sc_count + ss_count;
		}

		System.out.println("ss_count " + ss_count + " sc_count " + sc_count + " counter_small: " + counter_small + " Size: " + group_list_small.size());

		
		//testing purposes
		//for(int i = 0; i<group_list_small.size(); i++){
			//System.out.println("Cooperators: " + group_list_small.get(i).getN_cooperative() + " Selfish: " + group_list_small.get(i).getN_selfish() + " Total: " + group_list_small.get(i).getTotal());
		//}
		//40 times
		while(counter_large >= large_size){
			temp_group = new Group(R_large);
			for(int i = 0; i<large_size; i++){	
				r = new Random();
				//randomly pick individuals of 'small' size
				if(r.nextInt(2) == 0 && lc_count >= 0){ //cooperator
					temp_group.increment(First_Class.Type.COOPERATOR);
					lc_count--;
				}else{ //selfish
					if(ls_count >= 0){
						temp_group.increment(First_Class.Type.SELFISH);
						ls_count--;	
					}else if(lc_count >= 0){
						temp_group.increment(First_Class.Type.COOPERATOR);
						lc_count--;						
					}
				}	
			}
			group_list_large.add(temp_group);
			counter_large = lc_count + ls_count;
		}		
		System.out.println("ls_count " + ls_count + " lc_count " + lc_count + " counter_large: " + counter_large + " Size: " + group_list_large.size());
		//System.out.println("cooperative: " + group_list_large.get(0).getN_cooperative());
		//System.out.println("selfish: " + group_list_large.get(0).getN_selfish());
		
		//testing purposes
		//for(int i = 0; i<group_list_large.size(); i++){
			//System.out.println("Cooperators: " + group_list_large.get(i).getN_cooperative() + " Selfish: " + group_list_large.get(i).getN_selfish() + " Total: " + group_list_large.get(i).getTotal());
		//}
	}
	

	public void reproduce(){
		float new_c = 0, new_s = 0;
		float mutation_c = 1f, mutation_s = 1f;
		int cooperator_count, selfish_count;
		

		
			//reproduce small groups
			for(int i = 0; i < group_list_small.size(); i++){
				cooperator_count = group_list_small.get(i).getN_cooperative();
				selfish_count = group_list_small.get(i).getN_selfish();
				r = new Random();
				group_list_small.get(i).resourceReset(); //reset resources for each timestep.
				for(int o = 0; o < t; o++){ //reproduce for t generations.
					if(/*r.nextInt(2) == 0 && */cooperator_count > 0){ //randomly choose who gets first picks at the resources.						
						new_c = calculation(group_list_small.get(i), group_list_small.get(i).getN_cooperative(), group_list_small.get(i).getN_selfish(), First_Class.Type.COOPERATOR);						
						group_list_small.get(i).setN_cooperative(Math.round(new_c*mutation_c + new_s*(1-mutation_s)));
						cooperator_count--;
					}else if(selfish_count > 0){
						new_s = calculation(group_list_small.get(i), group_list_small.get(i).getN_cooperative(), group_list_small.get(i).getN_selfish(), First_Class.Type.SELFISH);					
						group_list_small.get(i).setN_selfish(Math.round(new_s*mutation_s + new_c*(1-mutation_c)));
						selfish_count--;
					}

					//System.out.println("Group i: " + i + " Cooperators: " + group_list_small.get(i).getN_cooperative() + " Selfish: " + group_list_small.get(i).getN_selfish() + " Total: " + group_list_small.get(i).getTotal());					
				}
			}		
			//reproduce large groups
			for(int i = 0; i < group_list_large.size(); i++){
				cooperator_count = group_list_large.get(i).getN_cooperative();
				selfish_count = group_list_large.get(i).getN_selfish();
				r = new Random();
				group_list_large.get(i).resourceReset(); //reset resources for each timestep.
				for(int o = 0; o < t; o++){ //reproduce for t generations.
					if(/*r.nextInt(2) == 0 && */selfish_count > 0){ //randomly choose who gets first picks at the resources
						new_s = calculation(group_list_large.get(i), group_list_large.get(i).getN_cooperative(), group_list_large.get(i).getN_selfish(), First_Class.Type.SELFISH);	
						group_list_large.get(i).setN_selfish(Math.round(new_s*mutation_s + new_c*(1-mutation_c)));
						selfish_count--;
					}else if(cooperator_count > 0){
						new_c = calculation(group_list_large.get(i), group_list_large.get(i).getN_cooperative(), group_list_large.get(i).getN_selfish(), First_Class.Type.COOPERATOR);
						group_list_large.get(i).setN_cooperative(Math.round(new_c*mutation_c + new_s*(1-mutation_s)));
						cooperator_count--;
					}
					
				}
				//System.out.println("Group i: " + i + " Cooperators: " + group_list_large.get(i).getN_cooperative() + " Selfish: " + group_list_large.get(i).getN_selfish() + " Total: " + group_list_large.get(i).getTotal());
			}	
	}
	
	
	private float calculation(Group temp, int ni_c, int ni_s, First_Class.Type type){
		float resource_consumption = 0;
		float result = 0;
		
		if(temp.getResource() > 0){
			if(type == First_Class.Type.COOPERATOR){
				resource_consumption = ((ni_c*G_cooperative*C_cooperative)/((ni_c*G_cooperative*C_cooperative)+(ni_s*G_selfish*C_selfish)))*temp.getResource();
				result = Math.round(ni_c*(1-K) + (resource_consumption/C_cooperative));
			}else if(type == First_Class.Type.SELFISH){
				resource_consumption = ((ni_s*G_selfish*C_selfish)/((ni_c*G_cooperative*C_cooperative)+(ni_s*G_selfish*C_selfish)))*temp.getResource();
				result = Math.round(ni_s*(1-K) + (resource_consumption/C_selfish));
			}
			
			if(temp.getResource() - resource_consumption < 0){
				temp.setResource(0);
			}else{
				temp.setResource(temp.getResource() - resource_consumption);
			}

		}else{ temp.setResource(0); };
		
		
		return result;
	}

	
	public void group_dispersal(){
		sc = 0;
		ss = 0;
		lc = 0;
		ls = 0;	
		//disperse small group
		for(int i = 0; i < group_list_small.size(); i++){
			sc += group_list_small.get(i).getN_cooperative();
			ss += group_list_small.get(i).getN_selfish();
		}		
		//disperse large group
		for(int i = 0; i < group_list_large.size(); i++){
			lc += group_list_large.get(i).getN_cooperative();
			ls += group_list_large.get(i).getN_selfish();
			

		}	
		
		System.out.println("lc: " + lc);
		System.out.println("ls: " + ls);
		System.out.println("sc: " + sc);
		System.out.println("ss: " + ss);	
	}
	

	
	public void normalise(){
		//take off 1 of each genotype until rescalling back to N is complete.
		long N_temp = sc + ss + lc + ls;
		
		//rescale small groups
		while(N_temp > N && sc > 100 && ss > 100 && lc > 100 && ls > 100){
			sc--;
			ss--;
			lc--;
			ls--;
		}
		
		System.out.println("lc1: " + lc);
		System.out.println("ls1: " + ls);
		System.out.println("sc1: " + sc);
		System.out.println("ss1: " + ss);		
		System.out.println("N_temp: " + N_temp);	

	}
	
	public float population_frequency(First_Class.Type type, First_Class.Size size){
		float temp = 0;
		
		if(type == First_Class.Type.COOPERATOR && size == First_Class.Size.SMALL){
			temp  = (float) sc/(sc+ss+lc+ls);
			return temp;
		}else if(type == First_Class.Type.SELFISH && size == First_Class.Size.SMALL){
			temp  = (float) ss/(sc+ss+lc+ls);
			return temp;			
		}else if(type == First_Class.Type.COOPERATOR && size == First_Class.Size.LARGE){
			temp  = (float) lc/(sc+ss+lc+ls);
			return temp;			
		}else if(type == First_Class.Type.SELFISH && size == First_Class.Size.LARGE){
			temp  = (float) ls/(sc+ss+lc+ls);
			return temp;			
		}
		return temp;

	}
	
	
}

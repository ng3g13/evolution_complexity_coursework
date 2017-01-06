package algorithm;

import java.util.Random;

public class Group {
	private int n_cooperative = 0;
	private int n_selfish = 0;
	private float resource, resource_reset;
	private int migration_value_c = 0, migration_value_s = 0;
	private int revert_c = 0, revert_s = 0;
	Random r;
	
	public Group(float resource){
		this.resource = resource;
		resource_reset = resource;
	}
	
	public void increment(First_Class.Type type){
		if(type == First_Class.Type.COOPERATOR){
			n_cooperative++;
		}else{
			n_selfish++;
		}
	}
	
	public void decrement(First_Class.Type type){
		if(type == First_Class.Type.COOPERATOR){
			n_cooperative--;
		}else{
			n_selfish--;
		}
	}
	
	public int getTotal(){
		return (n_cooperative + n_selfish);
	}
	
	public void resourceReset(){
		resource = resource_reset;
	}
	
	public float evaluateFitness(){
		float temp = (float) n_cooperative/n_selfish;
		return temp;
	}
	
	//migration methods
	
	public void update_migration(){
		r = new Random();
		if(r.nextInt(5)<2){ //20% chance for trait switch
			n_cooperative--; 
			migration_value_c++;
		}
		if(r.nextInt(5)<2){ //20% chance for trait switch
			n_selfish--; 
			migration_value_s++;
		}
	}
	
	public int return_migration(First_Class.Type type){
		if(type == First_Class.Type.COOPERATOR){
			return migration_value_c;
		}else{
			return migration_value_s;
		}
	}
	
	public void revert_store(){
		revert_c = n_cooperative;
		revert_s = n_selfish;
	}
	
	public void revert(){	//call if evaluated fitness is lower
		n_cooperative  = revert_c;
		n_selfish = revert_s;
	}
	
	public void reset(){
		migration_value_c = 0;
		migration_value_s = 0;
		revert_c = 0;
		revert_s = 0;
	}
	
	public int getN_cooperative() {
		return n_cooperative;
	}
	public void setN_cooperative(int n_cooperative) {
		this.n_cooperative = n_cooperative;
	}
	public int getN_selfish() {
		return n_selfish;
	}
	public void setN_selfish(int n_selfish) {
		this.n_selfish = n_selfish;
	}
	
	public void setResource(float resource){
		this.resource = resource;
	}
	
	public float getResource(){
		return resource;
	}
	
}

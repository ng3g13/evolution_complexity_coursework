package algorithm;

public class Group {
	private int n_cooperative = 0;
	private int n_selfish = 0;
	private First_Class.Size size;
	
	public Group(First_Class.Size size){
		this.size = size;
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
	
	public First_Class.Size getSize(){
		return size;
	}
	
	
}

package ranking;

import java.io.Serializable;

public class RankedItem implements Comparable<RankedItem>, Serializable {
	
	private static final long serialVersionUID = 1L;
	 
	String item;
	Double score;
	
	boolean hit;
	
	public RankedItem(String item, Double score) {
		super();
		this.item = item;
		this.score = score;
	}

	public String getItem() {
		return item;
	}
	
	public String getName() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean equals(Object obj){
		if(obj instanceof RankedItem){
			if(this.getName().equals(((RankedItem)obj).getName())){
				return true;
			}
		}
		return false;
		
	}
	
	public int compareTo(RankedItem obj){
		return this.score.compareTo(((RankedItem) obj).getScore());
	}
	
	
	
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
	
}

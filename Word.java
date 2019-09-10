import java.util.ArrayList;
public class Word{

	private String word; //the string associate with this Word object
	private boolean ignored = false; //in case user wants to ignore this misspelling
	private String replacement=""; //the string that the user chooses to replace the misspelled word with
	
	private ArrayList<String> suggestions = new ArrayList<String>(); //suggestions to replace word
	
	public Word(String w) //constructor
	{
		word = w;
	}
	//setters
	public void setWord(String w)
	{
		this.word = w;
	}
	
	public void setIgnored(boolean g) {
		this.ignored = g; 
	}
	
	public void setReplacement(String r) {
		this.replacement = r;
	}
	
	
	
	public void setSuggestions(ArrayList<String> l) {
		this.suggestions = l;
	}
	
	//getters
	public String getWord()
	{
		return this.word;
	}
	
	public boolean getIgnored() {
		return this.ignored;
	}
	public String getReplacement() {
		return this.replacement;
	}
	
	
	
	public ArrayList<String> getSuggestions() {
		return this.suggestions;
	}
	
	
		
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		//System.out.println("calling hashcode");
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}
	
	/*
	 * equals method that compares two words
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}	
	
	

}

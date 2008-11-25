package tokens.value;



public class word_token implements value_token {
	public String name;

	public word_token(String s) {
		this.name = s;
	}

	@Override
	public String toString() {
		return "word_[" + name + ']';
	}
}

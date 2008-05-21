package tokens;

public class string_token implements token {
	public String value;

	public string_token(String s) {
		this.value = s;
	}

	@Override
	public String toString() {
		return new StringBuilder('"').append(value).append('"');
	}
}

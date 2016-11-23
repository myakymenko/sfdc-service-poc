package quintiles.poc.container;

import com.google.gson.annotations.Expose;

public class OptionItem {

	@Expose()
	private String code;
	@Expose()
	private String label;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}

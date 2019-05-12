package br.com.goldenbeast.sample.domain;

public enum FileTypeEnum {

	CSV("csv"), HTML("html"), PDF("pdf"), XLS("xls"), XLSX("xlsx"), XML("xml");

	private String name;
	FileTypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}

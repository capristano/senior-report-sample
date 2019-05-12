package br.com.goldenbeast.sample.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum ReportTemplateEnum {

	PERSON("person", "person.jasper", "Relatório de Pessoas");

	private String templateName;
	private String fileNameDownload;
	ReportTemplateEnum(String name, String templateName, String fileNameDownload) {
		this.templateName = templateName;
		this.fileNameDownload = getName(fileNameDownload);
	}

	private String getName(String fileNameDownload) {
        DateFormat dateFormat = new SimpleDateFormat(" dd-MM-yyyy hhmmss");
        String strDate = dateFormat.format(new Date());
        return fileNameDownload.concat(strDate);
	}

	public String getTemplateName() {
		return templateName;
	}

	public String getFileNameDownload() {
		return fileNameDownload;
	}

}

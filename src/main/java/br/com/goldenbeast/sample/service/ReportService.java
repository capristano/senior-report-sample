package br.com.goldenbeast.sample.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import br.com.goldenbeast.sample.domain.FileTypeEnum;
import br.com.goldenbeast.sample.domain.ReportTemplateEnum;
import br.com.goldenbeast.sample.model.ReportStreaming;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

@Service
@Transactional
public class ReportService {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private PersonService personService;

	Logger logger = LoggerFactory.getLogger(PersonService.class);

	public ReportStreaming generate(ReportTemplateEnum template, FileTypeEnum type) {

		ReportStreaming reportStreaming = new ReportStreaming();
		logger.info("BEGIN - REPORT GENERATION");
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			String sourceFileName = resourceLoader.getResource("classpath:/reports/".concat(template.getTemplateName())).getFile().getAbsolutePath();
			Map<String, Object> parameters = new HashMap<>();
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(getReportListData(template));
			JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);

			if (jasperPrint != null) {
				switch (type) {
			        case HTML:
			        	JRHtmlExporter exporterHTML = new JRHtmlExporter();
			        	exporterHTML.setParameter(JRHtmlExporterParameter.JASPER_PRINT, jasperPrint);
			        	exporterHTML.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			        	exporterHTML.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
			        	exporterHTML.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			        	exporterHTML.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "UTF-8");
			        	exporterHTML.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM, out);
			        	exporterHTML.exportReport();
			            break;

			        case CSV:
			        	JRCsvExporter exporterCSV = new JRCsvExporter();
			        	exporterCSV.setParameter(JRCsvExporterParameter.JASPER_PRINT, jasperPrint);
			        	exporterCSV.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ";");
			        	exporterCSV.setParameter(JRCsvExporterParameter.CHARACTER_ENCODING, "UTF-8");
			        	exporterCSV.setParameter(JRCsvExporterParameter.OUTPUT_STREAM, out);
			        	exporterCSV.exportReport();
			            break;

			        case XML:
			        	JasperExportManager.exportReportToXmlStream(jasperPrint, out);
			            break;

			        case XLS:
			        	JRXlsExporter exporterXLS = new JRXlsExporter();
	                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
	                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
	                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
	                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
	                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
	                    exporterXLS.setParameter(JRXlsExporterParameter.CHARACTER_ENCODING, "UTF-8");
	                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out);
	                    exporterXLS.exportReport();
			            break;

			        case XLSX:
			        	JRXlsxExporter exporterXLSX = new JRXlsxExporter();
			        	exporterXLSX.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
			        	exporterXLSX.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			        	exporterXLSX.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			        	exporterXLSX.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			        	exporterXLSX.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			        	exporterXLSX.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out);
			        	exporterXLSX.exportReport();
			            break;

			        case PDF:
			        	JasperExportManager.exportReportToPdfStream(jasperPrint, out);
			            break;

			        default:
			        	logger.info("Unknown report format");
			    }

				reportStreaming.setIn(new ByteArrayInputStream(out.toByteArray()));
				reportStreaming.setOut(out);
			}
		} catch (JRException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			logger.info("END - REPORT GENERATION");
		}

		return reportStreaming;
	}

	private List<?> getReportListData(ReportTemplateEnum template){

		List<?> listDados = new ArrayList<>();
		switch (template) {
		case PERSON:
			listDados = personService.findAllByOrderByNameDesc();
			break;

		default:
			break;
		}
		return listDados;

	}

}

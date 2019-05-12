package br.com.goldenbeast.sample.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.goldenbeast.sample.domain.FileTypeEnum;
import br.com.goldenbeast.sample.domain.ReportTemplateEnum;
import br.com.goldenbeast.sample.model.Person;
import br.com.goldenbeast.sample.service.PersonService;
import br.com.goldenbeast.sample.service.ReportService;

@Controller
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@Autowired
	private ReportService reportService;

	Logger logger = LoggerFactory.getLogger(PersonController.class);

	@GetMapping("/findById")
	public @ResponseBody ResponseEntity<Person> findById(long id){
		Person person = null;
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		try {
			person = personService.findById(id);
			if(person != null) {
				httpStatus = HttpStatus.OK;
			}
		} catch (Throwable t) {
			logger.error("", t);
		}
		return new ResponseEntity<>(person, httpStatus);
	}

	@GetMapping("/findAll")
	public @ResponseBody ResponseEntity<Iterable<Person>> findAll(){
		Iterable<Person> iterablePerson = null;
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		try {
			iterablePerson = personService.findAll();
			if(iterablePerson != null && ((Collection<?>) iterablePerson).size() > 0) {
				httpStatus = HttpStatus.OK;
			}
		} catch (Throwable t) {
			logger.error("", t);
		}
		return new ResponseEntity<>(iterablePerson, httpStatus);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Person> save(@RequestBody Person p) {
		Person person = null;
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		try {
			person = personService.save(p);
			if(person != null) {
				httpStatus = HttpStatus.OK;
			}
		} catch (Throwable t) {
			logger.error("", t);
		}
		return new ResponseEntity<>(person, httpStatus);
	}

	@GetMapping(path = "/report")
	public void report(String templateName, String typeName, HttpServletResponse response) {
		try {
			ReportTemplateEnum template = ReportTemplateEnum.valueOf(templateName.toUpperCase());
			FileTypeEnum type = FileTypeEnum.valueOf(typeName.toUpperCase());
			response.setHeader("Content-Disposition", "attachment; filename=".concat(template.getFileNameDownload().concat(".").concat(type.getName())));
			IOUtils.copy(reportService.generate(template, type).getIn(), response.getOutputStream());
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (Throwable t) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			logger.error("", t);
		}
	}



}

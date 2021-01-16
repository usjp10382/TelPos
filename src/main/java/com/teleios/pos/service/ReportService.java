package com.teleios.pos.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class ReportService implements Serializable {
	private static final long serialVersionUID = 7666852078916234968L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public ReportService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void printAllItemsReport() throws JRException, IOException, Exception {
		LOGGER.info("<------Execute Print All Items Report In Report Service--------->");
		HttpServletResponse response = null;
		FacesContext context;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream stream;
		Map<String, Object> para = new HashedMap<String, Object>();

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				para.put("CRE_BY", auth.getName());
			} else {
				throw new AccessDeniedException("Un Authorized Access !");
			}
			para.put("BARCODE", "12345678984");
			para.put("IMG_PATH", this.getClass().getResourceAsStream("/reports/teleios img.jpg"));

			context = FacesContext.getCurrentInstance();
			response = (HttpServletResponse) context.getExternalContext().getResponse();

			stream = this.getClass().getResourceAsStream("/reports/ItemReport.jasper");

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(stream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, para,
					this.jdbcTemplate.getDataSource().getConnection());

			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(outputStream.size());
			response.setHeader("Content-disposition", "inline; filename=allItems.pdf");
			response.getOutputStream().write(outputStream.toByteArray());

			response.getOutputStream().flush();
			response.getOutputStream().close();

			context.responseComplete();
		} finally {
			this.jdbcTemplate.getDataSource().getConnection().close();
			// response.getOutputStream().close();

		}

	}

	public byte[] printAllItemsReportTwo() throws JRException, IOException, Exception {
		LOGGER.info("<------Execute Print All Items Report In Report Service--------->");
		try {
			String dir = "/reports/ItemReport.jasper";
			String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(dir);
			JasperPrint jasperPrint = JasperFillManager.fillReport(path, null,
					jdbcTemplate.getDataSource().getConnection());

			return JasperExportManager.exportReportToPdf(jasperPrint);
		} finally {
			LOGGER.info("Close Conn Cal----");
			this.jdbcTemplate.getDataSource().getConnection().close();
		}

	}

}

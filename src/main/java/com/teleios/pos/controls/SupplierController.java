package com.teleios.pos.controls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teleios.pos.dao.utill.TeleiosPosConstant;
import com.teleios.pos.model.Supplier;
import com.teleios.pos.service.SupplierService;

@Named("suppController")
@ViewScoped
public class SupplierController implements Serializable {
	private static final long serialVersionUID = -7043427749908389157L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SupplierController.class);

	@Autowired
	private SupplierService supplierService;

	// Common Objects
	private List<Supplier> allActiveSuppliyers;
	private List<Supplier> allActiveSuppFiltered;

	// Create New Supplier Related Objects
	private Supplier newSupplier = new Supplier();
	private Supplier havUpdateSuppliyer;
	private Supplier havDeleteSuppliyer;

	// Import Suppliyer From Exccel
	private transient UploadedFile uploadedFile;
	private boolean succUpd = false;
	private String updFileName;

	@PostConstruct
	public void init() {
		LOGGER.info("Execute Supplier Controller Init -------->");
		loadAllActiveSuppliyers();
	}

	private void loadAllActiveSuppliyers() {
		LOGGER.debug("<---- Execute Load All Active Suppliyers In Suppliyer Controllers ------>");
		try {
			this.allActiveSuppliyers = this.supplierService.getAllActiveSuppliyer((short) 1);
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Load All Active Suppliyers Couldnt Connect to Database--> ", ste);
			addErrorMessage("Fetch All Suppliyers", "Couldnt Connect to Database\n" + ste.getLocalizedMessage());
		} catch (EmptyResultDataAccessException ere) {
			LOGGER.error("Load All Active Suppliyers Empty Suppliyer ", ere);
			addErrorMessage("Fetch All Suppliyers", "Couldnt Find Any Suppliyers\n" + ere.getLocalizedMessage());
		} catch (DataAccessException dae) {
			LOGGER.error("Load All Active Suppliyers Data access Error--> ", dae);
			addErrorMessage("Fetch All Suppliyers", "Data access Error\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Load All Active Suppliyers System Error--> ", e);
			addErrorMessage("Fetch All Suppliyers", "System Error\n" + e.getLocalizedMessage());
		}
	}

	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		Supplier supplier = (Supplier) value;

		return supplier.getSupplierName().toLowerCase().contains(filterText)
				|| supplier.getAddress().toLowerCase().contains(filterText);
	}

	public void createNewSuppliyer() {
		LOGGER.info("<----- Execute Create New Supplier In Controller ------>");
		int saveState = 0;
		if (getNewSupplier() != null) {
			try {
				if (!getNewSupplier().getEmail().isEmpty()) {
					if (!getNewSupplier().getEmail().matches(TeleiosPosConstant.EMAIL_VERIFICATION)) {
						addErrorMessageWithId("supEmail", "Create New Supplier", "Invalied E-mail Address Formate!");
						return;
					}
				}

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null) {
					getNewSupplier().setCreateBy(auth.getName());
				} else {
					throw new AccessDeniedException("Un Authorized Access !");
				}
				getNewSupplier().setCreateDate(new Date());
				getNewSupplier().setSuppState((short) 1);

				saveState = this.supplierService.createNewSuppliyer(getNewSupplier());
				if (saveState > 0) {
					addMessage("supEmail", "Create New Suppliyer", "Suppliyer Create Success!");
					clear();
					loadAllActiveSuppliyers();
				} else {
					addErrorMessage("Create New Suppliyer", "Suppliyer Create Failed!");
				}

			} catch (SocketTimeoutException ste) {
				LOGGER.error("Create New Supplier Couldnt Connect To DB-->", ste);
				addErrorMessage("Create New Suppliyer",
						"Couldnt Reacheble To Database Server!\n" + ste.getLocalizedMessage());
			} catch (DataAccessException dae) {
				LOGGER.error("Create New Supplier Dataaccess Error-->", dae);
				addErrorMessage("Create New Suppliyer", "Dataaccess Error!\n" + dae.getLocalizedMessage());
			} catch (Exception e) {
				LOGGER.error("Create New Supplier Error--->", e);
				addErrorMessage("Create New Suppliyer",
						"Suppliyer Create Failed.. Error Ocuured!\n" + e.getLocalizedMessage());
			}
		}
	}

	public void updateSuppliyer() {
		LOGGER.info("<------- Execute Update Suppliyer In Controller -------->");
		int updateState = 0;
		try {
			if (getHavUpdateSuppliyer() != null) {

				if (!getHavUpdateSuppliyer().getEmail().isEmpty()) {
					if (!getHavUpdateSuppliyer().getEmail().matches(TeleiosPosConstant.EMAIL_VERIFICATION)) {
						addErrorMessageWithId("supEmailUp", "Update  Supplier", "Invalied E-mail Address Formate!");
						return;
					}
				}

				updateState = this.supplierService.updateSuppliyer(getHavUpdateSuppliyer());
				if (updateState > 0) {
					addMessage("supEmailUp", "Update Suppliyer", "Successfuly Update Suppliyer!");
					this.havUpdateSuppliyer = null;
					loadAllActiveSuppliyers();
				} else {
					addErrorMessageWithId("supEmailUp", "Update Suppliyer", "Suppliyer Update Failed !");
				}
			}
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Update Suppliyer Couldnt Connect To Database Server", ste);
			addErrorMessageWithId("supEmailUp", "Update Supplier", "Couldnt Connect To Database!");
		} catch (DataAccessException dae) {
			LOGGER.error("Update Suppliyer Data Access Error", dae);
			addErrorMessageWithId("supEmailUp", "Update Supplier", "Data Access Error!\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Update Suppliyer System Error", e);
			addErrorMessageWithId("supEmailUp", "Update Supplier",
					"Suppliyer Update Error.. Failed!\n" + e.getLocalizedMessage());
		}
	}

	public void deleteSuppliyer() {
		LOGGER.info("<------- Execute Delete Suppliyer In Controller -------->");
		int deleteState = 0;
		try {
			if (getHavDeleteSuppliyer() != null) {
				getHavDeleteSuppliyer().setSuppState((short) 0);
				deleteState = this.supplierService.deleteSuppliyer(getHavDeleteSuppliyer());
				if (deleteState > 0) {
					addMessage("Delete Suppliyer", "Successfuly Deleted Suppliyer!");
					this.havDeleteSuppliyer = null;
					loadAllActiveSuppliyers();
				} else {
					addErrorMessage("Delete Suppliyer", "Suppliyer Delete Failed !");
				}
			}
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Delete Suppliyer Couldnt Connect To Database Server", ste);
			addErrorMessage("Delete Supplier", "Couldnt Connect To Database!");
		} catch (DataAccessException dae) {
			LOGGER.error("Delete Suppliyer Data Access Error", dae);
			addErrorMessage("Delete Supplier", "Data Access Error!\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Delete Suppliyer System Error", e);
			addErrorMessage("Delete Supplier", "Suppliyer Delete Error.. Failed!\n" + e.getLocalizedMessage());
		}
	}

	// Suppliyer Create Using Excel Sheet
	public void handleFileUpload(FileUploadEvent event) {
		LOGGER.info("<----- Suppliyer Excel Sheet Upload Execute ------>");
		UploadedFile uploadedFile = event.getFile();
		// String fileName = uploadedFile.getFileName();
		// String contentType = uploadedFile.getContentType();

		try {
			String dir = "/resources/supps_heet";
			String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(dir);
			File updDirectory = new File(path);
			if (!updDirectory.exists()) {
				updDirectory.mkdir();
			}
			InputStream initialStream = uploadedFile.getInputStream();
			FileUtils.copyInputStreamToFile(initialStream, new File(updDirectory, uploadedFile.getFileName()));
			setUpdFileName(uploadedFile.getFileName());
			addMessage("Upload Suppliyer Excel Sheet", "Upload Suppliyer File Success!");
			setSuccUpd(true);
		} catch (IOException ioe) {
			LOGGER.error("Upload Suppliyer Excel Sheet Error", ioe);
			addErrorMessage("Upload Suppliyer Excel Sheet",
					"Upload Suppliyer Excel Sheet IO Excepption\n" + ioe.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Upload Suppliyer Excel Sheet Error", e);
			addErrorMessage("Upload Suppliyer Excel Sheet",
					"Upload Suppliyer Excel Sheet Error\n" + e.getLocalizedMessage());
		}
	}

	public void createImportSuppliyers() {
		LOGGER.info("<------- Execute Import Suppliyer In Suppliyer Controller -------->");
		Workbook workbook = null;
		FileInputStream excelFile = null;
		String createBy;
		Date createDate = new Date();
		List<Supplier> suppliers = new ArrayList<Supplier>();
		try {
			if (getUpdFileName() != null) {

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null) {
					getNewSupplier().setCreateBy(auth.getName());
				} else {
					throw new AccessDeniedException("Un Authorized Access !");
				}
				createBy = auth.getName();

				String dir = "/resources/supps_heet";
				String path = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath(dir + File.separator + getUpdFileName());

				excelFile = new FileInputStream(new File(path));

				workbook = new XSSFWorkbook(excelFile);
				Sheet datatypeSheet = workbook.getSheetAt(0);
				Iterator<Row> iterator = datatypeSheet.iterator();

				while (iterator.hasNext()) {

					Row currentRow = iterator.next();
					// Iterator<Cell> cellIterator = currentRow.iterator();

					Supplier supplier = new Supplier();

					supplier.setSupplierName(currentRow.getCell(0).getStringCellValue());
					supplier.setAddress(currentRow.getCell(1).getStringCellValue());
					supplier.setContactNumber(
							NumberToTextConverter.toText(currentRow.getCell(2).getNumericCellValue()));
					supplier.setCreateBy(createBy);
					supplier.setCreateDate(createDate);
					supplier.setFixedNumber(NumberToTextConverter.toText(currentRow.getCell(3).getNumericCellValue()));
					supplier.setEmail(currentRow.getCell(4).getStringCellValue());
					supplier.setSuppState((short) 1);

					suppliers.add(supplier);

					/*
					 * while (cellIterator.hasNext()) { Cell currentCell = cellIterator.next(); //
					 * getCellTypeEnum shown as deprecated for version 3.15 // getCellTypeEnum ill
					 * be renamed to getCellType starting from version 4.0
					 * 
					 * if (currentCell.getCellTypeEnum() == CellType.STRING) {
					 * System.out.print(currentCell.getStringCellValue() + "--"); } else if
					 * (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
					 * System.out.print(currentCell.getNumericCellValue() + "--"); }
					 * 
					 * 
					 * }
					 */

				}
				if (suppliers.size() > 0) {
					List<Supplier> suppliers2 = new ArrayList<Supplier>();
					for (int i = 0; i < 2500; i++) {
						for (Supplier sup : suppliers) {
							suppliers2.add(sup);
						}
					}
					int[] saveState = this.supplierService.createNewSuppliyer(suppliers2);
					if (saveState.length > 0) {
						addMessage("Import Suppliyer",
								"Sucessfuly Number Of: " + saveState.length + " Suppliyers Created!");
						loadAllActiveSuppliyers();
						setSuccUpd(false);
					} else {
						addErrorMessage("Import Suppliyers", "Suppliyers Import Failed !");
					}
				}
			}
		} catch (FileNotFoundException fne) {
			LOGGER.error("import Suppliyer Read Excel Sheet File Not Found...", fne);
			addErrorMessage("Import Suppliyer", "Suppliyer File Not Found!\n" + fne.getLocalizedMessage());
		} catch (IOException ioe) {
			LOGGER.error("import Suppliyer Read Excel Sheet File Not Found...", ioe);
			addErrorMessage("Import Suppliyer", "Suppliyer File IO Exception\n" + ioe.getLocalizedMessage());
		} catch (DataAccessException dae) {
			LOGGER.error("import Suppliyer Read Excel Sheet File Not Found...", dae);
			addErrorMessage("Import Suppliyer", "Data Access Exception\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Import Suppliyers From Excel System Error", e);
			addErrorMessage("Import Suppliyer", "Suppliyer System Error\n" + e.getLocalizedMessage());
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException ioe1) {
					LOGGER.error("Work Book Close Error---->", ioe1);
				}
			}
			if (excelFile != null) {
				try {
					excelFile.close();
				} catch (IOException ioe2) {
					LOGGER.error("Close File Io Exception Error-->", ioe2);
				}
			}
		}
	}

	private void addMessage(String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summery, details);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	private void addMessage(String clinId, String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summery, details);
		FacesContext.getCurrentInstance().addMessage(clinId, facesMessage);
	}

	private void addErrorMessage(String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summery, details);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	private void addErrorMessageWithId(String clientId, String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summery, details);
		FacesContext.getCurrentInstance().addMessage(clientId, facesMessage);
	}

	private void clear() {
		LOGGER.info("Execute Clear in Supplier Controller ------->");
		try {
			this.newSupplier = null;
			this.newSupplier = new Supplier();
		} catch (Exception e) {
			LOGGER.error("Clear input Error-->", e);
			addErrorMessage("Create New Suppliyer", "Clear input Error Ocuured!\n" + e.getLocalizedMessage());
		}
	}

	public Supplier getNewSupplier() {
		return newSupplier;
	}

	public void setNewSupplier(Supplier newSupplier) {
		this.newSupplier = newSupplier;
	}

	public List<Supplier> getAllActiveSuppliyers() {
		return allActiveSuppliyers;
	}

	public void setAllActiveSuppliyers(List<Supplier> allActiveSuppliyers) {
		this.allActiveSuppliyers = allActiveSuppliyers;
	}

	public List<Supplier> getAllActiveSuppFiltered() {
		return allActiveSuppFiltered;
	}

	public void setAllActiveSuppFiltered(List<Supplier> allActiveSuppFiltered) {
		this.allActiveSuppFiltered = allActiveSuppFiltered;
	}

	public Supplier getHavUpdateSuppliyer() {
		return havUpdateSuppliyer;
	}

	public void setHavUpdateSuppliyer(Supplier havUpdateSuppliyer) {
		this.havUpdateSuppliyer = havUpdateSuppliyer;
	}

	public Supplier getHavDeleteSuppliyer() {
		return havDeleteSuppliyer;
	}

	public void setHavDeleteSuppliyer(Supplier havDeleteSuppliyer) {
		this.havDeleteSuppliyer = havDeleteSuppliyer;
		deleteSuppliyer();
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public boolean isSuccUpd() {
		return succUpd;
	}

	public void setSuccUpd(boolean succUpd) {
		this.succUpd = succUpd;
	}

	public String getUpdFileName() {
		return updFileName;
	}

	public void setUpdFileName(String updFileName) {
		this.updFileName = updFileName;
	}

}

package com.teleios.pos.controls;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teleios.pos.model.Expenditure;
import com.teleios.pos.service.ExpenditureService;

@Named("expeControl")
@ViewScoped
public class ExpenditureController implements Serializable {

	/**
	 * @author Dilan Tharaka
	 * @Date 2020.12.01
	 * @Discription Manage bean for control expenditure
	 */

	private static final Logger LOGGER = LoggerFactory.getLogger(ExpenditureController.class);
	private static final long serialVersionUID = 1L;

	@Autowired
	private ExpenditureService expenditureService;

	private Expenditure selectedExpenditure = new Expenditure();
	private List<Expenditure> expenditures;
	private List<Expenditure> filExpenditures;
	private Expenditure deleteExpenditure = new Expenditure();

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		LOGGER.info("<------- Exp Controller Init Called----->");
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		if (params.isEmpty()) {
			return;
		}
		LOGGER.info("Request Parameter---> " + params.get("test"));
		loadAllExp();
	}

	private void loadAllExp() {
		try {
			this.selectedExpenditure.setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			getSelectedExpenditure().setCreateDate(new Date());
			this.expenditures = expenditureService.getAllExpenditures();
			this.selectedExpenditure.setExpId(this.expenditureService.getNextExpNumber());
		} catch (EmptyResultDataAccessException empe) {
			LOGGER.error("Loead All Exps Is Emplty", empe);
		} catch (DataAccessException dae) {
			LOGGER.error("Loead All Exps Is Daa Acc Error", dae);
		} catch (Exception e) {
			LOGGER.error("Unexpected Error---", e);
		}
	}

	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		LOGGER.info("<----Global Filter Function Called----->");
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}
		int filterInt = getInteger(filterText);

		Expenditure expenditure = (Expenditure) value;

		return expenditure.getExpId() == filterInt || expenditure.getDec().toLowerCase().contains(filterText)
				|| expenditure.getCreateBy().toLowerCase().contains(filterText);
	}

	private int getInteger(String string) {
		try {
			return Integer.valueOf(string);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	public void creaeNewExpenditure() {
		LOGGER.info("Execute Create New Exp--------->");
		int saveState = 0;
		try {
			getSelectedExpenditure().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			getSelectedExpenditure().setCreateDate(new Date());
			saveState = this.expenditureService.createNewExpendiure(getSelectedExpenditure());
			if (saveState > 0) {
				LOGGER.info("<--------- Create New Expenditure Success--------->");
				addMessage("Create New Expenditure", "Create New Expenditure Success!");
				loadAllExp();
				clearFiled();
			} else {
				addErrorMessage("Create New Expenditure", "Create New Expenditure Falied");
			}

		} catch (Exception e) {
			LOGGER.error("Create New Expenditure Error Ocurr----------> ", e);
			addErrorMessage("Create New Expenditure", "Create New Expenditure Falied\n" + e.getLocalizedMessage());
		}
	}

	public void updateExpenditure() {
		LOGGER.info("Call to update meth....");
		int updateState = 0;
		try {
			getSelectedExpenditure().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			updateState = this.expenditureService.updateExpenditure(getSelectedExpenditure());

			if (updateState > 0) {
				LOGGER.info("Update Success...");
				addMessage("Update Expenditure", "Update Expenditure Successfull");
				loadAllExp();
				clearFiled();
			} else {

				addErrorMessage("Update Expenditure", "Update Expenditure Faild");
			}
		} catch (Exception e) {
			LOGGER.error("Update Expenditure Fail", e);
			addErrorMessage("Update Expenditure", "Update Expenditure Faild\n" + e.getLocalizedMessage());
		}
	}

	public void deleteExpendature() {
		LOGGER.info("Call to delete method....");
		int deleteState = 0;

		try {
			deleteState = this.expenditureService.deleteExpenditure(getDeleteExpenditure());

			if (deleteState > 0) {
				LOGGER.info("Delete Success...");
				addMessage("Delete Expenditure", "Delete Expenditure Successfull");
				loadAllExp();
			} else {
				addErrorMessage("Delete Expenditure", "Delete Expenditure Faild");
			}
		} catch (Exception e) {
			LOGGER.error("Delete Expenditure Fail", e);
			addErrorMessage("Delete Expenditure", "Delete Expenditure Faild\n" + e.getLocalizedMessage());
		}
	}

	private void addMessage(String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summery, details);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	private void addErrorMessage(String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summery, details);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	private void clearFiled() {
		getSelectedExpenditure().setDec(null);
		
	}

	public List<Expenditure> getExpenditures() {
		return expenditures;
	}

	public void setExpenditures(List<Expenditure> expenditures) {
		this.expenditures = expenditures;
	}

	public List<Expenditure> getFilExpenditures() {
		return filExpenditures;
	}

	public void setFilExpenditures(List<Expenditure> filExpenditures) {
		this.filExpenditures = filExpenditures;
	}

	public Expenditure getSelectedExpenditure() {
		return selectedExpenditure;
	}

	public void setSelectedExpenditure(Expenditure selectedExpenditure) {
		this.selectedExpenditure = selectedExpenditure;

	}

	public Expenditure getDeleteExpenditure() {
		return deleteExpenditure;
	}

	public void setDeleteExpenditure(Expenditure deleteExpenditure) {
		this.deleteExpenditure = deleteExpenditure;
		deleteExpendature();
	}

}

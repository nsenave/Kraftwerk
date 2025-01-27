package fr.insee.kraftwerk.core.extradata.reportingdata;


import java.sql.Date;

import fr.insee.kraftwerk.core.Constants;
import fr.insee.kraftwerk.core.metadata.Variable;
import fr.insee.kraftwerk.core.metadata.VariableType;
import fr.insee.kraftwerk.core.rawdata.QuestionnaireData;
import fr.insee.kraftwerk.core.rawdata.SurveyRawData;
import fr.insee.kraftwerk.core.utils.DateUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class ReportingDataParser {

	private int maxStates = 0;
	private int maxAttempts = 0;

	protected void integrateReportingDataIntoUE(SurveyRawData surveyRawData, ReportingData reportingData, boolean withAllReportingData) {
		this.maxStates = countMaxStates(reportingData);
		this.maxAttempts = countMaxAttempts(reportingData);
		createReportingVariables(surveyRawData);
		addReportingValues(surveyRawData, reportingData, withAllReportingData);
	}

	private void createReportingVariables(SurveyRawData surveyRawData) {
		Variable variableInterviewer = new Variable(Constants.INTERVIEWER_ID_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "20");
		surveyRawData.getVariablesMap().putVariable(variableInterviewer);
		Variable variableOrganization = new Variable(Constants.ORGANIZATION_UNIT_ID_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "50");
		surveyRawData.getVariablesMap().putVariable(variableOrganization);
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.ADRESS_RGES_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "2"));
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.ADRESS_NUMFA_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "6"));
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.ADRESS_SSECH_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "2"));
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.ADRESS_LE_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "1"));
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.ADRESS_EC_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "1"));
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.ADRESS_BS_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "1"));
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.ADRESS_NOI_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "2"));
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.ADRESS_ID_STAT_INSEE,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "15"));
		for (int k = 1; k <= this.maxStates; k++) {
			Variable variableListStates = new Variable(Constants.STATE_SUFFIX_NAME + "_" + k,
					surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "50");
			surveyRawData.getVariablesMap().putVariable(variableListStates);
		}
		Variable variableLastState = new Variable(Constants.LAST_STATE_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "50");
		surveyRawData.getVariablesMap().putVariable(variableLastState);
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.OUTCOME_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "50"));
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.OUTCOME_DATE,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.DATE, "50"));
				surveyRawData.getVariablesMap().putVariable(new Variable(Constants.NUMBER_ATTEMPTS_NAME,
				surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "4"));
		surveyRawData.getVariablesMap().putVariable(new Variable(Constants.LAST_ATTEMPT_DATE,
				surveyRawData.getVariablesMap().getRootGroup(),  VariableType.DATE, "50"));
		for (int i = 1; i <= this.maxAttempts; i++) {
			Variable variableListAttempts = new Variable(Constants.OUTCOME_ATTEMPT_SUFFIX_NAME + "_" + i,
					surveyRawData.getVariablesMap().getRootGroup(), VariableType.STRING, "50");
			surveyRawData.getVariablesMap().putVariable(variableListAttempts);
		}
	}

	private void addReportingValues(SurveyRawData surveyRawData, ReportingData reportingData, boolean withAllReportingData) {
		for (int i = 0; i < reportingData.getListReportingDataUE().size(); i++) {
			ReportingDataUE reportingDataUE = reportingData.getListReportingDataUE().get(i);
			QuestionnaireData questionnaire =  surveyRawData.getQuestionnaires().stream().filter(questionnaireToSearch -> reportingDataUE
					.getIdentifier().equals(questionnaireToSearch.getIdentifier())).findAny().orElse(null);
			if (questionnaire == null && !withAllReportingData) {
				return ;
			}
			addReportingDataUEToQuestionnaire(surveyRawData, reportingDataUE, questionnaire);
		}
	}

	private void addReportingDataUEToQuestionnaire(SurveyRawData surveyRawData, ReportingDataUE reportingDataUE,
			QuestionnaireData questionnaire) {
		if (questionnaire == null) {
			questionnaire = new QuestionnaireData();
			questionnaire.setIdentifier(reportingDataUE.getIdentifier());
			surveyRawData.addQuestionnaire(questionnaire);
			log.info("Missing questionnaire for reporting data: {}.", reportingDataUE.getIdentifier() );
		}
		if (reportingDataUE.getInterviewerId() != null)
			questionnaire.getAnswers().putValue(Constants.INTERVIEWER_ID_NAME, reportingDataUE.getInterviewerId());
		if (reportingDataUE.getOrganizationUnitId() != null)
			questionnaire.getAnswers().putValue(Constants.ORGANIZATION_UNIT_ID_NAME,
					reportingDataUE.getOrganizationUnitId());
		if (reportingDataUE.getInseeSampleIdentifier() != null) {
			questionnaire.getAnswers().putValue(Constants.ADRESS_RGES_NAME, reportingDataUE.getInseeSampleIdentifier().getRges());
			questionnaire.getAnswers().putValue(Constants.ADRESS_NUMFA_NAME, reportingDataUE.getInseeSampleIdentifier().getNumfa());
			questionnaire.getAnswers().putValue(Constants.ADRESS_SSECH_NAME, reportingDataUE.getInseeSampleIdentifier().getSsech());
			questionnaire.getAnswers().putValue(Constants.ADRESS_LE_NAME, reportingDataUE.getInseeSampleIdentifier().getLe());
			questionnaire.getAnswers().putValue(Constants.ADRESS_EC_NAME, reportingDataUE.getInseeSampleIdentifier().getEc());
			questionnaire.getAnswers().putValue(Constants.ADRESS_BS_NAME, reportingDataUE.getInseeSampleIdentifier().getBs());
			questionnaire.getAnswers().putValue(Constants.ADRESS_NOI_NAME, reportingDataUE.getInseeSampleIdentifier().getNoi());
			questionnaire.getAnswers().putValue(Constants.ADRESS_ID_STAT_INSEE, reportingDataUE.getInseeSampleIdentifier().getIdStatInsee());
		}
		if (!reportingDataUE.getStates().isEmpty()) {
			addStates(reportingDataUE, questionnaire);
		}
		if (reportingDataUE.getContactOutcome() != null) {
			addContactOutcome(reportingDataUE, questionnaire);
		}
		if (!reportingDataUE.getContactAttempts().isEmpty()) {
			addContactAttempts(reportingDataUE, questionnaire);
		}
	}

	private void addContactAttempts(ReportingDataUE reportingDataUE, QuestionnaireData questionnaire) {
		for (int k = 1; k <= reportingDataUE.getContactAttempts().size(); k++) {
			questionnaire.getAnswers().putValue(Constants.OUTCOME_ATTEMPT_SUFFIX_NAME + "_" + k,
					ContactAttemptType.getAttemptType(reportingDataUE.getContactAttempts().get(k - 1).getStatus()));
		}
		questionnaire.getAnswers().putValue(Constants.LAST_ATTEMPT_DATE,
				DateUtils.formatDateToString(getLastContactAttempt(reportingDataUE).getDate()));
	}

	private void addContactOutcome(ReportingDataUE reportingDataUE, QuestionnaireData questionnaire) {
		ContactOutcome contactOutcome = reportingDataUE.getContactOutcome();
		questionnaire.getAnswers().putValue(Constants.OUTCOME_NAME, contactOutcome.getOutcomeType());
		if (contactOutcome.getDateEndContact() != 0L) {
			questionnaire.getAnswers().putValue(Constants.OUTCOME_DATE, DateUtils.formatDateToString(new Date(contactOutcome.getDateEndContact())));
		}
		questionnaire.getAnswers().putValue(Constants.NUMBER_ATTEMPTS_NAME,
					Integer.toString(contactOutcome.getTotalNumberOfContactAttempts()));
	}

	private void addStates(ReportingDataUE reportingDataUE, QuestionnaireData questionnaire) {
		for (int k = 1; k <= reportingDataUE.size(); k++) {
			questionnaire.getAnswers().putValue(Constants.STATE_SUFFIX_NAME + "_" + k,
					StateType.getStateType((reportingDataUE.getStates().get(k - 1)).getStateType()));
		}
		questionnaire.getAnswers().putValue(Constants.LAST_STATE_NAME,
				StateType.getStateType(
						(reportingDataUE.getStates().get(reportingDataUE.getStates().size() - 1))
								.getStateType()));
	}

	private ContactAttempt getLastContactAttempt(ReportingDataUE reportingDataUE) {
		return reportingDataUE.getContactAttempts().get(reportingDataUE.getContactAttempts().size()-1);
	}

	public int countMaxStates(ReportingData reportingData) {
		return reportingData.getListReportingDataUE().stream().mapToInt(ue -> ue.getStates().size()).max().getAsInt();
	}

	public int countMaxAttempts(ReportingData reportingData) {
		return reportingData.getListReportingDataUE().stream().mapToInt(ue -> ue.getContactAttempts().size()).max()
				.getAsInt();
	}
}

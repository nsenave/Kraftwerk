package cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import fr.insee.kraftwerk.core.Constants;
import fr.insee.kraftwerk.core.KraftwerkError;
import fr.insee.kraftwerk.core.dataprocessing.GroupProcessing;
import fr.insee.kraftwerk.core.rawdata.SurveyRawData;
import fr.insee.kraftwerk.core.rawdata.SurveyRawDataTest;
import fr.insee.kraftwerk.core.vtl.VtlBindings;
import fr.insee.kraftwerk.core.vtl.VtlExecute;
import fr.insee.kraftwerk.core.vtl.VtlJsonDatasetWriter;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

// Used in do_we_export_datasets
public class ExportDatasetDefinitions {
	public VtlBindings vtlBindings = new VtlBindings();
	public String tempDatasetPath = "";
	public SurveyRawData survey = null;
	
	VtlExecute vtlExecute = new VtlExecute();

	@Given("We have some SurveyRawData named {string}")
	public void initialize(String nameDataset) throws Exception {
		if (nameDataset.contentEquals("COLEMAN")) {
			survey = SurveyRawDataTest.createFakeCapiSurveyRawData();
		} else if (nameDataset.contentEquals("PAPER")) {
				survey = SurveyRawDataTest.createFakeCawiSurveyRawData();
			}
	}


	@When("I try to export the dataset named {string}")
	public void exportDataset(String nameDataset) throws Exception {
		VtlJsonDatasetWriter vtlJsonDatasetWriter = new VtlJsonDatasetWriter(survey, nameDataset);
		tempDatasetPath = vtlJsonDatasetWriter.writeVtlJsonDataset();
	}

	@When("I try to import the dataset named {string}")
	public void importDataset(String nameDataset) throws Exception {
		vtlExecute.putVtlDataset(tempDatasetPath, "OUTPUT_TEST_EXPORT", vtlBindings);
		// add group prefixes
		GroupProcessing groupProcessing = new GroupProcessing(vtlBindings, survey.getVariablesMap());
		groupProcessing.applyVtlTransformations("OUTPUT_TEST_EXPORT", null, new ArrayList<KraftwerkError>());
	}

	@Then("I should get some dataset values from {string}")
	public void checkDataset(String nameDataset) throws Exception {
		assertEquals(15, vtlBindings.getDataset("OUTPUT_TEST_EXPORT").getDataStructure().size());
		assertEquals(4, vtlBindings.getDataset("OUTPUT_TEST_EXPORT").getDataPoints().size());
		assertEquals("Purple", vtlBindings.getDataset("OUTPUT_TEST_EXPORT").getDataPoints().get(0).get("CARS_LOOP.CAR_COLOR"));
		assertTrue(vtlBindings.getDataset("OUTPUT_TEST_EXPORT").getDataStructure().keySet().contains(Constants.ROOT_IDENTIFIER_NAME));
		assertTrue(vtlBindings.getDataset("OUTPUT_TEST_EXPORT").getDataStructure().keySet().contains("AGE"));
	}

}
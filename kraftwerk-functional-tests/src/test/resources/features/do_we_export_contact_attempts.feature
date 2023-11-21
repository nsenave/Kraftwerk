Feature: Do we save correctly all contact attempts ?
  Everybody wants to know if we save them correctly

  Background:
    Given Step 0 : We have some survey in directory "SAMPLETEST-REPORTINGDATA-v1"
    # TODO put COMPLETE directory when ready
    When Step 1 : We launch main service


  Scenario Outline: Do we create contact attempts file with the right structure
    Then We should have a file named "<OutputFileName>" in directory "<Directory>" with <ExpectedAttemptFieldsCount> contact attempts fields

    Examples:
    # Parameters :
    # - Directory : Directory of test campaigns
    # - OutputFileName : Name of contact attempts file (with .csv extension)
    # - ExpectedAttemptFieldsCount : Expected contact attempts fields

    |Directory                        |OutputFileName                                    |ExpectedAttemptFieldsCount   |
    |SAMPLETEST-REPORTINGDATA-v1      |SAMPLETEST-REPORTINGDATA-v1_REPORTINGDATA.csv     |4                            |



  Scenario Outline: Do we have the good amount of lines
    Then We should have <ExpectedTotalCount> lines different than header in a file named "<OutputFileName>" in directory "<Directory>"

    Examples:
    # Parameters :
    # - Directory : Directory of test campaigns
    # - OutputFileName : Name of contact attempts file (with .csv extension)
    # - ExpectedTotalCount : Expected line count (other than header)

      |Directory                        |OutputFileName                                    |ExpectedTotalCount |
      |SAMPLETEST-REPORTINGDATA-v1      |SAMPLETEST-REPORTINGDATA-v1_REPORTINGDATA.csv     |3                  |

  Scenario Outline: Does the file have a correct date format
    Then In file named "<OutputFileName>" in directory "<Directory>" we should have the following date format : "<ExpectedDateFormat>"

    Examples:
    # Parameters :
    # - Directory : Directory of test campaigns
    # - OutputFileName : Name of contact attempts file (with .csv extension)
    # - ExpectedDateformat : Expected date format in file

      |Directory                        |OutputFileName                                    |ExpectedDateFormat  |
      |SAMPLETEST-REPORTINGDATA-v1      |SAMPLETEST-REPORTINGDATA-v1_REPORTINGDATA.csv     |yyyy-MM-dd-hh-mm-ss |


  Scenario Outline: The file has all the contact attempts
    Then For SurveyUnit "<SurveyUnitId>" we should have <ExpectedSpecificStatusCount> contact attempts with status "<ExpectedStatus>" in a file named "<OutputFileName>" in directory "<Directory>"

    Examples:
    # Parameters :
    # - Directory : Directory of test campaigns
    # - OutputFileName : Name of contact attempts file (with .csv extension)
    # - SurveyUnitId : Survey unit identifier
    # - ExpectedSpecificStatusCount : Expected count for said status
    # - ExpectedStatus : Expected status (in input file)

      |Directory                        |OutputFileName                                    |SurveyUnitId |ExpectedSpecificStatusCount  |ExpectedStatus   |
      |SAMPLETEST-REPORTINGDATA-v1      |SAMPLETEST-REPORTINGDATA-v1_REPORTINGDATA.csv     |0000003      |2                            |REF              |

  Scenario Outline: The root file doesn't have any contact attempt
    Then We shouldn't have any contact attempt in "<RootFileName>" in directory "<Directory>"

    Examples:
    # Parameters :
    # - Directory : Directory of test campaigns
    # - OutputFileName : Name of contact attempts file (with .csv extension)
    # - SurveyUnitId : Survey unit identifier
    # - ExpectedSpecificStatusCount : Expected count for said status
    # - ExpectedStatus : Expected status (in input file)

      |Directory                        |RootFileName                               |
      |SAMPLETEST-REPORTINGDATA-v1      |SAMPLETEST-REPORTINGDATA-v1_RACINE.csv|
Feature: Do we apply a script correctly?
  Everybody wants to know if we apply the VTL script

  Scenario Outline: Do we apply the VTL instructions ?
    Given We have some simple VTLBindings
    When I try to apply some VTL instruction : "<vtlScript>"
    When I try to apply some VTL instruction : "<vtlScript2>"
    When I try to apply some VTL instruction : "<vtlScript3>"
    Then The binding "<outputDataset>" should have <numberVariables> variables

    
    Examples: 
    # On applique plusieurs scripts et on voit si les fichiers finaux ont bien pris en compte ces scripts
    # Parameters : 
    # - vtlScript, vtlScript2, vtlScript3 : scripts in VTL to apply
    # - outputDataset : name of the final table generated by all the scripts
    # - numberVariables : expected number of variables in the final table
    |vtlScript                                 |vtlScript2                             |vtlScript3                        |outputDataset|numberVariables |
    |OUTPUT   := COLEMAN[keep IdUE, LAST_NAME];|OUTPUT2 := OUTPUT[keep LAST_NAME];     |                                  |OUTPUT2      |1               |
    |COLEMAN2 := COLEMAN[keep IdUE, LAST_NAME];|PAPER2  := PAPER[keep IdUE, LAST_NAME];|OUTPUT := union(COLEMAN2, PAPER2);|OUTPUT       |2               |
    
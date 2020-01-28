package com.example.surgicallogbook;

public class ProcedureProfile {
    String procedure, procedureName, site, side, anaesthesia, preOperativeDiagnosis, postOperativeDiagnosis, position, antibioticProphylaxis, incision, approach, intraOperativeFindings, additionalProcedures, detailsOfClosureTechniques, estimatedBloodLoss, prosthesisUsed, complications, tissueRemoved, pressureTubes, deepVeinThrombosis;

    public ProcedureProfile(String procedure, String procedureName, String site, String side, String anaesthesia, String preOperativeDiagnosis, String postOperativeDiagnosis, String position, String antibioticProphylaxis, String incision, String approach, String intraOperativeFindings, String additionalProcedures, String detailsOfClosureTechniques, String estimatedBloodLoss, String prosthesisUsed, String complications, String tissueRemoved, String pressureTubes, String deepVeinThrombosis) {
        this.procedure = procedure;
        this.procedureName = procedureName;
        this.site = site;
        this.side = side;
        this.anaesthesia = anaesthesia;
        this.preOperativeDiagnosis = preOperativeDiagnosis;
        this.postOperativeDiagnosis = postOperativeDiagnosis;
        this.position = position;
        this.antibioticProphylaxis = antibioticProphylaxis;
        this.incision = incision;
        this.approach = approach;
        this.intraOperativeFindings = intraOperativeFindings;
        this.additionalProcedures = additionalProcedures;
        this.detailsOfClosureTechniques = detailsOfClosureTechniques;
        this.estimatedBloodLoss = estimatedBloodLoss;
        this.prosthesisUsed = prosthesisUsed;
        this.complications = complications;
        this.tissueRemoved = tissueRemoved;
        this.pressureTubes = pressureTubes;
        this.deepVeinThrombosis = deepVeinThrombosis;
    }

    public String getProcedure() {
        return procedure;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public String getSite() {
        return site;
    }

    public String getSide() {
        return side;
    }

    public String getAnaesthesia() {
        return anaesthesia;
    }

    public String getPreOperativeDiagnosis() {
        return preOperativeDiagnosis;
    }

    public String getPostOperativeDiagnosis() {
        return postOperativeDiagnosis;
    }

    public String getPosition() {
        return position;
    }

    public String getAntibioticProphylaxis() {
        return antibioticProphylaxis;
    }

    public String getIncision() {
        return incision;
    }

    public String getApproach() {
        return approach;
    }

    public String getIntraOperativeFindings() {
        return intraOperativeFindings;
    }

    public String getAdditionalProcedures() {
        return additionalProcedures;
    }

    public String getDetailsOfClosureTechniques() {
        return detailsOfClosureTechniques;
    }

    public String getEstimatedBloodLoss() {
        return estimatedBloodLoss;
    }

    public String getProsthesisUsed() {
        return prosthesisUsed;
    }

    public String getComplications() {
        return complications;
    }

    public String getTissueRemoved() {
        return tissueRemoved;
    }

    public String getPressureTubes() {
        return pressureTubes;
    }

    public String getDeepVeinThrombosis() {
        return deepVeinThrombosis;
    }

    public ProcedureProfile() {
    }
}

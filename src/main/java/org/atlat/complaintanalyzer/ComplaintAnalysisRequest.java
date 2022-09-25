package org.atlat.complaintanalyzer;

import ai.expert.nlapi.v2.API;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintAnalysisRequest {

    String complaintText;

    API.Languages language;

}

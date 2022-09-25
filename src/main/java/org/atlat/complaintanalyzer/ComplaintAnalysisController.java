package org.atlat.complaintanalyzer;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("complaint-analysis")
public class ComplaintAnalysisController {


    @PostMapping(produces =  MediaType.APPLICATION_JSON_VALUE)
    public ComplaintAnalysis complaintAnalysis(@RequestBody ComplaintAnalysisRequest complaintAnalysisRequest) {
        try {
            return new ComplaintAnalyzer().getComplaintAnalysis(complaintAnalysisRequest.complaintText, complaintAnalysisRequest.language);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error analyzing complaint", e);
        }
    }

}

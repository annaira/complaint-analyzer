package org.atlat.complaintanalyzer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@ToString
@Getter
public class ComplaintAnalysis {

    List<String> emotionalTraits;

    List<String> esgCategories;

    String complaintTextWithoutPersonalInformation;

    Double sentiment;

}

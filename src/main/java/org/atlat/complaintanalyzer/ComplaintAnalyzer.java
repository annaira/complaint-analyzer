package org.atlat.complaintanalyzer;

import ai.expert.nlapi.security.*;
import ai.expert.nlapi.v2.API;
import ai.expert.nlapi.v2.cloud.*;
import ai.expert.nlapi.v2.message.AnalyzeResponse;
import ai.expert.nlapi.v2.message.CategorizeResponse;
import ai.expert.nlapi.v2.message.DetectResponse;
import ai.expert.nlapi.v2.model.Category;
import ai.expert.nlapi.v2.model.DocumentPosition;
import ai.expert.nlapi.v2.model.Extraction;
import ai.expert.nlapi.v2.model.ExtractionField;

import java.util.ArrayList;
import java.util.List;

public class ComplaintAnalyzer {

    public static Authentication createAuthentication() {
        DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
        Authenticator authenticator = new BasicAuthenticator(credentialsProvider);
        return new Authentication(authenticator);
    }

    public static Detector createDetector(Authentication authentication, String detector, API.Languages lang) {
        return new Detector(DetectorConfig.builder()
                .withVersion(API.Versions.V2)
                .withDetector(detector)
                .withLanguage(lang)
                .withAuthentication(authentication)
                .build());
    }

    public static void main(String[] args) throws Exception {
        String sampleText = getSampleText();

        API.Languages language = API.Languages.en;

        ComplaintAnalysis complaintAnalysis = new ComplaintAnalyzer().getComplaintAnalysis(sampleText, language);

        System.out.println(complaintAnalysis);
    }

    public static String getSampleText() {
        return "I work in the Sewing ABC factory in Hồ Chí Minh city. We must work so many hours of overtime here! It makes us really angry that we work so much for so little money. Our contracts say that we work from 6 am to 6 pm Mondays to Fridays. My co-worker Nhi Le worked here for 4 years, she said she never left the factory on time. But on most days, we cannot finish at 6 pm. The bosses give us just much more work to do! When we say that it is 6 pm, they start to insult us, scream, say that we can be replaced. It is really insulting! We finish on most days between 8 to 9 pm instead of at 6. I live at 37 Bạch Vân, Phường 5, Quận 5, Thành phố Hồ Chí Minh, Vietnam. This is far away from the factory. I often just arrive at 10 pm, this is so late! Also, we must come on many Saturdays as well to finish all the work they force us to do. All these extra hours are not compensated. We get the same salary, no matter what. It makes me so furious that there is no proper compensation for my work. The salary is also given to us in cash instead of via bank transfer. They say, it is better for us with taxes. But I think they just want to hide that they do not pay out our extra hours properly.";
    }

    public ComplaintAnalysis getComplaintAnalysis(String sampleText, API.Languages language) throws Exception {
        List<String> emotionalTraits = getEmotionalTraits(sampleText, language);
        List<String> esgCategories = getESGCategories(sampleText, language);
        String complaintTextWithoutPersonalInformation = hidePersonalInformation(sampleText, language);
        Double sentiment = getSentiment(sampleText, language);

        return ComplaintAnalysis.builder()
                .emotionalTraits(emotionalTraits)
                .esgCategories(esgCategories)
                .complaintTextWithoutPersonalInformation(complaintTextWithoutPersonalInformation)
                .sentiment(sentiment)
                .build();
    }

    private static Double getSentiment(String sampleText, API.Languages language) throws Exception {
        Analyzer analyzer = createAnalyzer(language);
        AnalyzeResponse response = analyzer.sentiment(sampleText);
        return response.getData().getSentiment().getOverall();
    }

    private static List<String> getEmotionalTraits(String sampleText, API.Languages language) throws Exception {
        Categorizer categorizer = new Categorizer(CategorizerConfig.builder()
                .withVersion(API.Versions.V2)
                .withTaxonomy("emotional-traits")
                .withLanguage(language)
                .withAuthentication(createAuthentication())
                .build());

        CategorizeResponse categorization = categorizer.categorize(sampleText);
        List<Category> categories = categorization.getData().getCategories();
        List<String> esgCategories = new ArrayList<>();
        for (Category category : categories) {
            esgCategories.add(category.getLabel());
        }
        return esgCategories;
    }

    private static List<String> getESGCategories(String text, API.Languages language) throws Exception {
        DetectResponse detect = createDetector(createAuthentication(), "esg-sentiment", language).detect(text);
        List<Category> categories = detect.getData().getCategories();
        List<String> esgCategories = new ArrayList<>();
        for (Category category : categories) {
            esgCategories.add(category.getLabel());
        }
        return esgCategories;
    }

    public static Analyzer createAnalyzer(API.Languages language) {
        return new Analyzer(AnalyzerConfig.builder()
                .withVersion(API.Versions.V2)
                .withContext("standard")
                .withLanguage(language)
                .withAuthentication(createAuthentication())
                .build());
    }

    private static String hidePersonalInformation(String text, API.Languages language) throws Exception {
        DetectResponse detect = createDetector(createAuthentication(), "pii", language).detect(text);
        List<Extraction> extractions = detect.getData().getExtractions();
        for (Extraction extraction : extractions) {
            if (extraction.getTemplate().equals("PII_PERSON")) {
                List<ExtractionField> fields = extraction.getFields();
                for (ExtractionField field : fields) {
                    for (DocumentPosition position : field.getPositions()) {
                        int start = (int) position.getStart();
                        int end = (int) position.getEnd();
                        text = text.substring(0, start) + "*".repeat(end - start) + text.substring(end);
                    }
                }
            }
        }
        return text;
    }

}

# Complaint Analyzer

This is [atlat](https://atlat.de/) 's submission to expert.ai's hackathon [Turn Language into Action: A Natural Language
Hackathon for Good](https://expertai-nlapi-092022.devpost.com/).

## Understand Human Rights Complaints

We at atlat want to ensure human rights by developing a grievance system for workers. Now, with the API of expert.ai, we can automatically understand the complaints better which helps to solve them.


### Spring API

To try out the API we created, run this Spring Boot boots project. The environment variables `EAI_USERNAME` and 
`EAI_PASSWORD` have to be set. 

To analyze a complaint, use the endpoint `/complaint-analysis` as described in the example below:
```
POST http://localhost:8080/complaint-analysis
Content-Type: application/json
Accept: application/json

{
"complaintText": "I work in the Sewing ABC factory in Hồ Chí Minh city. We must work so many hours of overtime here! It makes us really angry that we work so much for so little money. Our contracts say that we work from 6 am to 6 pm Mondays to Fridays. My co-worker Nhi Le worked here for 4 years, she said she never left the factory on time. But on most days, we cannot finish at 6 pm. The bosses give us just much more work to do! When we say that it is 6 pm, they start to insult us, scream, say that we can be replaced. It is really insulting! We finish on most days between 8 to 9 pm instead of at 6. I live at 37 Bạch Vân, Phường 5, Quận 5, Thành phố Hồ Chí Minh, Vietnam. This is far away from the factory. I often just arrive at 10 pm, this is so late! Also, we must come on many Saturdays as well to finish all the work they force us to do. All these extra hours are not compensated. We get the same salary, no matter what. It makes me so furious that there is no proper compensation for my work. The salary is also given to us in cash instead of via bank transfer. They say, it is better for us with taxes. But I think they just want to hide that they do not pay out our extra hours properly.",
"language": "en"
}
```

The class `ComplaintAnalyzer` can be run directly. The environment variables `EAI_USERNAME` and
`EAI_PASSWORD` have to be set here too.

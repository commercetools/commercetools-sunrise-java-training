# commercetools-sunrise-java-training

project for use in the Sunrise training (WIP)

## Bug fixing exercises

### Http Context and CompletionStage
* nothing to do with dependency injection
* see httpcontextexercise.HttpContextController

## Creation exercises

| exercise  | [GitHub Stream](app/githubstream/README.md) | [last viewed products](app/lastviewedproducts/README.md) |[better titles](app/bettertitles/README.md) |[bulky goods](app/bulkygoods/README.md) |
| ----------| --------------------------------------------|----------------------| ----------------------| ----------------------| 
|request related hooks| yes | yes |no | no | 
|page data hooks| yes | yes |yes |no | 
|variant hooks|no|yes|no | no | 
|adding hook interfaces required | no | yes |yes |yes |
| wiring into controller required  | no |no |multi|no |
| configuration should be externalized  | no |yes |no|no |
| need to inject instances  | no | yes |no|no |

### didemo
* DiDemoController to illustrate the scope for request scoped objects
* task use injectionSubject in singleton scope

### bulky goods

* add a custom line item if there are more than 3 line items



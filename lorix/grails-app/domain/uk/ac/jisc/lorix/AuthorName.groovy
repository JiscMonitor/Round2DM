package uk.ac.jisc.lorix


/* todo: Rename to ArticleAuthor  - Actually this ties a person to an article - researchers, RAs, tech staff, reviewers, etc*/
class AuthorName {


    /* The article this ArticleAuthor relates to */
    ResearchOutput researchOutput

    /* The name as it appeared */
    String fullname

    /* Any matched person */
    Person matchedPerson

    /* Attached to what org */
    DomainName domainName

    /* Attached to what org */
    Organisation institution

    /* Author, Research Assistant, Corresponding Author, etc, etc */
    RefdataValue role

    static constraints = {
        fullname unique: false, nullable: false, blank: false
        matchedPerson nullable: true
        institution nullable: true
        domainName nullable: true
        role nullable: true
    }
}

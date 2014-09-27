package uk.ac.jisc.lorix


class DomainName {

    /* domain name - lower case */
    String fqdn

    /* IF this fqdn belongs to an academic institution, create the link here */
    Organisation institution

    static constraints = {
        fqdn unique: true, nullable: false, blank: false
        institution nullable: true
    }
}

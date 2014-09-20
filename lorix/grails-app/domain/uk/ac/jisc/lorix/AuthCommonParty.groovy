package uk.ac.jisc.lorix

// Derived from https://github.com/ianibo/CommonAuthModelPlugin for quick prototype


class AuthCommonParty {

  String displayName
  String notes
  String email
  String twitter
  String facebook
  Boolean emailConfirmed
  String confirmationToken
  Date confirmationSent

  static mapping = {
    table name:'auth_common_party'
    notes type:'text'
  }

  static constraints = {
    displayName nullable:true, blank: true
    notes nullable:true, blank: true
    email nullable:true, blank:true
    twitter nullable:true, blank:true
    facebook nullable:true, blank:true
    emailConfirmed nullable:true, blank:true
    confirmationToken nullable:true, blank:true
    confirmationSent nullable:true, blank:true
  }

}

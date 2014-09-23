package uk.ac.jisc.lorix

class IdentifierNamespace {

  String value

  static mapping = {
    value column:'idns_value'
  }

  static constraints = {
    value (nullable:true, blank:false)
  }

}

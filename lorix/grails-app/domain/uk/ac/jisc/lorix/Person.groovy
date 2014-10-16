package uk.ac.jisc.lorix

import groovy.util.logging.Log4j

@Log4j
class Person extends LorixComponent {

  String fullname

  static constraints = {
    fullname    (nullable:true, blank:false, maxSize:2048)
  }

  static hasMany = [
    nameOccurrences:AuthorName
  ]

  static mappedBy = [
    nameOccurrences:'matchedPerson'
  ]

  static mapping = {
  }
}

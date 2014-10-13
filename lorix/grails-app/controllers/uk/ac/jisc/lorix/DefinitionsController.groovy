package uk.ac.jisc.lorix

import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.plugin.springsecurity.SpringSecurityUtils


class DefinitionsController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def lookup() { 
    log.debug("looking up ${params.id}");

    // Start off by mocking a user search
    def result = [
      'baseClass':'uk.ac.jisc.lorix.Person',
      'searchTemplate':[
        [property:'name', qparam:'p_name', prompt:'Name', placeholder:'Name']
      ],
      'searchResult':[
        [property:'name']
      ]
    ]

    render result as JSON
  }
}

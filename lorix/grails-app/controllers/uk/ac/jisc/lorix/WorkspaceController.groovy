package uk.ac.jisc.lorix


import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.plugin.springsecurity.SpringSecurityUtils



class WorkspaceController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() {
    def result = [:]
    result.user = springSecurityService.getCurrentUser()
    result
  }

}

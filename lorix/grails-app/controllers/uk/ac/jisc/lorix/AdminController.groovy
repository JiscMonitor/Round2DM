package uk.ac.jisc.lorix


import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.plugin.springsecurity.SpringSecurityUtils

class AdminController {

  def springSecurityService
  def adminService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def syncFederationData() { 
    adminService.syncFederationData();
  }
}

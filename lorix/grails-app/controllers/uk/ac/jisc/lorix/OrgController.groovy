package uk.ac.jisc.lorix

import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.plugin.springsecurity.SpringSecurityUtils



class OrgController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
    def result = [:]
    result.user = springSecurityService.getCurrentUser()
    result.org = Organisation.findByShortcode(params.shortcode)
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def home() { 
    def result = [:]
    result.user = springSecurityService.getCurrentUser()
    result.org = Organisation.findByShortcode(params.shortcode)
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def researcher() {
    def result = [:]
    result.user = springSecurityService.getCurrentUser()
    result.org = Organisation.findByShortcode(params.shortcode)
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def apcProcessing() {
    def result = [:]
    result.user = springSecurityService.getCurrentUser()
    result.org = Organisation.findByShortcode(params.shortcode)
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def describe() {
    def result = [:]
    result.user = springSecurityService.getCurrentUser()
    result.org = Organisation.findByShortcode(params.shortcode)
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def requestAPC() {
    def result = [:]
    result.user = springSecurityService.getCurrentUser()
    result.org = Organisation.findByShortcode(params.shortcode)
    result
  }


}

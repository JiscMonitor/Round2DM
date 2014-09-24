package uk.ac.jisc.lorix


import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.plugin.springsecurity.SpringSecurityUtils

class AdminController {

  def springSecurityService
  def adminService

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
  }

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def syncFederationData() { 
    adminService.syncFederationData();
    redirect(controller:'home',action:'index')
  }

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def approvePendingAssociations() { 
    def result=[:]   
    def pending = RefdataCategory.lookupOrCreate('AffiliationStatus','pending')
    result.pending = AuthCommonAffiliation.findAllByStatus(pending)
    result
  }

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def approveAffiliation() { 
    log.debug("approveAffiliation ${params}");
    def a = AuthCommonAffiliation.get(params.id)
    def approved = RefdataCategory.lookupOrCreate('AffiliationStatus','approved')
    a.status = approved
    a.save(flush:true, failOnError:true)
    redirect(action:'approvePendingAssociations')
  }

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def rejectAffiliation() { 
    log.debug("rejectAffiliation ${params}");
    def a = AuthCommonAffiliation.get(params.id)
    def rejected = RefdataCategory.lookupOrCreate('AffiliationStatus','rejected')
    a.status = rejected
    a.save(flush:true, failOnError:true)
    redirect(action:'approvePendingAssociations')
  }
}

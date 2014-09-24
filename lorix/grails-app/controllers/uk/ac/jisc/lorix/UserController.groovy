package uk.ac.jisc.lorix

import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.plugin.springsecurity.SpringSecurityUtils
import uk.ac.jisc.lorix.*;


class UserController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def profile() { 
    def result=[:]
    result.user = springSecurityService.getCurrentUser()
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def processRequestAffiliation() {
    log.debug("processRequestAffiliation org with id ${params.org} role ${params.role}");

    def user = springSecurityService.getCurrentUser()
    def org = Organisation.get(params.org)
    def formal_role = RefdataCategory.lookupOrCreate('role',params.role)

    if ( ( org != null ) && ( formal_role != null ) ) {
      def existingRel = AuthCommonAffiliation.find( { org==org && user==user && role==formal_role } )
      if ( existingRel ) {
        log.debug("existing rel");
        flash.error="You already have a relation with the requested organisation."
      }
      else {
        log.debug("Create new user_org entry....");
        def pending = RefdataCategory.lookupOrCreate('AffiliationStatus','pending')

        def userHome = ( user.affiliations.size() == 0 ) ? Boolean.TRUE : Boolean.FALSE

        def aca = new AuthCommonAffiliation(
                            dateRequested:new Date(),
                            status:pending,
                            org:org,
                            user:user,
                            userHome:userHome,
                            role:formal_role)
        aca.save(flush:true)
      }
    }
    else {
      log.error("Unable to locate org or role");
    }

    redirect(action: "profile")
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def makeHome() {
    log.debug("makeHome ${params}");
    def user = springSecurityService.getCurrentUser()
    def a = AuthCommonAffiliation.get(params.id)
 
    user.affiliations.each {
      if ( ( it.id == a.id ) && ( it.status.value=='approved' ) ) {
        it.userHome=Boolean.TRUE
      }
      else {
        it.userHome=Boolean.FALSE
      }
      it.save(flush:true)
    }
    redirect(action:'profile');
  }

}

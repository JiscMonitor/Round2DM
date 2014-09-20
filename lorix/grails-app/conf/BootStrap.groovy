import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.SecurityFilterPosition

import uk.ac.jisc.lorix.*

class BootStrap {

  def init = { servletContext ->
    SpringSecurityUtils.clientRegisterFilter('shibAuthFilter', SecurityFilterPosition.PRE_AUTH_FILTER)

    def userRole = AuthCommonRole.findByAuthority('ROLE_USER') ?: new AuthCommonRole(authority: 'ROLE_USER', roleType:'global').save(failOnError: true)
    def userAdmin = AuthCommonRole.findByAuthority('ROLE_ADMIN') ?: new AuthCommonRole(authority: 'ROLE_ADMIN', roleType:'global').save(failOnError: true)
  }


  def destroy = {
  }
}

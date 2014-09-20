import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.SecurityFilterPosition


class BootStrap {

  def init = { servletContext ->
    SpringSecurityUtils.clientRegisterFilter('shibAuthFilter', SecurityFilterPosition.PRE_AUTH_FILTER)
  }


  def destroy = {
  }
}

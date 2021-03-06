import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.SecurityFilterPosition

import uk.ac.jisc.lorix.*
import groovy.json.JsonSlurper
import org.springframework.core.io.Resource;
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes

class BootStrap {

  def grailsApplication

  def init = { servletContext ->

    ApplicationContext applicationContext = servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
    SpringSecurityUtils.clientRegisterFilter('shibAuthFilter', SecurityFilterPosition.PRE_AUTH_FILTER)

    def userRole = AuthCommonRole.findByAuthority('ROLE_USER') ?: new AuthCommonRole(authority: 'ROLE_USER', roleType:'global').save(failOnError: true)
    def userAdmin = AuthCommonRole.findByAuthority('ROLE_ADMIN') ?: new AuthCommonRole(authority: 'ROLE_ADMIN', roleType:'global').save(failOnError: true)

    if ( grailsApplication.config.localauth ) {
      users()
    }

    grailsApplication.config.globalLayouts = loadSystemDefinedLayouts(applicationContext)
  }


  def destroy = {
  }


  def users() {

    if ( grailsApplication.config.localauth ) {

      log.debug("localauth is set.. ensure user accounts present (From local config file) ${grailsApplication.config.sysusers}");

      grailsApplication.config.sysusers.each { su ->
        log.debug("test ${su.name} ${su.pass} ${su.display} ${su.roles}");
        def user = AuthCommonUser.findByUsername(su.name)
        if ( user ) {
          if ( user.password != su.pass ) {
            log.debug("Hard change of user password from config ${user.password} -> ${su.pass}");
            user.password = su.pass;
            user.save(failOnError: true)
          }
          else {
            log.debug("${su.name} present and correct");
          }
        }
        else {
          log.debug("Create user...");
          user = new AuthCommonUser(
                        username: su.name,
                        password: su.pass,
                        display: su.display,
                        email: su.email,
                        enabled: true).save(failOnError: true)
        }

        log.debug("Add roles for ${su.name}");
        su.roles.each { r ->
          def role = AuthCommonRole.findByAuthority(r)
          if ( ! ( user.authorities.contains(role) ) ) {
            log.debug("  -> adding role ${role}");
            AuthCommonUserAuthCommonRole.create user, role
          }
          else {
            log.debug("  -> ${role} already present");
          }
        }
      }
    }
  }


  def loadSystemDefinedLayouts(ctx) {

    def result = [:]

    try {
      Resource r = ctx.getResource("/WEB-INF/layouts");
      def f = r.getFile();
      log.debug("got layouts dir: ${f}");

      // see http://groovy.dzone.com/news/class-loading-fun-groovy for info on the strategy being used here

      if ( f.isDirectory() ) {
        GroovyClassLoader gcl = new GroovyClassLoader();
        log.debug("Using class loader: ${gcl.class.name}");

        f.listFiles().each { layout_file ->
          log.debug("Procesing ${layout_file}");
          // Parse JSON files and load
          def slurper = new JsonSlurper()
          def layout = slurper.parse(layout_file,'UTF-8')
          result[layout.shortcode] = layout
        }
      }
    }
    catch ( Exception e ) {
      log.error("Problem loading system defined layouts",e);
    }

    log.debug("systemMayouts: ${result}");
    result
  }
}

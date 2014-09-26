package uk.ac.jisc.lorix

class CreateController {

  def grailsApplication

  def inline() { 
    def result = [:]
    switch ( params.templateType ) {
      case 'static' :
        render(view:params.view, model:result)
        break;
      case 'dataDriven' :
        log.debug("lookup ${params.view}");
        result.layoutDefinition = grailsApplication.config.globalLayouts[params.view]
        render(view:'dataDriven',model:result)
        break;
    }
  }
}

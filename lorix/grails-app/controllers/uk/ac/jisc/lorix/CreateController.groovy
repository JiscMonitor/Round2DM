package uk.ac.jisc.lorix

class CreateController {

  def inline() { 
    switch ( params.templateType ) {
      case 'static' :
        render(view:params.view)
        break;
      case 'dataDriven' :
        render(view:'dataDriven')
        break;
    }
  }
}

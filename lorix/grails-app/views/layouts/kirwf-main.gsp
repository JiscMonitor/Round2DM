<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js" ng-app='kirwf'><!--<![endif]-->
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="LoRIX"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script language="JavaScript">
      var lorixBaseUrl="<g:createLink controller='ajaxSupport' action='lookup'/>";
    </script>

    <asset:javascript src="application.js"/>
    <asset:stylesheet href="main.css"/>
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>

    <g:layoutHead/>

  </head>

  <body ng-controller="KIRwfDesktop as desktop">
    <div id="wrapper">
      <!-- Navigation -->
      <nav class="navbar navbar-default navbar-static-top" role="navigation"
        style="margin-bottom: 0">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse"
            data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span  class="icon-bar"></span>
          </button>

          <g:link uri="/" class="navbar-brand">
            Local Research Info Exchange <g:meta name="app.version" />
          </g:link>
        </div>
        <!-- /.navbar-header -->
  
        <ul class="nav navbar-nav navbar-right">
          <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
              <i class="fa fa-user fa-fw"></i>
              User: ${request.user?.displayName ?: request.user?.username}
              <i class="fa fa-caret-down fa-fw"></i>
            </a>
            <ul class="dropdown-menu dropdown-user">
              <li>Nothing here {{'yet' + '!'}}</li>
              <li><g:link controller="user" action="profile"><i class="fa fa-user fa-fw"></i>  Profile</g:link></li>
              <li><g:link controller="home" action="about"><i class="fa fa-info fa-fw"></i>  About GOKb</g:link></li>
              <li class="divider"></li>
              <li><g:link controller="logout"><i class="fa  fa-sign-out fa-fw"></i> Logout</g:link></li>
              <li class="divider"></li>
            </ul> <!-- /.dropdown-user -->
          </li>
        <!-- /.dropdown -->
        </ul>
        <!-- /.navbar-top-links -->

        <div class="navbar-default sidebar" role="navigation">
          <div class="sidebar-nav navbar-collapse">
            <ul class="nav" id="side-menu">
              <li class="${params?.controller == "home"  ? 'active' : ''}"><g:link controller="home"><i class="fa fa-home fa-fw"></i> Home</g:link></li>
              <li>
                <a href="">My Folder</a>
                <ul class="nav nav-second-level">
                  <li ng-repeat="item in currentFolder"> <a href="" ng-click="addWorkspace()"><i ng-show="item.icon" class="fa {{item.icon}} fa-fw"></i> {{item.name}} </a></li>
                </ul>
              </li>
            </ul>
          </div>
          <!-- /.sidebar-collapse -->
        </div>
        <!-- /.navbar-static-side -->
      </nav>
  
      <!-- Page Content -->
      <div id="page-wrapper" class="${ params.controller ?: 'default' }-display" >
        <div class="row" >
          <div id="page-content" class="col-lg-12">
            <g:layoutBody />
          </div>
          <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
      </div>
      <!-- /#page-wrapper -->
  
    </div>
    <!-- /#wrapper -->
  </body>
</html>

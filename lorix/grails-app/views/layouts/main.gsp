<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
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

  <body>
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
  
        <sec:ifLoggedIn>
          <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
            	<a class="dropdown-toggle" data-toggle="dropdown" href="#">
              	<i class="fa fa-user fa-fw"></i>
              	User: ${request.user?.displayName ?: request.user?.username}
                <i class="fa fa-caret-down fa-fw"></i>
            	</a>
              <ul class="dropdown-menu dropdown-user">
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
        </sec:ifLoggedIn>

        <sec:ifNotLoggedIn>
          <ul class="nav navbar-nav navbar-right">
            <li><g:link controller="register"><i class="fa fa-edit fa-fw"></i> Register</g:link></li>
            <li><g:link controller="login"><i class="fa fa-sign-in fa-fw"></i> Sign in</g:link></li>
          </ul>
          <!-- /.navbar-top-links -->
        </sec:ifNotLoggedIn>
        
        <div class="navbar-default sidebar" role="navigation">
          <div class="sidebar-nav navbar-collapse">
            <ul class="nav" id="side-menu">
              <!--
                <li class="${params?.controller == "welcome"  ? 'active' : ''}"><g:link controller="welcome"><i class="fa fa-dashboard fa-fw"></i> My Dashboard</g:link></li>
              -->
              <li class="${params?.controller == "home"  ? 'active' : ''}"><g:link controller="home"><i class="fa fa-home fa-fw"></i> Home</g:link></li>
              
              <sec:ifLoggedIn>
                <li class="${params?.controller == "search" || params?.controller == "globalSearch"  ? 'active' : ''}"><a href="#"><i class="fa fa-search fa-fw"></i>Search<span class="fa arrow"></span></a>
                  <ul class="nav nav-second-level">
                    <li class="sidebar-search">
                      <g:form controller="globalSearch" action="index" method="get">
                        <label for="global-search" class="sr-only">Global Search</label>
                        <div class="input-group custom-search-form">
                          <input id="global-search" name="q" type="text" class="form-control" placeholder="Global Search...">
                          <span class="input-group-btn">
                            <button class="btn btn-default" type="submit">
                              <i class="fa fa-search"></i>
                            </button>
                          </span>
                        </div><!-- /input-group -->
                      </g:form>
                    </li>
  
                    <li class="divider"></li>
  
                    <g:each in="${session.userPereferences?.mainMenuSections}" var="secname,sec">
                      <g:each in="${sec}" var="srch">
                        <li class="menu-${secname.toLowerCase()}"><g:link controller="search" action="index" params="${[qbe:'g:'+srch.key]}" title="Search ${srch.value.title}">
                            <i class="fa fa-angle-double-right fa-fw"></i> ${srch.value.title}
                          </g:link></li>
                      </g:each>
                    </g:each>
                  </ul> <!-- /.nav-second-level --></li>
  
                <sec:ifAnyGranted roles="ROLE_ADMIN">
                  <li class="${params?.controller == "admin" ? 'active' : ''}"><a href="#"><i class="fa fa-wrench fa-fw"></i> Admin<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                      <li><g:link controller="admin" action="syncFederationData">Refresh FAM Data</g:link></li>
                      <li><g:link controller="admin" action="approvePendingAssociations">Approve Affiliations</g:link></li>
                      <li><g:link controller="home" action="about">About</g:link></li>
                    </ul></li>
                </sec:ifAnyGranted>
  
              </sec:ifLoggedIn>
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

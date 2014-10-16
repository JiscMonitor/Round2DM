<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="kirwf-main"/>
  </head>
  <body>
    <tabset>
      <tab heading="Static title">Static content</tab>
      <tab ng-repeat="tab in tabs" heading="{{tab.title}}" active="tab.active" disabled="tab.disabled">
        <div ng_init="cfg=tab.cfg" ng-include src="tab.view"></div>
      </tab>
    </tabset>
  </body>
</html>

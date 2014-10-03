<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="kirwf-main"/>
  </head>
  <body>
    This is an {{"angular" + " app"}}
    Tabs
      <tabset>
        <tab heading="Static title">Static content</tab>

        <tab ng-repeat="tab in tabs" heading="{{tab.title}}" active="tab.active" disabled="tab.disabled">
          {{tab.content}}
        </tab>
      </tabset>
    EndTabs
  </body>
</html>

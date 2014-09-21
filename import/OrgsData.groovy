#!/usr/bin/groovy

// @GrabResolver(name='es', root='https://oss.sonatype.org/content/repositories/releases')
@Grapes([
  @Grab(group='net.sf.opencsv', module='opencsv', version='2.0'),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0'),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2')
])


import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import org.apache.http.*
import org.apache.http.protocol.*
import org.apache.log4j.*
import au.com.bytecode.opencsv.CSVReader
import java.text.SimpleDateFormat


def starttime = System.currentTimeMillis();
def possible_date_formats = [
  new SimpleDateFormat('yyyy/MM/dd'),
  new SimpleDateFormat('dd/MM/yy'),
  new SimpleDateFormat('dd/MM/yyyy'),
  new SimpleDateFormat('yyyy/MM'),
  new SimpleDateFormat('yyyy')
];

def ukfam = null


println("Loading uk federation data...");
// Load the fam reconcilliation data
def target_service = new RESTClient('http://metadata.ukfederation.org.uk')

try {
  target_service.request(GET, ContentType.XML) { request ->
    uri.path='/ukfederation-metadata.xml'
    response.success = { resp, data ->
      // data is the xml document
      ukfam = data;
    }
    response.failure = { resp ->
      println("Error - ${resp.status}");
      System.out << resp
    }
  }
}
catch ( Exception e ) {
  e.printStackTrace();
}

println("Processing...");

if ( ukfam ) {
  ukfam.EntityDescriptor.each { ed ->
    // println("ID: ${ed.@ID.text()}")
    // println("OrgName: ${ed.Organization?.OrganizationName?.text()}")
    // println("Display: ${ed.Organization?.OrganizationDisplayName?.text()}")
    // println("URL: ${ed.Organization?.OrganizationURL?.text()}")

    ed.Extensions?.Scope?.each {
      if ( it.text().endsWith('.ac.uk') ) {
        println("Scope: ${it} ${ed.Organization?.OrganizationName?.text()} ${ed.Organization?.OrganizationDisplayName?.text()} ${ed.Organization?.OrganizationURL?.text()}");
      }
    }
  }
}
else {
  println("No UKFAM Data");
}


def resolveFAM(xmldoc, code, org) {

  def codes = code.split(';');
  def result = null;

  for ( ci = codes.iterator(); (ci.hasNext() && result==null); ) {
    def c = ci.next().trim();
    def famnode = xmldoc.EntityDescriptor.findAll { it.@entityID == c }
    if ( famnode.size() > 0 ) {
      // result=famnode[0].@ID.text();
      org.famId = famnode[0].@ID.text();
      org.scope = famnode[0].Extensions?.Scope?.text()
      println("scope: ${org.scope}");
    }
    else {
    }
  }

  result
}

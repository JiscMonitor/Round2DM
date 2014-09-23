package uk.ac.jisc.lorix

class Identifier extends LorixComponent {

  IdentifierNamespace namespace
  String value

  static constraints = {
    namespace (nullable:true, blank:true)
    value (nullable:true, blank:true)
  }

  static mapping = {
    namespace column:'id_namespace_fk', index:'id_value_idx'
        value column:'id_value', index:'id_value_idx'
  }

  static def lookupOrCreateCanonicalIdentifier(String ns, String value) {
    def identifier = null;
    if ( ( ns != null ) && ( value != null ) ) {
      def namespace = IdentifierNamespace.findByValue(ns) ?: new IdentifierNamespace(value:ns).save(failOnError:true);
      identifier = Identifier.findByNamespaceAndValue(namespace,value) ?: new Identifier(namespace:namespace, value:value).save(failOnError:true, flush:true)
    }
    else {
      throw new Exception("Cannot call lookupOrCreate for null namespace or value (${ns}:${value})");
    }
    identifier
  }

}

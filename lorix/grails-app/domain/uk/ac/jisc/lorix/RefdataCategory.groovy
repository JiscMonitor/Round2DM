package uk.ac.jisc.lorix

// Derived from https://github.com/ianibo/CommonAuthModelPlugin for quick prototype



class RefdataCategory {

  String desc
  String code
  Set values
  RefdataValue catType

  static mapping = {
      table name:'refdata_category'
         id column:'rdc_id'
       code column:'rdc_code'
    version column:'rdc_version'
       desc column:'rdc_description', index:'rdc_description_idx'
    catType column:'rdc_cat_type_fk'
     values sort:'value', order:'asc'

  }

  static hasMany = [
    values:RefdataValue
  ]

  static mappedBy = [
    values:'owner'
  ]

  static constraints = {
       code(nullable:true, blank:false);
       desc(nullable:true, blank:false);
    catType(nullable:true, blank:false);
  }

  static RefdataValue lookupOrCreate(category_name, value) {
    return lookupOrCreate(category_name,value,null);
  }

  static RefdataValue lookupOrCreate(category_name, value, sortkey) {
	
    def result = null;

    if ( value == null )
      throw new RuntimeException("Request to lookupOrCreate null value in category ${category_name}");

    RefdataValue.withTransaction { status ->
      // The category.
      def cat = RefdataCategory.findByDesc(category_name);
      if ( !cat ) {
        cat = new RefdataCategory(desc:category_name)
        cat.save(failOnError:true)
      }

      result = RefdataValue.findByOwnerAndValueIlike(cat, value)
	
      if ( !result ) {
	// Create and save a new refdata value.
        result = new RefdataValue(owner:cat, value:value, sortKey:sortkey)
        result.save(failOnError:true, flush:true)
      }

      // return the refdata value.
    }

    result
  }

//  def availableActions() {
//    [ [ code:'object::delete' , label: 'Delete' ] ]
//  }

  static String getOID(category_name, value) {
    String result = null
    def cat = RefdataCategory.findByDesc(category_name);
    if ( cat != null ) {
      def v = RefdataValue.findByOwnerAndValueIlike(cat, value)
      if ( v != null ) {
        result = "ianibbo.me.common.RefdataValue:${v.id}"
      }
    }
  }

  static RefdataValue lookup(category_name, value) {
    def cat = RefdataCategory.findByDesc(category_name);
    RefdataValue.findByOwnerAndValueIlike(cat, value)
  }
}

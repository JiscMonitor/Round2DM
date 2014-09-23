package uk.ac.jisc.lorix

class Combo {
  
  // All Combos should have a start date.
  Date startDate = new Date()
  
  // The Combos without an end date are the "current" values.
  Date endDate

  // Participant 1 - One of these
  LorixComponent fromComponent

  // Participant 2 - One of these
  LorixComponent toComponent

  static mapping = {
                id column:'combo_id'
           version column:'combo_version'
     fromComponent column:'combo_from_fk'       , index:'combo_from_idx'
       toComponent column:'combo_to_fk'         , index:'combo_to_idx'
           endDate column:'combo_end_date'
         startDate column:'combo_start_date'
  }

  static constraints = {
    fromComponent(nullable:false, blank:false)
    toComponent(nullable:false, blank:false)
    endDate(nullable:true, blank:false)
    startDate(nullable:true, blank:false)
  }
  
}

package uk.ac.jisc.lorix

class ResearchOutput extends LorixComponent {

  // Work title is mapped to LorixComponent.name

  static hasMany = [
  ]

  static mappedBy = [
  ]

  static comboConfig = [
    'appearences':'RO.Appearance'
  ]
}

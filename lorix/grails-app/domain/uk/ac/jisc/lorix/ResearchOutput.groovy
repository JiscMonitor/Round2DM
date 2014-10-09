package uk.ac.jisc.lorix

class ResearchOutput extends LorixComponent {

  // Work title is mapped to LorixComponent.name

  static hasMany = [
    people:AuthorName
  ]

  static mappedBy = [
    people:'researchOutput'
  ]

  static comboConfig = [
    'appearences':'RO.Appearance',
  ]
}

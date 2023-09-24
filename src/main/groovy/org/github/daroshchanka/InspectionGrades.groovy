package org.github.daroshchanka

class InspectionGrades<E> {

  final E forHigh
  final E forMedium

  InspectionGrades(E forHigh, E forMedium) {
    this.forHigh = forHigh
    this.forMedium = forMedium
  }

  InspectionGrades(List<E> list) {
    this.forHigh = list[0]
    this.forMedium = list[1]
  }

  InspectionStatus evaluate(E value) {
    if (forHigh > forMedium) {
      return value >= forHigh ? InspectionStatus.HIGH
          : value >= forMedium ? InspectionStatus.MEDIUM
          : InspectionStatus.LOW
    } else {
      if (value == 0) {
        return InspectionStatus.LOW
      }
      return value <= forHigh ? InspectionStatus.HIGH
          : value <= forMedium ? InspectionStatus.MEDIUM
          : InspectionStatus.LOW
    }
  }
}

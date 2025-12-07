object Main extends App {
  println("=== Функциональные упражнения ===")

  // Задание 1
  println("\n1. Поиск суммы:")
  val list1 = List(1, 2, 3, 4, 5)
  println(s"findSumFunctional($list1, 7) = ${Exercises.findSumFunctional(list1, 7)}")

  // Задание 2
  println("\n2. Хвостовая рекурсия:")
  println(s"tailRecRecursion(List(1,2,3)) = ${Exercises.tailRecRecursion(List(1, 2, 3))}")

  // Задание 3
  println("\n3. Бинарный поиск:")
  val sorted = List(1, 3, 5, 7, 9, 11)
  println(s"functionalBinarySearch($sorted, 7) = ${Exercises.functionalBinarySearch(sorted, 7)}")

  // Задание 4
  println("\n4. Генерация имен:")
  println(s"5 имен: ${Exercises.generateNames(5)}")

  println("\n=== Завершено ===")
}
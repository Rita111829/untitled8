import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random

object Exercises {

  
  def findSumFunctional(items: List[Int], sumValue: Int): (Int, Int) = {
    items.zipWithIndex.flatMap { case (x, i) =>
      items.zipWithIndex.collect {
        case (y, j) if i != j && x + y == sumValue => (i, j)
      }
    }.headOption.getOrElse((-1, -1))
  }

 
  def simpleRecursion(items: List[Int], index: Int = 1): Int = {
    items match {
      case head :: tail =>
        if (head % 2 == 0) {
          head * simpleRecursion(tail, index + 1) + index
        } else {
          -1 * head * simpleRecursion(tail, index + 1) + index
        }
      case _ => 1
    }
  }

  def tailRecRecursion(items: List[Int]): Int = {
    @tailrec
    def loop(reversed: List[Int], currentIndex: Int, acc: Int): Int = {
      reversed match {
        case head :: tail =>
          val newAcc = if (head % 2 == 0) {
            head * acc + currentIndex
          } else {
            -head * acc + currentIndex
          }
          loop(tail, currentIndex - 1, newAcc)
        case Nil => acc
      }
    }


    loop(items.reverse, items.length, 1)
  }
  
  def functionalBinarySearch(items: List[Int], value: Int): Option[Int] = {
    @tailrec
    def search(low: Int, high: Int): Option[Int] = {
      if (low > high) None
      else {
        val mid = low + (high - low) / 2
        items(mid) match {
          case x if x == value => Some(mid)
          case x if x < value => search(mid + 1, high)
          case _ => search(low, mid - 1)
        }
      }
    }
    if (items.isEmpty) None else search(0, items.length - 1)
  }
  
  def generateNames(namesCount: Int): List[String] = {
    if (namesCount < 0) throw new Throwable("Invalid namesCount")

    val vowels = List('a', 'e', 'i', 'o', 'u', 'y')
    val consonants = List('b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z')

    @tailrec
    def generate(count: Int, acc: List[String], used: Set[String]): List[String] = {
      if (count <= 0) acc
      else {
        val name = {
          val firstChar = Random.nextInt(26) + 'A'.toInt
          val length = Random.nextInt(5) + 3
          val rest = (1 until length).map { _ =>
            val chars = if (Random.nextBoolean()) vowels else consonants
            chars(Random.nextInt(chars.length))
          }.mkString
          s"${firstChar.toChar}$rest"
        }

        if (!used.contains(name)) {
          generate(count - 1, name :: acc, used + name)
        } else {
          generate(count, acc, used)
        }
      }
    }

    generate(namesCount, Nil, Set.empty).reverse
  }
}


object Utils {
  class PhoneBase(private val phones: mutable.ListBuffer[String]) {
    def insert(phone: String): Unit = phones += phone
    def list(): List[String] = phones.toList
    def delete(phone: String): Unit = phones.filterInPlace(_ != phone)
  }

  def checkPhoneNumber(num: String): Boolean =
    num.matches("^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")

  class SimplePhoneService(phonesBase: PhoneBase) {
    def findPhoneNumber(num: String): String = {
      val resulNums = phonesBase.list().filter(_ == num)
      if (resulNums.isEmpty)
        null
      else
        resulNums.head
    }

    def addPhoneToBase(phone: String): Unit = {
      if (checkPhoneNumber(phone))
        phonesBase.insert(phone)
      else
        throw new InternalError("Invalid phone string")
    }

    def deletePhone(phone: String): Unit = phonesBase.delete(phone)
  }

  trait ChangePhoneService {
    def changePhone(oldPhone: String, newPhone: String): String
  }
}

 

object SideEffectExercise {
  import Utils._

  class SimpleChangePhoneService(phoneService: SimplePhoneService) extends ChangePhoneService {
    override def changePhone(oldPhone: String, newPhone: String): String = {
      val oldPhoneRecord = phoneService.findPhoneNumber(oldPhone)
      if (oldPhoneRecord != null) {
        phoneService.deletePhone(oldPhoneRecord)
      }
      phoneService.addPhoneToBase(newPhone)
      "ok"
    }
  }

  class PhoneServiceSafety(unsafePhoneService: SimplePhoneService) {
    def findPhoneNumberSafe(num: String): Option[String] = {
      Option(unsafePhoneService.findPhoneNumber(num))
    }

    def addPhoneToBaseSafe(phone: String): Either[String, Unit] = {
      try {
        unsafePhoneService.addPhoneToBase(phone)
        Right(())
      } catch {
        case e: InternalError => Left(e.getMessage)
        case e: Throwable => Left("Unknown error")
      }
    }

    def deletePhoneSafe(phone: String): Either[String, Unit] = {
      try {
        unsafePhoneService.deletePhone(phone)
        Right(())
      } catch {
        case e: Throwable => Left("Error deleting phone")
      }
    }
  }

  class ChangePhoneServiceSafe(phoneServiceSafety: PhoneServiceSafety) extends ChangePhoneService {
    override def changePhone(oldPhone: String, newPhone: String): String = {
      
      val oldPhoneOpt = phoneServiceSafety.findPhoneNumberSafe(oldPhone)
      
      oldPhoneOpt match {
        case Some(phone) =>
          phoneServiceSafety.deletePhoneSafe(phone) match {
            case Left(error) => return s"Error deleting old phone: $error"
            case Right(_) => 
          }
        case None => 
      }
      
      phoneServiceSafety.addPhoneToBaseSafe(newPhone) match {
        case Right(_) => "ok"
        case Left(error) => s"Error adding new phone: $error"
      }
    }
  }
}
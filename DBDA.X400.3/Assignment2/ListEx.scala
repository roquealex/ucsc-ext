object ListEx {
	def listLength(xs : List[Int]) : Int = xs match {
		case Nil => 0
		case x::ys => 1 + listLength(ys)
	}

	def listReverse(xs : List[Int]) : List[Int] = xs match {
		case Nil => {xs}
		case x::ys => listReverse(ys):+x
	}
	def removeConsDup(xs : List[Int]) : List[Int] = xs match  {
		case Nil => xs
		case x::ys => x::removeConsDup(ys.dropWhile(_==x))
	}
	def removeDup(xs : List[Int]) : List[Int] = xs match  {
		case Nil => xs
		case x::ys => x::removeDup(ys.partition(_==x)._2)
	}

	def main(str : Array[String]) {
		val dups = List(1,1,1,1,2,3,3,4,4,5,1,2,2)
		val l1 = List(1,2,3,4,5,6)
		println("List length : "+listLength(l1))
		println("Reverse : "+listReverse(l1))
		println("No cons dup : "+removeConsDup(dups))
		println("No dup : "+removeDup(dups))
	}
}

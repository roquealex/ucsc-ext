object Balanced {

	def balance(chars: List[Char]): Boolean = {
		def innerBalance(cl:List[Char], level:Int) : Boolean = cl match {
			case Nil                 => level==0
			case c::rest if (c=='(') => innerBalance(rest,level+1)
			case c::rest if (c==')') => (level > 0) && innerBalance(rest,level-1)
			case c::rest             => innerBalance(rest,level)
		}
		return innerBalance(chars,0)
	}

	def balanceIter(chars: List[Char]): Boolean = {
		var level : Int = 0
		var cl = chars
		while(!cl.isEmpty){
			cl.head match {
				case '(' => level+=1
				case ')' => if (level>0) level-=1 else return false
				case _ =>
			}
			/*
			*/
			//println("c: " + cl.head + " l: "+ level)
			cl = cl.tail
		}
		return (level == 0)
	}



	def main(str : Array[String]) {
		val testStrings = List(
			"(if (zero? x) max (/ 1 x))",
			"I told him (that it’s not (yet) done). (But he wasn’t listening)",
			":-)",
			"())(",
			"(-_-)",
			"Expecting false for the following strings",
			"Sunn O)))",
			"Apple )(",
			"I told him (that it’s not yet) done). (But he wasn’t listening)",
			"I told him (that it’s not (yet done). (But he wasn’t listening)"
		)

		//val test = "I told him (that it’s not (yet) done). (But he wasn’t listening)"
		println("Balanced Parenthesis")
		//println("Testing  "+ test)
		//println("Result  "+ balance(test.toList))
		for ( str <- testStrings) {
			println("Testing balance of: "+str+" => "+
			        balance(str.toList) + "," +
			        balanceIter(str.toList))
		}
		//balanceIter(testStrings(3).toList)
	}
}

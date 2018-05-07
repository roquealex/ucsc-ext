object RLE {

	def encodingIter(input:String) : String = {
		var i:Int = 0
		val buf = new StringBuilder
		while(i < input.length) {
			val c = input.charAt(i)
			val idx = input.indexWhere(_!=c,i+1)
			val lim = if (idx>0) idx else input.length
			//println(s"Current $c, next index $lim")
			//println(s"${lim-i}$c")
			buf ++= (lim-i).toString
			buf += c
			i = lim
		}
		return(buf.toString)
	}

	def decodingIter(input:String) : String = {
		val isNotNum = (c:Char) => !(c>= '0' && c <='9')
		var i:Int = 0
		val buff = new StringBuilder
		while(i < input.length) {
			//val c = input.charAt(i)
			val idx = input.indexWhere(isNotNum,i)
			val lim = if (idx>=0) idx else input.length
			try {
				val rep = input.slice(i,lim).toInt
				for (i <- 0 until rep) buff += input.charAt(lim)
			} catch {
				// There is a character without number "23XX"
				case e : NumberFormatException => buff += input.charAt(lim)
				// String ends in number "2X12" bypassing number
				case e : StringIndexOutOfBoundsException => buff ++=  input.slice(i,lim)
			}
			i = lim+1
		}
		return(buff.toString)
	}
/*
*/




	def encoding(input:String) : String = {
		def charEncoding(cl:List[Char]) : String = cl match {
			case Nil => return("")
			case c::rest => {
				val (pre,post) = rest.span(_==c)
				return (s"${pre.length+1}$c" + charEncoding(post))
			}
		}
		return(charEncoding(input.toList))
	}

	def decoding(input:String) : String = {
		//println(input)
		val isNum = (c:Char) => (c>= '0' && c <='9')
		input match {
			case x if (x.isEmpty) => return "";
			// This is in case a letter comes alone let it pass:
			case x if (!isNum(x.head)) => return (x.head)+decoding(x.tail)
			case _ => {
				val (num, rest) = input.span(isNum)
				// can throw NumberFormatException
				return (rest.head.toString*num.toInt + decoding(rest.tail))
			}
		}
	}

	def main(arg : Array[String]) {
		val tests = List(
			("WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWW","12W1B12W3B8W"),
			("WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWWWWWWWWWWWWWWWWWWBWWWWWWWWWWWWWW","12W1B12W3B24W1B14W"),
			("aaabbcbbbccccdd","3a2b1c3b4c2d"),
			("XXXXYYYXXYXYYYYYYYYYYYY","4X3Y2X1Y1X12Y"),
			("ABCABCABCABC","1A1B1C1A1B1C1A1B1C1A1B1C"),
			("CommonRunner","1C1o2m1o1n1R1u2n1e1r"),
			("eeeeei","5e1i"),
			("","")
		)
		println("RLE test")
//encoding(result)
		//println(encoding(tests(1)._1))
		//println(tests(1)._2)
		//decoding(tests(1)._2)
		//decoding("")
		//println(decoding("2A3BCCDD1F10X"))
		for((raw,rle) <- tests) {
			println(s"Testing Encoding $raw")
			val enc = encoding(raw)
			println(s"Expected: $rle, Calculated $enc")
			println("Test Enc "+ (if (enc==rle) "PASSED" else "FAILED"))
			val encIt = encodingIter(raw)
			println(s"Expected (Iter): $rle, Calculated $encIt")
			println("Test Enc "+ (if (encIt==rle) "PASSED" else "FAILED"))
			println(s"Testing Decoding $rle")
			val dec = decoding(rle)
			println(s"Expected: $raw, Calculated $dec")
			println("Test Dec "+ (if (dec==raw) "PASSED" else "FAILED"))
			val decIt = decodingIter(rle)
			println(s"Expected (Iter): $raw, Calculated $decIt")
			println("Test Dec "+ (if (decIt==raw) "PASSED" else "FAILED"))
		}
		//encodingIter(tests(5)._1)
		//encodingIter("")
		//encodingIter("eeeeee")
		//println(decodingIter("2eC4dX12Y"))
		//println(decodingIter("aaaabaa"))
	}
}



/*

scala> val str = "AAAAABBBBBCCCD"
str: String = AAAAABBBBBCCCD

scala> str.span(_=='A')
res0: (String, String) = (AAAAA,BBBBBCCCD)

scala> val sp = str.span(_=='A')
sp: (String, String) = (AAAAA,BBBBBCCCD)

scala> sp._1
res1: String = AAAAA

scala> sp._2
res2: String = BBBBBCCCD

scala> 

scala> s"${sp._1.length}${sp._1.head}"
res4: String = 5A

				//java.lang.NumberFormatException
				//java.lang.StringIndexOutOfBoundsException

*/

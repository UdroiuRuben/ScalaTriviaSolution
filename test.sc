import com.adaptionsoft.games.trivia.utils.QuestionCategories

import java.util.Random
import scala.collection.mutable.ListBuffer

val rand: Random = new Random

rand.setSeed(1000)

(0 to 5).foreach(_ => println(rand.nextInt(5)))



List.tabulate(10)(i => s"ceva test ${i + 1}")

val x = List(1, 2, 3)

x(2)
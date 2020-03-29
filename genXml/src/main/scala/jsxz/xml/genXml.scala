package jsxz.xml

import scala.io.Source
import scala.xml.{Elem, Node, XML}

object genXml {

  def getParent(nodes: Map[String, List[String]], str: String): String = {
    val subNodes: Map[String, List[String]] = nodes.filter(e => e._2.contains(str))
    println("subNodes:" + subNodes.map(e => e._1).toList)
    subNodes.map {
      case e1 => {
        println(s"${e1._1}")
        if (str != e1._1 && e1._1 != "dmodule") {
          println("getParent:")
          getParent(nodes, e1._1)
        } else {
          List(e1._1)
        }

      }
    }.toList
    ""
  }

  def main(args: Array[String]): Unit = {
    val xsd = XML.loadFile("data/4.2/descript.xsd")
    val nodes: Map[String, List[String]] = Source.fromFile("data/4.2/descript.txt").getLines().map(e => {
      val arr: Array[String] = e.split(",")
      (arr.head.substring(0, arr.head.length - 8), arr.tail.toList)
    }).toMap
    println(nodes)
    getParent(nodes, "para")

    //    genChild()
    //    (xsd \\ "complexType").filter(e=>(e \@ "name".toString=="contentElemType")).map(e=>{
    //      findElement(e,xsd,"")
    //    })
  }

  /**
   * 没有停止条件
   *
   * @param n
   * @param xsd
   * @param preRef
   */
  def findElement(n: Node, xsd: Node, preRef: String): Unit = {
    val ref: String = (n \@ "ref").toString
    //    if(ref=="para") sys.exit()
    if (n.label == "element") {
      println(s"element:$ref")
      //查找元素引用
      if (ref != "" && ref != "para") {
        (xsd \\ "element").filter(e => (e \@ "name").toString == ref).map {
          case e => {
            val t: String = (e \@ "type").toString
            println(t)
            (xsd \\ "complexType").filter(e1 => e1 \@ "name".toString == t).map(e2 => {
              if (ref != preRef)
                findElement(e2, xsd, ref)
            })
          }
        }
      }

      ref
    }
    else {
      //非元素引用 比如group
      if (ref != "") {
        (xsd \\ "_").filter((_ \@ "name" == ref)).map(e => findElement(e, xsd, ref))
      }

      (n \ "_").map(e => findElement(e, xsd, preRef))
    }
  }

  def genChild(): Unit = {
    val xsd = XML.loadFile("data/4.2/descript.xsd")
    (xsd \\ "complexType").map {
      case e => {
        print((e \@ "name"))
        getChild(e, xsd)
        println()
      }
    }
  }

  def getChild(e: Node, xsd: Elem): String = {
    val ref = (e \@ "ref").toString
    if (e.label == "element") {
      print(s",$ref")
    }
    else {
      if (ref != "") {
        (xsd \\ "_").filter(t => (t \@ "name").toString == ref).map {
          case e1 => {
            getChild(e1, xsd)
          }
        }
      }
      (e \ "_").map {
        case e1 => getChild(e1, xsd)
      }
    }
    ""
  }

}

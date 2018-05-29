//import parsers.ELBAccessLogParser
//
//object ParserTest {
//
//  def main (args: Array[String]): Unit = {
//    val parser = new ELBAccessLogParser()
//    val line = "2015-07-22T02:41:43.738051Z marketpalce-shop 54.169.106.125:45737 10.0.4.244:80 0.000021 0.004062 0.000026 200 200 0 13820 \"GET https://paytm.com:443/shop/p/apple-ipad-air-2-wi-fi-16-gb-tablet-silver-MOBAPPLE-IPAD-ADUMM141CFA4A8BF_31205 HTTP/1.1\" \"Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36\" ECDHE-RSA-AES128-GCM-SHA256 TLSv1.2"
//    val entry = parser.readLineToLogEntry(line)
//    println(entry.page)
//  }
//
//}

import net.xqj.exist.ExistXQDataSource;

import javax.xml.xquery.*;


/**
 * Created by 47767573t on 23/02/16.
 */
public class Main{

    static XQDataSource xqs=new ExistXQDataSource();
    static String query = "";
    static XQConnection xconn;
    static XQPreparedExpression xqpe;
    static XQResultSequence xqResult;

    public static void main(String[]args) throws XQException {

        xconn = xqs.getConnection();
        xqs.setProperty("serverName","localhost");
        xqs.setProperty("port","8080");


        //Query de prueba inicial
        String query = "for $country in /mondial//country[count(.//city) > 2]\n" +
                "let $cities_pops := (\n" +
                "\tfor $c in $country//city[population]\n" +
                "\tlet $pnum := number($c/population[1])\n" +
                "\torder by $pnum descending\n" +
                "\treturn $c/population[1]\n" +
                ")\n" +
                "return\n" +
                "<result>\n" +
                "\t{$country/name}\n" +
                "\t<three-cities>\n" +
                "\t\t{sum($cities_pops[position()<=3])}\n" +
                "\t</three-cities>\n" +
                "</result>";

        mostrarQuery (query);

        //Query del ejercicio 1
        String query1 = "for $ctr in /mondial/country\n" +
                "where($ctr/population = max(/mondial/country/population))\n" +
                "return\n" +
                "<result>\n" +
                "\t{$ctr/name}\n" +
                "\t{$ctr/population}\n" +
                "</result>";

        mostrarQuery (query1);

        //Query del ejercicio 2
        String query2 = "for $water in /mondial//(lake|river|sea) \n" +
                "where $water/located/id(@country)/name=\"Russia\" \n" +
                "and count($water/located) = 2\n" +
                "order by $water/name\n" +
                "return\n" +
                "\telement {$water/name()} {$water/name/text()}";

        mostrarQuery (query2);

        xconn.close();
    }

    public static void mostrarQuery(String q) throws XQException {
        xqpe = xconn.prepareExpression(q);
        xqResult = xqpe.executeQuery();
        while(xqResult.next()){
            System.out.println(xqResult.getItemAsString(null));
        }
    }


}
